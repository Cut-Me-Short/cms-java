package com.cutmeshort.client;

import com.cutmeshort.client.api.TrackingApi;
import com.cutmeshort.client.core.ApiClient;
import com.cutmeshort.client.core.Configuration;
import com.cutmeshort.client.exception.ApiException;
import com.cutmeshort.client.model.LeadPayload;
import com.cutmeshort.client.model.SalePayload;
import com.cutmeshort.client.model.TrackResponse;

/**
 * Simplified CMS Client for tracking leads and sales.
 * 
 * This is the main entry point for using the CutMeShort SDK.
 * Users only need to initialize this client with their API token and use the simple methods.
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * CMSClient client = new CMSClient("your_api_token");
 * 
 * // Track a lead
 * TrackResponse leadResponse = client.trackLead()
 *     .clickId("click123")
 *     .eventName("lead_created")
 *     .customerExternalId("cust456")
 *     .customerName("John Doe")
 *     .customerEmail("john@example.com")
 *     .execute();
 * 
 * // Track a sale
 * TrackResponse saleResponse = client.trackSale()
 *     .clickId("click123")
 *     .eventName("sale_completed")
 *     .customerExternalId("cust456")
 *     .invoiceId("INV123")
 *     .amount(99.99)
 *     .currency("USD")
 *     .execute();
 * </pre>
 */
public class CMSClient {
    private TrackingApi trackingApi;
    private ApiClient apiClient;

    /**
     * Initialize CMSClient with API token.
     * 
     * @param apiToken The API authentication token
     */
    public CMSClient(String apiToken) {
        this.apiClient = Configuration.getDefaultApiClient();
        this.apiClient.setApiKey(apiToken);
        this.trackingApi = new TrackingApi(apiClient);
    }

    /**
     * Initialize CMSClient with custom API client.
     * 
     * @param apiClient Configured ApiClient instance
     */
    public CMSClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.trackingApi = new TrackingApi(apiClient);
    }

    /**
     * Get the underlying TrackingApi for advanced usage.
     * 
     * @return TrackingApi instance
     */
    public TrackingApi getTrackingApi() {
        return trackingApi;
    }

    /**
     * Start building a lead tracking request.
     * 
     * @return LeadBuilder for fluent API
     */
    public LeadBuilder trackLead() {
        return new LeadBuilder(this);
    }

    /**
     * Start building a sale tracking request.
     * 
     * @return SaleBuilder for fluent API
     */
    public SaleBuilder trackSale() {
        return new SaleBuilder(this);
    }

    /**
     * Execute lead tracking with the provided payload.
     * 
     * @param leadPayload The lead payload to track
     * @return TrackResponse from the server
     * @throws ApiException If the API call fails
     */
    protected TrackResponse executeLead(LeadPayload leadPayload) throws ApiException {
        return trackingApi.trackLead(leadPayload);
    }

    /**
     * Execute sale tracking with the provided payload.
     * 
     * @param salePayload The sale payload to track
     * @return TrackResponse from the server
     * @throws ApiException If the API call fails
     */
    protected TrackResponse executeSale(SalePayload salePayload) throws ApiException {
        return trackingApi.trackSale(salePayload);
    }

    /**
     * Builder class for constructing and executing lead tracking requests.
     * Provides a fluent interface for setting lead properties.
     */
    public static class LeadBuilder {
        private CMSClient client;
        private LeadPayload payload;

        public LeadBuilder(CMSClient client) {
            this.client = client;
            this.payload = new LeadPayload();
        }

        /**
         * Set the click ID for tracking.
         */
        public LeadBuilder clickId(String clickId) {
            payload.setClickId(clickId);
            return this;
        }

        /**
         * Set the event name (required).
         */
        public LeadBuilder eventName(String eventName) {
            payload.setEventName(eventName);
            return this;
        }

        /**
         * Set the customer external ID (required).
         */
        public LeadBuilder customerExternalId(String customerExternalId) {
            payload.setCustomerExternalId(customerExternalId);
            return this;
        }

        /**
         * Set the customer name.
         */
        public LeadBuilder customerName(String customerName) {
            payload.setCustomerName(customerName);
            return this;
        }

        /**
         * Set the customer email address.
         */
        public LeadBuilder customerEmail(String customerEmail) {
            payload.setCustomerEmail(customerEmail);
            return this;
        }

        /**
         * Execute the lead tracking request.
         * 
         * @return TrackResponse from the server
         * @throws ApiException If validation or API call fails
         */
        public TrackResponse execute() throws ApiException {
            validatePayload();
            return client.executeLead(payload);
        }

        private void validatePayload() throws ApiException {
            if (payload.getEventName() == null || payload.getEventName().isEmpty()) {
                throw new ApiException("eventName is required");
            }
            if (payload.getCustomerExternalId() == null || payload.getCustomerExternalId().isEmpty()) {
                throw new ApiException("customerExternalId is required");
            }
        }
    }

    /**
     * Builder class for constructing and executing sale tracking requests.
     * Provides a fluent interface for setting sale properties.
     */
    public static class SaleBuilder {
        private CMSClient client;
        private SalePayload payload;

        public SaleBuilder(CMSClient client) {
            this.client = client;
            this.payload = new SalePayload();
        }

        /**
         * Set the click ID (required).
         */
        public SaleBuilder clickId(String clickId) {
            payload.setClickId(clickId);
            return this;
        }

        /**
         * Set the event name (required).
         */
        public SaleBuilder eventName(String eventName) {
            payload.setEventName(eventName);
            return this;
        }

        /**
         * Set the customer external ID (required).
         */
        public SaleBuilder customerExternalId(String customerExternalId) {
            payload.setCustomerExternalId(customerExternalId);
            return this;
        }

        /**
         * Set the customer name.
         */
        public SaleBuilder customerName(String customerName) {
            payload.setCustomerName(customerName);
            return this;
        }

        /**
         * Set the customer email address.
         */
        public SaleBuilder customerEmail(String customerEmail) {
            payload.setCustomerEmail(customerEmail);
            return this;
        }

        /**
         * Set the invoice ID (required).
         */
        public SaleBuilder invoiceId(String invoiceId) {
            payload.setInvoiceId(invoiceId);
            return this;
        }

        /**
         * Set the sale amount (required).
         */
        public SaleBuilder amount(Double amount) {
            payload.setAmount(amount);
            return this;
        }

        /**
         * Set the currency code (e.g., "USD", "EUR").
         */
        public SaleBuilder currency(String currency) {
            payload.setCurrency(currency);
            return this;
        }

        /**
         * Execute the sale tracking request.
         * 
         * @return TrackResponse from the server
         * @throws ApiException If validation or API call fails
         */
        public TrackResponse execute() throws ApiException {
            validatePayload();
            return client.executeSale(payload);
        }

        private void validatePayload() throws ApiException {
            if (payload.getEventName() == null || payload.getEventName().isEmpty()) {
                throw new ApiException("eventName is required");
            }
            if (payload.getCustomerExternalId() == null || payload.getCustomerExternalId().isEmpty()) {
                throw new ApiException("customerExternalId is required");
            }
            if (payload.getInvoiceId() == null || payload.getInvoiceId().isEmpty()) {
                throw new ApiException("invoiceId is required");
            }
            if (payload.getAmount() == null) {
                throw new ApiException("amount is required");
            }
        }
    }
}
