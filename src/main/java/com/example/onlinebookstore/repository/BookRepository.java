package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.Book;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Book b SET b.isDeleted = true WHERE b.id = :id")
    void softDeleteById(Long id);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.categories c WHERE c.id=:categoryId")
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
