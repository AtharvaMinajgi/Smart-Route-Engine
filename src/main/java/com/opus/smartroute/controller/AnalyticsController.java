package com.opus.smartroute.controller;

import com.opus.smartroute.dto.ComparisonResultDTO;
import com.opus.smartroute.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Compare STATIC vs INTELLIGENT routing
     *
     * Example:
     * GET /analytics/compare?count=1000
     */
    @GetMapping("/compare")
    public ComparisonResultDTO compareModes(@RequestParam int count) {

        ComparisonResultDTO result = analyticsService.compareModes(count);

        String summary;

        if (result.getImprovementPercentage() > 0) {
            summary = String.format(
                    "AI routing improved success rate by %.2f%% compared to static routing.",
                    result.getImprovementPercentage()
            );
        } else if (result.getImprovementPercentage() < 0) {
            summary = String.format(
                    "AI routing underperformed static routing by %.2f%% in this scenario.",
                    Math.abs(result.getImprovementPercentage())
            );
        } else {
            summary = "AI routing and static routing performed equally in this scenario.";
        }

        result.setSummary(summary);
        return result;
    }
}