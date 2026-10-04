package org.lld.paymentgateway.entity;

import org.lld.paymentgateway.enums.PaymentMode;

import java.util.Map;

public class PaymentRequest {
    private String idempotencyKey;
    private String payorId;
    private double amount;
    private PaymentMode paymentMode;
    private Map<String , String> paymentDetails;

    public PaymentRequest(String idempotencyKey, String payorId, double amount, PaymentMode paymentMode, Map<String, String> paymentDetails) {
        this.idempotencyKey = idempotencyKey;
        this.payorId = payorId;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.paymentDetails = paymentDetails;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getPayorId() {
        return payorId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public Map<String, String> getPaymentDetails() {
        return paymentDetails;
    }
}
