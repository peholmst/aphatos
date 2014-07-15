package net.pkhsolutions.aphatos.gui.components;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
import net.pkhsolutions.aphatos.domain.GlossaryManagerAdapter;
import net.pkhsolutions.aphatos.gui.actions.CopyAction;
import net.pkhsolutions.aphatos.gui.actions.CutAction;
import net.pkhsolutions.aphatos.gui.actions.PasteAction;
import net.pkhsolutions.aphatos.gui.actions.contextsensitive.PopupMenuAdapter;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.gui.preferences.FontPreferences;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * UI element for adding new words to the current {@link net.pkhsolutions.aphatos.domain.Glossary}. The
 * glossary is fetched from a {@link net.pkhsolutions.aphatos.domain.GlossaryManager} and words are added by
 * using the {@link net.pkhsolutions.aphatos.domain.Glossary#extractAndAdd(String)}-method.
 *
 * @author Petter Holmström
 */
class AddWordsPane extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 123L;

    private JTextArea textArea;

    private JButton addBtn;

    private GlossaryManager glossaryManager;

    /**
     * Creates a new <code>AddWordsPane</code>.
     *
     * @param glossaryManager the glossary manager to use (never <code>null</code>).
     */
    public AddWordsPane(GlossaryManager glossaryManager) {
        super();
        assert glossaryManager != null : "glossaryManager must not be null";
        this.glossaryManager = glossaryManager;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        initComponents();
    }

    private void updateTextFonts() {
        textArea.setFont(FontPreferences.getInstance().getTextFont());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateTextFonts();
    }

    /**
     * Gets the glossary manager that this UI element uses.
     *
     * @return the glossary manager (never <code>null</code>).
     */
    public GlossaryManager getGlossaryManager() {
        return glossaryManager;
    }

    private void initComponents() {
        FormLayout layout = new FormLayout("fill:p:grow",
                "p, 3dlu, fill:pref:grow, 3dlu, p");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        addBtn = new JButton(MessageSource.getInstance()
                .get("wordsPane.addBtn"), Icons.ADD_ICON);
        addBtn.setToolTipText(MessageSource.getInstance().get(
                "wordsPane.addBtn.hint"));

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateEnablementState();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateEnablementState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateEnablementState();
            }
        });

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        glossaryManager.getCurrentGlossary().extractAndAdd(
                                textArea.getText());
                        textArea.setText("");
                        textArea.requestFocus();
                    }
                });
            }
        });

        JScrollPane textScroller = new JScrollPane(textArea);
        textScroller.setPreferredSize(new Dimension(100, 50));

        builder.addLabel(MessageSource.getInstance().get("wordsPane.title"), cc
                .xy(1, 1));
        builder.add(textScroller, cc.xy(1, 3));
        builder.add(addBtn, cc.xy(1, 5, "right,bottom"));

        add(builder.getPanel());

        glossaryManager.addListener(new GlossaryManagerAdapter() {
            @Override
            public void currentGlossaryChange(GlossaryManager sender,
                                              Glossary currentGlossary) {
                updateEnablementState();
            }
        });

        updateEnablementState();

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new CutAction());
        popupMenu.add(new CopyAction());
        popupMenu.add(new PasteAction());
        popupMenu.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                textArea.requestFocus();
            }
        });

        textArea.setComponentPopupMenu(popupMenu);
        updateTextFonts();
    }

    private void updateEnablementState() {
        textArea.setEnabled(glossaryManager.getCurrentGlossary() != null);
        addBtn
                .setEnabled(textArea.isEnabled()
                        && !textArea.getText().isEmpty());
    }

}
