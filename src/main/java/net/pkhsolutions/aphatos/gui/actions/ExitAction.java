package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * UI action that exists the application by closing the main window.
 *
 * @author Petter Holmström
 */
public class ExitAction extends AbstractAction {

    private static final long serialVersionUID = 123L;

    private JFrame mainFrame;

    /**
     * Creates a new <code>ExitAction</code>.
     *
     * @param mainFrame the main window (never <code>null</code>).
     */
    public ExitAction(JFrame mainFrame) {
        assert mainFrame != null : "mainFrame must not be null";
        this.mainFrame = mainFrame;
        putValue(Action.NAME, MessageSource.getInstance().get(
                "exitAction.label"));
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "exitAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "exitAction.description"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                /*
                 * The listeners won't get notified by simply closing the frame.
				 * Therefore, we'll simply notify them manually. The controller
				 * should take care of closing the window/exiting the
				 * application.
				 */
                for (WindowListener l : mainFrame.getWindowListeners()) {
                    l.windowClosing(new WindowEvent(mainFrame,
                            WindowEvent.WINDOW_CLOSING));
                }
            }
        });
    }
}
