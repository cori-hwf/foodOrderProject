package com.food.delivery.Mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.food.delivery.Entity.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    
}
