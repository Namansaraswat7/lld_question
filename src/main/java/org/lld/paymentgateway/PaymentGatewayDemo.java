package org.lld.paymentgateway;

import org.lld.paymentgateway.entity.PaymentRequest;
import org.lld.paymentgateway.entity.Transaction;
import org.lld.paymentgateway.enums.PaymentMode;
import org.lld.paymentgateway.observer.CustomerNotifier;
import org.lld.paymentgateway.repository.InMemoryTransactionRepository;

import java.util.Map;

public class PaymentGatewayDemo {
    public static void main(String[] args) {
        // 1. Setup the gateway
        PaymentGatewayService paymentGateway = new PaymentGatewayService(new InMemoryTransactionRepository());

        // 2. Register observers to be notified of transaction events
        //paymentGateway.addObserver(new MerchantNotifier());
        paymentGateway.addObserver(new CustomerNotifier());

        System.out.println("----------- SCENARIO 1: Successful Credit Card Payment -----------");
        // a. Merchant's backend creates a payment request
        PaymentRequest ccRequest = new PaymentRequest(
                "idem-cc-001", "U-123", 150.75,
                PaymentMode.CREDIT_CARD, Map.of("cardNumber", "1234..."));

        // b. Merchant's backend sends it to the gateway
        Transaction ccTxn = paymentGateway.processPayment(ccRequest);
        System.out.println("Final status: " + ccTxn.getPaymentStatus() + " — " + ccTxn.getStatusMessage());

        System.out.println("\n----------- SCENARIO 2: Successful UPI Payment -----------");
        PaymentRequest upiRequest = new PaymentRequest(
                "idem-upi-001", "U-456", 88.50,
                PaymentMode.UPI, Map.of("vpa", "customer@upi"));

        Transaction upiTxn = paymentGateway.processPayment(upiRequest);
        System.out.println("Final status: " + upiTxn.getPaymentStatus() + " — " + upiTxn.getStatusMessage());

        System.out.println("\n----------- SCENARIO 3: Duplicate idempotency key (no double charge) -----------");
        Transaction duplicate = paymentGateway.processPayment(ccRequest);
        System.out.println("Same transaction id: " + duplicate.getId().equals(ccTxn.getId()));
        System.out.println("Final status: " + duplicate.getPaymentStatus());
    }
}
