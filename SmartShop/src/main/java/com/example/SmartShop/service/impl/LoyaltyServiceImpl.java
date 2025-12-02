package com.example.SmartShop.service.impl;

import com.example.SmartShop.entity.Client;
import com.example.SmartShop.entity.enums.CustomerTier;
import com.example.SmartShop.repository.OrderRepository;
import com.example.SmartShop.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LoyaltyServiceImpl implements LoyaltyService {

    private final OrderRepository orderRepository;

    @Override
    public void updateClientTier(Client client) {

        long totalOrders = orderRepository.countOrdersByClient(client.getId());
        BigDecimal totalSpent = orderRepository.sumConfirmedOrdersAmount(client.getId());

        // Eviter null
        if (totalSpent == null) totalSpent = BigDecimal.ZERO;

        CustomerTier newTier = CustomerTier.BASIC;

        if (totalOrders >= 20 || totalSpent.compareTo(new BigDecimal("15000")) >= 0) {
            newTier = CustomerTier.PLATINUM;
        } else if (totalOrders >= 10 || totalSpent.compareTo(new BigDecimal("5000")) >= 0) {
            newTier = CustomerTier.GOLD;
        } else if (totalOrders >= 3 || totalSpent.compareTo(new BigDecimal("1000")) >= 0) {
            newTier = CustomerTier.SILVER;
        }

        client.setTier(newTier);
    }

    @Override
    public BigDecimal applyTierDiscount(Client client, BigDecimal subTotal) {

        CustomerTier tier = client.getTier();

        return switch (tier) {

            case SILVER -> subTotal.compareTo(new BigDecimal("500")) >= 0
                    ? subTotal.multiply(new BigDecimal("0.05"))
                    : BigDecimal.ZERO;

            case GOLD -> subTotal.compareTo(new BigDecimal("800")) >= 0
                    ? subTotal.multiply(new BigDecimal("0.10"))
                    : BigDecimal.ZERO;

            case PLATINUM -> subTotal.compareTo(new BigDecimal("1200")) >= 0
                    ? subTotal.multiply(new BigDecimal("0.15"))
                    : BigDecimal.ZERO;

            default -> BigDecimal.ZERO; // BASIC
        };
    }
}
