package com.cutmeshort.client.example;

import com.cutmeshort.client.CMSClient;
import com.cutmeshort.client.exception.ApiException;
import com.cutmeshort.client.model.TrackResponse;

/**
 * Simple usage examples for the CutMeShort SDK.
 * 
 * This demonstrates how easy it is to use the SDK with the simplified CMSClient.
 */
public class SimpleUsageExample {

    public static void main(String[] args) {
        try {
            // Initialize the client - just need the API token!
            CMSClient client = new CMSClient("your_api_token_here");

            // Example 1: Track a lead with minimal data
            System.out.println("=== Tracking Lead ===");
            TrackResponse leadResponse = client.trackLead()
                    .eventName("lead_signup")
                    .customerExternalId("user_123")
                    .execute();
            
            System.out.println("Lead tracked: " + leadResponse.getMessage());

            // Example 2: Track a lead with full information
            System.out.println("\n=== Tracking Lead with Full Data ===");
            TrackResponse detailedLeadResponse = client.trackLead()
                    .clickId("click_abc123")
                    .eventName("lead_created")
                    .customerExternalId("cust_456")
                    .customerName("John Doe")
                    .customerEmail("john@example.com")
                    .execute();
            
            System.out.println("Detailed lead tracked: " + detailedLeadResponse.getMessage());

            // Example 3: Track a sale
            System.out.println("\n=== Tracking Sale ===");
            TrackResponse saleResponse = client.trackSale()
                    .clickId("click_abc123")
                    .eventName("purchase_completed")
                    .customerExternalId("cust_456")
                    .invoiceId("INV_2026_001")
                    .amount(149.99)
                    .currency("USD")
                    .customerName("John Doe")
                    .customerEmail("john@example.com")
                    .execute();
            
            System.out.println("Sale tracked: " + saleResponse.getMessage());

        } catch (ApiException e) {
            System.err.println("API Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example showing async tracking (using underlying API directly if needed).
     */
    public static void asyncExample() {
        CMSClient client = new CMSClient("your_api_token_here");

        // For async operations, you can access the TrackingApi directly
        // This is for advanced use cases
        try {
            TrackResponse response = client.trackLead()
                    .eventName("lead_event")
                    .customerExternalId("user_789")
                    .execute();
            
            System.out.println("Async lead tracked: " + response.getMessage());
        } catch (ApiException e) {
            System.err.println("API Error: " + e.getMessage());
        }
    }
}
