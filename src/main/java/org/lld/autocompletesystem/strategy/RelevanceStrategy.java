package org.lld.autocompletesystem.strategy;

import org.lld.autocompletesystem.WordEntry;

import java.util.Comparator;
import java.util.List;

public class RelevanceStrategy implements RankingStrategy{
    @Override
    public List<WordEntry> rank(List<WordEntry> entries, String prefix) {
        return entries.stream()
                .sorted(Comparator
                        .comparingDouble(this::calculateRelevanceScore).reversed() // Primary: relevance score
                        .thenComparing(entry -> calculatePrefixMatch(entry.getWord(), prefix)) // Secondary: prefix match
                        .thenComparing(WordEntry::getWord)) // Tertiary: alphabetical
                .toList();
    }

    /**
     * Calculate overall relevance score combining entry type and prefix match
     */
    private double calculateRelevanceScore(WordEntry entry) {
        double typeScore = entry.getEntryType() != null ?
                entry.getEntryType().getRelevanceScore() : 0.5;

        // Add small bonus for usage (but not as dominant as in FrequencyStrategy)
        double usageBonus = Math.min(0.1, entry.getUsageCount() * 0.01);

        return typeScore + usageBonus;
    }

    /**
     * Calculate how well the word matches the prefix
     */
    private double calculatePrefixMatch(String word, String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return 0.0;
        }

        String lowerWord = word.toLowerCase();
        String lowerPrefix = prefix.toLowerCase();

        if (lowerWord.equals(lowerPrefix)) {
            return 1.0; // Exact match
        } else if (lowerWord.startsWith(lowerPrefix)) {
            return (double) lowerPrefix.length() / lowerWord.length();
        }

        return 0.0;
    }
}
