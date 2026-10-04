package org.lld.paymentgateway.observer;

import org.lld.paymentgateway.entity.Transaction;

public interface PaymentObserver {
    void onTransactionUpdate(Transaction transaction);
}
