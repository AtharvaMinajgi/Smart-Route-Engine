package com.opus.smartroute.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BanditStatsResponseDTO {

    private String routeName;
    private double alpha;
    private double beta;
    private double estimatedSuccessRate;
}
