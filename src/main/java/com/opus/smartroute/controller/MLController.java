package com.opus.smartroute.controller;

import com.opus.smartroute.service.ml.MLClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MLController {

    private final MLClientService mlClientService;

    @PostMapping("/ml/train")
    public Object trainModel() {
        return mlClientService.trainModel();
    }
}