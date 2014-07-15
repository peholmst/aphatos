package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.AppVersion;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action that displays the about box.
 *
 * @author Petter Holmström
 */
public class AboutAction extends AbstractAction {

    private static final long serialVersionUID = 123L;

    private JFrame mainFrame;

    /**
     * Creates a new <code>AboutAction</code>.
     *
     * @param mainFrame the main window (never <code>null</code>).
     */
    public AboutAction(JFrame mainFrame) {
        assert mainFrame != null : "mainFrame must not be null";
        this.mainFrame = mainFrame;
        putValue(Action.NAME, MessageSource.getInstance().get(
                "aboutAction.label"));
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "aboutAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "aboutAction.description"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Make a nicer-looking about box
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String msg = String.format("%s\n%s %s\n\n%s\n\n%s",
                        MessageSource.getInstance().get(
                                "aboutDialog.description"), MessageSource
                                .getInstance().get("aboutDialog.version"),
                        AppVersion.getVersion(),
                        MessageSource.getInstance()
                                .get("aboutDialog.copyright"), MessageSource
                                .getInstance().get("aboutDialog.license"));
                JOptionPane.showMessageDialog(mainFrame, msg, MessageSource
                                .getInstance().get("aboutDialog.title"),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

}
