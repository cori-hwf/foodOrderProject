package com.food.delivery.Entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

// intermediate table between setMeal & dish
@Data
public class SetmealDish implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;

  private Long setmealId;

  private Long dishId;

  private String name;

  private BigDecimal price;

  private Integer copies;

  private Integer sort;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @TableField(fill = FieldFill.INSERT)
  private Long createUser;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Long updateUser;

  private Integer isDeleted;
}
