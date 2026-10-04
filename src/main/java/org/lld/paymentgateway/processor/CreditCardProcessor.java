package org.lld.paymentgateway.processor;

import org.lld.paymentgateway.entity.PaymentRequest;
import org.lld.paymentgateway.entity.PaymentResponse;
import org.lld.paymentgateway.enums.PaymentStatus;

public class CreditCardProcessor extends AbstractPaymentProcessor {
    @Override
    protected PaymentResponse doProcess(PaymentRequest request) {
        System.out.println("Processing credit card payment of amount " + request.getAmount() + " ");
        return new PaymentResponse(PaymentStatus.SUCCESS, "Credit Card payment successful.");
    }
}
