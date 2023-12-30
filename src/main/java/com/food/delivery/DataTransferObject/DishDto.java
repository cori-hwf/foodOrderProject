package com.food.delivery.DataTransferObject;

import com.food.delivery.Entity.Dish;
import com.food.delivery.Entity.DishFlavor;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DishDto extends Dish {
  private List<DishFlavor> flavors = new ArrayList<>();

  private String categoryName;
}
