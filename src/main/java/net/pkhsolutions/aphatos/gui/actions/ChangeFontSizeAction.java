package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.gui.preferences.FontPreferences;
import net.pkhsolutions.aphatos.i18n.MessageSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * UI action that allows the user to change the size of the font in the text
 * fields.
 *
 * @author Petter Holmström
 */
public class ChangeFontSizeAction extends AbstractAction {

    // TODO Document me!

    private static final long serialVersionUID = 2524725934592007498L;
    private final Log logger = LogFactory.getLog(getClass());
    private JFrame mainFrame;

    public ChangeFontSizeAction(JFrame mainFrame) {
        assert mainFrame != null : "mainFrame must not be null";
        this.mainFrame = mainFrame;
        putValue(Action.NAME, MessageSource.getInstance().get(
                "changeFontSizeAction.label"));
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "changeFontSizeAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "changeFontSizeAction.description"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // CHECKME Replace with nicer looking dialog that allows the user to
        // change the font family and style as well.
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String result = JOptionPane.showInputDialog(mainFrame,
                        MessageSource.getInstance().get(
                                "changeFontSize.message"), FontPreferences
                                .getInstance().getTextFont().getSize());

                if (result != null) {
                    try {
                        Integer fontSize = Integer.valueOf(result);
                        Font font = FontPreferences.getInstance().getTextFont().deriveFont((float) fontSize);
                        FontPreferences.getInstance().setTextFont(font);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(mainFrame, String.format(
                                        MessageSource.getInstance().get(
                                                "changeFontSize.invalidFontSize"),
                                        result), MessageSource.getInstance().get(
                                        "changeFontSize.invalidFontSizeTitle"),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        try {
            if (SwingUtilities.isEventDispatchThread())
                task.run();
            else
                SwingUtilities.invokeAndWait(task);
        } catch (Exception ex) {
            logger.error("Swing error", ex);
        }
    }
}
