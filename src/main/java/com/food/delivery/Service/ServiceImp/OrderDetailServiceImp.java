package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.OrderDetail;
import com.food.delivery.Mapper.OrderDeatilMapper;
import com.food.delivery.Service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImp extends ServiceImpl<OrderDeatilMapper, OrderDetail>
    implements OrderDetailService {}
