package net.pkhsolutions.aphatos.gui.controller;

/**
 * Controller interface for different system operations. The methods in this
 * class may be called from any thread, UI operations will always be carried out
 * in the proper Swing thread.
 *
 * @author Petter Holmström
 */
public interface SystemUI {

    /**
     * Exist the application.
     */
    public void exitApplication();

}
