package com.hybris.cloud.sappairprojectclient.client;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.hybris.cloud.sappairprojectclient.model.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PACT consumer test for ServiceClient.
 * This test defines the contract between the client and the service provider.
 */
@ExtendWith(PactConsumerTestExt.class)
class ServiceClientPACTTest {

    private ServiceClient serviceClient;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        serviceClient = new ServiceClient(restTemplate);
    }

    @Pact(provider = "sap-pair-project", consumer = "sap-pair-project-client")
    public V4Pact getServicesSuccessPact(PactBuilder builder) {
        return builder
            .usingLegacyDsl()
            .given("services exist")
            .uponReceiving("a request to get all services")
                .path("/api/v1/services")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body("[" +
                    "{\"id\":\"1\",\"name\":\"Service 1\",\"description\":\"Description 1\",\"status\":\"ACTIVE\",\"version\":\"1.0\"}," +
                    "{\"id\":\"2\",\"name\":\"Service 2\",\"description\":\"Description 2\",\"status\":\"INACTIVE\",\"version\":\"2.0\"}" +
                    "]")
            .toPact()
            .asV4Pact()
            .get();
    }

    @Test
    @PactTestFor(pactMethod = "getServicesSuccessPact")
    void testGetServices_Success(MockServer mockServer) {
        // Arrange
        ReflectionTestUtils.setField(serviceClient, "baseUrl", mockServer.getUrl());

        // Act
        List<Service> result = serviceClient.getServices();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Service 1", result.get(0).getName());
        assertEquals("Service 2", result.get(1).getName());
        assertEquals("ACTIVE", result.get(0).getStatus());
        assertEquals("INACTIVE", result.get(1).getStatus());
    }

    @Pact(provider = "service-provider", consumer = "sap-pair-project-client")
    public V4Pact getServicesEmptyPact(PactBuilder builder) {
        return builder
            .usingLegacyDsl()
            .given("no services exist")
            .uponReceiving("a request to get all services when empty")
                .path("/api/v1/services")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body("[]")
            .toPact()
            .asV4Pact()
            .get();
    }

    @Test
    @PactTestFor(pactMethod = "getServicesEmptyPact")
    void testGetServices_EmptyResponse(MockServer mockServer) {
        // Arrange
        ReflectionTestUtils.setField(serviceClient, "baseUrl", mockServer.getUrl());

        // Act
        List<Service> result = serviceClient.getServices();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Pact(provider = "service-provider", consumer = "sap-pair-project-client")
    public V4Pact getServiceByIdSuccessPact(PactBuilder builder) {
        return builder
            .usingLegacyDsl()
            .given("service with id 1 exists")
            .uponReceiving("a request to get service by id 1")
                .path("/api/v1/services/1")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body("{\"id\":\"1\",\"name\":\"Service 1\",\"description\":\"Description 1\",\"status\":\"ACTIVE\",\"version\":\"1.0\"}")
            .toPact()
            .asV4Pact()
            .get();
    }

    @Test
    @PactTestFor(pactMethod = "getServiceByIdSuccessPact")
    void testGetServiceById_Success(MockServer mockServer) {
        // Arrange
        ReflectionTestUtils.setField(serviceClient, "baseUrl", mockServer.getUrl());

        // Act
        Service result = serviceClient.getServiceById("1");

        // Assert
        assertNotNull(result);
        assertEquals("Service 1", result.getName());
        assertEquals("1", result.getId());
        assertEquals("Description 1", result.getDescription());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals("1.0", result.getVersion());
    }

    @Pact(provider = "service-provider", consumer = "sap-pair-project-client")
    public V4Pact getServiceByIdNotFoundPact(PactBuilder builder) {
        return builder
            .usingLegacyDsl()
            .given("service with id 999 does not exist")
            .uponReceiving("a request to get service by id 999")
                .path("/api/v1/services/999")
                .method("GET")
            .willRespondWith()
                .status(404)
                .headers(Map.of("Content-Type", "application/json"))
                .body("{\"error\":\"Service not found\",\"message\":\"Service with id 999 not found\"}")
            .toPact()
            .asV4Pact()
            .get();
    }

    @Test
    @PactTestFor(pactMethod = "getServiceByIdNotFoundPact")
    void testGetServiceById_NotFound(MockServer mockServer) {
        // Arrange
        ReflectionTestUtils.setField(serviceClient, "baseUrl", mockServer.getUrl());

        // Act
        Service result = serviceClient.getServiceById("999");

        // Assert
        assertNull(result);
    }

    @Pact(provider = "service-provider", consumer = "sap-pair-project-client")
    public V4Pact getApiDocsSuccessPact(PactBuilder builder) {
        return builder
            .usingLegacyDsl()
            .given("API documentation is available")
            .uponReceiving("a request to get API documentation")
                .path("/v3/api-docs")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body("{\"openapi\":\"3.0.1\",\"info\":{\"title\":\"Service API\",\"version\":\"1.0.0\"},\"paths\":{\"/api/v1/services\":{\"get\":{\"summary\":\"Get all services\"}}}}")
            .toPact()
            .asV4Pact()
            .get();
    }

    @Test
    @PactTestFor(pactMethod = "getApiDocsSuccessPact")
    void testGetApiDocs_Success(MockServer mockServer) {
        // Arrange
        ReflectionTestUtils.setField(serviceClient, "baseUrl", mockServer.getUrl());

        // Act
        String result = serviceClient.getApiDocs();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("openapi"));
        assertTrue(result.contains("Service API"));
    }
}
