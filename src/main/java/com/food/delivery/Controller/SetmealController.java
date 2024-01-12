package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.delivery.DataTransferObject.SetmealDto;
import com.food.delivery.Entity.Category;
import com.food.delivery.Entity.Setmeal;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.CategoryService;
import com.food.delivery.Service.SetmealService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

  @Autowired SetmealService setmealService;

  @Autowired CategoryService categoryService;

  @GetMapping("/page")
  public Result<Page> getSetmealPage(
      @RequestParam int page, @RequestParam int pageSize, String name) {
    // log.info("{print page: {} pageSize: {} and name: {}}",page,pageSize,name);
    Page<Setmeal> setmealPage = new Page<>(page, pageSize);

    LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper
        .like(StringUtils.isNotEmpty(name), Setmeal::getName, name)
        .orderByDesc(Setmeal::getUpdateTime);

    setmealService.page(setmealPage, queryWrapper);

    Page<SetmealDto> setmealDtoPage = new Page(page, pageSize);
    BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");

    List<Setmeal> setmealRecords = setmealPage.getRecords();
    List<SetmealDto> setmealDtoRecords =
        setmealRecords.stream()
            .map(
                setmeal -> {
                  SetmealDto setmealDto = new SetmealDto();
                  BeanUtils.copyProperties(setmeal, setmealDto);

                  Long categoryId = setmeal.getCategoryId();
                  Category category = categoryService.getById(categoryId);
                  if (category != null) setmealDto.setCategoryName(category.getName());
                  else setmealDto.setCategoryName("Category not found");
                  return setmealDto;
                })
            .collect(Collectors.toList());

    setmealDtoPage.setRecords(setmealDtoRecords);

    return Result.success(setmealDtoPage);
  }

  @PostMapping
  public Result<String> saveSetmeal(@RequestBody SetmealDto setmealDto) {

    setmealService.saveSetmealWithDishes(setmealDto);

    return Result.success("Setmeal added successfully.");
  }

  @PostMapping("/status/{newStatus}")
  public Result<String> toggleStatus(@PathVariable int newStatus, @RequestParam List<Long> ids) {
    setmealService.toggleStatus(newStatus, ids);
    return Result.success("Setmeal status toggled successfully");
  }

  @DeleteMapping
  public Result<String> deleteSetmeals(@RequestParam List<Long> ids) {
    setmealService.batchDeleteSetmeals(ids);
    return Result.success("Setmeals deleted successfully");
  }
}
