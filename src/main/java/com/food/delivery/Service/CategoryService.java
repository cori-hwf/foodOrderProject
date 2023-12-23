package com.food.delivery.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.food.delivery.Entity.Category;

public interface CategoryService extends IService<Category> {
  public void remove(Long id);
}
