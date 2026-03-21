# CutMeShort Tracking SDK - Java Client

[![Java Version](https://img.shields.io/badge/Java-1.8%2B-blue)](https://www.java.com)
[![API Version](https://img.shields.io/badge/API-1.0.0-green)]()
[![Build Tool](https://img.shields.io/badge/Build-Maven%2FGradle-orange)]()

Official Java SDK for the **CutMeShort CMS Platform** - Track leads and sales events with comprehensive attribution support.

## Overview

The CutMeShort Java SDK enables you to track **lead** and **sales** events in your Java applications with ease. It provides a robust API for event tracking with support for:

- **Standard Event Tracking**: Direct lead and sales tracking with full attribution
- **Deferred Attribution**: Store click associations upfront and resolve them later
- **Automatic Retry Logic**: Built-in retry mechanisms for failed requests
- **Comprehensive Error Handling**: Detailed error responses with HTTP status codes
- **Security First**: API keys stored securely using environment variables

### What is Deferred Attribution?

Deferred attribution is useful when you want to track a click **before** knowing the customer's identity. Here's how it works:

1. **First Call**: Send a lead event with `mode: deferred` to store the clickId-to-customerExternalId association
2. **Later Calls**: Send normal lead events without `mode` - the backend automatically uses the stored association

**Example Use Case**: A user clicks an advertisement (clickId captured), but you don't yet know their email. Later, when they sign up, you send their customerExternalId. The system automatically correlates these events.



## Quick Start

### 1. Install

Add to your `pom.xml`:
```xml
<dependency>
    <groupId>com.cutmeshort</groupId>
    <artifactId>cms-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Initialize

```java
CMSClient client = new CMSClient(System.getenv("CUTMESHORT_API_KEY"));
```

That's it! No complex setup needed.

---

## Usage

### Track a Lead

```java
import com.cutmeshort.client.CMSClient;
import com.cutmeshort.client.exception.ApiException;

try {
    CMSClient client = new CMSClient(System.getenv("CUTMESHORT_API_KEY"));
    
    var response = client.trackLead()
        .clickId("click_456")                  // click id optional if second call of deferred mode
        .eventName("user_signup")              // What happened
        .customerExternalId("user_123")        // Your customer ID
        .customerName("John Doe")              // Optional
        .customerEmail("john@example.com")     // Optional
        .clickId("click_456")                  // Optional if deferred second attribution
        .execute();
    
    System.out.println("Success: " + response.getSuccess());
    
} catch (ApiException e) {
    System.err.println("Error: " + e.getMessage());
}
```

### Track a Sale

```java
try {
    CMSClient client = new CMSClient(System.getenv("CUTMESHORT_API_KEY"));
    
    var response = client.trackSale()
        .clickId("click_456")                  // Optional: click identifier
        .eventName("purchase")                 // Required: what happened
        .customerExternalId("user_123")        // Required: your customer ID
        .invoiceId("INV_001")                  // Required: order/invoice ID
        .amount(99.99)                         // Required: amount
        .currency("USD")                       // Optional: currency code
        .customerName("John Doe")              // Optional
        .customerEmail("john@example.com")     // Optional
        .execute();
    
    System.out.println("Sale tracked!");
    
} catch (ApiException e) {
    System.err.println("Error: " + e.getMessage());
}
```

---

## What You Need to Send

### For Leads (Minimum)
| Field | Required | Example |
|-------|----------|---------|
| `eventName` | ✅ Yes | `"signup"`, `"inquiry"`, `"browse"` |
| `customerExternalId` | ✅ Yes | Your unique customer ID |
| `clickId` | ❌ optional | Click ID from ad traffic |
| `customerName` | ❌ optional | `"John Doe"` |
| `customerEmail` | ❌ optional | `"john@example.com"` |

### For Sales (Minimum)
| Field | Required | Example |
|-------|----------|---------|
| `clickId` | ❌ optional  | Click ID from ad traffic |
| `eventName` | ✅ Yes | `"purchase"`, `"order"` |
| `customerExternalId` | ✅ Yes | Your unique customer ID |
| `invoiceId` | ✅ Yes | Order/invoice ID |
| `amount` | ✅ Yes | `99.99` |
| `currency` | ❌ optional | `"USD"`, `"EUR"` |
| `customerName` | ❌ optional | `"John Doe"` |
| `customerEmail` | ❌ optional | `"john@example.com"` |

---

## Real-World Example

```java
import com.cutmeshort.client.CMSClient;
import com.cutmeshort.client.exception.ApiException;

public class TrackingExample {
    private CMSClient client;
    
    public TrackingExample(String apiToken) throws Exception {
        this.client = new CMSClient(apiToken);
    }
    
    // When user signs up
    public void onUserSignup(String userId, String email, String name) {
        try {
            client.trackLead()
                .clickId("click_456")                  // Optional: click identifier
                .eventName("signup")
                .customerExternalId(userId)
                .customerEmail(email)
                .customerName(name)
                .execute();
            System.out.println("✓ Signup tracked");
        } catch (ApiException e) {
            System.err.println("Failed: " + e.getMessage());
        }
    }
    
    // When user makes a purchase
    public void onPurchase(String userId, String orderId, double amount) {
        try {
            client.trackSale()
                .clickId("click_id")
                .eventName("purchase")
                .customerExternalId(userId)
                .invoiceId(orderId)
                .amount(amount)
                .currency("USD")
                .execute();
            System.out.println("✓ Purchase tracked");
        } catch (ApiException e) {
            System.err.println("Failed: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        try {
            TrackingExample app = new TrackingExample(System.getenv("CUTMESHORT_API_KEY"));
            
            // User journey
            app.onUserSignup("user_123", "john@example.com", "John Doe");
            app.onPurchase("user_123", "order_1", 199.99);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## Setup API Token

### Option 1: Environment Variable (Recommended)

**Linux/Mac:**
```bash
export CUTMESHORT_API_KEY="your_api_token_here"
java -jar your_app.jar
```

**Windows:**
```cmd
set CUTMESHORT_API_KEY=your_api_token_here
java -jar your_app.jar
```

**In Java:**
```java
String token = System.getenv("CUTMESHORT_API_KEY");
CMSClient client = new CMSClient(token);
```

### Option 2: Direct Pass
```java
CMSClient client = new CMSClient("your_api_token_here");
```

---

## Error Handling

```java
try {
    var response = client.trackLead()
        .eventName("event")
        .customerExternalId("cust_123")
        .execute();
        
} catch (ApiException e) {
    // e.getCode() returns HTTP status code
    if (e.getCode() == 401) {
        System.err.println("Invalid API token");
    } else if (e.getCode() == 422) {
        System.err.println("Missing required fields");
    } else if (e.getCode() == 429) {
        System.err.println("Rate limited - wait before retrying");
    } else {
        System.err.println("Error: " + e.getMessage());
    }
}
```

---

## Common Issues

**Q: "Unauthorized" error (401)**
- Check your API token is correct
- Verify environment variable is set: `echo $CUTMESHORT_API_KEY`

**Q: "Missing required fields" error (422)**
- Check you've set all required fields (see table above)
- `eventName` and `customerExternalId` are always required

**Q: "Rate limited" error (429)**
- Wait a bit before retrying
- Don't send hundreds of requests at once

---

## That's It!

You now have everything you need to track leads and sales. Just:

1. **Initialize:** `CMSClient client = new CMSClient(token);`
2. **Track Lead:** `client.trackLead().eventName(...).customerExternalId(...).execute();`
3. **Track Sale:** `client.trackSale().clickId(...).invoiceId(...).amount(...).execute();`

For more details, see the source code documentation or contact support.
