package com.hybris.cloud.sappairprojectclient.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "service.api.base-url=https://test-api.example.com"
})
class ServiceBaseUrlConfigTest {

    @Value("${service.api.base-url}")
    private String baseUrl;

    @Test
    void testCustomApiBaseUrlIsSet() {
        // This test verifies that the custom base URL property is correctly set
        assertEquals("https://test-api.example.com", baseUrl);
    }
}
