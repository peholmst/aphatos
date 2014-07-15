package net.pkhsolutions.aphatos.domain.utils;

import net.pkhsolutions.aphatos.domain.GlossaryListener;
import net.pkhsolutions.aphatos.domain.PersistentGlossary;
import net.pkhsolutions.aphatos.domain.WordConverter;

import java.io.*;
import java.nio.charset.Charset;

/**
 * A wrapper for any {@link net.pkhsolutions.aphatos.domain.PersistentGlossary} that uses a {@link File} for
 * persistence.
 * <p/>
 * Note, that the listeners are not wrapped, i.e. they are registered with the
 * wrapped glossary, that's also responsible for firing the events. Thus, any
 * listeners that are registered with a <code>FileGlossary</code> and expect it
 * to be the sender of the events will find that the events are sent by the
 * wrapped glossary instead.
 *
 * @author Petter Holmström
 */
public class FileGlossary implements PersistentGlossary {

    private File file;

    private PersistentGlossary backend;

    /**
     * Creates a new <code>FileGlossary</code>.
     *
     * @param backend the backend glossary to use (never <code>null</code>).
     */
    public FileGlossary(PersistentGlossary backend) {
        assert backend != null : "backend must not be null";
        this.backend = backend;
    }

    /**
     * Gets the backend glossary.
     *
     * @return the backend glossary (never <code>null</code>).
     */
    PersistentGlossary getBackend() {
        return backend;
    }

    @Override
    public boolean isDirty() {
        return getBackend().isDirty();
    }

    @Override
    public void load(Reader reader) throws IOException {
        getBackend().load(reader);
    }

    @Override
    public void save(Writer writer) throws IOException {
        getBackend().save(writer);
    }

    /**
     * Gets the file to save to/load from.
     *
     * @return the file, <code>null</code> if none has been specified.
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the file to save to/load from.
     *
     * @param file the file to use (never <code>null</code>).
     */
    public void setFile(File file) {
        assert file != null : "file must not be null";
        this.file = file;
    }

    /**
     * Loads the glossary from the specified file.
     *
     * @throws IOException           on I/O errors.
     * @throws IllegalStateException if no file has been specified.
     * @see #getFile()
     */
    public void loadFromFile() throws IOException, IllegalStateException {
        if (file == null)
            throw new IllegalStateException("No file to load");
        // Always use UTF-8
        Reader reader = new InputStreamReader(new FileInputStream(file),
                Charset.forName("UTF-8"));
        load(reader);
        reader.close();
    }

    /**
     * Saves the glossary to the specified file.
     *
     * @throws IOException           on I/O errors.
     * @throws IllegalStateException if no file has been specified.
     * @see #getFile()
     */
    public void saveToFile() throws IOException, IllegalStateException {
        if (file == null)
            throw new IllegalStateException("No file to load");
        // Always use UTF-8
        Writer writer = new OutputStreamWriter(new FileOutputStream(file),
                Charset.forName("UTF-8"));
        save(writer);
        writer.close();
    }

    @Override
    public void add(String... words) {
        getBackend().add(words);
    }

    @Override
    public void addGlossaryListener(GlossaryListener listener) {
        getBackend().addGlossaryListener(listener);
    }

    @Override
    public boolean contains(String word) {
        return getBackend().contains(word);
    }

    @Override
    public void delete(int position) throws IndexOutOfBoundsException {
        getBackend().delete(position);
    }

    @Override
    public void extractAndAdd(String sentence) {
        getBackend().extractAndAdd(sentence);
    }

    @Override
    public String get(int position) throws IndexOutOfBoundsException {
        return getBackend().get(position);
    }

    @Override
    public int getSize() {
        return getBackend().getSize();
    }

    @Override
    public WordConverter getWordConverter() {
        return getBackend().getWordConverter();
    }

    @Override
    public void setWordConverter(WordConverter wordConverter) {
        getBackend().setWordConverter(wordConverter);
    }

    @Override
    public char[] getWordSeparators() {
        return getBackend().getWordSeparators();
    }

    @Override
    public void setWordSeparators(char... separators) {
        getBackend().setWordSeparators(separators);
    }

    @Override
    public int indexOf(String word) {
        return getBackend().indexOf(word);
    }

    @Override
    public void removeGlossaryListener(GlossaryListener listener) {
        getBackend().removeGlossaryListener(listener);
    }

    /**
     * Returns the name of the current file, or an empty string if no file has
     * been specified.
     */
    @Override
    public String getName() {
        return file != null ? file.getName() : "";
    }

    @Override
    public String toString() {
        return super.toString() + "[file=" + file + ", backend=" + getBackend()
                + "]";
    }
}
