package org.lld.paymentgateway.observer;

import org.lld.paymentgateway.entity.Transaction;
import org.lld.paymentgateway.enums.PaymentStatus;

public class CustomerNotifier implements PaymentObserver{
    @Override
    public void onTransactionUpdate(Transaction transaction) {
        if (transaction.getPaymentStatus() == PaymentStatus.SUCCESS) {
            System.out.println("--- CUSTOMER EMAIL ---");
            System.out.println("Your payment of " + transaction.getPaymentRequest().getAmount() + " was successful. Transaction ID: " + transaction.getId());
            System.out.println("----------------------");
        }
    }
}
