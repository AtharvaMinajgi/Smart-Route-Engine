package com.opus.smartroute.controller;

import com.opus.smartroute.dto.BanditStatsResponseDTO;
import com.opus.smartroute.repository.RouteBanditStatsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class BanditController {

    private final RouteBanditStatsRepository banditStatsRepository;

    @GetMapping("/bandit-stats")
    public List<BanditStatsResponseDTO> getBanditStats() {

        return banditStatsRepository.findAll()
                .stream()
                .map(stats -> BanditStatsResponseDTO.builder()
                        .routeName(stats.getRoute().getName())
                        .alpha(stats.getAlpha())
                        .beta(stats.getBeta())
                        .estimatedSuccessRate(
                                stats.getAlpha() /
                                (stats.getAlpha() + stats.getBeta())
                        )
                        .build())
                .collect(Collectors.toList());
    }
}
