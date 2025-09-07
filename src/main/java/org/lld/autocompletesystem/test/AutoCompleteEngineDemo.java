package org.lld.autocompletesystem.test;

import org.lld.autocompletesystem.AutoCompleteEngine;
import org.lld.autocompletesystem.EntryType;
import org.lld.autocompletesystem.strategy.FrequencyStrategy;
import org.lld.autocompletesystem.strategy.RelevanceStrategy;

import java.util.List;

public class AutoCompleteEngineDemo {
    public static void main(String[] args) {
        System.out.println("=== Simplified Autocomplete Engine Demo ===\n");

        // Demo 1: Basic functionality with frequency-based ranking
        demonstrateFrequencyRanking();

        // Demo 2: Relevance-based ranking
        demonstrateRelevanceRanking();

        // Demo 3: Context learning and language switching
        demonstrateContextLearning();

        // Demo 4: Usage tracking
        demonstrateUsageTracking();

        System.out.println("=== Demo Complete ===");
    }

    private static void demonstrateFrequencyRanking() {
        System.out.println("1. Frequency-Based Ranking");
        System.out.println("-".repeat(40));

        AutoCompleteEngine engine = new AutoCompleteEngine(new FrequencyStrategy());
        engine.initializeLanguage("java");

        // Add some custom words
        engine.addWord("customMethod", EntryType.METHOD, "java");
        engine.addWord("calculateTotal", EntryType.METHOD, "java");

        System.out.println("Suggestions for 'p' (before usage):");
        List<String> suggestions = engine.suggest("p", 5);
        suggestions.forEach(s -> System.out.println("  " + s));

        // Simulate usage
        engine.recordUsage("private");
        engine.recordUsage("private");
        engine.recordUsage("println");

        System.out.println("\nSuggestions for 'p' (after usage - frequency matters):");
        suggestions = engine.suggest("p", 5);
        suggestions.forEach(s -> System.out.println("  " + s));

        System.out.println();
    }

    private static void demonstrateRelevanceRanking() {
        System.out.println("2. Relevance-Based Ranking");
        System.out.println("-".repeat(40));

        AutoCompleteEngine engine = new AutoCompleteEngine(new RelevanceStrategy());
        engine.initializeLanguage("java");

        System.out.println("Suggestions for 'c' (relevance-based - keywords first):");
        List<String> suggestions = engine.suggest("c", 5);
        suggestions.forEach(s -> System.out.println("  " + s));

        System.out.println();
    }

    private static void demonstrateContextLearning() {
        System.out.println("3. Context Learning & Language Switching");
        System.out.println("-".repeat(40));

        AutoCompleteEngine engine = new AutoCompleteEngine();

        // Initialize multiple languages
        engine.initializeLanguage("java");
        engine.initializeLanguage("python");
        engine.initializeLanguage("javascript");

        // Test Java context
        engine.setLanguageContext("java");
        System.out.println("Java context - suggestions for 'cl':");
        List<String> javaSuggestions = engine.suggest("cl", 3);
        javaSuggestions.forEach(s -> System.out.println("  " + s));

        // Test Python context
        engine.setLanguageContext("python");
        System.out.println("\nPython context - suggestions for 'cl':");
        List<String> pythonSuggestions = engine.suggest("cl", 3);
        pythonSuggestions.forEach(s -> System.out.println("  " + s));

        // Test JavaScript context
        engine.setLanguageContext("javascript");
        System.out.println("\nJavaScript context - suggestions for 'cl':");
        List<String> jsSuggestions = engine.suggest("cl", 3);
        jsSuggestions.forEach(s -> System.out.println("  " + s));

        // Show context learning
        System.out.println("\nContext History (prefix -> language):");
        engine.getContextHistory().forEach((prefix, language) ->
                System.out.println("  '" + prefix + "' -> " + language));

        System.out.println();
    }

    private static void demonstrateUsageTracking() {
        System.out.println("4. Usage Tracking");
        System.out.println("-".repeat(40));

        AutoCompleteEngine engine = new AutoCompleteEngine(new FrequencyStrategy());
        engine.initializeLanguage("java");

        // Add custom words
        engine.addWord("processOrder", EntryType.METHOD, "java");
        engine.addWord("processPayment", EntryType.METHOD, "java");

        System.out.println("Initial suggestions for 'proc':");
        List<String> suggestions = engine.suggest("proc", 3);
        suggestions.forEach(s -> System.out.println("  " + s));

        // Record usage multiple times
        System.out.println("\nRecording usage of 'processPayment' 3 times...");
        engine.recordUsage("processPayment");
        engine.recordUsage("processPayment");
        engine.recordUsage("processPayment");

        System.out.println("Suggestions for 'proc' (after usage):");
        suggestions = engine.suggest("proc", 3);
        suggestions.forEach(s -> System.out.println("  " + s));

        System.out.println();
    }
}
