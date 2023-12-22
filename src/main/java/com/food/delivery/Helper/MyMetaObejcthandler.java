package com.food.delivery.Helper;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

// For common fields that are annotated with @TableField in entity file
// these functions will be called before executing the sql query to fill up the common fileds

@Component
@Slf4j
public class MyMetaObejcthandler implements MetaObjectHandler {
  @Override
  public void insertFill(MetaObject metaObject) {
    LocalDateTime currTime = LocalDateTime.now();
    metaObject.setValue("createTime", currTime);
    metaObject.setValue("createUser", BaseContext.getCurrentId());
    metaObject.setValue("updateTime", currTime);
    metaObject.setValue("updateUser", BaseContext.getCurrentId());
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    LocalDateTime currTime = LocalDateTime.now();
    metaObject.setValue("updateTime", currTime);
    metaObject.setValue("updateUser", BaseContext.getCurrentId());
  }
}
