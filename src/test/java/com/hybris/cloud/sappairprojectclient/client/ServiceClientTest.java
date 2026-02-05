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

class ServiceClientTest {

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
                new Service("1", "Service 1", "Description 1", "ACTIVE", "1.0"),
                new Service("2", "Service 2", "Description 2", "INACTIVE", "2.0")
        };

        ResponseEntity<Service[]> mockResponse = new ResponseEntity<>(mockServices, HttpStatus.OK);
        when(restTemplate.getForEntity(eq("http://localhost:8080/api/v1/services"), eq(Service[].class)))
                .thenReturn(mockResponse);

        // Act
        List<Service> result = serviceClient.getServices();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Service 1", result.get(0).getName());
        assertEquals("Service 2", result.get(1).getName());
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
    void testGetServiceById_Success() {
        // Arrange
        Service mockService = new Service("1", "Service 1", "Description 1", "ACTIVE", "1.0");
        ResponseEntity<Service> mockResponse = new ResponseEntity<>(mockService, HttpStatus.OK);

        when(restTemplate.getForEntity(eq("http://localhost:8080/api/v1/services/1"), eq(Service.class)))
                .thenReturn(mockResponse);

        // Act
        Service result = serviceClient.getServiceById("1");

        // Assert
        assertNotNull(result);
        assertEquals("Service 1", result.getName());
        assertEquals("1", result.getId());
    }

    @Test
    void testWithCustomBaseUrl() {
        // Arrange - Set a different base URL for this test
        String customBaseUrl = "https://staging-api.example.com";
        ReflectionTestUtils.setField(serviceClient, "baseUrl", customBaseUrl);

        Service mockService = new Service("1", "Custom Service", "Custom Description", "ACTIVE", "1.0");
        ResponseEntity<Service> mockResponse = new ResponseEntity<>(mockService, HttpStatus.OK);

        // Note: The expected URL now uses the custom base URL
        when(restTemplate.getForEntity(eq(customBaseUrl + "/api/v1/services/1"), eq(Service.class)))
                .thenReturn(mockResponse);

        // Act
        Service result = serviceClient.getServiceById("1");

        // Assert
        assertNotNull(result);
        assertEquals("Custom Service", result.getName());
        assertEquals("1", result.getId());
    }
}
