package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.delivery.DataTransferObject.OrderDto;
import com.food.delivery.Entity.OrderDetail;
import com.food.delivery.Entity.Orders;
import com.food.delivery.Helper.BaseContext;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.OrderDetailService;
import com.food.delivery.Service.OrderService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {
  @Autowired private OrderService orderService;

  @Autowired private OrderDetailService orderDetailService;

  @PostMapping("/submit")
  public Result<String> submit(@RequestBody Orders orders) {

    orderService.submit(orders);
    return Result.success("Order placed successfully");
  }

  @GetMapping("/userPage")
  public Result<Page> get(@RequestParam Long page, @RequestParam Long pageSize) {

    Long userId = BaseContext.getCurrentId();
    if (userId == null) return Result.error("User not logged in");
    Page<Orders> ordersPage = new Page<>(page, pageSize);
    Page<OrderDto> orderDtoPage = new Page<>(page, pageSize);

    LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(Orders::getUserId, userId).orderByDesc(Orders::getOrderTime);
    List<Orders> orderLists = orderService.list(lambdaQueryWrapper);

    List<OrderDto> orderDtos =
        orderLists.stream()
            .map(
                currOrder -> {
                  OrderDto orderDto = new OrderDto();
                  BeanUtils.copyProperties(currOrder, orderDto);

                  Long orderId = currOrder.getId();
                  LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                  lambdaQueryWrapper2.eq(OrderDetail::getOrderId, orderId);
                  List<OrderDetail> orderDetails = orderDetailService.list(lambdaQueryWrapper2);

                  orderDto.setOrderDetails(orderDetails);
                  return orderDto;
                })
            .collect(Collectors.toList());
    ;

    orderDtoPage.setRecords(orderDtos);
    return Result.success(orderDtoPage);
  }
}
