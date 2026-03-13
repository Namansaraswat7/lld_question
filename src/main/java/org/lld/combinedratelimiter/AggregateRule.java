package org.lld.combinedratelimiter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Rate limit rule that applies to a group of endpoints combined.
 * E.g., /upload + /download together must not exceed 50/hour.
 */
public final class AggregateRule {

    private final String groupId;
    private final Set<String> endpoints;
    private final List<PeriodLimit> periods;
    private final AlgorithmType algorithmType;

    public AggregateRule(String groupId, Set<String> endpoints,
                         List<PeriodLimit> periods, AlgorithmType algorithmType) {
        this.groupId = Objects.requireNonNull(groupId);
        this.endpoints = Collections.unmodifiableSet(
                Objects.requireNonNull(endpoints));
        this.periods = Collections.unmodifiableList(
                Objects.requireNonNull(periods));
        this.algorithmType = Objects.requireNonNull(algorithmType);
        if (this.endpoints.isEmpty() || this.periods.isEmpty()) {
            throw new IllegalArgumentException("endpoints and periods must be non-empty");
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public Set<String> getEndpoints() {
        return endpoints;
    }

    public List<PeriodLimit> getPeriods() {
        return periods;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }
}
