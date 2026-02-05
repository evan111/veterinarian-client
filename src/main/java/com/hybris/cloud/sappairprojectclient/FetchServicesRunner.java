package com.hybris.cloud.sappairprojectclient;

import com.hybris.cloud.sappairprojectclient.service.ServiceListingService;
import com.hybris.cloud.sappairprojectclient.client.ServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class FetchServicesRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(FetchServicesRunner.class);

    @Autowired
    private ServiceListingService serviceListingService;

    @Autowired
    private ServiceClient serviceClient;

    @Autowired
    private Environment environment;

    @Value("${service.api.base-url:http://localhost:8080}")
    private String defaultBaseUrl;

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
            // Allow setting API base URL before running fetchAndListServices
            configureApiBaseUrl(args);

            // This will call the fetchAndListServices method
            serviceListingService.fetchAndListServices();

            logger.info("fetchAndListServices execution completed!");

        } catch (Exception e) {
            logger.error("Error during fetchAndListServices execution: {}", e.getMessage(), e);
        }

        logger.info("=== fetchAndListServices Method Execution Complete ===");
    }

    private void configureApiBaseUrl(String... args) {
        String baseUrl = defaultBaseUrl;

        // Check for command line argument --api-base-url=<url>
        for (String arg : args) {
            if (arg.startsWith("--api-base-url=")) {
                baseUrl = arg.substring("--api-base-url=".length());
                logger.info("Using API base URL from command line: {}", baseUrl);
                break;
            }
        }

        // Check for environment variable
        String envBaseUrl = environment.getProperty("API_BASE_URL");
        if (envBaseUrl != null && !envBaseUrl.trim().isEmpty()) {
            baseUrl = envBaseUrl;
            logger.info("Using API base URL from environment variable: {}", baseUrl);
        }

        // Check for Spring property (this takes precedence over default)
        String propertyBaseUrl = environment.getProperty("service.api.base-url");
        if (propertyBaseUrl != null && !propertyBaseUrl.equals(defaultBaseUrl)) {
            baseUrl = propertyBaseUrl;
            logger.info("Using API base URL from application properties: {}", baseUrl);
        } else {
            logger.info("Using default API base URL: {}", baseUrl);
        }

        logger.info("✅ API base URL configured to: {}", baseUrl);
    }
}
