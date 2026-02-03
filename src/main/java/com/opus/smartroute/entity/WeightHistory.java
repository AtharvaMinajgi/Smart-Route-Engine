package com.opus.smartroute.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "weight_history")
public class WeightHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double successWeight;

    private Double latencyWeight;

    private Double mdrWeight;

    private String reason;

    private LocalDateTime recordedAt;
}
