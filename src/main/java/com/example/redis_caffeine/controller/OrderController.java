package com.example.redis_caffeine.controller;

import com.example.redis_caffeine.entity.Order;
import com.example.redis_caffeine.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 获取订单
    @GetMapping("/order/{id}")
    public Order getOrder(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    // 更新订单
    @PutMapping("/order/{id}")
    public void updateOrder(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        orderService.updateOrder(order);
    }

    // 删除订单
    @DeleteMapping("/order/{id}")
    public void deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrderById(id);
    }
}