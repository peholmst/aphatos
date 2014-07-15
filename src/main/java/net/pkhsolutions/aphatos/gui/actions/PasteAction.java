package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.gui.actions.contextsensitive.GloballyContextSensitiveAction;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Redirectable action for pasting the selected element from the clipboard.
 *
 * @author Petter Holmström
 */
public class PasteAction extends GloballyContextSensitiveAction {

    /**
     * The action command key for this action: <code>paste</code>
     */
    private static final String COMMAND_KEY = "paste";
    private static final long serialVersionUID = 123L;

    /**
     * Creates a new <code>PasteAction</code>.
     */
    public PasteAction() {
        super(COMMAND_KEY, MessageSource.getInstance().get(
                "pasteAction.label"), Icons.PASTE_ICON);
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "pasteAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "pasteAction.description"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
}
