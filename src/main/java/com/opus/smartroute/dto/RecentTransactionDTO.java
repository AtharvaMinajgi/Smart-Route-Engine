package com.opus.smartroute.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentTransactionDTO {

    private Long id;
    private String routeName;
    private String routingType;
    private String result;
    private Integer latencyMs;
    private Double scoreUsed;
    private LocalDateTime createdAt;
}