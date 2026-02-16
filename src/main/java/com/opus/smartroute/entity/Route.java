package com.opus.smartroute.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Route Name (AXIS, ICICI, HDFC)
    @Column(nullable = false, unique = true)
    private String name;

    // Network (VISA, MASTERCARD)
    @Column(nullable = false)
    private String network;

    // Merchant Discount Rate (cost factor)
    @Column(nullable = false)
    private Double baseMdr;

    // Whether route is active
    @Column(nullable = false)
    private Boolean isActive = true;

    // Base approval probability (0.0 - 1.0)
    @Column(nullable = false)
    private Double baseSuccessRate;

    // Base latency in milliseconds
    @Column(nullable = false)
    private Integer baseLatencyMs;

    // Risk factor (0.0 - 1.0)
    // Higher value → more likely to decline
    @Column(nullable = false)
    private Double riskFactor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}