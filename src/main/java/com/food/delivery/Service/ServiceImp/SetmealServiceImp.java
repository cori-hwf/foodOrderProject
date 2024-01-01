package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.DataTransferObject.SetmealDto;
import com.food.delivery.Entity.Setmeal;
import com.food.delivery.Entity.SetmealDish;
import com.food.delivery.Mapper.SetmealMapper;
import com.food.delivery.Service.SetmealDishService;
import com.food.delivery.Service.SetmealService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SetmealServiceImp extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService {

  @Autowired SetmealDishService setmealDishService;

  @Override
  @Transactional
  public void saveSetmealWithDishes(SetmealDto setmealDto) {

    // save setMeal table
    this.save(setmealDto);

    // save setMealDish table
    List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

    setmealDishes.stream()
        .map(
            setmealDish -> {
              setmealDish.setSetmealId(setmealDto.getId());
              return setmealDish;
            })
        .collect(Collectors.toList());

    setmealDishService.saveBatch(setmealDishes);
  }
}
