package com.hybris.cloud.sappairprojectclient;

import com.hybris.cloud.sappairprojectclient.service.ServiceListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class FetchServicesRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(FetchServicesRunner.class);

    @Autowired
    private ServiceListingService serviceListingService;

    public static void main(String[] args) {
        logger.info("=== Running fetchAndListServices Method ===");

        SpringApplication app = new SpringApplication(FetchServicesRunner.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Executing fetchAndListServices method...");

        try {
            // This will call the fetchAndListServices method
            serviceListingService.fetchAndListServices();

            logger.info("fetchAndListServices execution completed!");

        } catch (Exception e) {
            logger.error("Error during fetchAndListServices execution: {}", e.getMessage(), e);
        }

        logger.info("=== fetchAndListServices Method Execution Complete ===");
    }
}
