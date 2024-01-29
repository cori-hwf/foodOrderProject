package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.AddressBook;
import com.food.delivery.Entity.OrderDetail;
import com.food.delivery.Entity.Orders;
import com.food.delivery.Entity.ShoppingCart;
import com.food.delivery.Exception.CustomerizedException;
import com.food.delivery.Helper.BaseContext;
import com.food.delivery.Mapper.OrderMapper;
import com.food.delivery.Service.AddresBookService;
import com.food.delivery.Service.OrderDetailService;
import com.food.delivery.Service.OrderService;
import com.food.delivery.Service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImp extends ServiceImpl<OrderMapper, Orders> implements OrderService {

  @Autowired private OrderDetailService orderDetailService;

  @Autowired private ShoppingCartService shoppingCartService;

  @Autowired private AddresBookService addresBookService;

  @Transactional
  @Override
  public void submit(Orders order) {

    // get userId
    Long userId = BaseContext.getCurrentId();
    if (userId == null) throw new CustomerizedException("User not logged in");

    // get items from shopping cart based on userId
    LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
    List<ShoppingCart> shoppingCarts = shoppingCartService.list(lambdaQueryWrapper);

    if (shoppingCarts == null || shoppingCarts.size() == 0) {
      throw new CustomerizedException("No items in shoppingCart, you can't place order");
    }

    // get address
    Long addressBookId = order.getAddressBookId();
    AddressBook addressBook = addresBookService.getById(addressBookId);
    if (addressBook == null) {
      throw new CustomerizedException("addressBook not found");
    }

    // create orderId
    long orderId =
        IdWorker
            .getId(); // to avoid 2 loops we process shoppingCart first so that we need create the
    // oderId
    // accumulator totalAmount
    AtomicInteger totalAmt = new AtomicInteger(0);

    // process shoppingcarts to create orderDetail
    List<OrderDetail> orderDetails =
        shoppingCarts.stream()
            .map(
                shoppingCart -> {
                  OrderDetail orderDetail = new OrderDetail();
                  orderDetail.setOrderId(orderId);
                  orderDetail.setNumber(shoppingCart.getNumber());
                  orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
                  orderDetail.setDishId(shoppingCart.getDishId());
                  orderDetail.setSetmealId(shoppingCart.getSetmealId());
                  orderDetail.setName(shoppingCart.getName());
                  orderDetail.setImage(shoppingCart.getImage());
                  orderDetail.setAmount(shoppingCart.getAmount());
                  totalAmt.addAndGet(
                      shoppingCart
                          .getAmount()
                          .multiply(new BigDecimal(shoppingCart.getNumber()))
                          .intValue());
                  return orderDetail;
                })
            .collect(Collectors.toList());
    ;

    // save orders to order table
    order.setId(orderId);
    order.setOrderTime(LocalDateTime.now());
    order.setCheckoutTime(LocalDateTime.now());
    order.setStatus(2);
    order.setAmount(new BigDecimal(totalAmt.get())); // 总金额
    order.setUserId(userId);
    order.setConsignee(addressBook.getConsignee());
    order.setPhone(addressBook.getPhone());
    order.setAddress(
        (addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
            + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
            + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
            + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

    this.save(order);

    // for each item in shopping car create order detail object and insert into orderObject table
    orderDetails.forEach(
        orderDetail ->
            orderDetailService.save(orderDetail)); // orderDetailService.saveBatch(orderDetails)

    // delete shopping cart table based on userId
    // DELETE FROM shopping_cart WHERE (user_id = ?)
    shoppingCartService.remove(lambdaQueryWrapper);
  }
}
