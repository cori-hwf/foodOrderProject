package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.delivery.DataTransferObject.DishDto;
import com.food.delivery.Entity.Category;
import com.food.delivery.Entity.Dish;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.CategoryService;
import com.food.delivery.Service.DishService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  @GetMapping("/page")
  private Result<Page> getDishPage(
      @RequestParam(required = true) int page,
      @RequestParam(required = true) int pageSize,
      String name) {

    Page<Dish> pageInfo = new Page<>(page, pageSize);
    Page<DishDto> dishDtoPage = new Page<>();

    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper.like(name != null, Dish::getName, name);

    queryWrapper.orderByAsc(Dish::getSort);

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

  @PostMapping
  public Result<String> saveDish(@RequestBody DishDto dishWithFlavors) {

    dishService.saveWithFlavor(dishWithFlavors);

    return Result.success("the dish is saved successfully");
  }
}
