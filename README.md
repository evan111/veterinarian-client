# SAP Pair Project Client

A Spring Boot **client-only** application that demonstrates REST API consumption by calling `http://localhost:8080/api/v1/services` and internally processing the response. The application runs the `fetchAndListServices` method on startup, retrieves service data, and displays it through structured logging.

**Key Concept:** This is NOT a web service - it's a standalone client that connects to an existing REST API, fetches data, processes it internally, and exits gracefully without starting any web server.

**Important:** Designed to run alongside services on port 8080 without conflicts - it only makes outbound HTTP calls and never binds to any ports.

## What This Application Does

1. **Starts up** as a Spring Boot application (client-only mode)
2. **Connects** to your REST API at `http://localhost:8080/api/v1/services`
3. **Fetches** all available services via HTTP GET request
4. **Deserializes** JSON response into Service objects
5. **Lists internally** all service details with formatted logging
6. **Exits gracefully** after displaying results

## Features

- **Primary Function**: Executes `fetchAndListServices()` method on startup
- **REST Client**: Built-in HTTP client for API consumption
- **JSON Processing**: Automatic deserialization of service data
- **Structured Logging**: Professional output formatting for service details
- **Error Handling**: Graceful handling of connection and parsing errors
- **Zero Port Conflicts**: No web server - only outbound HTTP calls
- **Configurable**: Support for different base URLs and endpoints

## Project Structure

```
src/main/java/com/hybris/cloud/sappairprojectclient/
├── FetchServicesRunner.java               # **MAIN ENTRY POINT** - Executes fetchAndListServices
├── SapPairProjectClientApplication.java    # Spring Boot configuration class
├── client/
│   └── ServiceClient.java                  # REST client for calling services endpoint
├── config/
│   └── RestClientConfig.java              # RestTemplate configuration
├── model/
│   └── Service.java                       # Service model for JSON deserialization
└── service/
    └── ServiceListingService.java         # Service layer for listing operations
```

## Client-Only Configuration

The application is configured as a **client-only** application to avoid port conflicts:

- `WebApplicationType.NONE` - No embedded web server
- Uses `spring-web` instead of `spring-boot-starter-web` for RestTemplate support
- No server port configuration needed

## Service Data Model

The client expects your REST API to return service data in this JSON format:

```json
[
  {
    "id": "9dee2b1f-2f4b-47da-85f1-05d6b0c34d44",
    "name": "Grooming - Full Service",
    "description": "Complete grooming service including bath, haircut, nail trimming, and ear cleaning.",
    "status": "ACTIVE",
    "version": "1.0"
  },
  {
    "id": "2239c9ec-fd1a-4d88-bee4-bdb8c8089720", 
    "name": "Annual Health Checkup",
    "description": "Comprehensive annual physical examination including vital signs, general health assessment, and wellness consultation.",
    "status": "ACTIVE",
    "version": "2.0"
  }
]
```

**Note**: The client also supports the `/v3/api-docs` endpoint for OpenAPI documentation as a fallback.

## Configuration

The application can be configured using the following properties:

```properties
# Service API Configuration (defaults to localhost:8080)
service.api.base-url=http://localhost:8080

# Logging Configuration
logging.level.com.hybris.cloud.sappairprojectclient=INFO
logging.level.org.springframework.web.client=DEBUG

# Application Configuration
spring.application.name=sap-pair-project-client

# Disable web application (no server needed for client)
spring.main.web-application-type=none
```

## Usage

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- The target REST service running at `http://localhost:8080` (or configured URL)

### Building the Application

```bash
mvn clean package
```

### Running the Application

**Main Entry Point**: `FetchServicesRunner` - directly calls `fetchAndListServices` method

```bash
# Using Maven (runs FetchServicesRunner.main)
mvn spring-boot:run

# Using Java directly (requires Java 17+)
java -jar target/sap-pair-project-client-0.0.1-SNAPSHOT.jar

# With custom configuration
java -jar target/sap-pair-project-client-0.0.1-SNAPSHOT.jar --service.api.base-url=http://your-service-host:8080
```

### Port Conflict Resolution

**Problem:** If the client tried to start a web server on port 8080, it would conflict with the target service.

**Solution:** The application is configured as client-only:
- No embedded Tomcat server
- No web endpoints
- Runs, calls the service, logs results, and exits
- Perfect for calling services without port conflicts

### Example Output

When the application runs successfully, you'll see output like this:

```
2026-02-05T12:20:16.318  INFO --- Executing fetchAndListServices method...
2026-02-05T12:20:16.318  INFO --- Starting to fetch services from REST endpoint...
2026-02-05T12:20:16.318  INFO --- Calling REST endpoint: http://localhost:8080/api/v1/services
2026-02-05T12:20:16.397  INFO --- Successfully retrieved 6 services

=== SERVICE LISTING ===
Total services found: 6
========================
Service #1: Service{id='9dee2b1f-2f4b-47da-85f1-05d6b0c34d44', name='Grooming - Full Service', description='Complete grooming service including bath, haircut, nail trimming, and ear cleaning.', status='null', version='null'}

Service #2: Service{id='2239c9ec-fd1a-4d88-bee4-bdb8c8089720', name='Annual Health Checkup', description='Comprehensive annual physical examination including vital signs, general health assessment, and wellness consultation.', status='null', version='null'}

Service #3: Service{id='0d80718c-373a-47f6-82f2-290587f3b370', name='Vaccination - Rabies', description='Rabies vaccination for dogs and cats. Required by law in most states.', status='null', version='null'}

Service #4: Service{id='b6a8abfd-b381-48c3-9fbe-94256ba5b67c', name='Emergency Consultation', description='Immediate emergency care and assessment for urgent medical conditions.', status='null', version='null'}

Service #5: Service{id='36c2ba18-6c23-4755-bda2-d69bca7098cc', name='Dental Cleaning', description='Professional dental cleaning under anesthesia including scaling, polishing, and oral examination.', status='null', version='null'}

Service #6: Service{id='d6af8d26-b97b-4cfc-a3fc-13b8c1c42192', name='Spay/Neuter Surgery', description='Routine spay or neuter surgery with pre-surgical examination and post-operative care instructions.', status='null', version='null'}

=== END SERVICE LISTING ===
2026-02-05T12:20:16.399  INFO --- fetchAndListServices execution completed!
2026-02-05T12:20:16.399  INFO --- === fetchAndListServices Method Execution Complete ===
```

If the target service is not available, you'll see error handling:

```
2026-02-05 09:57:30.125  INFO --- Attempting to connect to REST endpoint at http://localhost:8080/api/v1/services
2026-02-05 09:57:30.126  ERROR --- Failed to connect to service at http://localhost:8080: Connection refused
2026-02-05 09:57:30.127  WARN --- No services retrieved. This could mean:
2026-02-05 09:57:30.128  WARN --- 1. The target service at http://localhost:8080 is not running
2026-02-05 09:57:30.129  WARN --- 2. The endpoint /api/v1/services does not exist
2026-02-05 09:57:30.130  WARN --- 3. There are network connectivity issues
2026-02-05 09:57:30.131  WARN --- 4. The response format doesn't match the expected Service model
```

## API Usage

### ServiceClient

Direct usage of the REST client:

```java
@Autowired
private ServiceClient serviceClient;

// Get OpenAPI documentation
String apiDocs = serviceClient.getApiDocs();

// Get all services
List<Service> services = serviceClient.getServices();

// Get a specific service by ID
Service service = serviceClient.getServiceById("service-id");
```

### ServiceListingService

Higher-level service operations:

```java
@Autowired
private ServiceListingService serviceListingService;

// Fetch and log API documentation
serviceListingService.fetchAndDisplayApiDocs();

// Fetch and log all services
serviceListingService.fetchAndListServices();

// Get services for further processing
List<Service> services = serviceListingService.getServicesList();

// Display a specific service
serviceListingService.fetchAndDisplayService("service-id");
```

## Error Handling

The client handles various error scenarios:

- **Connection Refused**: When the target service is not running
- **HTTP Errors**: Non-2xx responses are logged and handled gracefully
- **JSON Parsing**: Malformed responses are caught and logged
- **Network Timeouts**: Configurable timeouts prevent hanging requests

## Testing

The project includes unit tests for the ServiceClient:

```bash
mvn test
```

## Customization

To modify the expected JSON structure, update the `Service` model class:

```java
// Add new fields to the Service model
public class Service {
    private String id;
    private String name;
    private String description;
    private String status;
    private String version;
    // Add your custom fields here
}
```

To call different endpoints, modify the `ServiceClient` class or create additional client classes following the same pattern.

## Summary

This Spring Boot client application is a **data consumption demonstration tool** that:

### Primary Purpose
- **Demonstrates** how to consume REST APIs from a Spring Boot client
- **Shows** JSON deserialization and data processing
- **Provides** structured logging of retrieved service information
- **Serves** as a template for building REST API clients

### What It Does NOT Do
- ❌ Does not start a web server
- ❌ Does not provide REST endpoints  
- ❌ Does not bind to any ports
- ❌ Does not conflict with existing services

### What It DOES Do
- ✅ Connects to external REST APIs via HTTP client
- ✅ Fetches and processes JSON data
- ✅ Displays structured service information
- ✅ Handles errors gracefully
- ✅ Runs once and exits cleanly

### Perfect For
- **API Integration Testing**: Verify your REST endpoints work correctly
- **Data Processing Examples**: Learn how to consume and process API responses
- **Client Development**: Use as a starting point for building REST clients
- **Demonstration Purposes**: Show REST API consumption without server complexity

### Technical Architecture
- **Entry Point**: `FetchServicesRunner.main()` method
- **Execution Flow**: Spring Boot startup → `fetchAndListServices()` → HTTP call → Data processing → Logging → Graceful exit
- **Mode**: Client-only (`WebApplicationType.NONE`)
- **Dependencies**: Minimal Spring Boot setup with REST client capabilities

This application perfectly demonstrates REST API consumption patterns and serves as an excellent starting point for building more complex client applications.

