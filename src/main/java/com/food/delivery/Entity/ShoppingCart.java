package com.food.delivery.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ShoppingCart {

  private Long id;

  private String name;

  private Long userId;

  private Long dishId;

  private Long setmealId;

  private String dishFlavor;

  private Integer number;

  private BigDecimal amount;

  private String image;

  private LocalDateTime createTime;
}
