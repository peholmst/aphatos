package net.pkhsolutions.aphatos.models;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * A class that makes a {@link Glossary} available to a {@link JList} by
 * implementing {@link ListModel}.
 *
 * @author Petter Holmström
 */
public class GlossaryListModel extends AbstractListModel<String> implements
        GlossaryListener {

    private static final long serialVersionUID = 123L;

    /**
     * Protected logger.
     */
    final Log logger = LogFactory.getLog(getClass());

    private Glossary glossary;

    /**
     * Gets the glossary that this list model fetches its data from.
     *
     * @return the glossary, <code>null</code> if none has been specified.
     * @see #setGlossary(Glossary)
     */
    public Glossary getGlossary() {
        return glossary;
    }

    /**
     * Sets the glossary that this list model will fetch its data from.
     *
     * @param glossary the glossary to set.
     * @see #getGlossary()
     */
    public void setGlossary(Glossary glossary) {
        if (logger.isInfoEnabled())
            logger.info("Setting glossary to " + glossary);

        if (this.glossary != null) {
            if (logger.isDebugEnabled())
                logger.debug("Unregistering listener from " + this.glossary);
            this.glossary.removeGlossaryListener(this);
        }

        this.glossary = glossary;

        if (this.glossary != null) {
            if (logger.isDebugEnabled())
                logger.debug("Registering listener with " + this.glossary);
            this.glossary.addGlossaryListener(this);
        }
    }

    /**
     * Gets the current glossary.
     *
     * @return the glossary, never <code>null</code>.
     * @throws IllegalStateException if no glossary has been defined.
     * @see #getGlossary()
     */
    Glossary doGetGlossary() throws IllegalStateException {
        Glossary gl = getGlossary();
        if (gl == null)
            throw new IllegalStateException("No glossary defined");
        return gl;
    }

    @Override
    public String getElementAt(int index) {
        return doGetGlossary().get(index);
    }

    @Override
    public int getSize() {
        return doGetGlossary().getSize();
    }

    // Do not compare glossary with sender, as we could have wrapped glossaries!

    @Override
    public void glossaryRefreshed(Glossary sender) {
        fireContentsChanged(this, 0, getSize() - 1);
    }

    @Override
    public void wordAdded(Glossary sender, String word, int position) {
        fireIntervalAdded(this, position, position);
    }

    @Override
    public void wordDeleted(Glossary sender, String word, int position) {
        fireIntervalRemoved(this, position, position);
    }

}
