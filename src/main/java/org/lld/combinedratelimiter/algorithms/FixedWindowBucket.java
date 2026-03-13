package org.lld.combinedratelimiter.algorithms;

import org.lld.combinedratelimiter.PeriodLimit;
import org.lld.combinedratelimiter.RateLimitBucket;


public final class FixedWindowBucket implements RateLimitBucket {

    private final int maxRequests;
    private final long windowMs;
    private int count = 0;
    private long windowStartMillis;

    public FixedWindowBucket(PeriodLimit period) {
        this.maxRequests = period.getMaxRequests();
        this.windowMs = period.getWindowMs();
        this.windowStartMillis = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean wouldAllow(long nowMillis) {
        windowIfNeeded(nowMillis);
        return count < maxRequests;
    }

    @Override
    public synchronized void consume(long nowMillis) {
        windowIfNeeded(nowMillis);
        if (count < maxRequests) {
            count++;
        }
    }

    private void windowIfNeeded(long nowMillis) {
        long elapsed = nowMillis - windowStartMillis;
        if (elapsed >= windowMs) {
            windowStartMillis = nowMillis;
            count = 0;
        }
    }
}
