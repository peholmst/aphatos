package net.pkhsolutions.aphatos.gui.components;

import net.pkhsolutions.aphatos.domain.PersistentGlossary;
import net.pkhsolutions.aphatos.domain.utils.FileGlossary;
import net.pkhsolutions.aphatos.gui.controller.GlossaryFileUI;
import net.pkhsolutions.aphatos.i18n.MessageSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Implementation of {@link GlossaryFileUI} that provides some common
 * {@link FileGlossary}-operations that require user interaction.
 *
 * @author Petter Holmström
 */
public class DefaultGlossaryFileUI implements GlossaryFileUI {

    private final Log logger = LogFactory.getLog(getClass());

    private JFrame parent;

    /**
     * Creates a new <code>DefaultGlossaryFileUI</code>.
     *
     * @param parentFrame the parent frame of all UI dialogs shown by this UI.
     */
    public DefaultGlossaryFileUI(JFrame parentFrame) {
        assert parentFrame != null : "parentFrame must not be null";
        this.parent = parentFrame;
    }

    @Override
    public void showError(String title, String message, Exception exception) {
        JOptionPane.showMessageDialog(parent, message + exception.getMessage(),
                title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Saves the Checks whether <code>glossary</code> is an instance of
     * {@link FileGlossary} and whether a file has been specified for it. If so,
     * the glossary is saved to the specified file. If not,
     * {@link #saveGlossaryAs(PersistentGlossary)} is called. Exceptions are
     * handled and displayed to the user when appropriate.
     *
     * @param glossary the glossary to save.
     * @return the saved glossary on success, <code>null</code> on failure.
     * @see GlossaryFileUI#saveGlossary(PersistentGlossary)
     */
    @Override
    public synchronized PersistentGlossary saveGlossary(
            final PersistentGlossary glossary) {
        assert glossary != null : "glossary must not be null";
        if (glossary instanceof FileGlossary
                && ((FileGlossary) glossary).getFile() != null) {
            final PersistentGlossary[] result = new PersistentGlossary[1];
            result[0] = null;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        ((FileGlossary) glossary).saveToFile();
                        result[0] = glossary;
                    } catch (Exception ex) {
                        logger.error("Error saving glossary", ex);
                        showError(MessageSource.getInstance().get(
                                "io.saveError.title"), MessageSource
                                .getInstance().get("io.loadError.msg"), ex);
                    }
                }
            };
            try {
                if (SwingUtilities.isEventDispatchThread())
                    task.run();
                else
                    SwingUtilities.invokeAndWait(task);
            } catch (Exception e) {
                logger.error("Swing error", e);
                //e.printStackTrace();
            }
            return result[0];
        } else
            return saveGlossaryAs(glossary);
    }

    /**
     * Opens a file chooser dialog and lets the user select the file to save to.
     * If <code>glossary</code> is an instance of {@link FileGlossary}, it
     * will be saved to the specified file. If not, a new
     * <code>FileGlossary</code> is created around <code>glossary</code> and
     * saved. On success, the <code>FileGlossary</code> instance is returned;
     * on failure <code>null</code>. Exceptions are handled and displayed to
     * the user when appropriate.
     *
     * @param glossary the glossary to save.
     * @return the saved glossary on success, <code>null</code> on failure.
     * @see GlossaryFileUI#saveGlossaryAs(PersistentGlossary)
     */
    @Override
    public synchronized PersistentGlossary saveGlossaryAs(
            final PersistentGlossary glossary) {
        assert glossary != null : "glossary must not be null";
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(MessageSource.getInstance().get(
                "saveGlossaryAsAction.dialog.title"));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileNameExtensionFilter(MessageSource
                .getInstance().get("io.filter"), "txt"));

        final PersistentGlossary[] result = new PersistentGlossary[1];
        result[0] = null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileGlossary fg;
                        if (glossary instanceof FileGlossary)
                            fg = (FileGlossary) glossary;
                        else
                            fg = new FileGlossary(glossary);

                        File file = fc.getSelectedFile();
                        if (!file.getName().toLowerCase().endsWith(".txt")) {
                            file = new File(file.getAbsolutePath() + ".txt");
                        }

                        if (file.exists()) {
                            if (JOptionPane.showConfirmDialog(parent,
                                    MessageSource.getInstance().get(
                                            "confirmOverwrite.message"),
                                    MessageSource.getInstance().get(
                                            "confirmOverwrite.title"),
                                    JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                                return; // Do nothing
                            }
                        }
                        fg.setFile(file);
                        fg.saveToFile();
                        result[0] = fg;
                    } catch (Exception ex) {
                        logger.error("Error loading glossary", ex);
                        showError(MessageSource.getInstance().get(
                                "io.saveError.title"), MessageSource
                                .getInstance().get("io.loadError.msg"), ex);
                    }
                }
            }
        };
        try {
            if (SwingUtilities.isEventDispatchThread())
                task.run();
            else
                SwingUtilities.invokeAndWait(task);
        } catch (Exception e) {
            logger.error("Swing error", e);
            //e.printStackTrace();
        }
        return result[0];
    }

    /**
     * Opens a file chooser dialog and lets the user select the file to load. If
     * <code>glossary</code> is an instance of {@link FileGlossary}, it will
     * be loaded with the specified file. If not, a new
     * <code>FileGlossary</code> is created around <code>glossary</code> and
     * loaded. On success, the <code>FileGlossary</code> instance is returned;
     * on failure <code>null</code>. Exceptions are handled and displayed to
     * the user when appropriate.
     *
     * @param glossary the glossary to load.
     * @return the loaded glossary on success, <code>null</code> on failure.
     * @see GlossaryFileUI#loadGlossary(PersistentGlossary)
     */
    @Override
    public synchronized PersistentGlossary loadGlossary(
            final PersistentGlossary glossary) {
        assert glossary != null : "glossary must not be null";
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(MessageSource.getInstance().get(
                "openGlossaryAction.dialog.title"));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileNameExtensionFilter(MessageSource
                .getInstance().get("io.filter"), "txt"));

        final PersistentGlossary[] result = new PersistentGlossary[1];
        result[0] = null;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileGlossary fg;
                        if (glossary instanceof FileGlossary)
                            fg = (FileGlossary) glossary;
                        else
                            fg = new FileGlossary(glossary);
                        fg.setFile(fc.getSelectedFile());
                        fg.loadFromFile();
                        result[0] = fg;
                    } catch (Exception ex) {
                        showError(MessageSource.getInstance().get(
                                "io.loadError.title"), MessageSource
                                .getInstance().get("io.loadError.msg"), ex);
                    }
                }
            }
        };
        try {
            if (SwingUtilities.isEventDispatchThread())
                task.run();
            else
                SwingUtilities.invokeAndWait(task);
        } catch (Exception e) {
            logger.error("Swing error", e);
            //e.printStackTrace();
        }
        return result[0];
    }

    @Override
    public synchronized boolean confirmUnsavedChanges(
            final PersistentGlossary glossary) {
        if (glossary.isDirty()) {
            final boolean[] result = new boolean[1];
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    int choice = JOptionPane.showConfirmDialog(parent,
                            MessageSource.getInstance().get(
                                    "confirmUnsaved.message"), MessageSource
                                    .getInstance().get("confirmUnsaved.title"),
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (choice) {
                        case JOptionPane.CANCEL_OPTION:
                            result[0] = false;
                            break;
                        case JOptionPane.NO_OPTION:
                            result[0] = true;
                            break;
                        case JOptionPane.YES_OPTION:
                            result[0] = (saveGlossary(glossary) != null);
                            break;
                    }
                }
            };
            try {
                if (SwingUtilities.isEventDispatchThread())
                    task.run();
                else
                    SwingUtilities.invokeAndWait(task);
            } catch (Exception e) {
                logger.error("Swing error", e);
                //e.printStackTrace();
            }
            return result[0];
        }
        return true;
    }

}
