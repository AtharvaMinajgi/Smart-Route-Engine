package com.opus.smartroute.config;

import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.RouteBanditStats;
import com.opus.smartroute.repository.RouteBanditStatsRepository;
import com.opus.smartroute.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RouteRepository routeRepository;
    private final RouteBanditStatsRepository banditStatsRepository;

    @Override
    public void run(String... args) {

        seedRoutes();
        seedBanditStats();
    }

    private void seedRoutes() {

        if (routeRepository.count() == 0) {

            Route axis = Route.builder()
                    .name("AXIS")
                    .network("VISA")
                    .baseMdr(0.018)
                    .baseSuccessRate(0.92)
                    .baseLatencyMs(900)
                    .riskFactor(0.05)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Route icici = Route.builder()
                    .name("ICICI")
                    .network("VISA")
                    .baseMdr(0.021)
                    .baseSuccessRate(0.85)
                    .baseLatencyMs(1200)
                    .riskFactor(0.08)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Route hdfc = Route.builder()
                    .name("HDFC")
                    .network("VISA")
                    .baseMdr(0.020)
                    .baseSuccessRate(0.75)
                    .baseLatencyMs(1500)
                    .riskFactor(0.12)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            routeRepository.save(axis);
            routeRepository.save(icici);
            routeRepository.save(hdfc);

            System.out.println("Default routes seeded.");
        }
    }

    private void seedBanditStats() {

        routeRepository.findAll().forEach(route -> {

            if (banditStatsRepository.findByRoute(route).isEmpty()) {

                RouteBanditStats stats = RouteBanditStats.builder()
                        .route(route)
                        .alpha(1.0) // Initial prior
                        .beta(1.0)  // Initial prior
                        .build();

                banditStatsRepository.save(stats);
            }
        });

        System.out.println("Bandit stats initialized.");
    }
}