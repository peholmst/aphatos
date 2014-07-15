package net.pkhsolutions.aphatos.domain.impl;

import net.pkhsolutions.aphatos.domain.GlossaryListener;
import net.pkhsolutions.aphatos.domain.PersistentGlossary;
import net.pkhsolutions.aphatos.domain.PersistentGlossaryListener;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;

/**
 * Extended version of {@link SimpleGlossary} that implements the
 * {@link net.pkhsolutions.aphatos.domain.PersistentGlossary}-interface.
 *
 * @author Petter Holmström
 * @see net.pkhsolutions.aphatos.domain.PersistentGlossaryListener
 */
public class SimplePersistentGlossary extends SimpleGlossary implements
        PersistentGlossary {

    private boolean dirty = false;

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    protected int doAddWord(String word) {
        int p = super.doAddWord(word);
        if (!dirty && p != -1)
            dirty = true;
        return p;
    }

    @Override
    protected void doDeleteWord(int position) throws IndexOutOfBoundsException {
        super.doDeleteWord(position);
        dirty = true;
    }

    @Override
    public synchronized void load(Reader reader) throws IOException {
        getWords().clear();

        int ch;
        StringBuilder sb = new StringBuilder();
        while ((ch = reader.read()) != -1) {
            if (ch == '\n' || ch == '\r') {
                if (sb.length() > 0) {
                    getWords().add(sb.toString());
                    sb = new StringBuilder();
                }
            } else
                sb.append((char) ch);
        }
        if (sb.length() > 0)
            getWords().add(sb.toString());

        Collections.sort(getWords());
        dirty = false;

        for (GlossaryListener listener : getListeners()) {
            if (listener instanceof PersistentGlossaryListener)
                ((PersistentGlossaryListener) listener).glossaryLoaded(this);
        }

        notifyGlossaryRefreshed();
    }

    @Override
    public synchronized void save(Writer writer) throws IOException {
        String sep = (String) System.getProperties().get("line.separator");

        if (sep == null) {
            // Apparently, this happens sometimes when run via WebStart on MacOS X.
            logger.warn("Could not get line.separator system property, using default");
            sep = "\n";
        }

        for (String word : getWords()) {
            writer.write(word);
            writer.write(sep);
        }

        dirty = false;

        for (GlossaryListener listener : getListeners()) {
            if (listener instanceof PersistentGlossaryListener)
                ((PersistentGlossaryListener) listener).glossarySaved(this);
        }
    }

}
