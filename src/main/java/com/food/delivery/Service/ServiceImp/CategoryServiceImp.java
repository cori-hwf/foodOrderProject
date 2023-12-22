package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.Category;
import com.food.delivery.Mapper.CategoryMapper;
import com.food.delivery.Service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImp extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {}
