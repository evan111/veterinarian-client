package com.hybris.cloud.sappairprojectclient.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TestRestClientConfig {

    @Bean
    @Primary
    public RestTemplate testRestTemplate() {
        // You can customize the RestTemplate for testing here
        return new RestTemplate();
    }
}
