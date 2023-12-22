package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.delivery.Entity.Employee;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.EmployeeService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired private EmployeeService employeeService;

  @PostMapping("/login")
  public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
    String password = employee.getPassword();
    String decrptedPassword = employee.decryptPassword(password);

    // query the database
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Employee::getUsername, employee.getUsername());
    Employee emp =
        employeeService.getOne(queryWrapper); // since username is unique -> getOne will be valid

    // employee not exist
    if (emp == null) return Result.error("The employee does not exist!");
    // Incorrect password
    if (!emp.getPassword().equals(decrptedPassword)) return Result.error("Incorrect password!");
    // Frozen account
    if (emp.getStatus() == 0) return Result.error("The user account is frozen!");
    // Successful
    request.getSession().setAttribute("currEmployee", emp.getId());
    return Result.success(emp);
  }

  @PostMapping("logout")
  public Result<String> logout(HttpServletRequest request) {
    // clear the attribute from the session
    request.getSession().removeAttribute("currEmployee");
    return Result.success("Logout successfully.");
  }

  @PostMapping
  public Result<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
    // Since time and user setting are common fields, we let Basecontext and MetaObjectHandler to
    // handle to remove repetitive codes
    // set time
    // LocalDateTime currTime = LocalDateTime.now();
    // employee.setCreateTime(currTime);
    // employee.setUpdateTime(currTime);

    // set users
    // Long currEmployee =
    //     (Long)
    //         request
    //             .getSession()
    //             .getAttribute("currEmployee"); // getAttribute will return an Object type
    // employee.setCreateUser(currEmployee);
    // employee.setUpdateUser(currEmployee);

    // set default password
    String encryptedPassword = employee.decryptPassword("1234567");
    employee.setPassword(encryptedPassword);

    // no need to set default status as it is handled by the db
    employeeService.save(employee);

    return Result.success("employee successfully added");
  }

  @GetMapping("/page")
  public Result<Page> listEmployee(
      @RequestParam int page, @RequestParam int pageSize, String name) {

    // 构建分页构造器
    Page pageInfo = new Page(page, pageSize);

    // 构建条件构造器
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    // 添加过滤条件
    queryWrapper.like(
        StringUtils.isNotEmpty(name),
        Employee::getName,
        name); // the first boolean value is true -> add the condition to the query, else nothing
    // happen

    queryWrapper.orderByDesc(Employee::getUpdateTime);

    // 执行查询
    employeeService.page(
        pageInfo,
        queryWrapper); // need not return anything, it will save the queried result in the pageInfo

    return Result.success(pageInfo);
  }

  // this function is generalized to be used for any updates towards employee
  @PutMapping
  public Result<String> updateEmployee(HttpServletRequest request, @RequestBody Employee employee) {

    // these common fields are handled by MetaObjectHandler
    // log.info(employee.toString());
    // Long currUserId = (Long) request.getSession().getAttribute("currEmployee");
    // employee.setUpdateTime(LocalDateTime.now());
    // employee.setUpdateUser(currUserId);

    // LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    // queryWrapper.eq(Employee::getId, employee.getId());
    // employeeService.update(employee, queryWrapper);
    // easier way to update
    employeeService.updateById(employee);
    return Result.success("Employee updated successfully.");
  }

  @GetMapping("/{id}")
  public Result<Employee> getEmployeeById(@PathVariable Long id) {
    Employee empInfo = employeeService.getById(id);
    if (empInfo == null) return Result.error("The employee can not found");
    return Result.success(empInfo);
  }
}
