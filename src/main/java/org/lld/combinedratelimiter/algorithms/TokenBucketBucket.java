package org.lld.combinedratelimiter.algorithms;

import org.lld.combinedratelimiter.PeriodLimit;
import org.lld.combinedratelimiter.RateLimitBucket;


public final class TokenBucketBucket implements RateLimitBucket {

    private final int maxRequests;
    private final double refillRatePerMillis;
    private double tokens;
    private long lastRefillMillis;

    public TokenBucketBucket(PeriodLimit period) {
        this.maxRequests = period.getMaxRequests();
        this.refillRatePerMillis = (double) period.getMaxRequests() / period.getWindowMs();
        this.tokens = maxRequests;
        this.lastRefillMillis = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean wouldAllow(long nowMillis) {
        refill(nowMillis);
        return tokens >= 1.0;
    }

    @Override
    public synchronized void consume(long nowMillis) {
        refill(nowMillis);
        if (tokens >= 1.0) {
            tokens -= 1.0;
        }
    }

    private void refill(long nowMillis) {
        if (nowMillis <= lastRefillMillis) return;
        long delta = nowMillis - lastRefillMillis;
        double added = delta * refillRatePerMillis;
        tokens = Math.min(maxRequests, tokens + added);
        lastRefillMillis = nowMillis;
    }
}
