package net.pkhsolutions.aphatos.domain;

/**
 * Glossary listener that is notified of {@link PersistentGlossary}-specific
 * events, in addition to the ordinary glossary events.
 *
 * @author Petter Holmström
 */
public interface PersistentGlossaryListener extends GlossaryListener {

    /**
     * Notifies that <code>sender</code> has been loaded.
     *
     * @param sender the sending glossary.
     */
    public void glossaryLoaded(PersistentGlossary sender);

    /**
     * Notifies that <code>sender</code> has been saved.
     *
     * @param sender the sending glossary.
     */
    public void glossarySaved(PersistentGlossary sender);

}
