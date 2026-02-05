package com.hybris.cloud.sappairprojectclient.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ServiceTestProfileConfigTest {

    @Value("${service.api.base-url}")
    private String baseUrl;

    @Test
    void testTestProfileApiBaseUrl() {
        // This test uses the application-test.properties file
        // which sets the base URL to https://test-server.example.com
        assertEquals("https://test-server.example.com", baseUrl);
    }
}
