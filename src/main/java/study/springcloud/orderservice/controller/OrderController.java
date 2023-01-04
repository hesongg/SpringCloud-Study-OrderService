package study.springcloud.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.springcloud.orderservice.dto.OrderDto;
import study.springcloud.orderservice.repository.OrderEntity;
import study.springcloud.orderservice.service.OrderService;
import study.springcloud.orderservice.vo.RequestOrder;
import study.springcloud.orderservice.vo.ResponseOrder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/order-service/")
@RestController
public class OrderController {

    private final Environment env;
    private final OrderService orderService;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in Order Service on Port %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createUser(@PathVariable String userId,
                                                    @RequestBody RequestOrder order) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(order, OrderDto.class);
        orderDto.setUserId(userId);

        OrderDto returnOrderDto = orderService.createOrder(orderDto);

        ResponseOrder responseOrder = mapper.map(returnOrderDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable String userId) {

        List<OrderEntity> orders = (List<OrderEntity>) orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = orders.stream()
                .map(order -> new ModelMapper().map(order, ResponseOrder.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
