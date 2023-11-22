package com.food.delivery.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.food.delivery.Entity.Employee;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService; 

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        String decrptedPassword = employee.decryptPassword(password);

        //query the database
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);//since username is unique -> getOne will be valid

        //employee not exist
        if (emp == null) return Result.error("The employee does not exist!");
        //Incorrect password
        if (!emp.getPassword().equals(decrptedPassword)) return Result.error("Incorrect password!");
        //Frozen account
        if (emp.getStatus() == 0) return Result.error("The user account is frozen!");
        //Successful
        request.getSession().setAttribute("currEmployee", emp.getId());
        return Result.success(emp);
    }
}
