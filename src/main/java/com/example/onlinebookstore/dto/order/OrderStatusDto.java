package com.example.onlinebookstore.dto.order;

import com.example.onlinebookstore.model.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusDto {
    private Order.Status status;
}
