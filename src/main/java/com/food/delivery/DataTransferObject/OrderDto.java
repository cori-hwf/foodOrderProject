package com.food.delivery.DataTransferObject;

import com.food.delivery.Entity.OrderDetail;
import com.food.delivery.Entity.Orders;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto extends Orders {
  private List<OrderDetail> orderDetails;
}
