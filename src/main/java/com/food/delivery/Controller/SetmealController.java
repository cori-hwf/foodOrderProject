package com.food.delivery.Controller;

import com.food.delivery.DataTransferObject.SetmealDto;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

  @Autowired SetmealService setmealService;

  @PostMapping
  public Result<String> saveSetmeal(@RequestBody SetmealDto setmealDto) {

    setmealService.saveSetmealWithDishes(setmealDto);

    return Result.success("Setmeal added successfully.");
  }
}
