package net.pkhsolutions.aphatos.gui.components;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
import net.pkhsolutions.aphatos.gui.actions.CopyAction;
import net.pkhsolutions.aphatos.gui.actions.CutAction;
import net.pkhsolutions.aphatos.gui.actions.PasteAction;
import net.pkhsolutions.aphatos.gui.actions.contextsensitive.PopupMenuAdapter;
import net.pkhsolutions.aphatos.gui.components.completion.WordCompletionController;
import net.pkhsolutions.aphatos.gui.components.completion.WordCompletionModel;
import net.pkhsolutions.aphatos.gui.components.completion.WordCompletionView;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.gui.preferences.FontPreferences;
import net.pkhsolutions.aphatos.i18n.MessageSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author petter
 */
class ConstructSentencePane extends JPanel implements PropertyChangeListener {
    // FIXME complete this class!

    private static final long serialVersionUID = 123L;
    protected final Log logger = LogFactory.getLog(getClass());
    private JTextArea textArea;

    private JButton copyBtn;

    private GlossaryManager glossaryManager;

    private JList<String> completionList;

    private JScrollPane completionListSP;

    /**
     * Creates a new <code>ConstructSentencePane</code>.
     *
     * @param glossaryManager the glossary manager to use (never <code>null</code>).
     */
    public ConstructSentencePane(GlossaryManager glossaryManager) {
        super();
        assert glossaryManager != null : "glossaryManager must not be null";
        this.glossaryManager = glossaryManager;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        initComponents();
    }

    private void updateTextFonts() {
        textArea.setFont(FontPreferences.getInstance().getTextFont());
        completionList.setFont(FontPreferences.getInstance().getTextFont());
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

        copyBtn = new JButton(MessageSource.getInstance().get(
                "sentencePane.copyBtn"), Icons.COPY_ICON);
        copyBtn.setToolTipText(MessageSource.getInstance().get(
                "sentencePane.copyBtn.hint"));

        completionList = new JList<>();
        completionListSP = new JScrollPane(completionList);
        completionListSP.setVisible(false);

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

        copyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        String text = textArea.getText();
                        StringSelection selection = new StringSelection(text);
                        Toolkit.getDefaultToolkit().getSystemClipboard()
                                .setContents(selection, null);
                    }
                });
            }
        });

        JScrollPane textScroller = new JScrollPane(textArea);
        textScroller.setPreferredSize(new Dimension(100, 50));

        builder.addLabel(MessageSource.getInstance().get("sentencePane.title"),
                cc.xy(1, 1));
        builder.add(textScroller, cc.xy(1, 3));
        builder.add(copyBtn, cc.xy(1, 5, "right,bottom"));

        add(builder.getPanel());

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

        // Completion
        WordCompletionModel completionModel = new WordCompletionModel();
        WordCompletionView completionView = new WordCompletionView(textArea, completionList,
                glossaryManager) {

            protected void updateCompletionBox(boolean show, int caretPos) {
                if (!show) {
                    completionListSP.setVisible(false);
                } else if (getRootPane() != null) {
                    if (completionListSP.getParent() != getRootPane()
                            .getLayeredPane()) {
                        if (logger.isDebugEnabled())
                            logger
                                    .debug("Adding completion list to layered pane");
                        getRootPane().getLayeredPane().add(completionListSP,
                                JLayeredPane.POPUP_LAYER);
                    }

                    if (logger.isDebugEnabled())
                        logger.debug("Caret position: " + caretPos);
                    try {
                        Point caretLocation = textArea.modelToView(caretPos)
                                .getLocation();

                        Point completionLocation = getLocationInRootPane(textArea);

                        completionListSP.setSize(200, 100);

                        completionLocation.x += caretLocation.x;
                        completionLocation.y += caretLocation.y + completionList.getFont().getSize() + 5;//15;

                        if (completionLocation.x + 200 > getRootPane()
                                .getWidth())
                            completionLocation.x = getRootPane().getWidth() - 200;

                        if (completionLocation.y + 100 > getRootPane()
                                .getHeight())
                            completionLocation.y = completionLocation.y - 115;

                        completionListSP.setLocation(completionLocation);
                        completionListSP.setVisible(show);
                    } catch (BadLocationException ex) {
                        logger.error("Error getting caret location", ex);
                        completionListSP.setVisible(false);
                    }
                } else if (logger.isWarnEnabled())
                    logger.warn("No root pane available");
            }

            @Override
            public boolean isCompletionListVisible() {
                return completionListSP.isVisible();
            }
        };
        new WordCompletionController(completionModel, completionView);

        updateTextFonts();
    }

    private void updateEnablementState() {
        copyBtn.setEnabled(textArea.isEnabled()
                && !textArea.getText().isEmpty());
    }

    public void appendWord(String word) {
        if (textArea.getText().length() > 0 && !textArea.getText().endsWith(" "))
            textArea.append(" ");
        textArea.append(word);
    }

    private Point getLocationInRootPane(Component component) {
        Point p = component.getLocation();
        while (component.getParent() != getRootPane()) {
            component = component.getParent();
            p.x += component.getLocation().x;
            p.y += component.getLocation().y;
        }
        return p;
    }
}
