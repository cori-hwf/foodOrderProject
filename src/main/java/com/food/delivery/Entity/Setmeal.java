package com.food.delivery.Entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Setmeal {

  private Long id;

  private Long categoryId;

  // setmeal name
  private String name;

  private BigDecimal price;

  // 状态 0:not in use 1:in use
  private Integer status;

  private String code;

  private String description;

  // picture
  private String image;

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
