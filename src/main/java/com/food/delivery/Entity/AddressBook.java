package com.food.delivery.Entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import lombok.Data;

/** 地址簿 */
@Data
public class AddressBook {

  private Long id;

  private Long userId;

  private String consignee;

  private String phone;

  // 0 female 1 male
  private String sex;

  private String provinceCode;

  private String provinceName;

  private String cityCode;

  private String cityName;

  private String districtCode;

  private String districtName;

  // address detail
  private String detail;

  private String label;

  // default address 0:yes 1:no
  private Integer isDefault;

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
