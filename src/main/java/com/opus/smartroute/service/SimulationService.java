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

        double baseSuccessProbability = getBaseSuccessProbability(route);
        int latency = generateLatency(route);

        // 1️⃣ Amount effect (high value = slightly more risky)
        if (amount > 100000) {
            baseSuccessProbability -= 0.10;
        } else if (amount > 50000) {
            baseSuccessProbability -= 0.05;
        }

        // 2️⃣ Latency effect (slow route = slightly more failure prone)
        if (latency > 1800) {
            baseSuccessProbability -= 0.07;
        } else if (latency > 1400) {
            baseSuccessProbability -= 0.03;
        }

        // Keep probability within bounds
        baseSuccessProbability = Math.max(0.05, Math.min(0.99, baseSuccessProbability));

        boolean success = random.nextDouble() < baseSuccessProbability;

        if (success) {
            return SimulationResultDTO.builder()
                    .result(TransactionResult.SUCCESS)
                    .failureType(FailureType.NONE)
                    .latencyMs(latency)
                    .build();
        } else {
            return SimulationResultDTO.builder()
                    .result(TransactionResult.DECLINE)
                    .failureType(FailureType.ISSUER_DECLINE)
                    .latencyMs(latency)
                    .build();
        }
    }

    private double getBaseSuccessProbability(Route route) {

        switch (route.getName()) {
            case "AXIS":
                return 0.92;
            case "ICICI":
                return 0.85;
            case "HDFC":
                return 0.75;
            default:
                return 0.80;
        }
    }

    private int generateLatency(Route route) {

        switch (route.getName()) {
            case "AXIS":
                return 800 + random.nextInt(400);   // 800–1200 ms
            case "ICICI":
                return 1000 + random.nextInt(600);  // 1000–1600 ms
            case "HDFC":
                return 1200 + random.nextInt(800);  // 1200–2000 ms
            default:
                return 1000 + random.nextInt(600);
        }
    }
}