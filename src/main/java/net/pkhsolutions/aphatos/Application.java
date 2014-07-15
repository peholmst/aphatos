package net.pkhsolutions.aphatos;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import net.pkhsolutions.aphatos.gui.components.MainFrame;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Main application class that starts the program.
 *
 * @author Petter Holmström
 */
class Application {

    public static void main(String[] args) {
        System.out.println(String.format("AphaGlossary Version %s",
                AppVersion.getVersion()));
        System.out
                .println("This program comes with ABSOLUTELY NO WARRANTY; for details see the about box.\n"
                        + "This is free software, and you are welcome to redistribute it; for details see the about box\n"
                        + "under certain conditions");

        // Configure UI
        JFrame.setDefaultLookAndFeelDecorated(false);
        PlasticLookAndFeel.setPlasticTheme(new ExperienceBlue());
        PlasticLookAndFeel
                .setTabStyle(PlasticLookAndFeel.TAB_STYLE_METAL_VALUE);
        try {
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (Exception e) {
            LogFactory.getLog(Application.class).debug("An exception occurred while setting the look and feel", e);
        }

        // Show main form
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
