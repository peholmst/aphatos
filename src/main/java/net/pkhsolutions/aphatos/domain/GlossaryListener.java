package net.pkhsolutions.aphatos.domain;

/**
 * Listener interface to be implemented by all classes that want to be notified
 * of changes in a {@link Glossary}.
 *
 * @author Petter Holmström
 */
public interface GlossaryListener {

    /**
     * Notifies that <code>word</code> has been added to <code>sender</code>.
     *
     * @param sender   the sending glossary.
     * @param word     the added word.
     * @param position the new position of <code>word</code>.
     */
    public void wordAdded(Glossary sender, String word, int position);

    /**
     * Notifies that <code>word</code> has been deleted from
     * <code>sender</code>.
     *
     * @param sender   the sending glossary.
     * @param word     the deleted word.
     * @param position the position of <code>word</code>.
     */
    public void wordDeleted(Glossary sender, String word, int position);

    /**
     * Notifies that <code>sender</code> has been refreshed.
     *
     * @param sender the sending glossary.
     */
    public void glossaryRefreshed(Glossary sender);

}
