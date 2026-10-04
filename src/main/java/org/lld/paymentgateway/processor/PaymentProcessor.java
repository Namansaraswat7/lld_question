package org.lld.paymentgateway.processor;

import org.lld.paymentgateway.entity.PaymentRequest;
import org.lld.paymentgateway.entity.PaymentResponse;

public interface PaymentProcessor {
    PaymentResponse processPayment(PaymentRequest request) throws InterruptedException;
}
