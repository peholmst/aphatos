package net.pkhsolutions.aphatos.gui.controller;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
import net.pkhsolutions.aphatos.domain.GlossaryManagerListener;
import net.pkhsolutions.aphatos.domain.PersistentGlossary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Controller for the main application logic that listens to changes in both the
 * main window and the {@link net.pkhsolutions.aphatos.domain.GlossaryManager}. As the actions execute
 * operations on these to objects, events are transmitted to the controller, who
 * in turn makes sure the application state is updated accordingly. The
 * controller updates the user interface via {@link GlossaryFileUI},
 * {@link SystemUI} and {@link GlossaryTabsUI}.
 *
 * @author Petter Holmström
 */
public class GlossaryController implements GlossaryManagerListener,
        WindowListener {

    /**
     * Protected logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    private GlossaryManager glossaryManager;

    private GlossaryFileUI glossaryFileUI;

    private GlossaryTabsUI glossaryTabsUI;

    private SystemUI systemUI;

    /**
     * Creates a new <code>GlossaryController</code>. None of the parameters
     * may be <code>null</code>.
     *
     * @param glossaryManager the glossary manager to use.
     * @param glossaryFileUI  the glossary file UI to use.
     * @param glossaryTabsUI  the glossary tabs UI to use.
     * @param systemUI        the system UI to use.
     */
    public GlossaryController(GlossaryManager glossaryManager,
                              GlossaryFileUI glossaryFileUI, GlossaryTabsUI glossaryTabsUI,
                              SystemUI systemUI) {
        assert glossaryManager != null : "glossaryManager must not be null";
        assert glossaryFileUI != null : "glossaryFileUI must not be null";
        assert glossaryTabsUI != null : "glossaryTabsUI must not be null";
        assert systemUI != null : "systemUI must not be null";

        this.glossaryManager = glossaryManager;
        this.glossaryFileUI = glossaryFileUI;
        this.glossaryTabsUI = glossaryTabsUI;
        this.systemUI = systemUI;
    }

    /**
     * Gets the glossary manager used by this controller.
     *
     * @return the glossary manager.
     */
    public GlossaryManager getGlossaryManager() {
        return glossaryManager;
    }

    /**
     * Gets the glossary file UI used by this controller.
     *
     * @return the glossary file UI.
     */
    public GlossaryFileUI getGlossaryFileUI() {
        return glossaryFileUI;
    }

    /**
     * Gets the glossary tabs UI used by this controller.
     *
     * @return the glossary tabs UI.
     */
    public GlossaryTabsUI getGlossaryTabsUI() {
        return glossaryTabsUI;
    }

    /**
     * Gets the system UI used by this controller.
     *
     * @return the system UI.
     */
    public SystemUI getSystemUI() {
        return systemUI;
    }

    @Override
    public boolean addingGlossary(GlossaryManager sender, Glossary glossary) {
        // No operation.
        return true;
    }

    /**
     * Focuses the current glossary's tab in the main window.
     */
    @Override
    public void currentGlossaryChange(GlossaryManager sender,
                                      Glossary currentGlossary) {
        if (currentGlossary != null) {
            if (logger.isDebugEnabled())
                logger.debug("Selecting glossary tab in main window UI for "
                        + currentGlossary);
            getGlossaryTabsUI().selectGlossaryTab(currentGlossary);
        }
    }

    /**
     * Adds a new tab for the glossary to the main window.
     */
    @Override
    public void glossaryAdded(GlossaryManager sender, Glossary glossary) {
        if (logger.isDebugEnabled())
            logger.debug("Adding new tab to main window UI for " + glossary);
        getGlossaryTabsUI().addGlossaryTab(glossary);
    }

    /**
     * Removes the glossary's tab from the main window.
     */
    @Override
    public void glossaryRemoved(GlossaryManager sender, Glossary glossary) {
        if (logger.isDebugEnabled())
            logger.debug("Removing tab from main window UI for " + glossary);
        getGlossaryTabsUI().removeGlossaryTab(glossary);
    }

    /**
     * Allows the user to take care of any unsaved changes or cancel before the
     * glossary is removed.
     */
    @Override
    public boolean removingGlossary(GlossaryManager sender, Glossary glossary) {
        if (glossary instanceof PersistentGlossary) {
            if (logger.isDebugEnabled())
                logger.debug("Trying to remove persistent glossary " + glossary
                        + ", checking if there are unsaved changes");
            PersistentGlossary pg = (PersistentGlossary) glossary;
            if (pg.isDirty()
                    && !(getGlossaryFileUI().confirmUnsavedChanges(pg))) {
                if (logger.isDebugEnabled())
                    logger
                            .debug("Glossary was dirty and user clicked 'Cancel', preventing glossary from being removed");
                return false;
            }
        }
        return true;
    }

    @Override
    public void allGlossariesRemoved(GlossaryManager sender,
                                     int remainingGlossaries) {
        // No operation.
    }

    @Override
    public void removingAllGlossaries(GlossaryManager sender) {
        // No operation.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // No operation.
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if (logger.isDebugEnabled())
            logger.debug("Window is closed, exiting application");
        getSystemUI().exitApplication();
    }

    /**
     * Tries to remove all glossaries from the glossary manager and exits the
     * application on success.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        if (logger.isDebugEnabled())
            logger
                    .debug("Main window closing, removing all glossaries from glossary manager");
        if (getGlossaryManager().removeAll()) {
            if (logger.isDebugEnabled())
                logger.debug("All glossaries removed, closing window");
            e.getWindow().dispose();
        } else if (logger.isDebugEnabled())
            logger
                    .debug("Some glossaries could not be removed, keeping the window open");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // No operation.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // No operation.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // No operation.
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // No operation.
    }

}
