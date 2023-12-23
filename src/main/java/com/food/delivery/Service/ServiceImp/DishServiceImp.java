package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.Dish;
import com.food.delivery.Mapper.DishMapper;
import com.food.delivery.Service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImp extends ServiceImpl<DishMapper, Dish> implements DishService {}
