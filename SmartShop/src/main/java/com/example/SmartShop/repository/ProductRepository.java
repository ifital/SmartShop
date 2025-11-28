package com.example.SmartShop.repository;

import com.example.SmartShop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByNameAndDeletedFalse(String name);

    Page<Product> findAllByDeletedFalse(Pageable pageable);

    boolean existsByIdAndDeletedFalse(String id);
}
