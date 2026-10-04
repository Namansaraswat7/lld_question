package org.lld.paymentgateway.processor;

import org.lld.paymentgateway.entity.PaymentRequest;
import org.lld.paymentgateway.entity.PaymentResponse;
import org.lld.paymentgateway.enums.PaymentStatus;

public class UPIProcessor extends AbstractPaymentProcessor {
    @Override
    protected PaymentResponse doProcess(PaymentRequest request) {
        System.out.println("Processing UPI payment of " + request.getAmount() + " ");
        return new PaymentResponse(PaymentStatus.SUCCESS, "UPI payment successful.");
    }
}
