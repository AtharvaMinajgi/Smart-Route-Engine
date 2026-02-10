package com.opus.smartroute.config;

import com.opus.smartroute.entity.Route;
import com.opus.smartroute.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RouteRepository routeRepository;

    @Override
    public void run(String... args) {
        seedRoutes();
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
}