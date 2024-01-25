package com.food.delivery.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.food.delivery.Entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {}
