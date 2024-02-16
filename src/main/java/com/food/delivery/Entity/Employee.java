package com.food.delivery.Entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.util.DigestUtils;

@Data
public class Employee {

  private Long id;

  private String name;

  private String username;

  private String password;

  private String phone;

  private String sex;

  private String idNumber;

  private Integer status;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @TableField(fill = FieldFill.INSERT)
  private Long createUser;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Long updateUser;

  public String decryptPassword(String password) {
    return DigestUtils.md5DigestAsHex(password.getBytes());
  }
}
