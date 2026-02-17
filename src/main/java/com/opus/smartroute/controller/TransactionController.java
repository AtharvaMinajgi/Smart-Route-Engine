package com.opus.smartroute.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.opus.smartroute.dto.RecentTransactionDTO;
import com.opus.smartroute.dto.TransactionResponseDTO;
import com.opus.smartroute.enums.RoutingType;
import com.opus.smartroute.repository.TransactionRepository;
import com.opus.smartroute.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    /**
     * Single Transaction
     * Example:
     * /transactions/single?amount=5000&mode=STATIC
     */
    @PostMapping("/single")
    public TransactionResponseDTO processSingle(
            @RequestParam double amount,
            @RequestParam RoutingType mode
    ) {
        return transactionService.processTransaction(amount, mode);
    }
    
    @GetMapping("/recent")
    public List<RecentTransactionDTO> recent() {
        return transactionRepository
            .findTop10ByOrderByCreatedAtDesc()
            .stream()
            .map(tx -> RecentTransactionDTO.builder()
                .id(tx.getId())
                .routeName(
                    tx.getRoute() != null
                        ? tx.getRoute().getName()
                        : "UNKNOWN"
                )
                .routingType(
                    tx.getRoutingType() != null
                        ? tx.getRoutingType().name()
                        : "UNKNOWN"
                )
                .result(
                    tx.getResult() != null
                        ? tx.getResult().name()
                        : "UNKNOWN"
                )
                .latencyMs(tx.getLatencyMs())
                .scoreUsed(tx.getScoreUsed())
                .createdAt(tx.getCreatedAt())
                .build()
            )
            .toList();
    }

    /**
     * Bulk Transactions (for experiments)
     * Example:
     * /transactions/bulk?count=1000&mode=INTELLIGENT
     */
    @PostMapping("/bulk")
    public ResponseEntity<String> processBulk(
            @RequestParam int count,
            @RequestParam RoutingType mode
    ) {

        for (int i = 0; i < count; i++) {
            double randomAmount = generateRandomAmount();
            transactionService.processTransaction(randomAmount, mode);
        }

        return ResponseEntity.ok("Bulk transactions completed in " + mode + " mode");
    }

    private double generateRandomAmount() {
        return 1000 + (Math.random() * 200000);
    }
}