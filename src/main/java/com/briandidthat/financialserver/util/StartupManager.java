package com.briandidthat.financialserver.util;

import com.briandidthat.financialserver.controller.HealthCheckController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StartupManager {
    private static final Logger logger = LoggerFactory.getLogger(StartupManager.class);
    private static final int expectedTestCount = 2; // Since we have two backends to test (Coinbase, Twelve Data)
    private static int successfulCount = 0;

    public static void registerResult(String clazz, boolean status) {
        if (!status) {
            // if we are unable to reach the backend services, set health to false so kubernetes could restart
            HealthCheckController.setAvailable(false);
            logger.info("{} was unable to connect to the backend.", clazz);
            return;
        }

        successfulCount++;
        if (successfulCount == expectedTestCount) {
            logger.info("Application is healthy.");
            HealthCheckController.setAvailable(true);
            // reset the count to 0 if we have successfully started up the application
            successfulCount = 0;
        }
    }

}
