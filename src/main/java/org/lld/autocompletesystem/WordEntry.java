package org.lld.autocompletesystem;

public class WordEntry {
    private final String word;
    private final EntryType entryType;
    private final String language;
    private final int usageCount;

    public WordEntry(String word, EntryType entryType, String language, int usageCount) {
        this.word = word;
        this.entryType = entryType;
        this.language = language;
        this.usageCount = usageCount;
    }

    public String getWord() {
        return word;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public String getLanguage() {
        return language;
    }

    public int getUsageCount() {
        return usageCount;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s, usage: %d)",
                word, entryType, language, usageCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        WordEntry wordEntry = (WordEntry) obj;
        return word.equals(wordEntry.word) &&
                entryType == wordEntry.entryType &&
                language.equals(wordEntry.language);
    }

    @Override
    public int hashCode() {
        return word.hashCode() + entryType.hashCode() + language.hashCode();
    }
}
