package com.example.SmartShop.repository;

import com.example.SmartShop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client, String> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
