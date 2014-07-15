package net.pkhsolutions.aphatos.domain;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Extended variant of the {@link Glossary} interface that makes it possible to
 * save and load the glossary using {@link Writer}s and {@link Reader}s.
 *
 * @author Petter Holmström
 */
public interface PersistentGlossary extends Glossary {

    /**
     * Checks whether the glossary has been modified after it was saved or
     * loaded.
     *
     * @return <code>true</code> if the glossary is dirty, <code>false</code>
     * otherwise.
     */
    public boolean isDirty();

    /**
     * Saves the glossary to <code>writer</code>, setting the
     * <code>dirty</code> state to <code>false</code>. The saved glossary
     * is always sorted.
     *
     * @param writer the writer to save the glossary to (never <code>null</code>).
     * @throws IOException on I/O errors.
     * @see #isDirty()
     */
    public void save(Writer writer) throws IOException;

    /**
     * Loads the glossary from <code>reader</code>, replacing any existing
     * words and setting the <code>dirty</code> state to <code>false</code>.
     * If the loaded glossary is not sorted, it will. No other processing will
     * be made (such as ensuring the glossary has only one word on each line,
     * etc).
     *
     * @param reader the reader to load the glossary from (never <code>null</code>).
     * @throws IOException on I/O errors.
     * @see #isDirty()
     */
    public void load(Reader reader) throws IOException;

}
