package com.example.SmartShop.controller;


import com.example.SmartShop.dto.orderStatistic.ClientOrderHistoryDTO;
import com.example.SmartShop.dto.orderStatistic.ClientOrderStatisticsDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.service.ClientStatisticsService;
import com.example.SmartShop.service.UserService;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class ClientStatisticsController {

    private final ClientStatisticsService statisticsService;
    private final UserService userService;

    // -------------------------------
    // Vérification ADMIN
    // -------------------------------
    private void checkAdmin(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }
    }

    // -------------------------------
    // GET CLIENT ORDER STATISTICS (ADMIN ONLY)
    // -------------------------------
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ClientOrderStatisticsDTO> getClientStatistics(
            @PathVariable String clientId,
            HttpSession session
    ) {
        checkAdmin(session);
        ClientOrderStatisticsDTO stats = statisticsService.getClientStatistics(clientId);
        return ResponseEntity.ok(stats);
    }

    // -------------------------------
    // GET CLIENT ORDER HISTORY (ADMIN ONLY)
    // -------------------------------
    @GetMapping("/client/{clientId}/history")
    public ResponseEntity<List<ClientOrderHistoryDTO>> getClientOrderHistory(
            @PathVariable String clientId,
            HttpSession session
    ) {
        checkAdmin(session);
        List<ClientOrderHistoryDTO> history = statisticsService.getClientOrderHistory(clientId);
        return ResponseEntity.ok(history);
    }
}
