package net.pkhsolutions.aphatos.domain;

/**
 * Listener interface to be implemented by classes that wishes to receive event
 * notifications from a {@link GlossaryManager}.
 *
 * @author Petter Holmström
 */
public interface GlossaryManagerListener {

    /**
     * Notification sent when the current glossary of <code>sender</code> is
     * changed to <code>currentGlossary</code>.
     *
     * @param sender          the sending glossary manager.
     * @param currentGlossary the new current glossary, may be <code>null</code>.
     */
    public void currentGlossaryChange(GlossaryManager sender,
                                      Glossary currentGlossary);

    /**
     * Notification sent before <code>glossary</code> is added to
     * <code>sender</code>. A listener may prevent the operation by returning
     * <code>false</code>.
     *
     * @param sender   the sending glossary manager.
     * @param glossary the glossary to be added (never <code>null</code>).
     * @return <code>true</code> to allow the operation, <code>false</code> to
     * abort.
     */
    public boolean addingGlossary(GlossaryManager sender, Glossary glossary);

    /**
     * Notification sent after <code>glossary</code> has been added to
     * <code>sender</code>.
     *
     * @param sender   the sending glossary manager.
     * @param glossary the added glossary (never <code>null</code>).
     */
    public void glossaryAdded(GlossaryManager sender, Glossary glossary);

    /**
     * Notification sent before <code>glossary</code> is removed from
     * <code>sender</code>. A listener may prevent the operation by returning
     * <code>false</code>.
     *
     * @param sender   the sending glossary manager.
     * @param glossary the glossary to be removed (never <code>null</code>).
     * @return <code>true</code> to allow the operation, <code>false</code> to
     * abort.
     */
    public boolean removingGlossary(GlossaryManager sender, Glossary glossary);

    /**
     * Notification sent after <code>glossary</code> has been removed from
     * <code>sender</code>.
     *
     * @param sender   the sending glossary manager.
     * @param glossary the removed glossary (never <code>null</code>).
     */
    public void glossaryRemoved(GlossaryManager sender, Glossary glossary);

    /**
     * Notification sent before all glossaries will be removed from
     * <code>sender</code>.
     *
     * @param sender the sending glossary manager.
     * @see GlossaryManager#removeAll()
     */
    public void removingAllGlossaries(GlossaryManager sender);

    /**
     * Notification sent after all glossaries have been removed from
     * <code>sender</code>.
     *
     * @param sender              the sending glossary manager.
     * @param remainingGlossaries the number of remaining glossaries if one of the listeners aborted the operation.
     * @see GlossaryManager#removeAll()
     */
    public void allGlossariesRemoved(GlossaryManager sender,
                                     int remainingGlossaries);

}
