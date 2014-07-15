package net.pkhsolutions.aphatos.gui.preferences;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

/**
 * TODO Document and test me!
 *
 * @author Petter Holmström
 */
public final class FontPreferences extends AbstractPreferences {

    private static final String FONT_PREFERENCES = "font-preferences";
    private final Preferences prefs = Preferences.userNodeForPackage(getClass()).node(FONT_PREFERENCES);
    private static final String TEXT_FONT_SIZE = "text-font-size";
    private static final FontPreferences instance = new FontPreferences();
    private Font textFont;

    public static FontPreferences getInstance() {
        return instance;
    }

    public Font getTextFont() {
        if (textFont == null) {
            Font defFont = UIManager.getFont("TextArea.font");
            int fontSize = prefs.getInt(TEXT_FONT_SIZE, defFont.getSize());
            textFont = defFont.deriveFont((float) fontSize);
        }

        return textFont;
    }

    public void setTextFont(Font font) {
        assert font != null : "font must not be null";
        Font old = this.textFont;
        this.textFont = font;
        prefs.putInt(TEXT_FONT_SIZE, font.getSize());
        changeSupport.firePropertyChange("textFont", old, textFont);
    }

} 
