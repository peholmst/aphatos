package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.gui.actions.contextsensitive.GloballyContextSensitiveAction;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Redirectable action for copying the selected element to the clipboard.
 *
 * @author Petter Holmström
 */
public class CopyAction extends GloballyContextSensitiveAction {

    /**
     * The action command key for this action: <code>copy</code>
     */
    private static final String COMMAND_KEY = "copy";
    private static final long serialVersionUID = 123L;

    /**
     * Creates a new <code>CopyAction</code>.
     */
    public CopyAction() {
        super(COMMAND_KEY, MessageSource.getInstance().get(
                "copyAction.label"), Icons.COPY_ICON);
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "copyAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "copyAction.description"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
}
