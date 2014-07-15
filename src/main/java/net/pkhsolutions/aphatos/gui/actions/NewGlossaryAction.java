package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryFactory;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
import net.pkhsolutions.aphatos.domain.PersistentGlossary;
import net.pkhsolutions.aphatos.domain.utils.FileGlossary;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * UI action for creating a new {@link FileGlossary} and adding it to a
 * {@link GlossaryManager}. The actual glossary instance is created by calling
 * {@link net.pkhsolutions.aphatos.domain.GlossaryFactory#createGlossary(Class)}.
 *
 * @author Petter Holmström
 */
public class NewGlossaryAction extends AbstractGlossaryManagerAction {

    private static final long serialVersionUID = 123L;

    /**
     * Creates a new <code>NewGlossaryAction</code>.
     *
     * @param glossaryManager the glossary manager to use (never <code>null</code>).
     */
    public NewGlossaryAction(GlossaryManager glossaryManager) {
        super(glossaryManager);
        putValue(Action.NAME, MessageSource.getInstance().get(
                "newGlossaryAction.label"));
        putValue(Action.SMALL_ICON, Icons.NEW_ICON);
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "newGlossaryAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "newGlossaryAction.description"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Glossary glossary = new FileGlossary(GlossaryFactory
                        .createGlossary(PersistentGlossary.class));
                if (getGlossaryManager().addGlossary(glossary))
                    getGlossaryManager().setCurrentGlossary(glossary);
            }
        });
    }
}
