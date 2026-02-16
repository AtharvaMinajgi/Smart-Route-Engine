package com.opus.smartroute.service;

import com.opus.smartroute.dto.AdminRouteUpdateDTO;
import com.opus.smartroute.entity.Route;
import com.opus.smartroute.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public Route updateRoute(Long id, AdminRouteUpdateDTO dto) {

        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        // Update only if value provided
        if (dto.getBaseSuccessRate() != null) {
            route.setBaseSuccessRate(dto.getBaseSuccessRate());
        }

        if (dto.getBaseLatencyMs() != null) {
            route.setBaseLatencyMs(dto.getBaseLatencyMs());
        }

        if (dto.getRiskFactor() != null) {
            route.setRiskFactor(dto.getRiskFactor());
        }

        if (dto.getIsActive() != null) {
            route.setIsActive(dto.getIsActive());
        }

        route.setUpdatedAt(LocalDateTime.now());

        return routeRepository.save(route);
    }
}