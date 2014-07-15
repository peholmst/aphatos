package net.pkhsolutions.aphatos.gui.components;

import net.pkhsolutions.aphatos.gui.controller.SystemUI;

/**
 * Implementation of {@link net.pkhsolutions.aphatos.gui.controller.SystemUI}.
 *
 * @author Petter Holmström
 */
public class DefaultSystemUI implements SystemUI {

    @Override
    public void exitApplication() {
        System.exit(0);
    }

}
