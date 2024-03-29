package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.delivery.DataTransferObject.DishDto;
import com.food.delivery.Entity.Category;
import com.food.delivery.Entity.Dish;
import com.food.delivery.Entity.DishFlavor;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.CategoryService;
import com.food.delivery.Service.DishFlavorService;
import com.food.delivery.Service.DishService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

  @Autowired DishService dishService;

  @Autowired CategoryService categoryService;

  @Autowired DishFlavorService dishFlavorService;

  @Autowired RedisTemplate redisTemplate;

  @GetMapping("/{id}")
  public Result<DishDto> getCurrDish(@PathVariable Long id) {

    DishDto dishWithFlavor = dishService.getDishWithFlavor(id);
    return Result.success(dishWithFlavor);
  }

  @GetMapping("/page")
  private Result<Page> getDishPage(
      @RequestParam(required = true) int page,
      @RequestParam(required = true) int pageSize,
      String name) {

    Page<Dish> pageInfo = new Page<>(page, pageSize);
    Page<DishDto> dishDtoPage = new Page<>();

    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper.like(name != null, Dish::getName, name);

    queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

    dishService.page(pageInfo, queryWrapper);

    BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

    List<Dish> records = pageInfo.getRecords();

    // List<DishDto> dishes = new ArrayList<>();

    // for (Dish dish : records) {
    //   DishDto dishDto = new DishDto();

    //   BeanUtils.copyProperties(dish, dishDto);

    //   Long categoryId = dish.getCategoryId();
    //   String categoryName = categoryService.getById(categoryId).getName();

    //   dishDto.setCategoryName(categoryName);
    //   dishes.add(dishDto);
    // }

    // write in functional programming way
    List<DishDto> dishes =
        records.stream()
            .map(
                (currDish) -> {
                  DishDto dishDto = new DishDto();
                  BeanUtils.copyProperties(currDish, dishDto);
                  Category category = categoryService.getById(currDish.getCategoryId());
                  if (category != null) {
                    String categoryName = category.getName();
                    dishDto.setCategoryName(categoryName);
                  } else dishDto.setCategoryName("NIL");
                  return dishDto;
                })
            .collect(Collectors.toList());
    ;

    dishDtoPage.setRecords(dishes);

    return Result.success(dishDtoPage);
  }

  @GetMapping("/list")
  public Result<List<DishDto>> getDishbyCategory(Dish dish) {
    Long categoryId = dish.getCategoryId();

    // get dishDto if it exists in Redis
    ValueOperations opsForValue = redisTemplate.opsForValue();

    String key = categoryId.toString();

    List<DishDto> dishDtos = (List<DishDto>) opsForValue.get(key);
    if (dishDtos != null) {
      log.info("key {} found in Redis", key);
      return Result.success(dishDtos);
    }

    // else it is currently not cashed in redis, we will get from db and update redis
    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
    queryWrapper.eq(Dish::getStatus, 1); // filter away the disabled ones
    List<Dish> dishes = dishService.list(queryWrapper);

    dishDtos =
        dishes.stream()
            .map(
                currDish -> {
                  // create dishDto object
                  DishDto dishDto = new DishDto();
                  BeanUtils.copyProperties(currDish, dishDto);

                  // get flavors for the currDish
                  Long dishid = dishDto.getId();
                  LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                  lambdaQueryWrapper
                      .eq(dishid != null, DishFlavor::getDishId, dishid)
                      .eq(DishFlavor::getIsDeleted, 0);
                  // select * from dishFlavor where dishId = ? and isDeleted = 0
                  List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
                  ;

                  // set flavor list to dishDto object
                  dishDto.setFlavors(flavors);

                  return dishDto;
                })
            .collect(Collectors.toList());
    opsForValue.set(key, dishDtos, 60, TimeUnit.MINUTES);

    return Result.success(dishDtos);
  }

  @PostMapping
  public Result<String> saveDish(@RequestBody DishDto dishWithFlavors) {

    dishService.saveWithFlavor(dishWithFlavors);

    String categoryId = dishWithFlavors.getCategoryId().toString();
    redisTemplate.delete(categoryId); // clear the cache in redis to ensure consistency

    return Result.success("the dish is saved successfully");
  }

  @PostMapping("/status/{newStatus}")
  public Result<String> toggleDishesStatus(@PathVariable int newStatus, @RequestParam String ids) {

    dishService.batchToggleStatus(newStatus, ids);

    return Result.success("Status toggled successfully");
  }

  @PutMapping
  public Result<String> updateDish(@RequestBody DishDto dishWithFlavors) {
    dishService.updateWithFlavor(dishWithFlavors);

    Long categoryId = dishWithFlavors.getCategoryId();
    redisTemplate.delete(categoryId); // clear the cache in redis to ensure consistency

    return Result.success("Dish edited successully");
  }

  @DeleteMapping
  public Result<String> deleteDishes(@RequestParam String ids) {
    dishService.batchDeleteDish(ids);

    return Result.success("Dishes deleted successfully");
  }
}
