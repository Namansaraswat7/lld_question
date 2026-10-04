package org.lld.paymentgateway;

import org.lld.paymentgateway.entity.PaymentRequest;
import org.lld.paymentgateway.entity.PaymentResponse;
import org.lld.paymentgateway.entity.Transaction;
import org.lld.paymentgateway.enums.PaymentStatus;
import org.lld.paymentgateway.factory.PaymentProcessorFactory;
import org.lld.paymentgateway.observer.PaymentObserver;
import org.lld.paymentgateway.processor.PaymentProcessor;
import org.lld.paymentgateway.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentGatewayService {

    private final TransactionRepository transactionRepository;
    private final List<PaymentObserver> observers = new ArrayList<>();
    private final ConcurrentHashMap<String, Object> idempotencyLocks = new ConcurrentHashMap<>();

    public PaymentGatewayService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addObserver(PaymentObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PaymentObserver observer) {
        observers.remove(observer);
    }

    public Optional<Transaction> getTransaction(UUID transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public Transaction processPayment(PaymentRequest request) {
        validateRequest(request);

        Transaction transaction = transactionRepository.getOrCreate(
                request.getIdempotencyKey(),
                new Transaction(request));
        assertMatchingIdempotentRequest(request, transaction);

        Object lock = idempotencyLocks.computeIfAbsent(request.getIdempotencyKey(), k -> new Object());
        synchronized (lock) {
            Transaction current = transactionRepository.findByIdempotencyKey(request.getIdempotencyKey())
                    .orElse(transaction);

            if (current.getPaymentStatus().isTerminal()) {
                return current;
            }
            if (current.getPaymentStatus() == PaymentStatus.PENDING) {
                return current;
            }

            updateStatus(current, PaymentStatus.PENDING, "Payment submitted to processor");

            try {
                PaymentProcessor processor = PaymentProcessorFactory.getProcessor(request.getPaymentMode());
                PaymentResponse response = processor.processPayment(request);
                PaymentStatus outcome = response.getPaymentStatus().isTerminal()
                        ? response.getPaymentStatus()
                        : PaymentStatus.SUCCESS;
                updateStatus(current, outcome, response.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                updateStatus(current, PaymentStatus.FAILED, "Payment interrupted");
            } catch (Exception e) {
                updateStatus(current, PaymentStatus.FAILED, e.getMessage());
            }
            return current;
        }
    }

    private void assertMatchingIdempotentRequest(PaymentRequest incoming, Transaction existing) {
        PaymentRequest stored = existing.getPaymentRequest();
        if (Double.compare(stored.getAmount(), incoming.getAmount()) != 0
                || stored.getPaymentMode() != incoming.getPaymentMode()
                || !stored.getPayorId().equals(incoming.getPayorId())) {
            throw new IllegalArgumentException(
                    "Idempotency key already used with different payment parameters");
        }
    }

    private void validateRequest(PaymentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Payment request is required");
        }
        if (request.getIdempotencyKey() == null || request.getIdempotencyKey().isBlank()) {
            throw new IllegalArgumentException("Idempotency key is required");
        }
        if (request.getPayorId() == null || request.getPayorId().isBlank()) {
            throw new IllegalArgumentException("Payor id is required");
        }
        if (request.getPaymentMode() == null) {
            throw new IllegalArgumentException("Payment mode is required");
        }
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private void notifyObservers(Transaction transaction) {
        observers.forEach(o -> o.onTransactionUpdate(transaction));
    }

    private void updateStatus(Transaction transaction, PaymentStatus status, String message) {
        transaction.transitionTo(status);
        if (message != null) {
            transaction.setStatusMessage(message);
        }
        transactionRepository.save(transaction);
        notifyObservers(transaction);
    }
}
