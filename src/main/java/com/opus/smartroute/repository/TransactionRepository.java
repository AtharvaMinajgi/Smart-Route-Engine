package com.opus.smartroute.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.Transaction;
import com.opus.smartroute.enums.RoutingType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTop100ByRouteOrderByCreatedAtDesc(Route route);

    List<Transaction> findByRoutingType(RoutingType routingType);
    
    List<Transaction> findTop10ByOrderByCreatedAtDesc();
}