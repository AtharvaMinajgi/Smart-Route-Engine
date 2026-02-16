package com.opus.smartroute.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.opus.smartroute.dto.RoutingDecisionDTO;
import com.opus.smartroute.entity.Route;
import com.opus.smartroute.entity.RouteBanditStats;
import com.opus.smartroute.enums.TransactionResult;
import com.opus.smartroute.repository.RouteBanditStatsRepository;
import com.opus.smartroute.repository.RouteRepository;
import com.opus.smartroute.repository.RouteMetricsRepository;
import com.opus.smartroute.service.ml.MLClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BanditService {

	private final RouteRepository routeRepository;
	private final RouteBanditStatsRepository banditStatsRepository;
	private final RouteMetricsRepository routeMetricsRepository;

	private final Random random = new Random();

	/**
	 * Hybrid Route Selection: 70% ML probability 30% Thompson Sampaling exploration
	 */
	public RoutingDecisionDTO selectRouteWithML(double amount, MLClientService mlClientService) {

		List<Route> routes = routeRepository.findByIsActiveTrue();

		if (routes.isEmpty()) {
			throw new RuntimeException("No active routes available");
		}

		Route bestRoute = null;
		double bestScore = -1;
		double bestML = 0;
		double bestBandit = 0;

		for (Route route : routes) {

// Fetch bandit stats
			RouteBanditStats stats = banditStatsRepository.findByRoute(route)
					.orElseThrow(() -> new RuntimeException("Bandit stats missing"));

// Thompson Sampling (exploration)
			double banditSample = sampleBeta(stats.getAlpha(), stats.getBeta());

// Estimate latency from rolling metrics
			int estimatedLatency = routeMetricsRepository.findByRoute(route).map(m -> m.getAvgLatencyMs().intValue())
					.orElse(1000);

// Ask ML model for probability
			Double mlProbability = mlClientService.predictSuccess(route.getName(), amount, estimatedLatency);

			if (mlProbability == null) {
				mlProbability = 0.5; // fallback
			}

// Final hybrid score
			double finalScore = (0.7 * mlProbability) + (0.3 * banditSample);

			System.out.println("Route: " + route.getName() + " | ML Prob: " + mlProbability + " | Bandit: "
					+ banditSample + " | Final Score: " + finalScore);

			if (finalScore > bestScore) {
				bestScore = finalScore;
				bestRoute = route;
				bestML = mlProbability;
				bestBandit = banditSample;
			}
		}

		return RoutingDecisionDTO.builder().route(bestRoute).mlProbability(bestML).banditScore(bestBandit)
				.score(bestScore).build();
	}

	/*Update Bandit stats after transaction*/
	public void updateStats(Route route, TransactionResult result) {

		RouteBanditStats stats = banditStatsRepository.findByRoute(route)
				.orElseThrow(() -> new RuntimeException("Bandit stats missing"));

		if (result == TransactionResult.SUCCESS) {
			stats.setAlpha(stats.getAlpha() + 1);
		} else {
			stats.setBeta(stats.getBeta() + 1);
		}

		banditStatsRepository.save(stats);
	}

	/*Beta distribution sampling*/
	private double sampleBeta(double alpha, double beta) {

		double x = sampleGamma(alpha);
		double y = sampleGamma(beta);

		return x / (x + y);
	}

	/* Gamma distribution (Marsaglia-Tsang method)*/
	private double sampleGamma(double shape) {

		double d = shape - 1.0 / 3.0;
		double c = 1.0 / Math.sqrt(9.0 * d);

		while (true) {
			double x = random.nextGaussian();
			double v = 1.0 + c * x;

			if (v <= 0)
				continue;

			v = v * v * v;
			double u = random.nextDouble();

			if (u < 1 - 0.0331 * x * x * x * x)
				return d * v;

			if (Math.log(u) < 0.5 * x * x + d * (1 - v + Math.log(v)))
				return d * v;
		}
	}

	public void resetBanditStats() {

		var statsList = banditStatsRepository.findAll();

		statsList.forEach(stats -> {
			stats.setAlpha(1.0);
			stats.setBeta(1.0);
		});

		banditStatsRepository.saveAll(statsList);

		System.out.println("Bandit stats reset.");
	}
}