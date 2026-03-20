# CutMeShort Tracking SDK - Java Client

[![Java Version](https://img.shields.io/badge/Java-1.8%2B-blue)](https://www.java.com)
[![API Version](https://img.shields.io/badge/API-1.0.0-green)]()
[![Build Tool](https://img.shields.io/badge/Build-Maven%2FGradle-orange)]()

Official Java SDK for the **CutMeShort CMS Platform** - Track leads and sales events with comprehensive attribution support.

## Overview

The CutMeShort Java SDK enables you to track **lead** and **sales** events in your Java applications with ease. It provides a robust API for event tracking with support for:

- **Standard Event Tracking**: Direct lead and sales tracking with full attribution
- **Deferred Attribution**: Store click associations upfront and resolve them later
- **Synchronous & Asynchronous Calls**: Choose between blocking and non-blocking requests
- **Automatic Retry Logic**: Built-in retry mechanisms for failed requests
- **Comprehensive Error Handling**: Detailed error responses with HTTP status codes
- **Security First**: API keys stored securely using environment variables

### What is Deferred Attribution?

Deferred attribution is useful when you want to track a click **before** knowing the customer's identity. Here's how it works:

1. **First Call**: Send a lead event with `mode: deferred` to store the clickId-to-customerExternalId association
2. **Later Calls**: Send normal lead events without `mode` - the backend automatically uses the stored association

**Example Use Case**: A user clicks an advertisement (clickId captured), but you don't yet know their email. Later, when they sign up, you send their customerExternalId. The system automatically correlates these events.

## Features

✅ Easy-to-use API client  
✅ Support for lead and sales event tracking  
✅ Deferred attribution mode for flexible use cases  
✅ Synchronous and asynchronous request execution  
✅ Automatic retry with exponential backoff  
✅ Comprehensive error handling with detailed responses  
✅ Environment-based configuration  
✅ Built with security best practices  
✅ Full API documentation and code examples  
✅ Support for Java 1.8 and higher  

## Requirements

Building and using the SDK requires:

- **Java 1.8+** or higher
- **Maven 3.8.3+** or **Gradle 7.2+**
- **OkHttp 4.x** (included as dependency)
- **Gson 2.8.x+** (included as dependency)

## Installation

### Option 1: Build and Install Locally (Recommended)

Clone or download this SDK and install it to your local Maven repository:

```bash
mvn clean install
```

Then add this dependency to your project's `pom.xml`:

```xml
<dependency>
    <groupId>com.cutmeshort</groupId>
    <artifactId>cms-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Option 2: Build with Gradle

If using Gradle, run:

```bash
./gradlew clean build
```

Add this to your `build.gradle`:

```groovy
repositories {
    mavenLocal()       // Uses the locally installed JAR
    mavenCentral()
}

dependencies {
    implementation "com.cutmeshort:cms-java:1.0.0"
}
```

### Option 3: Manual JAR Installation

Execute:

```bash
mvn clean package
```

Then manually add these JAR files to your classpath:
- `target/cms-java-1.0.0.jar`
- All JARs in `target/lib/` directory

---

## Quick Start

Here's the minimal code to get started:

```java
import com.cutmeshort.client.core.ApiClient;
import com.cutmeshort.client.exception.ApiException;
import com.cutmeshort.client.core.Configuration;
import com.cutmeshort.client.auth.ApiKeyAuth;
import com.cutmeshort.client.api.TrackingApi;
import com.cutmeshort.client.model.LeadPayload;
import com.cutmeshort.client.model.TrackResponse;

public class QuickStart {
    public static void main(String[] args) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            
            // Set your API key (from environment variable)
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            // Create the API instance
            TrackingApi api = new TrackingApi(client);
            
            // Create a lead payload
            LeadPayload lead = new LeadPayload()
                .clickId("click_123456")
                .eventName("lead_created")
                .customerExternalId("customer_789");
            
            // Track the lead
            TrackResponse response = api.trackLead(lead);
            
            System.out.println("Success: " + response.getSuccess());
            System.out.println("Message: " + response.getMessage());
            
        } catch (ApiException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Status Code: " + e.getCode());
        }
    }
}
```

---

## Authentication

### Setting Up API Authentication

The SDK uses **API Key authentication** via the `Authorization Bearer<>` header. Never hardcode your API key in your source code!

**Note:** The base URL is **automatically configured** to default url. You only need to set your API key.

#### Recommended: Use Environment Variables

```java
import com.cutmeshort.client.core.ApiClient;
import com.cutmeshort.client.core.Configuration;
import com.cutmeshort.client.auth.ApiKeyAuth;

// Get API key from environment variable
String apiKey = System.getenv("CUTMESHORT_API_KEY");
if (apiKey == null || apiKey.isEmpty()) {
    throw new RuntimeException("CUTMESHORT_API_KEY environment variable not set");
}

// Initialize SDK (base URL automatically set)
ApiClient client = Configuration.getDefaultApiClient();
ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
auth.setApiKey(apiKey);
```

#### Linux/Mac Environment Setup

```bash
export CUTMESHORT_API_KEY="your_api_key_here"
java -jar your_app.jar
```

#### Windows Environment Setup

```cmd
set CUTMESHORT_API_KEY=your_api_key_here
java -jar your_app.jar
```

---

## Usage Examples

### Tracking Leads

A **lead** is a potential customer. Track leads when users sign up, show interest, or complete initial actions.

#### Basic Lead Tracking

```java
import com.cutmeshort.client.core.ApiClient;
import com.cutmeshort.client.core.Configuration;
import com.cutmeshort.client.auth.ApiKeyAuth;
import com.cutmeshort.client.api.TrackingApi;
import com.cutmeshort.client.model.LeadPayload;
import com.cutmeshort.client.model.TrackResponse;

public class LeadTrackingExample {
    public static void main(String[] args) {
        try {
            // Initialize API client (base URL automatically configured)
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            // Create lead payload with required fields
            LeadPayload lead = new LeadPayload()
                .clickId("click_abc123")                          // Required
                .eventName("user_signup")                         // Required
                .customerExternalId("user_12345");                // Required
            
            // Track the lead
            TrackResponse response = api.trackLead(lead);
            
            if (response.getSuccess()) {
                System.out.println("✓ Lead tracked successfully");
                System.out.println("Message: " + response.getMessage());
            } else {
                System.out.println("✗ Failed to track lead");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### Lead Tracking with Detailed Information

```java
import java.time.OffsetDateTime;
import java.net.URI;

public class DetailedLeadExample {
    public static void main(String[] args) {
        try {
            // Setup API client (base URL automatically configured)
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            // Create lead with all available fields
            LeadPayload lead = new LeadPayload()
                .clickId("click_xyz789")
                .eventName("premium_signup")
                .customerExternalId("cust_54321")
                .customerName("John Doe")                         // Optional
                .customerEmail("john@example.com")                // Optional
                .customerAvatar(new URI("https://example.com/avatar.jpg")) // Optional
                .timestamp(OffsetDateTime.now());                 // Optional - ISO 8601
            
            TrackResponse response = api.trackLead(lead);
            
            System.out.println("Success: " + response.getSuccess());
            System.out.println("Message: " + response.getMessage());
            if (response.getData() != null) {
                System.out.println("Data: " + response.getData());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Tracking Sales

A **sale** is a completed purchase. Track sales when customers complete a transaction.

#### Basic Sales Tracking

```java
import com.cutmeshort.client.api.TrackingApi;
import com.cutmeshort.client.model.SalePayload;

public class SalesTrackingExample {
    public static void main(String[] args) {
        try {
            // Setup API client (base URL automatically configured)
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            // Create sale payload - all fields are required
            SalePayload sale = new SalePayload()
                .clickId("click_sale123")                         // Required
                .eventName("purchase_completed")                  // Required
                .customerExternalId("user_12345")                 // Required
                .invoiceId("inv_2024_001")                        // Required
                .amount(99.99)                                    // Required
                .currency("USD");                                 // Required
            
            TrackResponse response = api.trackSale(sale);
            
            if (response.getSuccess()) {
                System.out.println("✓ Sale tracked successfully");
                System.out.println("Invoice: inv_2024_001");
                System.out.println("Amount: $99.99");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### Detailed Sales Tracking with Customer Info

```java
import java.time.OffsetDateTime;
import java.net.URI;

public class DetailedSalesExample {
    public static void main(String[] args) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            // Create sale with customer details
            SalePayload sale = new SalePayload()
                .clickId("click_xyz789")
                .eventName("order_completed")
                .customerExternalId("cust_54321")
                .customerName("Jane Smith")                       // Optional
                .customerEmail("jane@company.com")                // Optional
                .customerAvatar(new URI("https://example.com/profile.jpg")) // Optional
                .invoiceId("inv_2024_456")
                .amount(249.50)
                .currency("USD")
                .timestamp(OffsetDateTime.now());                 // Optional
            
            TrackResponse response = api.trackSale(sale);
            
            System.out.println("Transaction Success: " + response.getSuccess());
            System.out.println("Message: " + response.getMessage());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Deferred Attribution

Use deferred attribution when you don't know the customer's identity at the time of click tracking.

#### Step 1: Create Deferred Association

```java
import com.cutmeshort.client.model.LeadPayload;

public class DeferredAttributionStep1 {
    public static void main(String[] args) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            // User clicks an ad, we capture the clickId but don't know their identity yet
            // Send with mode: deferred to store the association
            LeadPayload deferredLead = new LeadPayload()
                .clickId("click_adcampaign_001")                  // The click from the ad
                .eventName("ad_click")
                .customerExternalId("temp_user_unknown")          // Temporary/placeholder ID
                .mode(LeadPayload.ModeEnum.DEFERRED);             // IMPORTANT: Set mode to deferred
            
            TrackResponse response = api.trackLead(deferredLead);
            
            System.out.println("Deferred association created: " + response.getSuccess());
            System.out.println("Message: " + response.getMessage());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### Step 2: Send Normal Lead Event (Later)

```java
public class DeferredAttributionStep2 {
    public static void main(String[] args) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            // Later, the user signs up and we now know their identity
            // Send a normal lead event WITHOUT mode - the backend uses the stored deferred association
            LeadPayload normalLead = new LeadPayload()
                .eventName("user_signup")
                .customerExternalId("user_12345")                 // Real customer ID
                // NOTE: No clickId, no mode
            
            TrackResponse response = api.trackLead(normalLead);
            
            System.out.println("Lead event tracked: " + response.getSuccess());
            System.out.println("Message: " + response.getMessage());
            System.out.println("Note: Backend automatically resolves ad_click -> user_signup");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Async Requests

For non-blocking operations, use asynchronous requests:

```java
import com.cutmeshort.client.core.ApiCallback;

public class AsyncTrackingExample {
    public static void main(String[] args) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            LeadPayload lead = new LeadPayload()
                .clickId("click_async_001")
                .eventName("async_lead")
                .customerExternalId("async_customer_123");
            
            // Define callback for async response
            ApiCallback<TrackResponse> callback = new ApiCallback<TrackResponse>() {
                @Override
                public void onSuccess(TrackResponse result, int statusCode, java.util.Map<String, java.util.List<String>> responseHeaders) {
                    System.out.println("✓ Async request successful!");
                    System.out.println("Success: " + result.getSuccess());
                    System.out.println("Message: " + result.getMessage());
                }

                @Override
                public void onFailure(ApiException e, int statusCode, java.util.Map<String, java.util.List<String>> responseHeaders) {
                    System.err.println("✗ Async request failed!");
                    System.err.println("Status Code: " + statusCode);
                    System.err.println("Error: " + e.getMessage());
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
                    System.out.println("Upload progress: " + bytesWritten + "/" + contentLength);
                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
                    System.out.println("Download progress: " + bytesRead + "/" + contentLength);
                }
            };
            
            // Execute async request
            okhttp3.Call call = api.trackLeadAsync(lead, callback);
            
            // Optional: Cancel if needed
            // call.cancel();
            
            // Keep application alive for async callback
            Thread.sleep(5000);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## Error Handling

The SDK provides detailed error information to help with debugging:

```java
import com.cutmeshort.client.exception.ApiException;

public class ErrorHandlingExample {
    public static void main(String[] args) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            LeadPayload lead = new LeadPayload()
                .clickId("click_123")
                .eventName("test_event")
                .customerExternalId("customer_123");
            
            TrackResponse response = api.trackLead(lead);
            
        } catch (ApiException e) {
            // Handle API errors
            System.err.println("API Error occurred!");
            System.err.println("Status Code: " + e.getCode());
            System.err.println("Message: " + e.getMessage());
            System.err.println("Response Body: " + e.getResponseBody());
            System.err.println("Response Headers: " + e.getResponseHeaders());
            
            // Handle specific HTTP status codes
            switch (e.getCode()) {
                case 400:
                    System.err.println("❌ Bad Request - Invalid payload format");
                    break;
                case 401:
                    System.err.println("❌ Unauthorized - Invalid or missing API key");
                    break;
                case 403:
                    System.err.println("❌ Forbidden - Insufficient permissions");
                    break;
                case 404:
                    System.err.println("❌ Not Found - Resource doesn't exist");
                    break;
                case 422:
                    System.err.println("❌ Validation Error - Check your payload");
                    break;
                case 429:
                    System.err.println("⏱️ Rate Limit Exceeded - Wait before retrying");
                    break;
                case 500:
                    System.err.println("❌ Server Error - Try again later");
                    break;
                default:
                    System.err.println("❌ Unexpected error");
            }
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

### Common HTTP Status Codes

| Code | Meaning | Solution |
|------|---------|----------|
| 200 | Success | Request processed successfully |
| 400 | Bad Request | Check your payload JSON format |
| 401 | Unauthorized | Verify your API key via environment variable |
| 403 | Forbidden | Your API key lacks required permissions |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Resource already exists |
| 422 | Validation Failed | Check required fields are present |
| 429 | Too Many Requests | Rate limited - wait before retrying |
| 500 | Server Error | Temporary server issue - retry later |
| 502 | Gateway Error | Service temporarily unavailable |
| 503 | Service Unavailable | Maintenance - try again later |
| 504 | Gateway Timeout | Request timeout - try again |

---

## Configuration

### Environment Variables

The SDK supports the following environment variables for configuration:

```bash
# API Configuration
CUTMESHORT_API_URL="api url"          # Base API URL
CUTMESHORT_API_KEY="your_api_key_here"                   # Your API key (REQUIRED)
CUTMESHORT_ENVIRONMENT="production"                       # Environment: development, staging, production

# Connection Configuration
CUTMESHORT_CONNECT_TIMEOUT_SECONDS=30                     # Connection timeout
CUTMESHORT_READ_TIMEOUT_SECONDS=60                        # Read timeout
CUTMESHORT_WRITE_TIMEOUT_SECONDS=60                       # Write timeout

# Debug Configuration
CUTMESHORT_DEBUG_MODE=false                               # Enable debug logging
CUTMESHORT_VERIFY_SSL=true                                # SSL verification (use false for dev only!)
```

### Programmatic Configuration

```java
import org.openapitools.client.ApiClient;
import org.openapitools.client.Configuration;

public class ConfigurationExample {
    public static void main(String[] args) {
        // Create a custom API client (base URL automatically set to https://www.cutmeshort.com)
        ApiClient client = new ApiClient();
        
        // Set timeouts (in milliseconds)
        client.setConnectTimeout(30000);  // 30 seconds
        client.setReadTimeout(60000);     // 60 seconds
        client.setWriteTimeout(60000);    // 60 seconds
        
        // Enable debug logging
        client.setDebugging(true);
        
        // Set user agent
        client.setUserAgent("MyApp/1.0.0");
        
        // Set default authentication
        ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
        auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
        
        // Set as default client for all API instances
        Configuration.setDefaultApiClient(client);
        
        System.out.println("Client configured successfully!");
    }
}
```

---

## Advanced Usage

### Batch Processing

```java
import java.util.ArrayList;
import java.util.List;

public class BatchTrackingExample {
    public static void main(String[] args) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            // Batch track multiple leads
            List<LeadPayload> leads = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                LeadPayload lead = new LeadPayload()
                    .clickId("click_batch_" + i)
                    .eventName("batch_lead")
                    .customerExternalId("customer_batch_" + i);
                leads.add(lead);
            }
            
            // Track all leads
            int successCount = 0;
            int failureCount = 0;
            
            for (LeadPayload lead : leads) {
                try {
                    TrackResponse response = api.trackLead(lead);
                    if (response.getSuccess()) {
                        successCount++;
                        System.out.println("✓ Tracked: " + lead.getCustomerExternalId());
                    } else {
                        failureCount++;
                        System.out.println("✗ Failed: " + lead.getCustomerExternalId());
                    }
                } catch (ApiException e) {
                    failureCount++;
                    System.err.println("Error tracking " + lead.getCustomerExternalId() + ": " + e.getMessage());
                }
            }
            
            System.out.println("\nBatch Summary:");
            System.out.println("Successful: " + successCount);
            System.out.println("Failed: " + failureCount);
            System.out.println("Total: " + leads.size());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Retry Logic with Backoff

```java
public class RetryExample {
    private static final int MAX_RETRIES = 3;
    private static final int INITIAL_DELAY_MS = 1000;
    
    public static void main(String[] args) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            LeadPayload lead = new LeadPayload()
                .clickId("click_retry_001")
                .eventName("retry_event")
                .customerExternalId("retry_customer_123");
            
            TrackResponse response = trackLeadWithRetry(api, lead, 0);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static TrackResponse trackLeadWithRetry(TrackingApi api, LeadPayload lead, int attempt) throws ApiException {
        try {
            return api.trackLead(lead);
        } catch (ApiException e) {
            // Retry on 5xx errors or rate limiting
            if ((e.getCode() >= 500 || e.getCode() == 429) && attempt < MAX_RETRIES) {
                int delayMs = INITIAL_DELAY_MS * (int) Math.pow(2, attempt);
                System.out.println("Retry " + (attempt + 1) + " after " + delayMs + "ms...");
                
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                return trackLeadWithRetry(api, lead, attempt + 1);
            } else {
                throw e;
            }
        }
    }
}
```

### Custom HTTP Headers

```java
public class CustomHeadersExample {
    public static void main(String[] args) {
        try {
            ApiClient client = Configuration.getDefaultApiClient();
            
            // Add custom headers
            client.addDefaultHeader("X-Custom-Header", "CustomValue");
            client.addDefaultHeader("X-Request-ID", "req_12345_" + System.currentTimeMillis());
            
            ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
            auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
            
            TrackingApi api = new TrackingApi(client);
            
            LeadPayload lead = new LeadPayload()
                .clickId("click_custom_001")
                .eventName("custom_header_event")
                .customerExternalId("cust_custom_123");
            
            TrackResponse response = api.trackLead(lead);
            System.out.println("Request with custom headers successful: " + response.getSuccess());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## Complete Examples

### Example 1: E-commerce Store

```java
/**
 * Complete example: Track leads and sales for an e-commerce store
 */
public class EcommerceExample {
    
    private TrackingApi trackingApi;
    
    public EcommerceExample() throws Exception {
        // Initialize API (base URL automatically configured)
        ApiClient client = Configuration.getDefaultApiClient();
        
        ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
        auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
        
        this.trackingApi = new TrackingApi(client);
    }
    
    public void onUserAdClick(String clickId) {
        try {
            LeadPayload lead = new LeadPayload()
                .clickId(clickId)
                .eventName("ad_click")
                .customerExternalId("anonymous_user");
            
            trackingApi.trackLead(lead);
            System.out.println("✓ Ad click tracked: " + clickId);
        } catch (ApiException e) {
            System.err.println("✗ Failed to track ad click: " + e.getMessage());
        }
    }
    
    public void onUserSignup(String userId, String email, String name) {
        try {
            LeadPayload lead = new LeadPayload()
                .eventName("user_signup")
                .customerExternalId(userId)
                .customerEmail(email)
                .customerName(name);
            
            trackingApi.trackLead(lead);
            System.out.println("✓ User signup tracked: " + userId);
        } catch (ApiException e) {
            System.err.println("✗ Failed to track signup: " + e.getMessage());
        }
    }
    
    public void onPurchaseComplete(String userId, String orderId, double amount, String currency) {
        try {
            SalePayload sale = new SalePayload()
                .clickId("click_" + orderId)
                .eventName("purchase_completed")
                .customerExternalId(userId)
                .invoiceId(orderId)
                .amount(amount)
                .currency(currency)
                .timestamp(OffsetDateTime.now());
            
            trackingApi.trackSale(sale);
            System.out.println("✓ Sale tracked: " + orderId + " ($" + amount + ")");
        } catch (Exception e) {
            System.err.println("✗ Failed to track sale: " + e.getMessage());
        }
    }
    
    // Main test
    public static void main(String[] args) {
        try {
            EcommerceExample shop = new EcommerceExample();
            
            // Simulate user journey
            System.out.println("=== E-commerce Tracking Example ===\n");
            
            shop.onUserAdClick("click_campaign_2024_001");
            shop.onUserSignup("user_12345", "john@example.com", "John Doe");
            shop.onPurchaseComplete("user_12345", "order_2024_001", 149.99, "USD");
            
            System.out.println("\n✓ All events tracked successfully!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

### Example 2: SaaS Application

```java
/**
 * Complete example: Track leads and sales for a SaaS application
 */
public class SaasBusiness {
    
    private TrackingApi trackingApi;
    
    public SaasBusiness() throws Exception {
        ApiClient client = Configuration.getDefaultApiClient();
        
        ApiKeyAuth auth = (ApiKeyAuth) client.getAuthentication("ApiKeyAuth");
        auth.setApiKey(System.getenv("CUTMESHORT_API_KEY"));
        
        this.trackingApi = new TrackingApi(client);
    }
    
    // Track trial signup
    public void onTrialSignup(String companyId, String companyName, String email) {
        try {
            LeadPayload trial = new LeadPayload()
                .eventName("trial_started")
                .customerExternalId(companyId)
                .customerName(companyName)
                .customerEmail(email)
                .timestamp(OffsetDateTime.now());
            
            trackingApi.trackLead(trial);
            System.out.println("✓ Trial started: " + companyId);
        } catch (ApiException e) {
            System.err.println("✗ Failed to track trial: " + e.getMessage());
        }
    }
    
    // Track subscription purchase
    public void onSubscriptionPurchase(String companyId, String plan, double monthlyPrice) {
        try {
            SalePayload subscription = new SalePayload()
                .clickId("sub_click_" + companyId)
                .eventName("subscription_purchased")
                .customerExternalId(companyId)
                .invoiceId("INV_" + System.currentTimeMillis())
                .amount(monthlyPrice)
                .currency("USD");
            
            trackingApi.trackSale(subscription);
            System.out.println("✓ Subscription purchased: " + plan + " ($" + monthlyPrice + "/mo)");
        } catch (ApiException e) {
            System.err.println("✗ Failed to track subscription: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        try {
            SaasBusiness saas = new SaasBusiness();
            
            System.out.println("=== SaaS Tracking Example ===\n");
            
            saas.onTrialSignup("company_001", "Tech Startup Inc", "admin@techstartup.com");
            saas.onSubscriptionPurchase("company_001", "Professional", 99.00);
            
            System.out.println("\n✓ SaaS events tracked successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## API Reference

### TrackingApi

#### trackLead(LeadPayload)

Track a lead event.

**Parameters:**
- `leadPayload` (LeadPayload) - Required. Lead event data

**Returns:** `TrackResponse`

**HTTP Method:** POST
**Endpoint:** `/api/sdk/track-lead`

---

#### trackLeadAsync(LeadPayload, ApiCallback)

Track a lead event asynchronously.

**Parameters:**
- `leadPayload` (LeadPayload) - Required. Lead event data
- `_callback` (ApiCallback<TrackResponse>) - Callback for async result

**Returns:** `okhttp3.Call`

---

#### trackSale(SalePayload)

Track a sale event.

**Parameters:**
- `salePayload` (SalePayload) - Required. Sale event data

**Returns:** `TrackResponse`

**HTTP Method:** POST
**Endpoint:** `/api/sdk/track-sale`

---

#### trackSaleAsync(SalePayload, ApiCallback)

Track a sale event asynchronously.

**Parameters:**
- `salePayload` (SalePayload) - Required. Sale event data
- `_callback` (ApiCallback<TrackResponse>) - Callback for async result

**Returns:** `okhttp3.Call`

---

## Best Practices

✅ **DO:**
- Use environment variables for API keys
- Implement error handling for all API calls
- Use deferred attribution when appropriate
- Batch track events when possible
- Include customer details for better attribution
- Use ISO 8601 timestamps
- Implement retry logic for failed requests
- Log API responses for debugging
- Test with both sync and async calls

❌ **DON'T:**
- Hardcode API keys in source code
- Ignore API errors and exceptions
- Send duplicate events
- Use invalid timestamp formats
- Expose API keys in logs or error messages
- Make requests without proper authentication
- Send sensitive data in event names
- Bypass error handling

---

## Troubleshooting

### Issue: "API key not found" or Authorization Fails

**Solution:**
```bash
# Check if environment variable is set
echo $CUTMESHORT_API_KEY

# Set it if missing
export CUTMESHORT_API_KEY="your_key_here"

# Verify in Java
System.out.println(System.getenv("CUTMESHORT_API_KEY"));
```

### Issue: "Connection Refused" or "Network Error"

**Solution:**
```java
// Verify API endpoint
System.out.println("Using API URL: " + client.getBasePath());

// Increase timeouts for slow networks
client.setConnectTimeout(60000); // 60 seconds
client.setReadTimeout(120000);   // 120 seconds
```

### Issue: "Invalid payload" or HTTP 400

**Solution:**
```java
// Verify required fields are present
if (lead.getEventName() == null || lead.getCustomerExternalId() == null) {
    System.err.println("Missing required fields!");
}
```

### Issue: "Rate limited" or HTTP 429

**Solution:**
- Wait before retrying (check Retry-After header)
- Implement exponential backoff between retries
- Batch requests to reduce frequency

---

## Version

- **SDK Version:** 1.0.0
- **API Version:** 1.0.0
- **Build Date:** 2026-03-20
- **Generator:** OpenAPI Generator 7.20.0

---

**Happy Tracking! 🚀**

For more information, visit the [CutMeShort CMS Platform](https://www.cutmeshort.com)


