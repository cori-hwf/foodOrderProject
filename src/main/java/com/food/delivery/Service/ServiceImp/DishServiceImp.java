package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.DataTransferObject.DishDto;
import com.food.delivery.Entity.Dish;
import com.food.delivery.Entity.DishFlavor;
import com.food.delivery.Mapper.DishMapper;
import com.food.delivery.Service.DishFlavorService;
import com.food.delivery.Service.DishService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DishServiceImp extends ServiceImpl<DishMapper, Dish> implements DishService {

  @Autowired DishFlavorService dishFlavorService;

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
}
