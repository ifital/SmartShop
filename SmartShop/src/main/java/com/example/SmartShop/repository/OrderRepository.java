package com.example.SmartShop.repository;

import com.example.SmartShop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByClientId(String clientId);

    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.client.id = :clientId
    """)
    Long countOrdersByClient(String clientId);

    @Query("""
        SELECT COALESCE(SUM(o.totalTtc), 0)
        FROM Order o
        WHERE o.client.id = :clientId AND o.status = com.example.SmartShop.entity.enums.OrderStatus.CONFIRMED
    """)
    java.math.BigDecimal sumConfirmedOrdersAmount(String clientId);

    @Query("""
        SELECT MIN(o.createdAt)
        FROM Order o
        WHERE o.client.id = :clientId
    """)
    java.time.LocalDateTime getFirstOrderDate(String clientId);

    @Query("""
        SELECT MAX(o.createdAt)
        FROM Order o
        WHERE o.client.id = :clientId
    """)
    java.time.LocalDateTime getLastOrderDate(String clientId);

    // Historique
    List<Order> findByClientIdOrderByCreatedAtDesc(String clientId);
}
