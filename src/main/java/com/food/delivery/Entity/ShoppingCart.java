package com.food.delivery.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ShoppingCart implements Serializable {

  private static final long serialVersionUID = 1L;
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
