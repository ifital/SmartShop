package com.example.SmartShop.repository;

import com.example.SmartShop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByName(String name);
}
