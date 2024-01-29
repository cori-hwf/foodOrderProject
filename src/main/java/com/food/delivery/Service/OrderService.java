package com.food.delivery.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.food.delivery.Entity.Orders;

public interface OrderService extends IService<Orders> {
  public void submit(Orders order);
}
