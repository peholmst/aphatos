package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.gui.actions.contextsensitive.GloballyContextSensitiveAction;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Redirectable action for cutting the selected element to the clipboard.
 *
 * @author Petter Holmström
 */
public class CutAction extends GloballyContextSensitiveAction {

    /**
     * The action command key for this action: <code>cut</code>
     */
    private static final String COMMAND_KEY = "cut";
    private static final long serialVersionUID = 123L;

    /**
     * Creates a new <code>CutAction</code>.
     */
    public CutAction() {
        super(COMMAND_KEY, MessageSource.getInstance().get("cutAction.label"),
                Icons.CUT_ICON);
        putValue(Action.SMALL_ICON, Icons.CUT_ICON);
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "cutAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "cutAction.description"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
}
