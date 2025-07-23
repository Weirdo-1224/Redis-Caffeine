package com.example.redis_caffeine.mapper;


import com.example.redis_caffeine.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    Order getOrderById(Integer id);
    int updateOrderById(Order order);
    int deleteOrderById(Integer id);
}
