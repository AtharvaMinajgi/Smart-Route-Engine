package com.opus.smartroute.config;

import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.RouteBanditStats;
import com.opus.smartroute.repository.RouteBanditStatsRepository;
import com.opus.smartroute.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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

    /* ---------------- ROUTE SEEDING ---------------- */

    private void seedRoutes() {

        if (routeRepository.count() > 0) {
            System.out.println("Routes already exist. Skipping seeding.");
            return;
        }

        List<Route> routes = List.of(

            // -------- AXIS --------
            buildRoute("AXIS_VISA", "AXIS", "VISA", 0.018, 0.92, 900, 0.06),
            buildRoute("AXIS_MASTERCARD", "AXIS", "MASTERCARD", 0.019, 0.90, 1000, 0.08),
            buildRoute("AXIS_RUPAY", "AXIS", "RUPAY", 0.017, 0.88, 1150, 0.11),

            // -------- ICICI --------
            buildRoute("ICICI_VISA", "ICICI", "VISA", 0.020, 0.90, 950, 0.07),
            buildRoute("ICICI_MASTERCARD", "ICICI", "MASTERCARD", 0.021, 0.88, 1050, 0.09),
            buildRoute("ICICI_RUPAY", "ICICI", "RUPAY", 0.019, 0.86, 1200, 0.12),

            // -------- HDFC --------
            buildRoute("HDFC_VISA", "HDFC", "VISA", 0.019, 0.88, 1000, 0.08),
            buildRoute("HDFC_MASTERCARD", "HDFC", "MASTERCARD", 0.020, 0.86, 1100, 0.10),
            buildRoute("HDFC_RUPAY", "HDFC", "RUPAY", 0.018, 0.84, 1300, 0.13)
        );

        routeRepository.saveAll(routes);
        System.out.println("✅ 9 routes seeded successfully.");
    }

    private Route buildRoute(
            String name,
            String bank,
            String network,
            double mdr,
            double successRate,
            int latency,
            double risk
    ) {
        return Route.builder()
                .name(name)
                .network(network)
                .baseMdr(mdr)
                .baseSuccessRate(successRate)
                .baseLatencyMs(latency)
                .riskFactor(risk)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /* ---------------- BANDIT SEEDING ---------------- */

    private void seedBanditStats() {

        routeRepository.findAll().forEach(route -> {

            banditStatsRepository.findByRoute(route)
                    .orElseGet(() -> {
                        RouteBanditStats stats = RouteBanditStats.builder()
                                .route(route)
                                .alpha(1.0)
                                .beta(1.0)
                                .build();
                        return banditStatsRepository.save(stats);
                    });
        });

        System.out.println("Bandit stats initialized (a=1, b=1).");
    }
}