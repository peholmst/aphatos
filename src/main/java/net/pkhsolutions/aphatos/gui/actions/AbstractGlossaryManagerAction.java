package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.domain.GlossaryManager;

import javax.swing.*;

/**
 * Abstract user interface action that invokes operations on a
 * {@link GlossaryManager}.
 *
 * @author Petter Holmström
 */
abstract class AbstractGlossaryManagerAction extends AbstractAction {

    private static final long serialVersionUID = 2726939579809717410L;

    private GlossaryManager glossaryManager;

    /**
     * Creates a new <code>AbstractGlossaryManagerAction</code>.
     *
     * @param glossaryManager the glossary manager to use (never <code>null</code>).
     */
    AbstractGlossaryManagerAction(GlossaryManager glossaryManager) {
        super();
        assert glossaryManager != null : "glossaryManager must not be null";
        this.glossaryManager = glossaryManager;
    }

    /**
     * Gets the glossary manager that this action operates on.
     *
     * @return the glossary manager.
     */
    GlossaryManager getGlossaryManager() {
        return glossaryManager;
    }
}
