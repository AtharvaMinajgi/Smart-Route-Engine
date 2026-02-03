package com.opus.smartroute.entity;

import java.time.LocalDateTime;

import com.opus.smartroute.enums.FailureType;
import com.opus.smartroute.enums.RoutingType;
import com.opus.smartroute.enums.TransactionResult;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Enumerated(EnumType.STRING)
    private RoutingType routingType;

    @Enumerated(EnumType.STRING)
    private TransactionResult result;

    @Enumerated(EnumType.STRING)
    private FailureType failureType;

    private Integer latencyMs;

    private Double scoreUsed;

    private LocalDateTime createdAt;
}
