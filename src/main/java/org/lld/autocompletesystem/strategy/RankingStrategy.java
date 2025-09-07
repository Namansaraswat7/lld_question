package org.lld.autocompletesystem.strategy;

import org.lld.autocompletesystem.WordEntry;

import java.util.List;

public interface RankingStrategy {
    /**
     * Rank the given word entries based on the strategy's algorithm
     * @param entries List of word entries to rank
     * @param prefix The search prefix
     * @return Ranked list of entries (highest relevance first)
     */
    List<WordEntry> rank(List<WordEntry> entries, String prefix);
}
