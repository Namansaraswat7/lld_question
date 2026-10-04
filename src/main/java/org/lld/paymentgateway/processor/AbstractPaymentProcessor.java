package org.lld.paymentgateway.processor;

import org.lld.paymentgateway.entity.PaymentRequest;
import org.lld.paymentgateway.entity.PaymentResponse;
import org.lld.paymentgateway.enums.PaymentStatus;

import static java.lang.Thread.sleep;

abstract class AbstractPaymentProcessor implements PaymentProcessor {
    private static final int MAX_RETRIES = 3;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) throws InterruptedException {
        int attempts = 0;
        long backoffMs = 100;
        PaymentResponse response;
        do {
            response = doProcess(request); // same idempotency key each attempt
            if (response.getPaymentStatus() != PaymentStatus.FAILED) break;
            sleep(backoffMs);
            backoffMs *= 2; // 100ms, 200ms, 400ms ...
        } while (++attempts < MAX_RETRIES);
        return response;
    }

    protected abstract PaymentResponse doProcess(PaymentRequest request);
}
