package com.example.onlinebookstore.dto.order;

import com.example.onlinebookstore.model.Order;
import com.example.onlinebookstore.validation.ValidStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusDto {
    @NotNull
    @ValidStatus(message = "Invalid order status. "
            + "Valid values are: PROCESSING, DELIVERED, COMPLETED")
    private Order.Status status;
}
