package com.opus.smartroute.controller;

import com.opus.smartroute.dto.AdminRouteUpdateDTO;
import com.opus.smartroute.entity.Route;
import com.opus.smartroute.repository.RouteRepository;
import com.opus.smartroute.service.BanditService;
import com.opus.smartroute.service.RouteService;
import com.opus.smartroute.service.ml.MLClientService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BanditService banditService;
    private final MLClientService mlClientService;
    private final RouteRepository routeRepository;
    private final RouteService routeService;

    /* Reset Bandit Learning */
    @PostMapping("/reset-bandit")
    public ResponseEntity<String> resetBanditStats() {

        banditService.resetBanditStats();

        return ResponseEntity.ok("Bandit statistics reset successfully.");
    }

    /* Train ML Model */
    @PostMapping("/train-ml")
    public ResponseEntity<?> trainML() {

        Object result = mlClientService.trainModel();

        return ResponseEntity.ok(result);
    }

    /* Check ML Status */
    @GetMapping("/ml-status")
    public ResponseEntity<?> mlStatus() {

        boolean ready = mlClientService.isModelReady();

        return ResponseEntity.ok(Map.of(
                "ml_ready", ready
        ));
    }

    /* View All Routes (for dashboard/admin UI)*/
    @GetMapping("/routes")
    public ResponseEntity<List<Route>> getAllRoutes() {

        return ResponseEntity.ok(routeRepository.findAll());
    }
    
    /* Update Route Config (Live) */
    @PutMapping("/routes/{id}")
    public ResponseEntity<Route> updateRoute(
            @PathVariable Long id,
            @RequestBody AdminRouteUpdateDTO dto
    ) {
        Route updated = routeService.updateRoute(id, dto);
        return ResponseEntity.ok(updated);
    }
}