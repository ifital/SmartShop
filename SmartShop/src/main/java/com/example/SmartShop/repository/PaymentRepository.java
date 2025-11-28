package com.example.SmartShop.repository;

import com.example.SmartShop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    // Récupérer tous les paiements d'une commande spécifique
    List<Payment> findByOrderId(String orderId);

    // Vérifier le nombre de paiements existants pour une commande
    boolean existsByOrderId(String orderId);
}
