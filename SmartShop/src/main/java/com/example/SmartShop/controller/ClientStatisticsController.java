package com.example.SmartShop.controller;

import com.example.SmartShop.dto.clientStatistic.ClientOrderHistoryDTO;
import com.example.SmartShop.dto.clientStatistic.ClientOrderStatisticsDTO;
import com.example.SmartShop.dto.user.UserDTO;
import com.example.SmartShop.entity.enums.UserRole;
import com.example.SmartShop.exception.AccessDeniedException;
import com.example.SmartShop.service.ClientStatisticsService;
import com.example.SmartShop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Client Statistics", description = "Statistiques et historique des commandes des clients")
public class ClientStatisticsController {

    private final ClientStatisticsService statisticsService;
    private final UserService userService;

    private void checkAdmin(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé");
        }
    }

    private void checkClient(HttpSession session) {
        UserDTO currentUser = userService.getCurrentUser(session);
        if (currentUser.getRole() != UserRole.CLIENT) {
            throw new AccessDeniedException("Accès refusé");
        }
    }

    @Operation(summary = "Statistiques des commandes d'un client", description = "Retourne le nombre total de commandes, montant total dépensé, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ClientOrderStatisticsDTO> getClientStatistics(
            @PathVariable String clientId,
            HttpSession session
    ) {
        checkAdmin(session);
        ClientOrderStatisticsDTO stats = statisticsService.getClientStatistics(clientId);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Historique des commandes d'un client", description = "Retourne la liste des commandes passées par un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historique récupéré avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé pour les non-admins")
    })
    @GetMapping("/client/{clientId}/history")
    public ResponseEntity<List<ClientOrderHistoryDTO>> getClientOrderHistory(
            @PathVariable String clientId,
            HttpSession session
    ) {
        checkClient(session);
        List<ClientOrderHistoryDTO> history = statisticsService.getClientOrderHistory(clientId);
        return ResponseEntity.ok(history);
    }
}
