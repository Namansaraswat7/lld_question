package org.lld.combinedratelimiter.algorithms;

import org.lld.combinedratelimiter.PeriodLimit;
import org.lld.combinedratelimiter.RateLimitBucket;

import java.util.ArrayDeque;
import java.util.Deque;


public final class SlidingWindowBucket implements RateLimitBucket {

    private final int maxRequests;
    private final long windowMs;
    private final Deque<Long> timestamps = new ArrayDeque<>();

    public SlidingWindowBucket(PeriodLimit period) {
        this.maxRequests = period.getMaxRequests();
        this.windowMs = period.getWindowMs();
    }

    @Override
    public synchronized boolean wouldAllow(long nowMillis) {
        evictExpired(nowMillis);
        return timestamps.size() < maxRequests;
    }

    @Override
    public synchronized void consume(long nowMillis) {
        evictExpired(nowMillis);
        if (timestamps.size() < maxRequests) {
            timestamps.addLast(nowMillis);
        }
    }

    private void evictExpired(long nowMillis) {
        long threshold = nowMillis - windowMs;
        while (!timestamps.isEmpty() && timestamps.peekFirst() < threshold) {
            timestamps.removeFirst();
        }
    }
}
