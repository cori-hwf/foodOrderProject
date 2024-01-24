package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.DataTransferObject.SetmealDto;
import com.food.delivery.Entity.Setmeal;
import com.food.delivery.Entity.SetmealDish;
import com.food.delivery.Exception.CustomerizedException;
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

  @Override
  public void batchDeleteSetmeals(List<Long> ids) {

    // check if status == 0 -> shall not delete
    LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
    // count(*) from Setmeal s where s.id in (id1,id2...) and s.status = 1
    queryWrapper.in(Setmeal::getId, ids);
    queryWrapper.eq(Setmeal::getStatus, 1);
    Integer count = this.count(queryWrapper);

    if (count > 0) {
      throw new CustomerizedException(
          "At least one of the setMeal is enabled. Disable all before deletion");
    }

    // delete the setmeal from setmeal table
    this.removeByIds(ids);
    // delete setmealDish from setmealDish table
    LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper();
    queryWrapper2.in(SetmealDish::getDishId, ids);
    setmealDishService.remove(queryWrapper2);
  }

  @Override
  public void toggleStatus(int newStatus, List<Long> ids) {

    LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.in(Setmeal::getId, ids);

    Setmeal setmeal = new Setmeal();
    setmeal.setStatus(newStatus);

    this.update(setmeal, lambdaQueryWrapper);
  }
}
