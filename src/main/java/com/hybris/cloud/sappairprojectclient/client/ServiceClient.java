package com.hybris.cloud.sappairprojectclient.client;

import com.hybris.cloud.sappairprojectclient.model.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Component
public class ServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ServiceClient.class);

    @Value("${service.api.base-url:http://localhost:8080}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public ServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Calls the /api/v1/services endpoint and returns the list of services
     * @return List of Service objects
     */
    public List<Service> getServices() {
        try {
            String url = baseUrl + "/api/v1/services";
            logger.info("Calling REST endpoint: {}", url);

            ResponseEntity<Service[]> response = restTemplate.getForEntity(url, Service[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Service> services = Arrays.asList(response.getBody());
                logger.info("Successfully retrieved {} services", services.size());
                return services;
            } else {
                logger.warn("Received non-successful response: {}", response.getStatusCode());
                return List.of();
            }

        } catch (ResourceAccessException e) {
            logger.error("Failed to connect to service at {}: {}", baseUrl, e.getMessage());
            return List.of();
        } catch (Exception e) {
            logger.error("Error calling service endpoint: {}", e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * Calls the /v3/api-docs endpoint and returns the OpenAPI documentation
     * @return OpenAPI documentation as a string
     */
    public String getApiDocs() {
        try {
            String url = baseUrl + "/v3/api-docs";
            logger.info("Calling REST endpoint: {}", url);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.info("Successfully retrieved API documentation");
                return response.getBody();
            } else {
                logger.warn("Received non-successful response: {}", response.getStatusCode());
                return null;
            }

        } catch (ResourceAccessException e) {
            logger.error("Failed to connect to service at {}: {}", baseUrl, e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error calling API docs endpoint: {}", e.getMessage(), e);
            return null;
        }
    }


    /**
     * Gets a single service by ID
     * @param serviceId the service ID
     * @return Service object or null if not found
     */
    public Service getServiceById(String serviceId) {
        try {
            String url = baseUrl + "/api/v1/services/" + serviceId;
            logger.info("Calling REST endpoint: {}", url);

            ResponseEntity<Service> response = restTemplate.getForEntity(url, Service.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.info("Successfully retrieved service with ID: {}", serviceId);
                return response.getBody();
            } else {
                logger.warn("Service with ID {} not found or error occurred: {}", serviceId, response.getStatusCode());
                return null;
            }

        } catch (ResourceAccessException e) {
            logger.error("Failed to connect to service at {}: {}", baseUrl, e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error calling service endpoint for ID {}: {}", serviceId, e.getMessage(), e);
            return null;
        }
    }
}
