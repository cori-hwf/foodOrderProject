package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.Orders;
import com.food.delivery.Mapper.OrderMapper;
import com.food.delivery.Service.OrderService;

public class OrderServiceImp extends ServiceImpl<OrderMapper, Orders> implements OrderService {}
