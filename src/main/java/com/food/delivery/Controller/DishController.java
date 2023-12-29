package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.delivery.DataTransferObject.DishDto;
import com.food.delivery.Entity.Dish;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.DishService;
import lombok.extern.slf4j.Slf4j;
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

  @GetMapping("/page")
  private Result<Page> getDishPage(
      @RequestParam(required = true) int page, @RequestParam(required = true) int pageSize) {

    Page<Dish> pageInfo = new Page<>(page, pageSize);

    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByAsc(Dish::getSort);
    dishService.page(pageInfo, queryWrapper);

    return Result.success(pageInfo);
  }

  @PostMapping
  public Result<String> saveDish(@RequestBody DishDto dishWithFlavors) {

    dishService.saveWithFlavor(dishWithFlavors);

    return Result.success("the dish is saved successfully");
  }
}
