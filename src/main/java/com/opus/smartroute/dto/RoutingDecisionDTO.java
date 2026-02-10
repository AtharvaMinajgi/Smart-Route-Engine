package com.opus.smartroute.dto;

import com.opus.smartroute.entity.Route;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoutingDecisionDTO {

    private Route route;
    private double score;
    private double successRate;
    private double normalizedLatency;
    private double mdr;
}