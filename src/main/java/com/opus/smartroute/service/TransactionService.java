package com.opus.smartroute.service;

import com.opus.smartroute.dto.SimulationResultDTO;
import com.opus.smartroute.dto.TransactionResponseDTO;
import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.Transaction;
import com.opus.smartroute.enums.RoutingType;
import com.opus.smartroute.repository.TransactionRepository;
import com.opus.smartroute.service.ml.MLClientService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BanditService banditService;
    private final SimulationService simulationService;
    private final TransactionRepository transactionRepository;
    private final MetricsService metricsService;
    private final MLClientService mlClientService;

    public TransactionResponseDTO processTransaction(double amount) {

        // 1️⃣ Hybrid AI selects route
        Route selectedRoute = banditService.selectRouteWithML(
                amount,
                mlClientService
        );

        // 2️⃣ Simulate transaction outcome
        SimulationResultDTO simulation = simulationService.simulate(
                selectedRoute,
                amount
        );

        // 3️⃣ Save transaction
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .route(selectedRoute)
                .routingType(RoutingType.INTELLIGENT)
                .result(simulation.getResult())
                .failureType(simulation.getFailureType())
                .latencyMs(simulation.getLatencyMs())
                .scoreUsed(null)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        // 4️⃣ Update rolling metrics
        metricsService.updateMetrics(selectedRoute);

        // 5️⃣ Update bandit learning stats
        banditService.updateStats(selectedRoute, simulation.getResult());

        // 6️⃣ Return API response
        return TransactionResponseDTO.builder()
                .selectedRoute(selectedRoute.getName())
                .score(null)
                .result(simulation.getResult().name())
                .failureType(simulation.getFailureType().name())
                .latencyMs(simulation.getLatencyMs())
                .build();
    }
}