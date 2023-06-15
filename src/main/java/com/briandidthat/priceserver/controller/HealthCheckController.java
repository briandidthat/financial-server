package com.briandidthat.priceserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    private static Boolean available = false;
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    public static void setAvailable(boolean status) {
        if (!status) {
            logger.error("Application unhealthy. Shutting down...");
        }
        else {
            logger.info("Application healthy. Startup completed");
            available = true;
        }
    }

    @GetMapping("/health")
    public String isAvalailable() {
        if (!available) throw new RuntimeException();
        return "AVAILABLE";
    }

    @GetMapping("/readyz")
    public String isReady() {
        if (!available) throw new RuntimeException();
        return "READY";
    }
}
