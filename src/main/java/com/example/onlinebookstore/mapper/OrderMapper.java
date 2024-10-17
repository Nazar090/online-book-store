package com.example.onlinebookstore.mapper;

import com.example.onlinebookstore.config.MapperConfig;
import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.OrderRequestDto;
import com.example.onlinebookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = { OrderItemMapper.class })
public interface OrderMapper {
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);

    Order toEntity(OrderRequestDto requestDto);
}
