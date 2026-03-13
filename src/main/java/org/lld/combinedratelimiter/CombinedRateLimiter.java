package org.lld.combinedratelimiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class CombinedRateLimiter {

    private final Map<String, EndpointRule> endpointRules = new ConcurrentHashMap<>();
    private final Map<String, AggregateRule> aggregateRules = new ConcurrentHashMap<>();
    private final BucketFactory bucketFactory = new BucketFactory();

    // Per (clientId, endpoint) buckets for endpoint-level limits
    private final Map<String, RateLimitBucket> endpointBuckets = new ConcurrentHashMap<>();
    // Per (clientId, groupId) buckets for aggregate limits
    private final Map<String, RateLimitBucket> groupBuckets = new ConcurrentHashMap<>();

    // Striped locks: one lock per (clientId, endpoint) to minimize contention
    private final Map<String, Object> locks = new ConcurrentHashMap<>();


     // Register an endpoint rule. Replaces any existing rule for the endpoint.
    public void addEndpointRule(EndpointRule rule) {
        endpointRules.put(rule.getEndpoint(), rule);
    }


     // Register an aggregate rule. Replaces any existing rule for the group.

    public void addAggregateRule(AggregateRule rule) {
        aggregateRules.put(rule.getGroupId(), rule);
    }


    public boolean allowRequest(String clientId, String endpoint) {
        List<RateLimitBucket> buckets = collectBuckets(clientId, endpoint);
        // if endpoint is not registered in any bucket then return true
        if (buckets.isEmpty()) {
            return true;
        }

        String lockKey = clientId + "|" + endpoint;
        Object lock = locks.computeIfAbsent(lockKey, k -> new Object());

        synchronized (lock) {

            // 5 per min -> true  // 50 per hour -> false
            long now = System.currentTimeMillis();
            for (RateLimitBucket bucket : buckets) {
                if (!bucket.wouldAllow(now)) {
                    return false;
                }
            }
            for (RateLimitBucket bucket : buckets) {
                bucket.consume(now);
            }
            return true;
        }
    }

    private List<RateLimitBucket> collectBuckets(String clientId, String endpoint) {
        List<RateLimitBucket> result = new ArrayList<>();

        EndpointRule epRule = endpointRules.get(endpoint);
        if (epRule != null) {
            for (PeriodLimit p : epRule.getPeriods()) {
                String key = "ep:" + endpoint + ":" + p.getWindowMs() + ":" + clientId;
                result.add(endpointBuckets.computeIfAbsent(key,
                        k -> bucketFactory.create(p, epRule.getAlgorithmType())));
            }
        }

        for (AggregateRule aggRule : aggregateRules.values()) {
            if (aggRule.getEndpoints().contains(endpoint)) {
                for (PeriodLimit p : aggRule.getPeriods()) {
                    String key = "grp:" + aggRule.getGroupId() + ":" + p.getWindowMs() + ":" + clientId;
                    result.add(groupBuckets.computeIfAbsent(key,
                            k -> bucketFactory.create(p, aggRule.getAlgorithmType())));
                }
            }
        }

        return result;
    }
}
