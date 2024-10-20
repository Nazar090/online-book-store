package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dto.order.OrderDto;
import com.example.onlinebookstore.dto.order.OrderRequestDto;
import com.example.onlinebookstore.dto.order.OrderStatusDto;
import com.example.onlinebookstore.dto.orderitem.OrderItemDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Operations related to orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place a new order",
            description = "Creates and places a new order in the system")
    public OrderDto createOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.createOrder(getUserId(), requestDto);
    }

    @GetMapping
    @Operation(summary = "Get Order Item Details",
            description = "Retrieve the details of a specific item within an order"
                    + " using the order ID and item ID.")
    public List<OrderDto> getAll() {
        return orderService.findAll();
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get Order Items",
            description = "Retrieve a list of all items within a specific order by order ID.")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Get Order Item by ID",
            description = "Retrieve a specific item within an order "
                    + "using the order ID and item ID.")
    public OrderItemDto getItemById(@PathVariable Long orderId, @PathVariable Long id) {
        return orderService.getItemById(orderId, id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Order Status",
            description = "Update the status of a specific order by order ID.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDto updateStatus(@PathVariable Long id,
                                 @RequestBody @Valid OrderStatusDto statusDto) {
        return orderService.updateStatus(id, statusDto);
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        throw new EntityNotFoundException(
                "Can't find authentication name by authentication " + authentication);
    }
}
