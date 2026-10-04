package org.lld.paymentgateway.entity;

import org.lld.paymentgateway.enums.PaymentMode;
import org.lld.paymentgateway.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private PaymentMode paymentMode;
    private double amount; // Money
    private PaymentRequest paymentRequest;
    private PaymentStatus paymentStatus;
    private String statusMessage;
    private LocalDateTime timestamp;

    public Transaction(PaymentRequest paymentRequest) {
        this.id = UUID.randomUUID();
        this.paymentRequest = paymentRequest;
        this.amount = paymentRequest.getAmount();
        this.paymentMode = paymentRequest.getPaymentMode();
        this.paymentStatus = PaymentStatus.INITIATED;
        this.timestamp = LocalDateTime.now();
    }

    public String getIdempotencyKey() {
        return paymentRequest.getIdempotencyKey();
    }

    public void transitionTo(PaymentStatus nextStatus) {
        paymentStatus.assertCanTransitionTo(nextStatus);
        this.paymentStatus = nextStatus;
    }

    public UUID getId() {
        return id;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
