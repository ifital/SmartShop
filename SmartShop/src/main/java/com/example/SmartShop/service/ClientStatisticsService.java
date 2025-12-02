package com.example.SmartShop.service;



import com.example.SmartShop.dto.clientStatistic.ClientOrderHistoryDTO;
import com.example.SmartShop.dto.clientStatistic.ClientOrderStatisticsDTO;

import java.util.List;

public interface ClientStatisticsService {

    ClientOrderStatisticsDTO getClientStatistics(String clientId);

    List<ClientOrderHistoryDTO> getClientOrderHistory(String clientId);
}
