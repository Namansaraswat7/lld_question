package org.lld.paymentgateway.factory;

import org.lld.paymentgateway.enums.PaymentMode;
import org.lld.paymentgateway.processor.CreditCardProcessor;
import org.lld.paymentgateway.processor.PaymentProcessor;
import org.lld.paymentgateway.processor.UPIProcessor;

public class PaymentProcessorFactory {
    public static PaymentProcessor getProcessor(PaymentMode method) {
        return switch (method) {
            case CREDIT_CARD -> new CreditCardProcessor();
            case UPI -> new UPIProcessor();
           // case PAYPAL -> new PayPalProcessor();
            // case BANK_TRANSFER -> new BankTransferProcessor();
            default -> throw new IllegalArgumentException("Unsupported payment method: " + method);
        };
    }
}
