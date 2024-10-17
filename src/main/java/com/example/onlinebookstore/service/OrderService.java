package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.OrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderStatusDto;
import com.example.onlinebookstore.dto.orderitem.OrderItemDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(Long userId, OrderRequestDto requestDto);

    List<OrderDto> findAll();

    List<OrderItemDto> getOrderItemsByOrderId(Long orderId);

    OrderItemDto getItemById(Long orderId, Long itemId);

    OrderDto updateStatus(Long orderId, OrderStatusDto statusDto);
}