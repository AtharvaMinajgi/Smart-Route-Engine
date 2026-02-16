package com.opus.smartroute.dto;

import com.opus.smartroute.entity.Route;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoutingDecisionDTO {

    private Route route;

    // ML predicted success probability
    private double mlProbability;

    // Thompson sampling score
    private double banditScore;

    // Final hybrid score
    private double score;
}