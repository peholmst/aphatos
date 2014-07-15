package net.pkhsolutions.aphatos.gui.actions.contextsensitive;

import javax.swing.*;

/**
 * TODO Document me!
 *
 * @author Petter Holmström
 */
public interface DelegateActionSourceSelectionStrategy {

    /**
     * @param actionName
     * @param oldFocusOwner
     * @param newFocusOwner
     * @return
     */
    public JComponent getDelegateActionSource(String actionName, JComponent oldFocusOwner, JComponent newFocusOwner);
}
