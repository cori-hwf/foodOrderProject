package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.ShoppingCart;
import com.food.delivery.Mapper.ShoppingCartMapper;
import com.food.delivery.Service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImp extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService {}
