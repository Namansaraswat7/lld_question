package org.lld.autocompletesystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TrieNode {

    private Map<Character,TrieNode> children;

    private boolean isEndOfWord;

    // Metadata for end-of-word nodes
    private AtomicInteger usageCount;
    private String word;
    private EntryType entryType;
    private String language; // java, python, javascript

    public TrieNode() {
        this.isEndOfWord = false;
        this.children = new HashMap<>();
        this.usageCount = new AtomicInteger(0);
    }

    // Getters and setters
    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        this.isEndOfWord = endOfWord;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public int getUsageCount() {
        return usageCount.get();
    }

    public void incrementUsageCount() {
        this.usageCount.getAndIncrement();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Insert a word into the trie
     */
    public void insert(String word, EntryType entryType, String language) {
        TrieNode current = this;

        // print ->
        // p
        // r -> p
        // i -> reference of r
        for (char ch : word.toCharArray()) {
            current.getChildren().putIfAbsent(ch, new TrieNode());
            current = current.getChildren().get(ch);
        }

        current.setEndOfWord(true);
        current.setWord(word);
        current.setEntryType(entryType);
        current.setLanguage(language);
    }

    /**
     * Search for a word and increment usage count if found
     */
    public boolean search(String word) {
        TrieNode node = findNode(word);
        if (node != null && node.isEndOfWord()) {
            node.incrementUsageCount();
            return true;
        }
        return false;
    }

    /**
     * Check if any word starts with the given prefix
     */
    public boolean startsWith(String prefix) {
        return findNode(prefix) != null;
    }

    /**
     * Get all suggestions for a given prefix
     */
    public List<WordEntry> getSuggestions(String prefix) {
        List<WordEntry> suggestions = new ArrayList<>();
        TrieNode prefixNode = findNode(prefix);

        if (prefixNode != null) {
            collectWords(prefixNode, prefix, suggestions);
        }

        return suggestions;
    }

    /**
     * Get suggestions filtered by language
     */
    public List<WordEntry> getSuggestions(String prefix, String language) {
        List<WordEntry> allSuggestions = getSuggestions(prefix);

        if (language == null || language.isEmpty()) {
            return allSuggestions;
        }

        return allSuggestions.stream()
                .filter(entry -> language.equals(entry.getLanguage()))
                .toList();
    }

    private TrieNode findNode(String word) {
        TrieNode current = this;

        for (char ch : word.toCharArray()) {
            if (!current.getChildren().containsKey(ch)) {
                return null;
            }
            current = current.getChildren().get(ch);
        }

        return current;
    }

    // pri
    private void collectWords(TrieNode node, String currentWord, List<WordEntry> suggestions) {
        if (node.isEndOfWord()) {
            suggestions.add(new WordEntry(
                    currentWord,
                    node.getEntryType(),
                    node.getLanguage(),
                    node.getUsageCount()
            ));
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            collectWords(entry.getValue(), currentWord + entry.getKey(), suggestions);
        }
    }


}
