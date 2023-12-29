package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.delivery.Entity.Category;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.CategoryService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
  @Autowired private CategoryService categoryService;

  @PostMapping
  public Result<String> addCategory(@RequestBody Category category) {
    categoryService.save(category);
    return Result.success("Category added successfully");
  }

  @GetMapping("/page")
  public Result<Page> getCategoryPage(
      @RequestParam(required = true) int page, @RequestParam(required = true) int pageSize) {
    Page<Category> pageInfo = new Page(page, pageSize);

    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByAsc(Category::getSort);

    categoryService.page(pageInfo, queryWrapper);
    log.info(pageInfo.toString());
    return Result.success(pageInfo);
  }

  @GetMapping("/list")
  public Result<List<Category>> listCategory(@RequestParam Integer type) {

    LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(type != null, Category::getType, type);
    lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

    List<Category> list = categoryService.list(lambdaQueryWrapper);

    return Result.success(list);
  }

  @DeleteMapping
  public Result<String> deleteCategory(@RequestParam Long ids) {
    categoryService.remove(ids);
    return Result.success("The category is deleted successfully.");
  }

  @PutMapping
  public Result<String> editCategory(@RequestBody Category updatedCategory) {
    // log.info(updatedCategory.toString());
    categoryService.updateById(updatedCategory);
    return Result.success("The category is updated successfully");
  }
}
