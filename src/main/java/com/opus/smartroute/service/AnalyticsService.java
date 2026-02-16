package com.opus.smartroute.service;

import com.opus.smartroute.dto.ComparisonResultDTO;
import com.opus.smartroute.entity.Transaction;
import com.opus.smartroute.enums.RoutingType;
import com.opus.smartroute.enums.TransactionResult;
import com.opus.smartroute.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TransactionService transactionService;
    private final BanditService banditService;
    private final TransactionRepository transactionRepository;

    public ComparisonResultDTO compareModes(int count) {

        //Reset bandit learning
        banditService.resetBanditStats();

        // Clear previous transactions
        transactionRepository.deleteAll();

        // Run STATIC transactions
        for (int i = 0; i < count; i++) {
            double randomAmount = generateRandomAmount();
            transactionService.processTransaction(randomAmount, RoutingType.STATIC);
        }

        List<Transaction> staticTx = transactionRepository.findAll();

        double staticSuccessRate = calculateSuccessRate(staticTx);
        double staticLatency = calculateAvgLatency(staticTx);

        //️Reset again for fair comparison
        banditService.resetBanditStats();
        transactionRepository.deleteAll();

        // Run INTELLIGENT transactions
        for (int i = 0; i < count; i++) {
            double randomAmount = generateRandomAmount();
            transactionService.processTransaction(randomAmount, RoutingType.INTELLIGENT);
        }

        List<Transaction> intelligentTx = transactionRepository.findAll();

        double intelligentSuccessRate = calculateSuccessRate(intelligentTx);
        double intelligentLatency = calculateAvgLatency(intelligentTx);

        double improvement = 
                ((intelligentSuccessRate - staticSuccessRate) / staticSuccessRate) * 100;

        return ComparisonResultDTO.builder()
                .totalTransactions(count)
                .staticSuccessRate(round(staticSuccessRate))
                .intelligentSuccessRate(round(intelligentSuccessRate))
                .staticAvgLatency(round(staticLatency))
                .intelligentAvgLatency(round(intelligentLatency))
                .improvementPercentage(round(improvement))
                .build();
    }

    private double calculateSuccessRate(List<Transaction> transactions) {
        long successCount = transactions.stream()
                .filter(t -> t.getResult() == TransactionResult.SUCCESS)
                .count();

        return (double) successCount / transactions.size();
    }

    private double calculateAvgLatency(List<Transaction> transactions) {
        return transactions.stream()
                .mapToInt(Transaction::getLatencyMs)
                .average()
                .orElse(0);
    }

    private double generateRandomAmount() {
        return 1000 + (Math.random() * 200000);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}