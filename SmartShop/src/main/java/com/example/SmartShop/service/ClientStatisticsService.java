package com.example.SmartShop.service;



import com.example.SmartShop.dto.orderStatistic.ClientOrderHistoryDTO;
import com.example.SmartShop.dto.orderStatistic.ClientOrderStatisticsDTO;

import java.util.List;

public interface ClientStatisticsService {

    ClientOrderStatisticsDTO getClientStatistics(String clientId);

    List<ClientOrderHistoryDTO> getClientOrderHistory(String clientId);
}
