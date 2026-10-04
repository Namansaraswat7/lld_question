package org.lld.paymentgateway.repository;

import org.lld.paymentgateway.entity.Transaction;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

    void save(Transaction transaction);

    Optional<Transaction> findById(UUID transactionId);

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

    /**
     * Returns an existing transaction for the key, or creates, persists, and returns a new one.
     */
    Transaction getOrCreate(String idempotencyKey, Transaction transaction);
}
