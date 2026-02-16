package com.opus.smartroute.service;

import com.opus.smartroute.dto.SimulationResultDTO;
import com.opus.smartroute.entity.Route;
import com.opus.smartroute.enums.FailureType;
import com.opus.smartroute.enums.TransactionResult;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SimulationService {

    private final Random random = new Random();

    public SimulationResultDTO simulate(Route route, double amount) {

        // Base success probability
        double effectiveSuccessRate = route.getBaseSuccessRate();

        // Apply route risk
        effectiveSuccessRate -= route.getRiskFactor();

        // High amount penalty
        if (amount > 100000) {
            effectiveSuccessRate -= 0.10;
        } else if (amount > 50000) {
            effectiveSuccessRate -= 0.05;
        }

        // Clamp probability
        effectiveSuccessRate = Math.max(0.05, Math.min(0.99, effectiveSuccessRate));

        // Simulate latency
        int baseLatency = route.getBaseLatencyMs();
        int variance = random.nextInt(500); // up to +500ms
        int finalLatency = baseLatency + variance;

        // Rare system failure (1%)
        if (random.nextDouble() < 0.01) {
            return SimulationResultDTO.builder()
                    .result(TransactionResult.DECLINE)
                    .failureType(FailureType.SYSTEM_ERROR)
                    .latencyMs(finalLatency)
                    .build();
        }

        // Latency-based timeout
        if (finalLatency > 2000) {
            return SimulationResultDTO.builder()
                    .result(TransactionResult.DECLINE)
                    .failureType(FailureType.ROUTE_TIMEOUT)
                    .latencyMs(finalLatency)
                    .build();
        }

        // Fraud block (if high risk)
        if (route.getRiskFactor() > 0.20 && random.nextDouble() < 0.30) {
            return SimulationResultDTO.builder()
                    .result(TransactionResult.DECLINE)
                    .failureType(FailureType.FRAUD_BLOCK)
                    .latencyMs(finalLatency)
                    .build();
        }

        // Success or normal issuer decline
        boolean success = random.nextDouble() < effectiveSuccessRate;

        if (success) {
            return SimulationResultDTO.builder()
                    .result(TransactionResult.SUCCESS)
                    .failureType(FailureType.NONE)
                    .latencyMs(finalLatency)
                    .build();
        } else {
            return SimulationResultDTO.builder()
                    .result(TransactionResult.DECLINE)
                    .failureType(FailureType.ISSUER_DECLINE)
                    .latencyMs(finalLatency)
                    .build();
        }
    }
}