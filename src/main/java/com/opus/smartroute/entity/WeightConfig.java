package com.opus.smartroute.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "weight_config")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeightConfig {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double successWeight;

    private Double latencyWeight;

    private Double mdrWeight;

    private LocalDateTime lastUpdated;
}
