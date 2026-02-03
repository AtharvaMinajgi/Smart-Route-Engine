package com.opus.smartroute.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.RouteMetrics;

public interface RouteMetricsRepository extends JpaRepository<RouteMetrics, Long> {

    Optional<RouteMetrics> findByRoute(Route route);
}