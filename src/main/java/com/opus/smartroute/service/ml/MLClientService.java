package com.opus.smartroute.service.ml;

import com.opus.smartroute.dto.ml.MLTransactionDTO;
import com.opus.smartroute.dto.ml.MLTrainRequestDTO;
import com.opus.smartroute.entity.Transaction;
import com.opus.smartroute.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MLClientService {

    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    private static final String ML_TRAIN_URL = "http://127.0.0.1:8000/train";
    private static final String ML_PREDICT_URL = "http://127.0.0.1:8000/predict";

    public Object trainModel() {

        List<Transaction> transactions = transactionRepository.findAll();

        List<MLTransactionDTO> mlData = transactions.stream()
                .map(tx -> new MLTransactionDTO(
                        tx.getRoute().getName(),
                        tx.getAmount(),
                        tx.getLatencyMs(),
                        tx.getResult().name()
                ))
                .collect(Collectors.toList());

        MLTrainRequestDTO request = new MLTrainRequestDTO(mlData);

        return restTemplate.postForObject(
                ML_TRAIN_URL,
                request,
                Object.class
        );
    }

    public Double predictSuccess(String route, double amount, int latency) {

        Map<String, Object> request = new HashMap<>();
        request.put("route", route);
        request.put("amount", amount);
        request.put("latency", latency);

        Map response = restTemplate.postForObject(
                ML_PREDICT_URL,
                request,
                Map.class
        );

        if (response == null || !response.containsKey("success_probability")) {
            return null;
        }

        return ((Number) response.get("success_probability")).doubleValue();
    }
}