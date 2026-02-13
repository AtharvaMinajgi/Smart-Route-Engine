package com.opus.smartroute.repository;

import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.RouteBanditStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteBanditStatsRepository extends JpaRepository<RouteBanditStats, Long> {

    Optional<RouteBanditStats> findByRoute(Route route);
}
