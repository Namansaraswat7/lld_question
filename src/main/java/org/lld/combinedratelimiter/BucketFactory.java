package org.lld.combinedratelimiter;

import org.lld.combinedratelimiter.algorithms.FixedWindowBucket;
import org.lld.combinedratelimiter.algorithms.LeakyBucketBucket;
import org.lld.combinedratelimiter.algorithms.SlidingWindowBucket;
import org.lld.combinedratelimiter.algorithms.TokenBucketBucket;

public final class BucketFactory {

    public RateLimitBucket create(PeriodLimit period, AlgorithmType type) {
        return switch (type) {
            case TOKEN_BUCKET -> new TokenBucketBucket(period);
            case SLIDING_WINDOW -> new SlidingWindowBucket(period);
            case LEAKY_BUCKET -> new LeakyBucketBucket(period);
            case FIXED_WINDOW -> new FixedWindowBucket(period);
        };
    }
}
