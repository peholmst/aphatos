package net.pkhsolutions.aphatos.gui.actions;

import net.pkhsolutions.aphatos.domain.GlossaryFactory;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
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
 * User interface action for loading a glossary from an existing file. This
 * action will create a new {@link net.pkhsolutions.aphatos.domain.PersistentGlossary}-instance using
 * {@link net.pkhsolutions.aphatos.domain.GlossaryFactory}, wrap it with a {@link FileGlossary}, load it using
 * {@link net.pkhsolutions.aphatos.gui.controller.GlossaryFileUI#loadGlossary(net.pkhsolutions.aphatos.domain.PersistentGlossary)}, add it to the
 * glossary manager and make it the current glossary.
 *
 * @author Petter Holmström
 * @see net.pkhsolutions.aphatos.domain.GlossaryManager
 * @see net.pkhsolutions.aphatos.gui.controller.GlossaryFileUI
 */
public class OpenGlossaryAction extends AbstractGlossaryManagerAction {

    private static final long serialVersionUID = 123L;

    private GlossaryFileUI gui;

    /**
     * Creates a new <code>OpenGlossaryAction</code>.
     *
     * @param glossaryManager the glossary manager to work on.
     * @param gui             the glossary file GUI to use.
     */
    public OpenGlossaryAction(GlossaryManager glossaryManager,
                              GlossaryFileUI gui) {
        super(glossaryManager);
        assert gui != null : "gui must not be null";
        this.gui = gui;

        putValue(Action.NAME, MessageSource.getInstance().get(
                "openGlossaryAction.label"));
        putValue(Action.SMALL_ICON, Icons.OPEN_ICON);
        putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, Integer
                .parseInt(MessageSource.getInstance().get(
                        "openGlossaryAction.mnemonicIndex")));
        putValue(Action.SHORT_DESCRIPTION, MessageSource.getInstance().get(
                "openGlossaryAction.description"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PersistentGlossary fg = new FileGlossary(GlossaryFactory
                .createGlossary(PersistentGlossary.class));
        if (gui.loadGlossary(fg) != null
                && getGlossaryManager().addGlossary(fg))
            getGlossaryManager().setCurrentGlossary(fg);
    }
}
