package com.example.onlinebookstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE employees SET is_deleted = true WHERE id=?")
@SQLRestriction(value = "is_deleted=false")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @JsonProperty("first_name")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @JsonProperty("last_name")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @JsonProperty("shipping_address")
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    private boolean isDeleted = false;
}
