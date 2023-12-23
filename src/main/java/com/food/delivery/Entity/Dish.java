package com.food.delivery.Entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Dish {

  private Long id;

  private String name;

  private Long categoryId;

  private BigDecimal price;

  private String code;

  // picture
  private String image;

  private String description;

  // 0 not selling anymore 1 selling
  private Integer status;

  // order
  private Integer sort;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @TableField(fill = FieldFill.INSERT)
  private Long createUser;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Long updateUser;

  // 是否删除
  private Integer isDeleted;
}
