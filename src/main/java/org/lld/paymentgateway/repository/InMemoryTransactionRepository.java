package org.lld.paymentgateway.repository;

import org.lld.paymentgateway.entity.Transaction;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<UUID, Transaction> idMap = new ConcurrentHashMap<>();
    private final Map<String, Transaction> idempotencyKeyMap = new ConcurrentHashMap<>();

    @Override
    public void save(Transaction transaction) {
        idMap.put(transaction.getId(), transaction);
        idempotencyKeyMap.put(transaction.getIdempotencyKey(), transaction);
    }

    @Override
    public Optional<Transaction> findById(UUID transactionId) {
        return Optional.ofNullable(idMap.get(transactionId));
    }

    @Override
    public Optional<Transaction> findByIdempotencyKey(String idempotencyKey) {
        return Optional.ofNullable(idempotencyKeyMap.get(idempotencyKey));
    }

    @Override
    public Transaction getOrCreate(String idempotencyKey, Transaction transaction) {
        Transaction existing = idempotencyKeyMap.get(idempotencyKey);
        if (existing != null) {
            return existing;
        }

        Transaction prior = idempotencyKeyMap.putIfAbsent(idempotencyKey, transaction);
        if (prior != null) {
            return prior;
        }

        idMap.put(transaction.getId(), transaction);
        return transaction;
    }
}
