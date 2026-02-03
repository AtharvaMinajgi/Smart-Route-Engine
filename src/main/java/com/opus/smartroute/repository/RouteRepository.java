package com.opus.smartroute.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opus.smartroute.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByName(String name);

    List<Route> findByIsActiveTrue();
}
