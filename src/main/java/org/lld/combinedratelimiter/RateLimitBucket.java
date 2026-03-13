package org.lld.combinedratelimiter;


public interface RateLimitBucket {

    boolean wouldAllow(long nowMillis);


    void consume(long nowMillis);
}
