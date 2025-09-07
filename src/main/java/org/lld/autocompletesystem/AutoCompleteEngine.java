package org.lld.autocompletesystem;

import org.lld.autocompletesystem.strategy.FrequencyStrategy;
import org.lld.autocompletesystem.strategy.RankingStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoCompleteEngine {
    private final TrieNode root;
    private final RankingStrategy rankingStrategy;
    private final Map<String, String> contextHistory; // Track search patterns by language
    private String currentLanguage; // Current context language

    public AutoCompleteEngine() {
        this(new FrequencyStrategy());
    }

    public AutoCompleteEngine(RankingStrategy rankingStrategy) {
        this.root = new TrieNode();
        this.rankingStrategy = rankingStrategy;
        this.contextHistory = new HashMap<>();
        this.currentLanguage = null;
    }

    /**
     * Set the current language context
     */
    public void setLanguageContext(String language) {
        this.currentLanguage = language;
    }

    /**
     * Get the current language context
     */
    public String getLanguageContext() {
        return currentLanguage;
    }

    /**
     * Add a word to the autocomplete system
     */
    public void addWord(String word, EntryType entryType, String language) {
        root.insert(word, entryType, language);
    }

    /**
     * Main suggest method - returns ranked suggestions and learns from search
     */
    public List<String> suggest(String prefix, int maxResults) {
        // Record this search for context learning
        recordSearch(prefix);

        // Get suggestions based on current language context
        List<WordEntry> suggestions;
        if (currentLanguage != null) {
            suggestions = root.getSuggestions(prefix, currentLanguage);
        } else {
            suggestions = root.getSuggestions(prefix);
        }

        // Rank suggestions using the strategy
        List<WordEntry> rankedSuggestions = rankingStrategy.rank(suggestions, prefix);

        // Extract words and limit results
        return rankedSuggestions.stream()
                .map(WordEntry::getWord)
                .limit(maxResults)
                .toList();
    }

    /**
     * Simplified suggest method with default max results
     */
    public List<String> suggest(String prefix) {
        return suggest(prefix, 10);
    }

    /**
     * Record usage of a word (when user selects it)
     */
    public void recordUsage(String word) {
        root.search(word); // This increments usage count
    }

    /**
     * Initialize with language-specific keywords
     */
    public void initializeLanguage(String language) {
        switch (language.toLowerCase()) {
            case "java" -> initializeJava();
            case "python" -> initializePython();
            case "javascript" -> initializeJavaScript();
        }
        setLanguageContext(language);
    }

    /**
     * Get context learning statistics
     */
    public Map<String, String> getContextHistory() {
        return new HashMap<>(contextHistory);
    }

    private void recordSearch(String prefix) {
        if (currentLanguage != null) {
            contextHistory.put(prefix, currentLanguage);
        }
    }

    private void initializeJava() {
        // Java keywords
        String[] keywords = {
                "public", "private", "protected", "static", "final", "abstract",
                "class", "interface", "extends", "implements", "package", "import",
                "int", "double", "float", "boolean", "char", "byte", "short", "long",
                "if", "else", "while", "for", "do", "switch", "case", "default",
                "try", "catch", "finally", "throw", "throws", "return", "break", "continue"
        };

        for (String keyword : keywords) {
            addWord(keyword, EntryType.KEYWORD, "java");
        }

        // Common Java methods
        String[] methods = {
                "println", "print", "toString", "equals", "hashCode", "length", "size",
                "get", "set", "add", "remove", "contains", "isEmpty", "clear"
        };

        for (String method : methods) {
            addWord(method, EntryType.METHOD, "java");
        }

        // Common Java classes
        String[] classes = {
                "String", "Integer", "Double", "Boolean", "ArrayList", "HashMap",
                "LinkedList", "HashSet", "Scanner", "System", "Object"
        };

        for (String className : classes) {
            addWord(className, EntryType.CLASS, "java");
        }
    }

    private void initializePython() {
        // Python keywords
        String[] keywords = {
                "def", "class", "if", "elif", "else", "while", "for", "in", "try", "except",
                "finally", "with", "as", "import", "from", "return", "yield", "lambda",
                "and", "or", "not", "is", "None", "True", "False", "pass", "break", "continue"
        };

        for (String keyword : keywords) {
            addWord(keyword, EntryType.KEYWORD, "python");
        }

        // Common Python methods
        String[] methods = {
                "print", "len", "range", "enumerate", "zip", "map", "filter", "sorted",
                "append", "extend", "insert", "remove", "pop", "index", "count"
        };

        for (String method : methods) {
            addWord(method, EntryType.METHOD, "python");
        }
    }

    private void initializeJavaScript() {
        // JavaScript keywords
        String[] keywords = {
                "var", "let", "const", "function", "if", "else", "while", "for", "do",
                "switch", "case", "default", "try", "catch", "finally", "throw", "return",
                "break", "continue", "class", "extends", "import", "export", "from"
        };

        for (String keyword : keywords) {
            addWord(keyword, EntryType.KEYWORD, "javascript");
        }

        // Common JavaScript methods
        String[] methods = {
                "console.log", "alert", "prompt", "confirm", "parseInt", "parseFloat",
                "push", "pop", "shift", "unshift", "slice", "splice", "indexOf", "includes"
        };

        for (String method : methods) {
            addWord(method, EntryType.METHOD, "javascript");
        }
    }
}
