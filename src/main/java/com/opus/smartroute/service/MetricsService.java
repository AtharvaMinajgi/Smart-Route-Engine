package com.opus.smartroute.service;

import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.RouteMetrics;
import com.opus.smartroute.entity.Transaction;
import com.opus.smartroute.enums.FailureType;
import com.opus.smartroute.enums.TransactionResult;
import com.opus.smartroute.repository.RouteMetricsRepository;
import com.opus.smartroute.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final TransactionRepository transactionRepository;
    private final RouteMetricsRepository routeMetricsRepository;

    private static final int WINDOW_SIZE = 100;

    public void updateMetrics(Route route) {

        List<Transaction> recentTransactions =
                transactionRepository.findTop100ByRouteOrderByCreatedAtDesc(route);

        if (recentTransactions.isEmpty()) {
            return;
        }

        int approvedCount = 0;
        int timeoutCount = 0;
        int totalConsidered = 0;
        double totalLatency = 0;

        for (Transaction tx : recentTransactions) {

            totalLatency += tx.getLatencyMs();

            // Only penalize route-level failures
            if (tx.getResult() == TransactionResult.SUCCESS) {
                approvedCount++;
                totalConsidered++;
            }
            else if (tx.getFailureType() == FailureType.ROUTE_TIMEOUT) {
                timeoutCount++;
                totalConsidered++;
            }
            // Ignore issuer declines and customer errors for route success rate
        }

        double successRate = totalConsidered > 0
                ? (double) approvedCount / totalConsidered
                : 0.95;

        double timeoutRate = totalConsidered > 0
                ? (double) timeoutCount / totalConsidered
                : 0.0;

        double avgLatency = totalLatency / recentTransactions.size();

        RouteMetrics metrics = routeMetricsRepository.findByRoute(route)
                .orElse(RouteMetrics.builder()
                        .route(route)
                        .build());

        metrics.setRollingSuccessRate(successRate);
        metrics.setRollingTimeoutRate(timeoutRate);
        metrics.setAvgLatencyMs(avgLatency);
        metrics.setTotalSamples(recentTransactions.size());
        metrics.setLastUpdated(LocalDateTime.now());

        routeMetricsRepository.save(metrics);
    }
}