package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Category c SET c.isDeleted = true WHERE c.id = :id")
    void softDeleteById(Long id);
}
