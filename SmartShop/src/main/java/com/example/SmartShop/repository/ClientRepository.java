package com.example.SmartShop.repository;

import com.example.SmartShop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {

    boolean existsByEmail(String email);
}
