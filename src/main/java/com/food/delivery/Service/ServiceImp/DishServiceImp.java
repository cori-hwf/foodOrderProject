package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.DataTransferObject.DishDto;
import com.food.delivery.Entity.Dish;
import com.food.delivery.Entity.DishFlavor;
import com.food.delivery.Exception.CustomerizedException;
import com.food.delivery.Mapper.DishMapper;
import com.food.delivery.Service.DishFlavorService;
import com.food.delivery.Service.DishService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DishServiceImp extends ServiceImpl<DishMapper, Dish> implements DishService {

  @Autowired DishFlavorService dishFlavorService;

  @Autowired RedisTemplate redisTemplate;

  @Transactional
  @Override
  public void saveWithFlavor(DishDto dishDto) {
    this.save(dishDto);

    Long id = dishDto.getId(); // after saving the dish, id will be filled in back automatically

    List<DishFlavor> dishFlavors = dishDto.getFlavors();

    List<DishFlavor> dishFlavors2 =
        dishFlavors.stream()
            .map(
                dishFlavor -> {
                  dishFlavor.setDishId(id);
                  return dishFlavor;
                })
            .collect(Collectors.toList());

    dishFlavorService.saveBatch(dishFlavors2); // save the dishflavors
  }

  @Override
  public DishDto getDishWithFlavor(Long id) {

    Dish dish = this.getById(id);

    if (dish == null) throw new CustomerizedException("Dish can not be found in database");

    LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(DishFlavor::getDishId, id);
    List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);

    DishDto dishDto = new DishDto();
    BeanUtils.copyProperties(dish, dishDto);
    dishDto.setFlavors(dishFlavors);

    return dishDto;
  }

  @Override
  @Transactional
  public void updateWithFlavor(DishDto dishDto) {
    // update the dish info
    this.updateById(dishDto);

    // delete the dish flavor
    LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
    lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
    dishFlavorService.remove(lambdaQueryWrapper);

    // add the updated dish flavors
    List<DishFlavor> flavors = dishDto.getFlavors();
    Long dishId = dishDto.getId();
    List<DishFlavor> flavorsWithDishId =
        flavors.stream()
            .map(
                (dishFlavor) -> {
                  dishFlavor.setDishId(dishId);
                  return dishFlavor;
                })
            .collect(Collectors.toList());
    dishFlavorService.saveBatch(flavorsWithDishId);
  }

  @Override
  public void batchToggleStatus(int newStatus, String ids) {
    String[] splitIds = ids.split(",");
    for (String id : splitIds) {
      Dish currDish = this.getById(Long.parseLong(id));
      String categoryId = currDish.getCategoryId().toString();
      if (redisTemplate.hasKey(categoryId)) {
        redisTemplate.delete(categoryId);
      } // clear the cache in redis to ensure consistency
      currDish.setStatus(newStatus);
      this.updateById(currDish);
    }
  }

  @Override
  @Transactional
  public void batchDeleteDish(String ids) {
    String categoryId;
    String[] splitIds = ids.split(",");
    for (String id : splitIds) {

      long longId = Long.parseLong(id);

      // if status of the Dish is 1 (enabled) -> shall not be deleted
      Dish currDish = this.getById(longId);
      categoryId = currDish.getCategoryId().toString();
      log.info("currDish: {} , status equal to 1: {}", currDish, currDish.getStatus() == 1);
      if (currDish != null && (currDish.getStatus() == 1))
        throw new CustomerizedException(
            "At least one of the dish is currently enabled. Disable all dishes before deletion!");

      // delete the dish
      this.removeById(longId);

      // clear the cache in redis to ensure consistency
      if (redisTemplate.hasKey(categoryId)) {
        redisTemplate.delete(categoryId);
      } // clear the cache in redis to ensure consistency

      // delete the related dish flavors
      LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
      lambdaQueryWrapper.eq(DishFlavor::getDishId, longId);
      dishFlavorService.remove(lambdaQueryWrapper);
    }
  }
}
