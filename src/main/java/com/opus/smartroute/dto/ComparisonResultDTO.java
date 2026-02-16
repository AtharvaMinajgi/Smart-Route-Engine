package com.opus.smartroute.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparisonResultDTO {

    private int totalTransactions;

    private double staticSuccessRate;
    private double intelligentSuccessRate;

    private double staticAvgLatency;
    private double intelligentAvgLatency;

    private double improvementPercentage;
}