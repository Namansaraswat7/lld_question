package org.lld.combinedratelimiter.algorithms;

import org.lld.combinedratelimiter.PeriodLimit;
import org.lld.combinedratelimiter.RateLimitBucket;


public final class LeakyBucketBucket implements RateLimitBucket {

    private final int maxRequests;
    private final double drainRatePerMillis;
    private double currentLevel;
    private long lastUpdateMillis;

    public LeakyBucketBucket(PeriodLimit period) {
        this.maxRequests = period.getMaxRequests();
        this.drainRatePerMillis = (double) period.getMaxRequests() / period.getWindowMs();
        this.currentLevel = 0.0;
        this.lastUpdateMillis = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean wouldAllow(long nowMillis) {
        leak(nowMillis);
        return currentLevel < maxRequests;
    }

    @Override
    public synchronized void consume(long nowMillis) {
        leak(nowMillis);
        if (currentLevel < maxRequests) {
            currentLevel += 1.0;
        }
    }

    private void leak(long nowMillis) {
        if (nowMillis <= lastUpdateMillis) return;
        long delta = nowMillis - lastUpdateMillis;
        double leaked = delta * drainRatePerMillis;
        currentLevel = Math.max(0.0, currentLevel - leaked);
        lastUpdateMillis = nowMillis;
    }
}
