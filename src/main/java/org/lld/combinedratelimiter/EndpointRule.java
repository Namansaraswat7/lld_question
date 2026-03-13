package org.lld.combinedratelimiter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class EndpointRule {

    private final String endpoint;
    private final List<PeriodLimit> periods;
    private final AlgorithmType algorithmType;

    public EndpointRule(String endpoint, List<PeriodLimit> periods, AlgorithmType algorithmType) {
        this.endpoint = Objects.requireNonNull(endpoint, "endpoint");
        this.periods = Collections.unmodifiableList(
                Objects.requireNonNull(periods, "periods"));
        this.algorithmType = Objects.requireNonNull(algorithmType, "algorithmType");
        if (this.periods.isEmpty()) {
            throw new IllegalArgumentException("At least one period required");
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public List<PeriodLimit> getPeriods() {
        return periods;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }
}
