package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
import net.pkhsolutions.aphatos.domain.GlossaryManagerAdapter;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * User interface action that removes the current glossary from its glossary
 * manager.
 *
 * @author Petter Holmström
 * @see GlossaryManager
 */
public class CloseGlossaryAction extends AbstractGlossaryManagerAction {

    private static final long serialVersionUID = 123L;

    /**
     * Creates a new <code>CloseGlossaryAction</code>.
     *
     * @param glossaryManager the glossary manager to work on.
     */
    public CloseGlossaryAction(GlossaryManager glossaryManager) {
        super(glossaryManager);
        putValue(Action.NAME, MessageSource.getInstance().get(
                "closeGlossaryAction.label"));
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "closeGlossaryAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "closeGlossaryAction.description"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        setEnabled(getGlossaryManager().getCurrentGlossary() != null);
        getGlossaryManager().addListener(new GlossaryManagerAdapter() {
            @Override
            public void currentGlossaryChange(GlossaryManager sender,
                                              Glossary currentGlossary) {
                setEnabled(currentGlossary != null);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                assert getGlossaryManager().getCurrentGlossary() != null : "current glossary should not be null when this action is perfomed";
                getGlossaryManager().removeGlossary(
                        getGlossaryManager().getCurrentGlossary());
            }
        });
    }
}
