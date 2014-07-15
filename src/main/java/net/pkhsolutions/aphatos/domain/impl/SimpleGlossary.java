package net.pkhsolutions.aphatos.domain.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link net.pkhsolutions.aphatos.domain.Glossary} that stores the words in an
 * {@link ArrayList}. Words consisting of only numbers are not included. This
 * class is thread-safe.
 *
 * @author Petter Holmström
 */
public class SimpleGlossary extends AbstractGlossary {

    private final List<String> words = new ArrayList<>();

    @Override
    public synchronized void add(String... words) {
        assert words != null : "words must not be null";
        int p;
        for (String w : words) {
            if ((p = doAddWord(w)) != -1)
                notifyWordAdded(this.words.get(p), p);
        }
    }

    /**
     * Inserts <code>word</code> into the glossary on its proper place,
     * without notifying any listeners.
     *
     * @param word the word to add.
     * @return the position of <code>word</code>, or -1 if not added.
     */
    int doAddWord(String word) {
        assert word != null : "word must not be null";
        try {
            Integer.parseInt(word);
        } catch (NumberFormatException e) {
            String convertedWord = getWordConverter() != null ? getWordConverter()
                    .processWord(word)
                    : word;
            int p = Collections.binarySearch(this.words, convertedWord);
            if (p < 0) {
                p = -p - 1;
                this.words.add(p, convertedWord);
                return p;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(String word) {
        assert word != null : "word must not be null";
        return words.contains(word);
    }

    @Override
    public synchronized void delete(int position)
            throws IndexOutOfBoundsException {
        String word = get(position);
        doDeleteWord(position);
        notifyWordDeleted(word, position);
    }

    /**
     * Deletes the word on <code>position</code> without notifying any
     * listeners.
     *
     * @param position the position of the word to delete.
     * @throws IndexOutOfBoundsException if <code>position</code> is out of bounds.
     */
    void doDeleteWord(int position) throws IndexOutOfBoundsException {
        words.remove(position);
    }

    @Override
    public String get(int position) throws IndexOutOfBoundsException {
        return words.get(position);
    }

    @Override
    public int getSize() {
        return words.size();
    }

    @Override
    public int indexOf(String word) {
        assert word != null : "word must not be null";
        return words.indexOf(word);
    }

    /**
     * Provides direct access to the internal word list.
     *
     * @return the internal word list.
     */
    final List<String> getWords() {
        return words;
    }
}
