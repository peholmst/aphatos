package net.pkhsolutions.aphatos.domain;

/**
 * Interface defining a glossary of words. A glossary is always sorted in
 * ascending alpha-numerical order. Words may be added or removed from the
 * glossary at any time.
 *
 * @author Petter Holmström
 */
public interface Glossary {

    /**
     * The default word separator characters.
     */
    public static final char[] DEFAULT_WORD_SEPARATORS = {' ', '.', ',', ':',
            ';', '-', '_', '(', ')', '[', ']', '!', '?', '"', '\n', '\t', '\r',
            '&', '/', '|', '\\', '<', '>', '+'};

    /**
     * Checks if the glossary contains <code>word</code>.
     *
     * @param word the word to check.
     * @return <code>true</code> if the word is in the glossary,
     * <code>false</code> otherwise.
     * @see #indexOf(String)
     */
    public boolean contains(String word);

    /**
     * Gets the number of words currently in the glossary.
     *
     * @return the number of words.
     */
    public int getSize();

    /**
     * Gets the word at <code>position</code>.
     *
     * @param position the position of the word.
     * @return the word.
     * @throws IndexOutOfBoundsException if <code>position</code> was invalid.
     */
    public String get(int position) throws IndexOutOfBoundsException;

    /**
     * Adds <code>words</code> to the glossary. Words that already are present
     * in the glossary are ignored. Before the words are added, they are
     * processed by a {@link WordConverter}, if present. The glossary remains
     * sorted after this operation.
     *
     * @param words the words to add.
     * @see #getWordConverter()
     */
    public void add(String... words);

    /**
     * Extracts all the words from <code>sentence</code>, using the
     * separators return by {@link #getWordSeparators()}, and adds them to the
     * glossary. Words that already are present in the glossary are ignored.
     * Before the words are added, they are processed by a {@link WordConverter},
     * if present. The glossary remains sorted after this operation.
     *
     * @param sentence the sentence to extract and add.
     * @see #getWordSeparators()
     * @see #getWordConverter()
     */
    public void extractAndAdd(String sentence);

    /**
     * Gets the position of <code>word</code> in the glossary.
     *
     * @param word the word to check.
     * @return the position of the word, or -1 if not found.
     */
    public int indexOf(String word);

    /**
     * Removes the word at <code>position</code> from the glossary.
     *
     * @param position the position of the word to remove.
     * @throws IndexOutOfBoundsException if <code>position</code> was invalid.
     */
    public void delete(int position) throws IndexOutOfBoundsException;

    /**
     * Gets all characters that are to be considered as word separators when
     * processing sentences (for example space, period, comma, etc.). If no
     * custom separators are specified, {@link #DEFAULT_WORD_SEPARATORS} will be
     * used.
     *
     * @return an array of separator characters.
     * @see #DEFAULT_WORD_SEPARATORS
     * @see #setWordSeparators(char...)
     */
    public char[] getWordSeparators();

    /**
     * Sets all characters that are to be considered as word separators when
     * processing sentences.
     *
     * @param separators the word separators to use.
     * @see #getWordSeparators()
     */
    public void setWordSeparators(char... separators);

    /**
     * Gets the word converter to use for last-minute processing of words before
     * they are added to the glossary.
     *
     * @return the word converter if specified, <code>null</code> otherwise.
     * @see #setWordConverter(WordConverter)
     */
    public WordConverter getWordConverter();

    /**
     * Sets the word converter to use for last-minute processing of words before
     * they are added to the glossary.
     *
     * @param wordConverter the word converter to set, <code>null</code> to use none.
     * @see #getWordConverter()
     */
    public void setWordConverter(WordConverter wordConverter);

    /**
     * Adds <code>listener</code> to the set of listeners to be notified of
     * changes in the glossary. If an equal listener is already present, nothing
     * happens.
     *
     * @param listener the listener to add.
     * @see #removeGlossaryListener(GlossaryListener)
     */
    public void addGlossaryListener(GlossaryListener listener);

    /**
     * Removes <code>listener</code> from the set of listeners. If no such
     * listener is found, nothing happens.
     *
     * @param listener the listener to remove.
     * @see #addGlossaryListener(GlossaryListener)
     */
    public void removeGlossaryListener(GlossaryListener listener);

    /**
     * Gets the name of this glossary, if available.
     *
     * @return the name of this glossary, may be empty (but never
     * <code>null</code>).
     */
    public String getName();
}
