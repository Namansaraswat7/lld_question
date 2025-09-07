package org.lld.autocompletesystem.strategy;

import org.lld.autocompletesystem.WordEntry;

import java.util.Comparator;
import java.util.List;

public class FrequencyStrategy implements RankingStrategy{
    @Override
    public List<WordEntry> rank(List<WordEntry> entries, String prefix) {
        return entries.stream()
                .sorted(Comparator
                        .comparingInt(WordEntry::getUsageCount).reversed() // Primary: usage count
                        //.thenComparing(entry -> calculatePrefixMatch(entry.getWord(), prefix)) // Secondary: prefix match
                        .thenComparing(WordEntry::getWord)) // Tertiary: alphabetical
                .toList();
    }

    /**
     * Calculate how well the word matches the prefix
     * Returns higher score for better matches
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
            // Score based on how much of the word the prefix covers
            return (double) lowerPrefix.length() / lowerWord.length();
        }

        return 0.0;
    }
}
