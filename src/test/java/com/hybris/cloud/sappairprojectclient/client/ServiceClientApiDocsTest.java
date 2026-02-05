package com.hybris.cloud.sappairprojectclient.client;

import com.hybris.cloud.sappairprojectclient.model.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ServiceClientApiDocsTest {

    @Mock
    private RestTemplate restTemplate;

    private ServiceClient serviceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serviceClient = new ServiceClient(restTemplate);
        ReflectionTestUtils.setField(serviceClient, "baseUrl", "http://localhost:8080");
    }

    @Test
    void testGetServices_Success() {
        // Arrange
        Service[] mockServices = {
                new Service("1", "Payment Service", "Payment processing service", "ACTIVE", "1.0"),
                new Service("2", "User Service", "User management service", "ACTIVE", "2.0")
        };

        ResponseEntity<Service[]> mockResponse = new ResponseEntity<>(mockServices, HttpStatus.OK);
        when(restTemplate.getForEntity(eq("http://localhost:8080/api/v1/services"), eq(Service[].class)))
                .thenReturn(mockResponse);

        // Act
        List<Service> result = serviceClient.getServices();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Payment Service", result.get(0).getName());
        assertEquals("User Service", result.get(1).getName());
    }

    @Test
    void testGetApiDocs_Success() {
        // Arrange
        String mockApiDocs = "{\"openapi\":\"3.0.1\",\"info\":{\"title\":\"Test API\",\"version\":\"1.0\"}}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockApiDocs, HttpStatus.OK);

        when(restTemplate.getForEntity(eq("http://localhost:8080/v3/api-docs"), eq(String.class)))
                .thenReturn(mockResponse);

        // Act
        String result = serviceClient.getApiDocs();

        // Assert
        assertNotNull(result);
        assertEquals(mockApiDocs, result);
        assertTrue(result.contains("openapi"));
        assertTrue(result.contains("Test API"));
    }

    @Test
    void testGetServices_EmptyResponse() {
        // Arrange
        ResponseEntity<Service[]> mockResponse = new ResponseEntity<>(new Service[0], HttpStatus.OK);
        when(restTemplate.getForEntity(eq("http://localhost:8080/api/v1/services"), eq(Service[].class)))
                .thenReturn(mockResponse);

        // Act
        List<Service> result = serviceClient.getServices();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetApiDocs_EmptyResponse() {
        // Arrange
        ResponseEntity<String> mockResponse = new ResponseEntity<>("", HttpStatus.OK);
        when(restTemplate.getForEntity(eq("http://localhost:8080/v3/api-docs"), eq(String.class)))
                .thenReturn(mockResponse);

        // Act
        String result = serviceClient.getApiDocs();

        // Assert - empty string should be returned as-is
        assertEquals("", result);
    }

    @Test
    void testGetApiDocs_NotFound() {
        // Arrange
        ResponseEntity<String> mockResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        when(restTemplate.getForEntity(eq("http://localhost:8080/v3/api-docs"), eq(String.class)))
                .thenReturn(mockResponse);

        // Act
        String result = serviceClient.getApiDocs();

        // Assert
        assertNull(result);
    }
}
