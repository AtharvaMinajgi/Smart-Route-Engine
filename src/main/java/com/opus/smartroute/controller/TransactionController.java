package com.opus.smartroute.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opus.smartroute.dto.TransactionResponseDTO;
import com.opus.smartroute.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/single")
    public TransactionResponseDTO processSingle(@RequestParam double amount) {
        return transactionService.processTransaction(amount);
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> processBulk(@RequestParam int count) {

        for (int i = 0; i < count; i++) {
            double randomAmount = generateRandomAmount();
            transactionService.processTransaction(randomAmount);
        }

        return ResponseEntity.ok("Bulk transactions completed");
    }

    private double generateRandomAmount() {
        return 1000 + (Math.random() * 200000);
    }
}
