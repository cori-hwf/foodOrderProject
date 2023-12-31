package com.food.delivery.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.food.delivery.DataTransferObject.DishDto;
import com.food.delivery.Entity.Dish;

public interface DishService extends IService<Dish> {
  public void saveWithFlavor(DishDto dishDto);

  public DishDto getDishWithFlavor(Long id);

  public void updateWithFlavor(DishDto dishDto);

  public void batchToggleStatus(int newStatus, String ids);

  public void batchDeleteDish(String ids);
}
