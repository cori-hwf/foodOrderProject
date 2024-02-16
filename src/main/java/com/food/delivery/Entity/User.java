package com.food.delivery.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("\"user\"")
public class User {

  private Long id;

  // 姓名
  private String name;

  // 手机号
  private String phone;

  // email
  private String email;

  // 0: female 1:male
  private String sex;

  private String idNumber;

  // profile picture
  private String avatar;

  // 0: disabled 1:enabled
  private Integer status;
}
