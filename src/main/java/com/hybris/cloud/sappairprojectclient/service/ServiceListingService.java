package com.hybris.cloud.sappairprojectclient.service;

import com.hybris.cloud.sappairprojectclient.client.ServiceClient;
import com.hybris.cloud.sappairprojectclient.model.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceListingService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceListingService.class);

    private final ServiceClient serviceClient;

    public ServiceListingService(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * Fetches API documentation from the /v3/api-docs endpoint
     */
    public void fetchAndDisplayApiDocs() {
        logger.info("Starting to fetch API documentation from REST endpoint...");

        String apiDocs = serviceClient.getApiDocs();

        if (apiDocs == null || apiDocs.trim().isEmpty()) {
            logger.warn("No API documentation found or error occurred while fetching");
            return;
        }

        logger.info("=== API DOCUMENTATION ===");
        logger.info("Documentation length: {} characters", apiDocs.length());
        logger.info("Content preview (first 1000 characters):");
        logger.info("---");
        String preview = apiDocs.length() > 1000 ? apiDocs.substring(0, 1000) + "\n... (truncated)" : apiDocs;
        logger.info(preview);
        logger.info("---");
        logger.info("=== END API DOCUMENTATION ===");
    }

    /**
     * Fetches services from the REST endpoint and lists them internally
     */
    public void fetchAndListServices() {
        logger.info("Starting to fetch services from REST endpoint...");

        List<Service> services = serviceClient.getServices();

        if (services.isEmpty()) {
            logger.warn("No services found or error occurred while fetching services");
            return;
        }

        logger.info("=== SERVICE LISTING ===");
        logger.info("Total services found: {}", services.size());
        logger.info("========================");

        for (int i = 0; i < services.size(); i++) {
            Service service = services.get(i);
            logger.info("Service #{}: {}", (i + 1), service);
        }

        logger.info("=== END SERVICE LISTING ===");
    }

    /**
     * Fetches services and returns them for further processing
     * @return List of services
     */
    public List<Service> getServicesList() {
        logger.info("Fetching services list...");
        List<Service> services = serviceClient.getServices();
        logger.info("Retrieved {} services", services.size());
        return services;
    }

    /**
     * Fetches a specific service by ID and logs it
     * @param serviceId the service ID to fetch
     */
    public void fetchAndDisplayService(String serviceId) {
        logger.info("Fetching service with ID: {}", serviceId);

        Service service = serviceClient.getServiceById(serviceId);

        if (service != null) {
            logger.info("=== SERVICE DETAILS ===");
            logger.info("Service found: {}", service);
            logger.info("=======================");
        } else {
            logger.warn("Service with ID '{}' not found", serviceId);
        }
    }
}
