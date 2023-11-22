package com.food.delivery.Service.ServiceImp;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.Employee;
import com.food.delivery.Mapper.EmployeeMapper;
import com.food.delivery.Service.EmployeeService;

@Service
public class EmployeeServiceImp extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
    
}
