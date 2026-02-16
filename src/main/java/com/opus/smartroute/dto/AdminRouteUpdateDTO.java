package com.opus.smartroute.dto;

import lombok.Data;

@Data
public class AdminRouteUpdateDTO {

    private Double baseSuccessRate;
    private Integer baseLatencyMs;
    private Double riskFactor;
    private Boolean isActive;
}