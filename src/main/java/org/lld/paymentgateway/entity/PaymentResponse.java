package org.lld.paymentgateway.entity;

import org.lld.paymentgateway.enums.PaymentStatus;

public class PaymentResponse {
    private PaymentStatus paymentStatus;
    private String message;

    public PaymentResponse(PaymentStatus paymentStatus, String message) {
        this.paymentStatus = paymentStatus;
        this.message = message;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getMessage() {
        return message;
    }
}
