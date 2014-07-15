package net.pkhsolutions.aphatos.gui.actions.contextsensitive;

import javax.swing.*;

/**
 * TODO Document and test me!
 *
 * @author Petter Holmström
 */
public class DefaultDelegateActionSourceSelectionStrategy implements DelegateActionSourceSelectionStrategy {
    @Override
    public JComponent getDelegateActionSource(String actionName,
                                              JComponent oldFocusOwner, JComponent newFocusOwner) {
        if (newFocusOwner == null)
            return oldFocusOwner; // Use old focus owner if there is no new focus owner.

        //System.out.println(newFocusOwner);

        Action a = newFocusOwner.getActionMap().get(actionName);
        if (a == null && oldFocusOwner != null) {
            // If we move to a new JList, JTextArea or JTextField, only the actions it provides are allowed.
            // Otherwise, use old focus owner.
            // CHECKME This hack is to prevent some actions from being accidentally executed on the wrong focus owner.
            // Preferably, it should be replaced with some kind of 'active action object' method.
            if (newFocusOwner instanceof JList || newFocusOwner instanceof JTextArea || newFocusOwner instanceof JTextField)
                return null;
            else
                return oldFocusOwner;
        }

        return newFocusOwner; // Otherwise, use new focus owner.
    }
}