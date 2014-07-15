package net.pkhsolutions.aphatos.gui.controller;

import net.pkhsolutions.aphatos.domain.Glossary;

/**
 * Controller interface to the user interface that deals with the glossary tabs
 * in the main window. The methods in this class may be called from any thread,
 * UI operations will always be carried out in the proper Swing thread.
 *
 * @author Petter Holmström
 */
public interface GlossaryTabsUI {

    /**
     * Adds a tab for <code>glossary</code> to the main window.
     *
     * @param glossary the glossary to add.
     */
    public void addGlossaryTab(Glossary glossary);

    /**
     * Removes the tab of <code>glossary</code> from the main window.
     *
     * @param glossary the glossary to remove.
     */
    public void removeGlossaryTab(Glossary glossary);

    /**
     * Selects the tab of <code>glossary</code> in the main window.
     *
     * @param glossary the glossary to select.
     */
    public void selectGlossaryTab(Glossary glossary);

}
