package com.hybris.cloud.sappairprojectclient.demo;

import com.hybris.cloud.sappairprojectclient.client.ServiceClient;
import com.hybris.cloud.sappairprojectclient.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Deprecated // This class is deprecated - FetchServicesRunner is now the main entry point
// @Component // Commented out so this won't be auto-discovered by Spring Boot
public class ClientDemoRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ClientDemoRunner.class);

    @Autowired
    private ServiceClient serviceClient;

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== SAP Pair Project Client Demo (Client-Only Mode) ===");
        logger.info("Note: This application does NOT start a web server - no port conflicts!");
        logger.info("Attempting to connect to REST endpoint at http://localhost:8080/api/v1/services");

        try {
            // Test the services endpoint (primary test)
            List<Service> services = serviceClient.getServices();

            if (services.isEmpty()) {
                logger.warn("No services retrieved. This could mean:");
                logger.warn("1. The target service at http://localhost:8080 is not running");
                logger.warn("2. The endpoint /api/v1/services does not exist");
                logger.warn("3. There are network connectivity issues");
                logger.warn("4. The response format doesn't match the expected Service model");

                // Also try API docs endpoint as fallback
                logger.info("Attempting fallback to API documentation endpoint...");
                String apiDocs = serviceClient.getApiDocs();

                if (apiDocs != null && !apiDocs.trim().isEmpty()) {
                    logger.info("✅ Successfully retrieved API documentation as fallback!");
                    logger.info("API Docs preview (first 200 characters):");
                    String preview = apiDocs.length() > 200 ? apiDocs.substring(0, 200) + "..." : apiDocs;
                    logger.info("---");
                    logger.info(preview);
                    logger.info("---");
                } else {
                    logger.warn("Both service and API docs endpoints are unavailable");
                }
            } else {
                logger.info("✅ Successfully retrieved {} service(s):", services.size());
                for (int i = 0; i < services.size(); i++) {
                    Service service = services.get(i);
                    logger.info("  [{}] ID: {}, Name: {}, Status: {}, Version: {}",
                               (i + 1), service.getId(), service.getName(),
                               service.getStatus(), service.getVersion());
                }
            }

        } catch (Exception e) {
            logger.error("Error occurred while calling the service: {}", e.getMessage());
            logger.info("Make sure the target service is running at http://localhost:8080");
        }

        logger.info("=== Demo completed (Client will now exit) ===");
        logger.info("Client-only mode benefits:");
        logger.info("✅ No web server started - no port conflicts");
        logger.info("✅ Safe to run alongside services on port 8080");
        logger.info("✅ Lightweight - only makes REST calls and exits");
        logger.info("");
        logger.info("To test with a real service:");
        logger.info("1. Start your service at http://localhost:8080");
        logger.info("2. Ensure it has a /api/v1/services endpoint");
        logger.info("3. Run this client application again");
    }
}
