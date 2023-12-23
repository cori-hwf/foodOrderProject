package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.Setmeal;
import com.food.delivery.Mapper.SetmealMapper;
import com.food.delivery.Service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImp extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService {}
