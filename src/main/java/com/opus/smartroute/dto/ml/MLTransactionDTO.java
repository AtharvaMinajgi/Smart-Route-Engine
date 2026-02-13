package com.opus.smartroute.dto.ml;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MLTransactionDTO {
    private String route;
    private double amount;
    private int latency;
    private String result;
}
