package com.food.delivery.Entity;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderDetail {

  private Long id;

  // 名称
  private String name;

  // 订单id
  private Long orderId;

  // 菜品id
  private Long dishId;

  // 套餐id
  private Long setmealId;

  // 口味
  private String dishFlavor;

  // 数量
  private Integer number;

  // 金额
  private BigDecimal amount;

  // 图片
  private String image;
}
