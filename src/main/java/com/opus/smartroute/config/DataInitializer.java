package com.opus.smartroute.config;

import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.WeightConfig;
import com.opus.smartroute.repository.RouteRepository;
import com.opus.smartroute.repository.WeightConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RouteRepository routeRepository;
    private final WeightConfigRepository weightConfigRepository;

    @Override
    public void run(String... args) {

        seedRoutes();
        seedWeights();
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

    private void seedWeights() {

        if (weightConfigRepository.count() == 0) {

            WeightConfig defaultWeights = WeightConfig.builder()
                    .successWeight(0.6)
                    .latencyWeight(0.25)
                    .mdrWeight(0.15)
                    .lastUpdated(LocalDateTime.now())
                    .build();

            weightConfigRepository.save(defaultWeights);

            System.out.println("Default weight configuration seeded.");
        }
    }
}