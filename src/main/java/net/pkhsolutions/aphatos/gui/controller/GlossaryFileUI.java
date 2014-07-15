package net.pkhsolutions.aphatos.gui.controller;

import net.pkhsolutions.aphatos.domain.PersistentGlossary;

/**
 * Controller interface to the user interface that deals with I/O operations.
 * The methods in this class may be called from any thread, UI operations will
 * always be carried out in the proper Swing thread.
 *
 * @author Petter Holmström
 */
public interface GlossaryFileUI {

    /**
     * Displays an error message to the user.
     *
     * @param title     the title of the error dialog.
     * @param message   the error message to display.
     * @param exception the exception that caused the error.
     */
    public void showError(String title, String message, Exception exception);

    /**
     * Asks the user whether the changes to <code>glossary</code> should be
     * saved. If the user chooses 'Yes', the changes are saved and
     * <code>true</code> is returned. If the choice is 'No', no changes are
     * saved but <code>true</code> is still returned. If the user chooses to
     * cancel the entire operation, no changes are saved and <code>false</code>
     * are returned.
     *
     * @param glossary the glossary in question.
     * @return <code>true</code> if the operation is allowed to continue,
     * <code>false</code> otherwise.
     */
    public boolean confirmUnsavedChanges(PersistentGlossary glossary);

    /**
     * Saves <code>glossary</code>. If no file has been specified, the user
     * is prompted for a file name. Exceptions are handled and displayed to the
     * user when appropriate.
     *
     * @param glossary the glossary to save.
     * @return the saved glossary on success, <code>null</code> on failure.
     */
    public PersistentGlossary saveGlossary(PersistentGlossary glossary);

    /**
     * Opens file chooser dialog and lets the user select a file name, then
     * saves <code>glossary</code> to the file. Exceptions are handled and
     * displayed to the user when appropriate.
     *
     * @param glossary the glossary to save.
     * @return the saved glossary on success, <code>null</code> on failure.
     */
    public PersistentGlossary saveGlossaryAs(PersistentGlossary glossary);

    /**
     * Opens a file chooser dialog and lets the user select the file to load,
     * then loads <code>glossary</code> from the file. Exceptions are handled
     * and displayed to the user when appropriate.
     *
     * @param glossary the glossary to load.
     * @return the loaded glossary on success, <code>null</code> on failure.
     */
    public PersistentGlossary loadGlossary(PersistentGlossary glossary);

}