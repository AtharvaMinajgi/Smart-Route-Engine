package com.opus.smartroute.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDTO {

    private String selectedRoute;
    private Double score;
    private String result;
    private String failureType;
    private int latencyMs;
}
