package com.opus.smartroute.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteDTO {

    private Long id;
    private String name;
    private String network;

    // 🔧 BASE VALUES (Admin edits these)
    private Double baseSuccessRate;
    private Integer baseLatencyMs;
    private Double riskFactor;

    // 📊 DERIVED VALUE (Dashboard display)
    private Double effectiveSuccessRate;
}