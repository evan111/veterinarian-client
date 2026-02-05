# How to Set API Base URL from Tests

This document provides several approaches to set the API base URL when testing your Spring Boot application.

## Current Configuration

The `ServiceClient` class uses Spring's `@Value` annotation to inject the API base URL:

```java
@Value("${service.api.base-url:http://localhost:8080}")
private String baseUrl;
```

This means the base URL can be overridden using Spring properties.

## Approaches to Set API Base URL in Tests

### 1. Using `@TestPropertySource` (Recommended for Integration Tests)

This approach sets properties directly in the test class annotation:

```java
@SpringBootTest
@TestPropertySource(properties = {
    "service.api.base-url=https://test-api.example.com"
})
class MyIntegrationTest {
    
    @Value("${service.api.base-url}")
    private String baseUrl;

    @Test
    void testCustomApiBaseUrl() {
        assertEquals("https://test-api.example.com", baseUrl);
        // Your test logic here
    }
}
```

**When to use:** Best for integration tests where you want to override properties for the entire test class.

### 2. Using Test Profiles (Recommended for Consistent Test Configuration)

Create `src/test/resources/application-test.properties`:
```properties
service.api.base-url=https://test-server.example.com
logging.level.com.hybris.cloud.sappairprojectclient=DEBUG
```

Then use `@ActiveProfiles("test")` in your test:

```java
@SpringBootTest
@ActiveProfiles("test")
class MyTestProfileTest {
    
    @Value("${service.api.base-url}")
    private String baseUrl;

    @Test
    void testTestProfileApiBaseUrl() {
        assertEquals("https://test-server.example.com", baseUrl);
        // Your test logic here
    }
}
```

**When to use:** Best when you have consistent test configuration that should be reused across multiple test classes.

### 3. Using `ReflectionTestUtils.setField()` (For Unit Tests)

This approach directly sets the field value using reflection:

```java
@Mock
private RestTemplate restTemplate;

private ServiceClient serviceClient;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
    serviceClient = new ServiceClient(restTemplate);
    
    // Set custom base URL for this test
    ReflectionTestUtils.setField(serviceClient, "baseUrl", "https://staging-api.example.com");
}

@Test
void testWithCustomBaseUrl() {
    // Mock the RestTemplate call with the custom URL
    when(restTemplate.getForEntity(eq("https://staging-api.example.com/api/v1/services/1"), eq(Service.class)))
        .thenReturn(new ResponseEntity<>(mockService, HttpStatus.OK));

    // Your test logic here
    Service result = serviceClient.getServiceById("1");
    assertNotNull(result);
}
```

**When to use:** Best for unit tests where you want granular control over individual test methods and don't need the Spring context.

### 4. Using `@DynamicPropertySource` (For Programmatic Property Setting)

This approach allows you to set properties programmatically:

```java
@SpringBootTest
class MyDynamicPropertiesTest {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // This could be calculated dynamically, e.g., from TestContainers
        registry.add("service.api.base-url", () -> "https://dynamic-test-api.example.com");
    }

    @Test
    void testDynamicApiBaseUrl() {
        // Your test logic here
    }
}
```

**When to use:** Best when you need to calculate the URL programmatically, such as when using TestContainers with dynamic ports.

### 5. Using System Properties

Set system properties before creating the Spring context:

```java
@BeforeAll
static void setUpSystemProperties() {
    System.setProperty("service.api.base-url", "https://system-prop-api.example.com");
}

@AfterAll
static void cleanUpSystemProperties() {
    System.clearProperty("service.api.base-url");
}
```

**When to use:** Good for quick tests or when you can't modify test class annotations.

## Summary

| Approach | Best For | Pros | Cons |
|----------|----------|------|------|
| `@TestPropertySource` | Integration tests | Simple, declarative | Limited to static values |
| Test Profiles | Consistent test config | Reusable, organized | Requires separate properties file |
| `ReflectionTestUtils` | Unit tests | Granular control, fast | Couples to implementation details |
| `@DynamicPropertySource` | Dynamic values | Programmatic, flexible | More complex setup |
| System Properties | Quick tests | Simple setup | Global scope, cleanup needed |

## Working Examples

All the approaches above have been tested and are working in the project:

- `ServiceBaseUrlConfigTest.java` - Uses `@TestPropertySource`
- `ServiceTestProfileConfigTest.java` - Uses `@ActiveProfiles` with test properties file  
- `ServiceClientTest.java` - Uses `ReflectionTestUtils.setField()`

Choose the approach that best fits your testing needs!
