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
        resetBanditStats();
    }

    private void seedRoutes() {

        if (routeRepository.count() == 0) {

            Route axis = Route.builder()
                    .name("AXIS")
                    .network("VISA")
                    .baseMdr(0.018)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Route icici = Route.builder()
                    .name("ICICI")
                    .network("VISA")
                    .baseMdr(0.021)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Route hdfc = Route.builder()
                    .name("HDFC")
                    .network("VISA")
                    .baseMdr(0.020)
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
    
    private void resetBanditStats() {

        banditStatsRepository.findAll().forEach(stats -> {
            stats.setAlpha(1);
            stats.setBeta(1);
        });

        banditStatsRepository.saveAll(banditStatsRepository.findAll());

        System.out.println("Bandit stats reset.");
    }
}