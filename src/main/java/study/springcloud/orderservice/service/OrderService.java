package study.springcloud.orderservice.service;

import study.springcloud.orderservice.dto.OrderDto;
import study.springcloud.orderservice.repository.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto getOrderByOrderId(String orderId);

    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
