package com.opus.smartroute.dto;


import com.opus.smartroute.enums.FailureType;
import com.opus.smartroute.enums.TransactionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimulationResultDTO {

    private TransactionResult result;
    private FailureType failureType;
    private int latencyMs;
}
