package com.example.redis_caffeine.service.impl;

import com.example.redis_caffeine.annonation.DoubleCache;
import com.example.redis_caffeine.entity.Order;
import com.example.redis_caffeine.mapper.OrderMapper;
import com.example.redis_caffeine.service.OrderService;
import com.example.redis_caffeine.utils.CacheType;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 周同学
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional // 添加类级别事务
public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;

    private RedisTemplate<String, Object> redisTemplate;

    private Cache<String, Object> orderCache;

    //-----------------------------V1------------------------------------------
    /*@Override
    public Order getOrderById(Integer id) {
        String key = CacheConstant.ORDER + id;
        return (Order) orderCache.get(key, k -> {
            // 先查询 Redis
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj != null) {
                log.info("get data from redis");
                if (obj instanceof Order) {
                    return (Order) obj;
                } else {
                    // 如果不是Order类型，可能是反序列化问题，重新查询数据库
                    log.warn("Unexpected type from Redis, expected Order but got {}", obj.getClass());
                }
            }

            // Redis没有或类型不匹配则查询 DB
            log.info("get data from database");
            Order myOrder = orderMapper.getOrderById(id);
            redisTemplate.opsForValue().set(key, myOrder, 120, TimeUnit.SECONDS);
            return myOrder;
        });
    }*/


    /*@Override
    public void deleteOrderById(Integer id) {
        log.info("delete order");
        orderMapper.deleteOrderById(id);
        String key= CacheConstant.ORDER + id;
        redisTemplate.delete(key);
        orderCache.invalidate(key);
    }*/


    /*@Override
    public void updateOrder(Order order) {
        log.info("update order data");
        String key=CacheConstant.ORDER + order.getId();
        orderMapper.updateOrderById(order);
        //修改 Redis
        redisTemplate.opsForValue().set(key,order,120, TimeUnit.SECONDS);
        // 修改本地缓存
        orderCache.put(key,order);
    }*/


    //-----------------------------V2------------------------------------------
    /*@Cacheable(value = "order",key = "#id")
    @Override
    public Order getOrderById(Integer id) {
        String key = CacheConstant.ORDER + id;

        // 先查询 Redis
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj != null) {
            log.info("get data from redis");
            if (obj instanceof Order) {
                return (Order) obj;
            } else {
                // 如果不是Order类型，可能是反序列化问题，重新查询数据库
                log.warn("Unexpected type from Redis, expected Order but got {}", obj.getClass());
            }
        }

        // Redis没有或类型不匹配则查询 DB
        log.info("get data from database");
        Order myOrder = orderMapper.getOrderById(id);
        redisTemplate.opsForValue().set(key, myOrder, 120, TimeUnit.SECONDS);
        return myOrder;

    }*/

    /*@Override
    @CachePut(cacheNames = "order",key = "#order.id")
    public Order updateOrder(Order order) {
        log.info("update order data");
        orderMapper.updateOrderById(order);
        //修改 Redis
        redisTemplate.opsForValue().set(CacheConstant.ORDER + order.getId(),
                order, 120, TimeUnit.SECONDS);

        return order;
    }*/

    /*@Override
    @CacheEvict(cacheNames = "order",key = "#id")
    public void deleteOrderById(Integer id) {
        log.info("delete order");
        orderMapper.deleteOrderById(id);
        redisTemplate.delete(CacheConstant.ORDER + id);
    }*/



    //-----------------------------V3------------------------------------------
    /**
     * 根据订单ID获取订单信息
     * 使用 @DoubleCache 注解，类型为 FULL，会执行完整的缓存操作逻辑
     * @param id 订单ID
     * @return 订单对象
     */
    @Override
    @DoubleCache(cacheName = "order", key = "#id",
            type = CacheType.FULL)
    public Order getOrderById(Integer id) {
        return orderMapper.getOrderById(id);
    }

    /**
     * 更新订单信息
     * 使用 @DoubleCache 注解，类型为 PUT，会执行缓存更新操作
     * @param order 订单对象
     */
    @Override
    @DoubleCache(cacheName = "order", key = "#id",
            type = CacheType.PUT)
    public void updateOrder(Order order) {
        orderMapper.updateOrderById(order);
    }

    /**
     * 根据订单ID删除订单信息
     * 使用 @DoubleCache 注解，类型为 DELETE，会执行缓存删除操作
     * @param id 订单ID
     */
    @Override
    @DoubleCache(cacheName = "order", key = "#id",
            type = CacheType.DELETE)
    public void deleteOrderById(Integer id) {
        orderMapper.deleteOrderById(id);
    }
}

