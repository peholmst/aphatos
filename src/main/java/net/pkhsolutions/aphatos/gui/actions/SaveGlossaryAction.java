package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
import net.pkhsolutions.aphatos.domain.GlossaryManagerAdapter;
import net.pkhsolutions.aphatos.domain.PersistentGlossary;
import net.pkhsolutions.aphatos.domain.utils.FileGlossary;
import net.pkhsolutions.aphatos.gui.controller.GlossaryFileUI;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * User interface action for saving the current glossary to a file, using
 * {@link net.pkhsolutions.aphatos.gui.controller.GlossaryFileUI#saveGlossary(net.pkhsolutions.aphatos.domain.PersistentGlossary)}. The glossary is
 * expected to be a {@link FileGlossary}.
 *
 * @author Petter Holmström
 * @see GlossaryManager
 * @see net.pkhsolutions.aphatos.gui.controller.GlossaryFileUI
 */
public class SaveGlossaryAction extends AbstractGlossaryManagerAction {

    private static final long serialVersionUID = 123L;

    private GlossaryFileUI gui;

    /**
     * Creates a new <code>SaveGlossaryAction</code>.
     *
     * @param glossaryManager the glossary manager to work on.
     * @param gui             the glossary file GUI to use.
     */
    public SaveGlossaryAction(GlossaryManager glossaryManager,
                              GlossaryFileUI gui) {
        super(glossaryManager);
        assert gui != null : "gui must not be null";
        this.gui = gui;

        putValue(Action.NAME, MessageSource.getInstance().get(
                "saveGlossaryAction.label"));
        putValue(Action.SMALL_ICON, Icons.SAVE_ICON);
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "saveGlossaryAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "saveGlossaryAction.description"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
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
        PersistentGlossary pg = (PersistentGlossary) getGlossaryManager()
                .getCurrentGlossary();
        assert pg != null : "current glossary should not be null when this action is performed";
        assert pg instanceof FileGlossary : "found non-FileGlossary in glossary manager";
        gui.saveGlossary(pg);
    }
}
