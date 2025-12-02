package com.example.SmartShop.service;

import com.example.SmartShop.entity.Client;

import java.math.BigDecimal;

public interface LoyaltyService {

    // calcule automatiquement le nouveau tier
    void updateClientTier(Client client);

    // applique la remise selon le tier
    BigDecimal applyTierDiscount(Client client, BigDecimal subTotal);

}
