package org.lld.combinedratelimiter;

import java.util.Objects;


public final class PeriodLimit {

    private final int maxRequests;
    private final long windowMs;

    public PeriodLimit(int maxRequests, long windowMs) {
        if (maxRequests <= 0 || windowMs <= 0) {
            throw new IllegalArgumentException("maxRequests and windowMs must be positive");
        }
        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
    }

    public static PeriodLimit perMinute(int maxRequests) {
        return new PeriodLimit(maxRequests, 60_000L);
    }

    public static PeriodLimit perHour(int maxRequests) {
        return new PeriodLimit(maxRequests, 3_600_000L);
    }

    public static PeriodLimit perDay(int maxRequests) {
        return new PeriodLimit(maxRequests, 86_400_000L);
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public long getWindowMs() {
        return windowMs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodLimit that = (PeriodLimit) o;
        return maxRequests == that.maxRequests && windowMs == that.windowMs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxRequests, windowMs);
    }
}
