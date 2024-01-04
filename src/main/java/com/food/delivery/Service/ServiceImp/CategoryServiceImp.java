package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.Category;
import com.food.delivery.Entity.Dish;
import com.food.delivery.Entity.Setmeal;
import com.food.delivery.Exception.CustomerizedException;
import com.food.delivery.Mapper.CategoryMapper;
import com.food.delivery.Service.CategoryService;
import com.food.delivery.Service.DishService;
import com.food.delivery.Service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImp extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

  @Autowired private DishService dishService;

  @Autowired private SetmealService setmealService;

  @Override
  public void remove(Long id) {

    // check if any dish binds with the category
    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Dish::getCategoryId, id);
    int countDish = dishService.count(queryWrapper);
    if (countDish > 0) {
      throw new CustomerizedException("The category is binded with some dishes");
    } // throw exception}

    // check if any setmeal binds with the category
    LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
    queryWrapper1.eq(Setmeal::getCategoryId, id);
    int countSetmeal = setmealService.count(queryWrapper1);
    if (countSetmeal > 0) {
      throw new CustomerizedException("The category is binded with some setmeals");
    } // throw exception

    // if no exception thrown, we can safely delete the category
    super.removeById(id);
  }
}
