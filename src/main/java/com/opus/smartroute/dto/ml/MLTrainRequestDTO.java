package com.opus.smartroute.dto.ml;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MLTrainRequestDTO {
    private List<MLTransactionDTO> transactions;
}