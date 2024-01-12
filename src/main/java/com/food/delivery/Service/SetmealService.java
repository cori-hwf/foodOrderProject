package com.food.delivery.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.food.delivery.DataTransferObject.SetmealDto;
import com.food.delivery.Entity.Setmeal;
import java.util.List;

public interface SetmealService extends IService<Setmeal> {
  public void saveSetmealWithDishes(SetmealDto setmealDto);

  public void batchDeleteSetmeals(List<Long> ids);

  public void toggleStatus(int newStatus, List<Long> ids);
}
