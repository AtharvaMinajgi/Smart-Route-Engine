package com.opus.smartroute.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDTO {

    private String selectedRoute;

    // Final hybrid score
    private Double score;

    // Explainable AI components
    private Double mlProbability;
    private Double banditScore;

    private String result;
    private String failureType;
    private Integer latencyMs;
}