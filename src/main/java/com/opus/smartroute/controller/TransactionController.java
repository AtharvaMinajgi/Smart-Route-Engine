package com.opus.smartroute.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.opus.smartroute.dto.TransactionResponseDTO;
import com.opus.smartroute.enums.RoutingType;
import com.opus.smartroute.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

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