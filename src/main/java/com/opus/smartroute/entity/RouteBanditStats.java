package com.opus.smartroute.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "route_bandit_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteBanditStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "route_id", unique = true)
    private Route route;

    // Alpha = successes + 1
    private double alpha;

    // Beta = failures + 1
    private double beta;
}