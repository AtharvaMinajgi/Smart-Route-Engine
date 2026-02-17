package com.opus.smartroute.controller;

import com.opus.smartroute.dto.AdminRouteUpdateDTO;
import com.opus.smartroute.dto.RouteDTO;
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
	        return ResponseEntity.ok(mlClientService.trainModel());
	    }

	    /* Check ML Status */
	    @GetMapping("/ml-status")
	    public ResponseEntity<?> mlStatus() {
	        return ResponseEntity.ok(
	            Map.of("ml_ready", mlClientService.isModelReady())
	        );
	    }

	    /* View All Routes (EFFECTIVE values) */
	    @GetMapping("/routes")
	    public List<RouteDTO> getRoutes() {
	        return routeRepository.findAll().stream()
	            .map(r -> RouteDTO.builder()
	                .id(r.getId())
	                .name(r.getName())
	                .network(r.getNetwork())

	                // BASE VALUES (FOR ADMIN FORM)
	                .baseSuccessRate(r.getBaseSuccessRate())
	                .baseLatencyMs(r.getBaseLatencyMs())
	                .riskFactor(r.getRiskFactor())

	                // DERIVED (FOR DASHBOARD)
	                .effectiveSuccessRate(
	                    routeService.calculateEffectiveSuccess(r)
	                )
	                .build()
	            )
	            .toList();
	    }

	    /* Update Route Config */
	    @PutMapping("/routes/{id}")
	    public ResponseEntity<Route> updateRoute(
	            @PathVariable Long id,
	            @RequestBody AdminRouteUpdateDTO dto
	    ) {
	        return ResponseEntity.ok(routeService.updateRoute(id, dto));
	    }
	}