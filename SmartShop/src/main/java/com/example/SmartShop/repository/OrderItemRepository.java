package com.example.SmartShop.repository;

import com.example.SmartShop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    // Récupérer tous les items d'une commande spécifique
    List<OrderItem> findByOrderId(String orderId);

    // Vérifier si un produit existe déjà dans une commande (optionnel)
    boolean existsByOrderIdAndProductId(String orderId, String productId);

    boolean existsByProductId(String productId);

}
