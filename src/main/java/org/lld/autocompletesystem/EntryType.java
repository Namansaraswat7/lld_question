package org.lld.autocompletesystem;

public enum EntryType {
    KEYWORD(1.0),
    METHOD(0.9),
    VARIABLE(0.8),
    CLASS(0.85),
    PACKAGE(0.7);

    private final double relevanceScore;

    EntryType(double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public double getRelevanceScore() {
        return relevanceScore;
    }

}
