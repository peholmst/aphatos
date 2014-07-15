package net.pkhsolutions.aphatos.gui.components;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.gui.actions.CopyAction;
import net.pkhsolutions.aphatos.gui.actions.DeleteAction;
import net.pkhsolutions.aphatos.gui.actions.contextsensitive.PopupMenuAdapter;
import net.pkhsolutions.aphatos.gui.preferences.FontPreferences;
import net.pkhsolutions.aphatos.i18n.MessageSource;
import net.pkhsolutions.aphatos.models.FilteredGlossaryListModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// FIXME Document GlossaryPane
class GlossaryPane extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 123L;

    protected final Log logger = LogFactory.getLog(getClass());
    private final FilteredGlossaryListModel listModel = new FilteredGlossaryListModel();
    private JList<String> glossaryList;
    private JTextField filter;

    private ConstructSentencePane constructSentencePane;

    public GlossaryPane(Glossary glossary) {
        this(glossary, null);
    }

    public GlossaryPane(Glossary glossary, ConstructSentencePane constructSentencePane) {
        super();
        assert glossary != null : "glossary must not be null";
        listModel.setGlossary(glossary);
        this.constructSentencePane = constructSentencePane;
        initComponents();
    }

    public Glossary getGlossary() {
        return listModel.getGlossary();
    }

    private void updateTextFonts() {
        glossaryList.setFont(FontPreferences.getInstance().getTextFont());
        filter.setFont(FontPreferences.getInstance().getTextFont());
        // TODO redraw layout
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateTextFonts();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        FormLayout layout = new FormLayout("p, 3dlu, fill:p:grow",
                "p, 3dlu, fill:p:grow");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        // CHECKME Add an option for using default size border or thin border
        builder.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        CellConstraints cc = new CellConstraints();

        glossaryList = new JList<>(listModel);
        glossaryList
                .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        glossaryList.setVisibleRowCount(-1);

        filter = new JTextField();
        filter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                listModel.setFilter(filter.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                listModel.setFilter(filter.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                listModel.setFilter(filter.getText());
            }
        });

        JScrollPane listScroller = new JScrollPane(glossaryList);

        builder.addLabel(MessageSource.getInstance().get(
                "glossaryPane.filterLbl"), cc.xy(1, 1));
        builder.add(filter, cc.xy(3, 1));

        builder.add(listScroller, cc.xyw(1, 3, 3));

        add(builder.getPanel());

        glossaryList.getActionMap().put("delete", new AbstractAction() {
            private static final long serialVersionUID = 6591909784674009603L;

            @Override
            public void actionPerformed(ActionEvent e) {
                for (Object word : glossaryList.getSelectedValuesList()) {
                    if (word != null) {
                        // CHECKME Change this code to use indexes instead.
                        listModel.getGlossary().delete(
                                listModel.getGlossary().indexOf((String) word));
                        glossaryList.clearSelection();
                    }
                }
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new CopyAction());
        popupMenu.add(new DeleteAction());
        popupMenu.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                glossaryList.requestFocus();
            }
        });

        glossaryList.setComponentPopupMenu(popupMenu);

        // Double-click copies the selected word (as requested by Client)
        glossaryList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (constructSentencePane == null) {
                        ActionEvent ae = new ActionEvent(glossaryList, 0, "copy");
                        glossaryList.getActionMap().get("copy").actionPerformed(ae);
                    } else {
                        if (glossaryList.getSelectedValue() != null) {
                            constructSentencePane.appendWord(glossaryList.getSelectedValue());
                        }
                    }
                }
            }
        });
        updateTextFonts();
        // TODO Add label with word count
    }
}
