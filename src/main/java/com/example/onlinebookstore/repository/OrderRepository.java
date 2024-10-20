package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
