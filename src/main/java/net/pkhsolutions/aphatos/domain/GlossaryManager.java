package net.pkhsolutions.aphatos.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Manager for a collection of {@link Glossary}-instances, of which one is
 * always the current one. This class is thread-safe.
 *
 * @author Petter Holmström
 */
public class GlossaryManager {

    // TODO Document in which order the events are published

    /**
     * Protected logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    private final List<Glossary> glossaries = new ArrayList<>();

    private final Set<GlossaryManagerListener> listeners = new HashSet<>();

    private Glossary currentGlossary;

    /**
     * Gets all the glossaries currently managed by this manager.
     *
     * @return an unmodifiable list of glossaries.
     */
    public List<Glossary> getGlossaries() {
        return Collections.unmodifiableList(glossaries);
    }

    /**
     * Adds <code>glossary</code> to the list of managed glossaries. If the
     * glossary is already in the list, nothing happens. If the list is empty,
     * the glossary will automatically be set to the current glossary.
     *
     * @param glossary the glossary to add (never <code>null</code>).
     * @return <code>true</code> if the glossary was added, <code>false</code>
     * otherwise.
     * @see #setCurrentGlossary(Glossary)
     */
    public synchronized boolean addGlossary(Glossary glossary) {
        assert glossary != null : "glossary must not be null";

        if (logger.isInfoEnabled())
            logger.info("Adding glossary " + glossary);

        if (!glossaries.contains(glossary)) {
            if (logger.isDebugEnabled())
                logger.debug("Notifying listeners that " + glossary
                        + " is about to be added");

            for (GlossaryManagerListener l : listeners)
                if (!l.addingGlossary(this, glossary)) {
                    if (logger.isWarnEnabled())
                        logger.warn("Adding of " + glossary + " aborted by "
                                + l);
                    return false;
                }

            glossaries.add(glossary);

            if (logger.isDebugEnabled())
                logger.debug("Notifying listeners that " + glossary
                        + " has been added");

            for (GlossaryManagerListener l : listeners)
                l.glossaryAdded(this, glossary);
            if (getCurrentGlossary() == null)
                setCurrentGlossary(glossary);
            return true;
        }

        if (logger.isWarnEnabled())
            logger.warn("Glossary already exists");

        return false;
    }

    /**
     * Removes <code>glossary</code> from the list of managed glossaries. If the
     * glossary is not in the list, nothing happens. If the glossary is the
     * current glossary, the current glossary will be changed to
     * <code>null</code> if there are no other glossaries, or to another
     * glossary if there are several glossaries in the list.
     *
     * @param glossary the glossary to remove (never <code>null</code>).
     * @return <code>true</code> if the glossary was removed, <code>false</code>
     * otherwise.
     * @see #setCurrentGlossary(Glossary)
     */
    public synchronized boolean removeGlossary(Glossary glossary) {
        assert glossary != null : "glossary must not be null";

        if (logger.isInfoEnabled())
            logger.info("Removing glossary " + glossary);

        int ix = glossaries.indexOf(glossary);
        if (ix > -1)
            return remove(ix, glossary);

        if (logger.isWarnEnabled())
            logger.warn("Glossary not found");

        return false;
    }

    /**
     * Removes the glossary at position <code>index</code> from the list of
     * managed glossaries. If the glossary is the current glossary, the current
     * glossary will be changed to <code>null</code> if there are no other
     * glossaries, or to another glossary if there are several glossaries in the
     * list.
     *
     * @param index the index of the glossary to remove.
     * @return <code>true</code> if the glossary was removed, <code>false</code>
     * otherwise.
     * @throws IndexOutOfBoundsException if <code>index</code> was out of bounds.
     * @see #setCurrentGlossary(Glossary)
     */
    public synchronized boolean removeGlossary(int index)
            throws IndexOutOfBoundsException {
        if (logger.isInfoEnabled())
            logger.info("Removing glossary at position " + index);
        return remove(index, null);
    }

    private boolean remove(int index, Glossary glossary)
            throws IndexOutOfBoundsException {
        if (glossary == null)
            glossary = glossaries.get(index);
        else {
            if (index < 0 || index >= glossaries.size())
                throw new IndexOutOfBoundsException();
        }

        if (logger.isDebugEnabled())
            logger.debug("Notifying listeners that " + glossary
                    + " is about to be removed");

        for (GlossaryManagerListener l : listeners)
            if (!l.removingGlossary(this, glossary)) {
                if (logger.isWarnEnabled())
                    logger.warn("Removal of " + glossary + " aborted by " + l);
                return false;
            }

        if (currentGlossary == glossary) {
            if (glossaries.size() > 1)
                doSetCurrentGlossary(glossaries.get(index == 0 ? index + 1
                        : index - 1));
            else
                doSetCurrentGlossary(null);
        }
        glossaries.remove(index);

        if (logger.isDebugEnabled())
            logger.debug("Notifying listeners that " + glossary
                    + " has been removed");

        for (GlossaryManagerListener l : listeners)
            l.glossaryRemoved(this, glossary);

        return true;
    }

    /**
     * Removes all managed glossaries, setting the current glossary to
     * <code>null</code>. If one or more glossaries could not be removed, one of
     * them will be set to the current glossary.
     *
     * @return <code>true</code> if all glossaries were removed,
     * <code>false</code> otherwise.
     */
    public synchronized boolean removeAll() {
        if (logger.isInfoEnabled())
            logger.info("Removing all glossaries");

        for (GlossaryManagerListener l : listeners)
            l.removingAllGlossaries(this);

        boolean result = true;
        try {
            for (int i = glossaries.size() - 1; i >= 0; i--) {
                Glossary glossary = glossaries.get(i);

                if (logger.isDebugEnabled())
                    logger.debug("Notifying listeners that " + glossary
                            + " is about to be removed");

                for (GlossaryManagerListener l : listeners)
                    if (!l.removingGlossary(this, glossary)) {
                        if (logger.isWarnEnabled())
                            logger.warn("Removal of " + glossary
                                    + " aborted by " + l);

                        if (currentGlossary == null)
                            doSetCurrentGlossary(glossary);

                        throw new RemovalPreventedException();
                    }

                if (currentGlossary == glossary)
                    doSetCurrentGlossary(null);

                glossaries.remove(i);

                if (logger.isDebugEnabled())
                    logger.debug("Notifying listeners that " + glossary
                            + " has been removed");

                for (GlossaryManagerListener l : listeners)
                    l.glossaryRemoved(this, glossary);
            }
        } catch (RemovalPreventedException e) {
            result = false;
        }
        for (GlossaryManagerListener l : listeners)
            l.allGlossariesRemoved(this, glossaries.size());

        return result;
    }

    /**
     * Gets the current glossary.
     *
     * @return the current glossary, or <code>null</code> if none is available.
     */
    public Glossary getCurrentGlossary() {
        return currentGlossary;
    }

    /**
     * Changes the current glossary to <code>glossary</code>. The glossary must
     * exist in the list of glossaries, otherwise an exception is thrown.
     *
     * @param glossary the glossary to use as current (never <code>null</code>).
     */
    public synchronized void setCurrentGlossary(Glossary glossary) {
        assert glossary != null : "glossary must not be null";
        doSetCurrentGlossary(glossary);
    }

    void doSetCurrentGlossary(Glossary glossary) {
        if (glossary != null && !glossaries.contains(glossary)) {
            if (logger.isErrorEnabled())
                logger.error("Tried to change current glossary to " + glossary
                        + ", which is not in the list");

            throw new IllegalArgumentException("glossary not found in list");
        }
        if (this.currentGlossary != glossary) {

            if (logger.isInfoEnabled())
                logger.info("Setting default glossary to " + glossary);

            this.currentGlossary = glossary;

            if (logger.isDebugEnabled())
                logger
                        .debug("Notifying listeners that the current glossary has changed");

            for (GlossaryManagerListener l : listeners)
                l.currentGlossaryChange(this, glossary);
        }
    }

    /**
     * Adds <code>listener</code> to the set of listeners to be notified of
     * changes to this manager. If an equal listener is already present, nothing
     * happens.
     *
     * @param listener the listener to add (never <code>null</code>).
     */
    public synchronized void addListener(GlossaryManagerListener listener) {
        assert listener != null : "listener must not be null";
        listeners.add(listener);
        if (logger.isDebugEnabled())
            logger.debug("Listener " + listener + " added");
    }

    /**
     * Removes <code>listener</code> from the set of listeners. If no such
     * listener is found, nothing happens.
     *
     * @param listener the listener to remove (never <code>null</code>).
     */
    public synchronized void removeListener(GlossaryManagerListener listener) {
        assert listener != null : "listener must not be null";
        listeners.remove(listener);
        if (logger.isDebugEnabled())
            logger.debug("Listener " + listener + " removed");
    }

    private class RemovalPreventedException extends Exception {

        private static final long serialVersionUID = -4350511924584954878L;
    }
}
