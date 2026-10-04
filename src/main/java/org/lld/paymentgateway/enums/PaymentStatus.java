package org.lld.paymentgateway.enums;

public enum PaymentStatus {
    INITIATED,
    PENDING,
    SUCCESS,
    FAILED;

    public boolean isTerminal() {
        return this == SUCCESS || this == FAILED;
    }

    public void assertCanTransitionTo(PaymentStatus next) {
        if (this == next) {
            return;
        }
        if (isTerminal()) {
            throw new IllegalStateException("Cannot transition from terminal status " + this + " to " + next);
        }
        switch (this) {
            case INITIATED -> {
                if (next != PENDING && next != FAILED) {
                    throw new IllegalStateException("Invalid transition " + this + " -> " + next);
                }
            }
            case PENDING -> {
                if (next != SUCCESS && next != FAILED) {
                    throw new IllegalStateException("Invalid transition " + this + " -> " + next);
                }
            }
            default -> throw new IllegalStateException("Unknown status " + this);
        }
    }
}
