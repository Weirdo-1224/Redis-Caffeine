package com.example.redis_caffeine.service;

import com.example.redis_caffeine.entity.Order;

public interface OrderService {

    Order getOrderById(Integer id);

    //Order updateOrder(Order order);
    void updateOrder(Order order);

    void deleteOrderById(Integer id);
}
