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

        int latency = generateLatency(route);
        TransactionResult result;
        FailureType failureType = FailureType.NONE;

        // Timeout condition (based on latency)
        if (latency > 1800) {
            result = TransactionResult.TIMEOUT;
            failureType = FailureType.ROUTE_TIMEOUT;
        }
        else {
            //️Random issuer decline (5% chance)
            if (random.nextDouble() < 0.05) {
                result = TransactionResult.DECLINE;
                failureType = FailureType.ISSURE_DECLINE;
            }
            // Random customer error (3% chance)
            else if (random.nextDouble() < 0.03) {
                result = TransactionResult.CUSTOMER_ERROR;
                failureType = FailureType.WRONG_PIN;
            }
            else {
                result = TransactionResult.SUCCESS;
            }
        }

        return SimulationResultDTO.builder()
                .result(result)
                .failureType(failureType)
                .latencyMs(latency)
                .build();
    }

    private int generateLatency(Route route) {

        int baseLatency;

        switch (route.getName()) {
            case "AXIS":
                baseLatency = 900;
                break;
            case "ICICI":
                baseLatency = 1100;
                break;
            case "HDFC":
                baseLatency = 800;
                break;
            default:
                baseLatency = 1000;
        }

        // Add random fluctuation
        int fluctuation = random.nextInt(600); // 0–600ms

        return baseLatency + fluctuation;
    }
}