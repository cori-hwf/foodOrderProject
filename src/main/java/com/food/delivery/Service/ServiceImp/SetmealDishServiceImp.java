package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.SetmealDish;
import com.food.delivery.Mapper.SetmealDishMapper;
import com.food.delivery.Service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImp extends ServiceImpl<SetmealDishMapper, SetmealDish>
    implements SetmealDishService {}
