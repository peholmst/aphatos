package net.pkhsolutions.aphatos.gui.components;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
import net.pkhsolutions.aphatos.domain.utils.GlossaryManagerPersister;
import net.pkhsolutions.aphatos.gui.actions.*;
import net.pkhsolutions.aphatos.gui.controller.GlossaryController;
import net.pkhsolutions.aphatos.gui.controller.GlossaryFileUI;
import net.pkhsolutions.aphatos.gui.controller.GlossaryTabsUI;
import net.pkhsolutions.aphatos.gui.controller.SystemUI;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.gui.preferences.FontPreferences;
import net.pkhsolutions.aphatos.i18n.MessageSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

public class MainFrame extends JFrame {

    // FIXME Complete MainFrame (document, etc.)

    private static final long serialVersionUID = 123L;

    private final Log logger = LogFactory.getLog(getClass());

    private final GlossaryManager glossaryManager;
    private final Preferences prefs;
    private GlossaryFileUI glossaryFileUI;
    private GlossaryTabsUI glossaryTabsUI;
    private SystemUI systemUI;
    private JTabbedPane glossaryTabs;
    private JSplitPane sp;
    private Action exitAction;
    private Action newGlossaryAction;
    private Action openGlossaryAction;
    private Action saveGlossaryAction;
    private Action saveGlossaryAsAction;
    private Action closeGlossaryAction;
    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private Action deleteAction;
    private Action aboutAction;
    private Action changeFontSizeAction;

    public MainFrame() {
        super();
        prefs = Preferences.userNodeForPackage(getClass()).node("main-frame");
        glossaryManager = new GlossaryManager();
        GlossaryManagerPersister glossaryManagerPersister = new GlossaryManagerPersister();
        glossaryManager.addListener(glossaryManagerPersister);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle(MessageSource.getInstance().get("mainFrame.title"));

        initializeComponents(); // Creates the UI instances
        initializeActions();
        initializeMenu();
        initializeToolbar();

        // Create controller and register it
        GlossaryController glossaryController = new GlossaryController(glossaryManager,
                glossaryFileUI, glossaryTabsUI, systemUI);
        glossaryManager.addListener(glossaryController);
        addWindowListener(glossaryController);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (logger.isDebugEnabled())
                    logger.debug("Saving window size and location preferences");
                prefs.putInt("height", getHeight());
                prefs.putInt("width", getWidth());
                prefs.putInt("x", getLocation().x);
                prefs.putInt("y", getLocation().y);
                prefs.putInt("sp", sp.getDividerLocation());
                prefs.putInt("sp_last", sp.getLastDividerLocation());
            }
        });

        // Load glossaries
        glossaryManagerPersister.loadGlossaries(glossaryManager);

        // Open empty glossary if no glossaries have been loaded
        if (glossaryManager.getGlossaries().isEmpty())
            newGlossaryAction.actionPerformed(null);

        // CHECKME AddWordsPane should have focus

        if (logger.isDebugEnabled())
            logger.debug("Loading window size and location preferences");

        setSize(prefs.getInt("width", 400), prefs.getInt("height", 400));
        setLocation(prefs.getInt("x", 0), prefs.getInt("y", 0));
        sp.setDividerLocation(prefs.getInt("sp", 0));
        sp.setLastDividerLocation(prefs.getInt("sp_last", 0));
    }

    private void initializeActions() {
        newGlossaryAction = new NewGlossaryAction(glossaryManager);
        openGlossaryAction = new OpenGlossaryAction(glossaryManager,
                glossaryFileUI);
        closeGlossaryAction = new CloseGlossaryAction(glossaryManager);
        saveGlossaryAsAction = new SaveGlossaryAsAction(glossaryManager,
                glossaryFileUI);
        saveGlossaryAction = new SaveGlossaryAction(glossaryManager,
                glossaryFileUI);
        exitAction = new ExitAction(this);
        aboutAction = new AboutAction(this);
        cutAction = new CutAction();
        copyAction = new CopyAction();
        pasteAction = new PasteAction();
        deleteAction = new DeleteAction();
        changeFontSizeAction = new ChangeFontSizeAction(this);
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        // UI:
        menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

        JMenu fileMenu = new JMenu(MessageSource.getInstance().get(
                "mainFrame.fileMenu.label"));
        fileMenu.add(newGlossaryAction);
        fileMenu.add(openGlossaryAction);
        fileMenu.add(closeGlossaryAction);
        fileMenu.addSeparator();
        fileMenu.add(saveGlossaryAction);
        fileMenu.add(saveGlossaryAsAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu(MessageSource.getInstance().get(
                "mainFrame.editMenu.label"));
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.addSeparator();
        editMenu.add(deleteAction);
        editMenu.addSeparator();
        editMenu.add(changeFontSizeAction);
        menuBar.add(editMenu);

        JMenu helpMenu = new JMenu(MessageSource.getInstance().get(
                "mainFrame.helpMenu.label"));
        // CHECKME Add Help Contents action to menu (and write manual)
        helpMenu.add(aboutAction);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void initializeToolbar() {
        JToolBar toolbar = new JToolBar();
        // UI:
        toolbar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        toolbar.setRollover(true);

        toolbar.add(newGlossaryAction);
        toolbar.add(openGlossaryAction);
        toolbar.add(saveGlossaryAction);
        toolbar.add(saveGlossaryAsAction);
        toolbar.addSeparator();
        toolbar.add(cutAction);
        toolbar.add(copyAction);
        toolbar.add(pasteAction);
        toolbar.addSeparator();
        toolbar.add(deleteAction);

        add(toolbar, BorderLayout.NORTH);
    }

    private void initializeComponents() {
        AddWordsPane addWordsPane = new AddWordsPane(glossaryManager);
        FontPreferences.getInstance().addPropertyChangeListener(addWordsPane);
        ConstructSentencePane constructSentencePane = new ConstructSentencePane(glossaryManager);
        FontPreferences.getInstance().addPropertyChangeListener(constructSentencePane);

        glossaryTabs = new JTabbedPane();
        glossaryTabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                GlossaryPane gp = (GlossaryPane) glossaryTabs
                        .getSelectedComponent();
                if (gp != null)
                    glossaryManager.setCurrentGlossary(gp.getGlossary());
            }
        });

        JTabbedPane editorTabs = new JTabbedPane();
        editorTabs.addTab(MessageSource.getInstance().get(
                        "mainFrame.addWordsTab.label"), Icons.ADD_WORDS_ICON,
                addWordsPane);
        editorTabs.addTab(MessageSource.getInstance().get(
                        "mainFrame.constructSentenceTab.label"), Icons.SENTENCES_ICON,
                constructSentencePane);

        sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorTabs,
                glossaryTabs);
        sp.setOneTouchExpandable(true);
        sp.setBorder(null); // We don't want any borders around the components

        add(sp);

        glossaryTabsUI = new DefaultGlossaryTabsUI(glossaryTabs);
        ((DefaultGlossaryTabsUI) glossaryTabsUI).setConstructSentencePane(constructSentencePane);
        glossaryFileUI = new DefaultGlossaryFileUI(this);
        systemUI = new DefaultSystemUI();
    }
}
