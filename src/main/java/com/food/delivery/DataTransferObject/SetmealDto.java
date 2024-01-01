package com.food.delivery.DataTransferObject;

import com.food.delivery.Entity.Setmeal;
import com.food.delivery.Entity.SetmealDish;
import java.util.List;
import lombok.Data;

@Data
public class SetmealDto extends Setmeal {

  private List<SetmealDish> setmealDishes;

  private String categoryName;
}
