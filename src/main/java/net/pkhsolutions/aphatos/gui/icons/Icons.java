package net.pkhsolutions.aphatos.gui.icons;

import javax.swing.*;
import java.net.URL;

/**
 * Utility class that contains all icons used by this application.
 *
 * @author Petter Holmström
 */
public class Icons {

    // TODO Create icon for the application

    public static final Icon GLOSSARY_ICON = loadIcon("glossary.gif");
    public static final Icon ADD_ICON = loadIcon("add.gif");
    public static final Icon NEW_ICON = loadIcon("new.gif");
    public static final Icon OPEN_ICON = loadIcon("open.gif");
    public static final Icon SAVE_ICON = loadIcon("save.gif");
    public static final Icon SAVE_AS_ICON = loadIcon("saveas.gif");
    public static final Icon CUT_ICON = loadIcon("cut.gif");
    public static final Icon COPY_ICON = loadIcon("copy.gif");
    public static final Icon PASTE_ICON = loadIcon("paste.gif");
    public static final Icon DELETE_ICON = loadIcon("delete.gif");
    public static final Icon ADD_WORDS_ICON = loadIcon("addwords.gif");
    public static final Icon SENTENCES_ICON = loadIcon("sentences.gif");
    /**
     * The package containing all icons:
     * <code>{@value}</code>
     */
    private static final String ICON_PACKAGE = "/net/pkhsolutions/aphatos/gui/icons/";
    public static Icon CLOSE_ICON = loadIcon("close.gif");
    public static Icon HELP_ICON = loadIcon("help.gif");
    public static Icon SAVE_ALL_ICON = loadIcon("saveall.gif");

    /**
     * Loads the specified icon from <code>ICON_PACKAGE</code>.
     *
     * @param fileName the file name of the icon.
     * @return the icon.
     */
    private static Icon loadIcon(String fileName) {
        URL url = Icons.class.getResource(ICON_PACKAGE + fileName);
        return new ImageIcon(url);
    }
}
