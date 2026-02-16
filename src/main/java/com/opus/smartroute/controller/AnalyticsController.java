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

    @GetMapping("/compare")
    public ComparisonResultDTO compareModes(@RequestParam int count) {
        return analyticsService.compareModes(count);
    }
}