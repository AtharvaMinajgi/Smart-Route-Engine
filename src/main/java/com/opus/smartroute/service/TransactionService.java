package com.opus.smartroute.service;

import com.opus.smartroute.dto.SimulationResultDTO;
import com.opus.smartroute.dto.TransactionResponseDTO;
import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.Transaction;
import com.opus.smartroute.enums.RoutingType;
import com.opus.smartroute.repository.RouteRepository;
import com.opus.smartroute.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final RouteRepository routeRepository;
    private final SimulationService simulationService;
    private final TransactionRepository transactionRepository;
    private final MetricsService metricsService;

    public TransactionResponseDTO processTransaction(double amount) {

        // 1️⃣ TEMPORARY: Pick first active route
        List<Route> activeRoutes = routeRepository.findByIsActiveTrue();

        if (activeRoutes.isEmpty()) {
            throw new RuntimeException("No active routes available");
        }

        Route selectedRoute = activeRoutes.get(0);

        // 2️⃣ Simulate Transaction
        SimulationResultDTO simulation = simulationService.simulate(
                selectedRoute,
                amount
        );

        // 3️⃣ Save Transaction
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .route(selectedRoute)
                .routingType(RoutingType.INTELLIGENT)
                .result(simulation.getResult())
                .failureType(simulation.getFailureType())
                .latencyMs(simulation.getLatencyMs())
                .scoreUsed(null) // No scoring for now
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        // 4️⃣ Update Metrics
        metricsService.updateMetrics(selectedRoute);

        // 5️⃣ Return Response
        return TransactionResponseDTO.builder()
                .selectedRoute(selectedRoute.getName())
                .score(null)
                .result(simulation.getResult().name())
                .failureType(simulation.getFailureType().name())
                .latencyMs(simulation.getLatencyMs())
                .build();
    }
}