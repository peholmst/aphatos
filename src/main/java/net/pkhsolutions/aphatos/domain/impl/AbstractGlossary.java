package net.pkhsolutions.aphatos.domain.impl;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryListener;
import net.pkhsolutions.aphatos.domain.WordConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Abstract base class for {@link net.pkhsolutions.aphatos.domain.Glossary}-implementations. This class is
 * thread-safe.
 *
 * @author Petter Holmström
 */
public abstract class AbstractGlossary implements Glossary {

    final Log logger = LogFactory.getLog(getClass());
    private final Set<GlossaryListener> listeners = new HashSet<>();
    private char[] separators = DEFAULT_WORD_SEPARATORS;
    private WordConverter wordConverter;

    @Override
    public WordConverter getWordConverter() {
        return wordConverter;
    }

    @Override
    public synchronized void setWordConverter(WordConverter wordConverter) {
        this.wordConverter = wordConverter;
    }

    @Override
    public char[] getWordSeparators() {
        return separators;
    }

    @Override
    public synchronized void setWordSeparators(char... separators) {
        assert separators != null : "separators must not be null";
        this.separators = separators;
    }

    @Override
    public synchronized void extractAndAdd(String sentence) {
        assert sentence != null : "sentence must not be null";
        if (sentence.isEmpty())
            return;

        String delimiters = new String(getWordSeparators());
        StringTokenizer st = new StringTokenizer(sentence, delimiters);
        while (st.hasMoreTokens())
            add(st.nextToken());
    }

    /**
     * Notifies all registered {@link GlossaryListener}s that <code>word</code>
     * has been added to this glossary at <code>position</code>.
     *
     * @param word     the added word.
     * @param position the position of <code>word</code>.
     * @see GlossaryListener#wordAdded(Glossary, String, int)
     */
    void notifyWordAdded(String word, int position) {
        for (GlossaryListener listener : listeners)
            listener.wordAdded(this, word, position);
    }

    /**
     * Notifies all registered {@link GlossaryListener}s that <code>word</code>
     * at <code>position</code> has been deleted from this glossary.
     *
     * @param word     the deleted word.
     * @param position the position of <code>word</code>.
     * @see GlossaryListener#wordDeleted(Glossary, String, int)
     */
    void notifyWordDeleted(String word, int position) {
        for (GlossaryListener listener : listeners)
            listener.wordDeleted(this, word, position);
    }

    /**
     * Notifies all registered {@link GlossaryListener}s that the glossary has
     * been refreshed.
     *
     * @see GlossaryListener#glossaryRefreshed(Glossary)
     */
    void notifyGlossaryRefreshed() {
        for (GlossaryListener listener : listeners)
            listener.glossaryRefreshed(this);
    }

    /**
     * Provides direct access to the internal set of listeners.
     *
     * @return the internal listener set.
     */
    final Set<GlossaryListener> getListeners() {
        return listeners;
    }

    @Override
    public synchronized void addGlossaryListener(GlossaryListener listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void removeGlossaryListener(GlossaryListener listener) {
        listeners.remove(listener);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
