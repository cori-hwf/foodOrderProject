package com.food.delivery.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.food.delivery.Entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}
