package org.lld.autocompletesystem.test;

import org.lld.autocompletesystem.AutoCompleteEngine;
import org.lld.autocompletesystem.EntryType;
import org.lld.autocompletesystem.strategy.FrequencyStrategy;
import org.lld.autocompletesystem.strategy.RankingStrategy;
import org.lld.autocompletesystem.strategy.RelevanceStrategy;

import java.util.List;
import java.util.Scanner;

public class InteractiveDemo {
    private static final String SEPARATOR = "=".repeat(60);
    private static final String DIVIDER = "-".repeat(60);

    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("🚀 INTERACTIVE AUTOCOMPLETE ENGINE DEMO");
        System.out.println(SEPARATOR);

        Scanner scanner = new Scanner(System.in);
        AutoCompleteEngine engine = setupEngine(scanner);

        System.out.println("\n✅ Engine initialized! You can now:");
        System.out.println("   • Add custom words");
        System.out.println("   • Test suggestions");
        System.out.println("   • Switch ranking strategies");
        System.out.println("   • Change language contexts");
        System.out.println("   • View system statistics");

        runInteractiveSession(scanner, engine);

        System.out.println("\n" + SEPARATOR);
        System.out.println("👋 Thank you for testing the Autocomplete Engine!");
        System.out.println(SEPARATOR);

        scanner.close();
    }

    private static AutoCompleteEngine setupEngine(Scanner scanner) {
        System.out.println("\n📋 INITIAL SETUP");
        System.out.println(DIVIDER);

        // Choose ranking strategy
        System.out.println("Choose ranking strategy:");
        System.out.println("1. Frequency-based (ranks by usage count)");
        System.out.println("2. Relevance-based (ranks by entry type)");
        System.out.print("Enter choice (1-2): ");

        int strategyChoice = getIntInput(scanner, 1, 2);
        RankingStrategy strategy = strategyChoice == 1 ?
                new FrequencyStrategy() : new RelevanceStrategy();

        AutoCompleteEngine engine = new AutoCompleteEngine(strategy);
        System.out.println("✅ Created engine with " +
                (strategyChoice == 1 ? "Frequency" : "Relevance") + " strategy");

        // Choose initial language
        System.out.println("\nChoose initial language to load:");
        System.out.println("1. Java");
        System.out.println("2. Python");
        System.out.println("3. JavaScript");
        System.out.println("4. Skip (start empty)");
        System.out.print("Enter choice (1-4): ");

        int langChoice = getIntInput(scanner, 1, 4);
        if (langChoice != 4) {
            String[] languages = {"java", "python", "javascript"};
            String selectedLang = languages[langChoice - 1];
            engine.initializeLanguage(selectedLang);
            System.out.println("✅ Loaded " + selectedLang + " keywords and methods");
        } else {
            System.out.println("✅ Starting with empty engine");
        }

        return engine;
    }

    private static void runInteractiveSession(Scanner scanner, AutoCompleteEngine engine) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("🎮 INTERACTIVE SESSION STARTED");
        System.out.println(SEPARATOR);

        printHelp();

        while (true) {
            System.out.print("\nautocomplete> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+", 2);
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "suggest", "s" -> handleSuggest(scanner, engine, parts);
                    case "add", "a" -> handleAdd(scanner, engine, parts);
                    case "use", "u" -> handleUse(scanner, engine, parts);
                    case "context", "c" -> handleContext(scanner, engine, parts);
                    case "strategy", "st" -> handleStrategy(scanner, engine);
                    case "load", "l" -> handleLoad(scanner, engine, parts);
                    case "stats" -> handleStats(engine);
                    case "clear" -> handleClear(engine);
                    case "demo" -> runQuickDemo(engine);
                    case "help", "h" -> printHelp();
                    case "quit", "q", "exit" -> {
                        System.out.println("👋 Goodbye!");
                        return;
                    }
                    default -> System.out.println("❌ Unknown command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    private static void handleSuggest(Scanner scanner, AutoCompleteEngine engine, String[] parts) {
        String prefix;
        if (parts.length > 1) {
            prefix = parts[1];
        } else {
            System.out.print("Enter prefix to search: ");
            prefix = scanner.nextLine().trim();
        }

        if (prefix.isEmpty()) {
            System.out.println("❌ Prefix cannot be empty");
            return;
        }

        System.out.print("Max results (default 10): ");
        String maxStr = scanner.nextLine().trim();
        int maxResults = maxStr.isEmpty() ? 10 : Integer.parseInt(maxStr);

        long startTime = System.nanoTime();
        List<String> suggestions = engine.suggest(prefix, maxResults);
        long endTime = System.nanoTime();

        System.out.println("\n🔍 Suggestions for '" + prefix + "':");
        if (suggestions.isEmpty()) {
            System.out.println("   No suggestions found");
        } else {
            for (int i = 0; i < suggestions.size(); i++) {
                System.out.printf("   %d. %s%n", i + 1, suggestions.get(i));
            }
        }
        System.out.printf("⏱️  Response time: %.2f ms%n", (endTime - startTime) / 1_000_000.0);

        // Ask if user wants to select one
        if (!suggestions.isEmpty()) {
            System.out.print("Select a suggestion to record usage (1-" + suggestions.size() + ", or Enter to skip): ");
            String selection = scanner.nextLine().trim();
            if (!selection.isEmpty()) {
                try {
                    int index = Integer.parseInt(selection) - 1;
                    if (index >= 0 && index < suggestions.size()) {
                        String selectedWord = suggestions.get(index);
                        engine.recordUsage(selectedWord);
                        System.out.println("✅ Recorded usage of '" + selectedWord + "'");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid selection");
                }
            }
        }
    }

    private static void handleAdd(Scanner scanner, AutoCompleteEngine engine, String[] parts) {
        String word;
        if (parts.length > 1) {
            word = parts[1];
        } else {
            System.out.print("Enter word to add: ");
            word = scanner.nextLine().trim();
        }

        if (word.isEmpty()) {
            System.out.println("❌ Word cannot be empty");
            return;
        }

        System.out.println("Choose entry type:");
        System.out.println("1. KEYWORD");
        System.out.println("2. METHOD");
        System.out.println("3. VARIABLE");
        System.out.println("4. CLASS");
        System.out.print("Enter choice (1-4): ");

        int typeChoice = getIntInput(scanner, 1, 4);
        EntryType[] types = {EntryType.KEYWORD, EntryType.METHOD, EntryType.VARIABLE, EntryType.CLASS};
        EntryType entryType = types[typeChoice - 1];

        System.out.print("Enter language (or Enter for current context): ");
        String language = scanner.nextLine().trim();
        if (language.isEmpty()) {
            language = engine.getLanguageContext();
            if (language == null) {
                language = "generic";
            }
        }

        engine.addWord(word, entryType, language);
        System.out.println("✅ Added '" + word + "' as " + entryType + " in " + language);
    }

    private static void handleUse(Scanner scanner, AutoCompleteEngine engine, String[] parts) {
        String word;
        if (parts.length > 1) {
            word = parts[1];
        } else {
            System.out.print("Enter word to record usage: ");
            word = scanner.nextLine().trim();
        }

        if (word.isEmpty()) {
            System.out.println("❌ Word cannot be empty");
            return;
        }

        engine.recordUsage(word);
        System.out.println("✅ Recorded usage of '" + word + "'");
    }

    private static void handleContext(Scanner scanner, AutoCompleteEngine engine, String[] parts) {
        if (parts.length > 1) {
            String language = parts[1];
            engine.setLanguageContext(language);
            System.out.println("✅ Set language context to: " + language);
        } else {
            System.out.println("Current language context: " +
                    (engine.getLanguageContext() != null ? engine.getLanguageContext() : "None"));
            System.out.print("Enter new language context (or Enter to clear): ");
            String language = scanner.nextLine().trim();

            if (language.isEmpty()) {
                engine.setLanguageContext(null);
                System.out.println("✅ Cleared language context");
            } else {
                engine.setLanguageContext(language);
                System.out.println("✅ Set language context to: " + language);
            }
        }
    }

    private static void handleStrategy(Scanner scanner, AutoCompleteEngine engine) {
        System.out.println("⚠️  Strategy switching requires creating a new engine.");
        System.out.println("This will lose current data. Continue? (y/N): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            System.out.println("Choose new strategy:");
            System.out.println("1. Frequency-based");
            System.out.println("2. Relevance-based");
            System.out.print("Enter choice (1-2): ");

            int choice = getIntInput(scanner, 1, 2);
            // Note: In a real implementation, you'd need to recreate the engine
            // For this demo, we'll just show the message
            System.out.println("✅ Would switch to " +
                    (choice == 1 ? "Frequency" : "Relevance") + " strategy");
            System.out.println("💡 Tip: Restart the program to actually switch strategies");
        }
    }

    private static void handleLoad(Scanner scanner, AutoCompleteEngine engine, String[] parts) {
        String language;
        if (parts.length > 1) {
            language = parts[1];
        } else {
            System.out.println("Available languages: java, python, javascript");
            System.out.print("Enter language to load: ");
            language = scanner.nextLine().trim().toLowerCase();
        }

        if (language.equals("java") || language.equals("python") || language.equals("javascript")) {
            engine.initializeLanguage(language);
            System.out.println("✅ Loaded " + language + " keywords and methods");
        } else {
            System.out.println("❌ Unsupported language. Available: java, python, javascript");
        }
    }

    private static void handleStats(AutoCompleteEngine engine) {
        System.out.println("\n📊 ENGINE STATISTICS");
        System.out.println(DIVIDER);
        System.out.println("Current language context: " +
                (engine.getLanguageContext() != null ? engine.getLanguageContext() : "None"));

        var history = engine.getContextHistory();
        System.out.println("Search history entries: " + history.size());

        if (!history.isEmpty()) {
            System.out.println("\nRecent searches:");
            history.entrySet().stream()
                    .limit(5)
                    .forEach(entry -> System.out.println("  '" + entry.getKey() + "' -> " + entry.getValue()));
        }
    }

    private static void handleClear(AutoCompleteEngine engine) {
        System.out.println("⚠️  This will clear search history. Continue? (y/N): ");
        // Note: In a real implementation, you'd add a clear method to the engine
        System.out.println("💡 Tip: Restart the program to clear all data");
    }

    private static void runQuickDemo(AutoCompleteEngine engine) {
        System.out.println("\n🎬 QUICK DEMO");
        System.out.println(DIVIDER);

        // Add some demo words
        engine.addWord("customMethod", EntryType.METHOD, "demo");
        engine.addWord("customVariable", EntryType.VARIABLE, "demo");
        engine.addWord("CustomClass", EntryType.CLASS, "demo");

        System.out.println("Added demo words: customMethod, customVariable, CustomClass");

        // Test suggestions
        System.out.println("\nSuggestions for 'cust':");
        List<String> suggestions = engine.suggest("cust", 5);
        suggestions.forEach(s -> System.out.println("  • " + s));

        // Record usage
        if (!suggestions.isEmpty()) {
            engine.recordUsage(suggestions.get(0));
            System.out.println("\nRecorded usage of: " + suggestions.get(0));

            System.out.println("Suggestions for 'cust' after usage:");
            List<String> newSuggestions = engine.suggest("cust", 5);
            newSuggestions.forEach(s -> System.out.println("  • " + s));
        }

        System.out.println("\n✅ Demo completed!");
    }

    private static void printHelp() {
        System.out.println("\n📖 AVAILABLE COMMANDS:");
        System.out.println(DIVIDER);
        System.out.println("suggest, s [prefix]  - Get suggestions for a prefix");
        System.out.println("add, a [word]        - Add a new word to the system");
        System.out.println("use, u [word]        - Record usage of a word");
        System.out.println("context, c [lang]    - View/set language context");
        System.out.println("strategy, st         - Switch ranking strategy");
        System.out.println("load, l [language]   - Load language keywords (java/python/javascript)");
        System.out.println("stats                - Show engine statistics");
        System.out.println("clear                - Clear search history");
        System.out.println("demo                 - Run a quick demonstration");
        System.out.println("help, h              - Show this help");
        System.out.println("quit, q, exit        - Exit the program");
        System.out.println(DIVIDER);
        System.out.println("💡 Tip: You can use short forms (s, a, u, c, etc.)");
    }

    private static int getIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.print("Please enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
