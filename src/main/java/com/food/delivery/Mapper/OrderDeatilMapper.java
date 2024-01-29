package com.food.delivery.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.food.delivery.Entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDeatilMapper extends BaseMapper<OrderDetail> {}
