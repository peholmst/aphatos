package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.gui.actions.contextsensitive.GloballyContextSensitiveAction;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Redirectable action for deleting the selected element.
 *
 * @author Petter Holmström
 */
public class DeleteAction extends GloballyContextSensitiveAction {

    /**
     * The action command key for this action: <code>delete</code>
     */
    private static final String COMMAND_KEY = "delete";
    private static final long serialVersionUID = 123L;

    /**
     * Creates a new <code>DeleteAction</code>.
     */
    public DeleteAction() {
        super(COMMAND_KEY, MessageSource.getInstance().get(
                "deleteAction.label"), Icons.DELETE_ICON);
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "deleteAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "deleteAction.description"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                KeyEvent.VK_DELETE, 0));
    }
}
