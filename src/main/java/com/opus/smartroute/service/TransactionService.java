package com.opus.smartroute.service;

import com.opus.smartroute.dto.RoutingDecisionDTO;
import com.opus.smartroute.dto.SimulationResultDTO;
import com.opus.smartroute.dto.TransactionResponseDTO;
import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.Transaction;
import com.opus.smartroute.enums.RoutingType;
import com.opus.smartroute.repository.RouteRepository;
import com.opus.smartroute.repository.TransactionRepository;
import com.opus.smartroute.service.ml.MLClientService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BanditService banditService;
    private final SimulationService simulationService;
    private final TransactionRepository transactionRepository;
    private final RouteRepository routeRepository;
    private final MetricsService metricsService;
    private final MLClientService mlClientService;
    
    private int staticCounter = 0;

    public TransactionResponseDTO processTransaction(double amount, RoutingType mode) {

        Route selectedRoute;
        RoutingDecisionDTO decision = null;

        // 1️⃣ Route Selection
        if (mode == RoutingType.STATIC) {

            selectedRoute = selectStaticRoute();

        } else {

            decision = banditService.selectRouteWithML(amount, mlClientService);
            selectedRoute = decision.getRoute();
        }

        // 2️⃣ Simulate transaction
        SimulationResultDTO simulation =
                simulationService.simulate(selectedRoute, amount);

        // 3️⃣ Save transaction
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .route(selectedRoute)
                .routingType(mode)
                .result(simulation.getResult())
                .failureType(simulation.getFailureType())
                .latencyMs(simulation.getLatencyMs())
                .scoreUsed(decision != null ? decision.getScore() : null)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        // 4️⃣ Update rolling metrics
        metricsService.updateMetrics(selectedRoute);

        // 5️⃣ Update bandit only for intelligent mode
        if (mode == RoutingType.INTELLIGENT) {
            banditService.updateStats(selectedRoute, simulation.getResult());
        }

        // 6️⃣ Return API response
        return TransactionResponseDTO.builder()
                .selectedRoute(selectedRoute.getName())
                .score(decision != null ? decision.getScore() : null)
                .mlProbability(decision != null ? decision.getMlProbability() : null)
                .banditScore(decision != null ? decision.getBanditScore() : null)
                .result(simulation.getResult().name())
                .failureType(simulation.getFailureType().name())
                .latencyMs(simulation.getLatencyMs())
                .build();
    }

    /**
     * Static baseline routing (always AXIS)
     */
    private Route selectStaticRoute() {

        List<Route> activeRoutes = routeRepository.findByIsActiveTrue();

        if (activeRoutes.isEmpty()) {
            throw new RuntimeException("No active routes available");
        }

        Route selected = activeRoutes.get(staticCounter % activeRoutes.size());

        staticCounter++;

        return selected;
    }
}