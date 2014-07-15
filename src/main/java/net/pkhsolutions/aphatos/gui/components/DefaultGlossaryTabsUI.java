package net.pkhsolutions.aphatos.gui.components;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.PersistentGlossary;
import net.pkhsolutions.aphatos.domain.PersistentGlossaryAdapter;
import net.pkhsolutions.aphatos.gui.controller.GlossaryTabsUI;
import net.pkhsolutions.aphatos.gui.icons.Icons;
import net.pkhsolutions.aphatos.gui.preferences.FontPreferences;
import net.pkhsolutions.aphatos.i18n.MessageSource;

import javax.swing.*;

/**
 * Implementation of {@link GlossaryTabsUI}.
 *
 * @author Petter Holmström
 */
public class DefaultGlossaryTabsUI implements GlossaryTabsUI {

    private JTabbedPane glossaryTabs;

    private ConstructSentencePane constructSentencePane;

    /**
     * Creates a new <code>DefaultGlossaryTabsUI</code>.
     *
     * @param glossaryTabs the tabbed pane for glossary tabs.
     */
    public DefaultGlossaryTabsUI(JTabbedPane glossaryTabs) {
        assert glossaryTabs != null : "glossaryTabs must not be null";
        this.glossaryTabs = glossaryTabs;
    }

    public void setConstructSentencePane(ConstructSentencePane constructSentencePane) {
        this.constructSentencePane = constructSentencePane;
    }

    /**
     * Gets the tabbed pane containing glossary tabs.
     *
     * @return the tabbed pane.
     */
    JTabbedPane getGlossaryTabs() {
        return glossaryTabs;
    }

    private String constructTitle(Glossary glossary) {
        PersistentGlossary pga = (PersistentGlossary) glossary;
        String title = glossary.getName();
        if (title.isEmpty())
            title = MessageSource.getInstance().get("untitled");
        if (pga.isDirty())
            title = "*" + title;
        return title;
    }

    @Override
    public synchronized void addGlossaryTab(final Glossary glossary) {
        final GlossaryPane gp = new GlossaryPane(glossary, constructSentencePane);
        // Register pane with font preferences to allow it to update itself if the font is changed at runtime.
        FontPreferences.getInstance().addPropertyChangeListener(gp);

        getGlossaryTabs().add(constructTitle(glossary), gp);
        getGlossaryTabs().setIconAt(getGlossaryTabs().getTabCount() - 1,
                Icons.GLOSSARY_ICON);
        // TODO Add close buttons to glossary tabs
        glossary.addGlossaryListener(new PersistentGlossaryAdapter() {
            // We can't use sender, as it will point to the
            // backend glossary while we need the FileGlossary wrapper

            private void updateTitle() {
                getGlossaryTabs().setTitleAt(
                        getGlossaryTabs().indexOfComponent(gp),
                        constructTitle(glossary));
            }

            @Override
            public void glossarySaved(PersistentGlossary sender) {
                updateTitle();
            }

            @Override
            public void glossaryLoaded(PersistentGlossary sender) {
                updateTitle();
            }

            @Override
            public void wordAdded(Glossary sender, String word, int position) {
                updateTitle();
            }

            @Override
            public void wordDeleted(Glossary sender, String word, int position) {
                updateTitle();
            }

            @Override
            public void glossaryRefreshed(Glossary sender) {
                updateTitle();
            }
        });
    }

    @Override
    public synchronized void removeGlossaryTab(Glossary glossary) {
        for (int i = 0; i < getGlossaryTabs().getTabCount(); i++) {
            GlossaryPane gp = (GlossaryPane) getGlossaryTabs()
                    .getComponentAt(i);
            if (gp.getGlossary() == glossary) {
                getGlossaryTabs().removeTabAt(i);
                // And remove listener from font preferences
                FontPreferences.getInstance().removePropertyChangeListener(gp);
                return;
            }
        }
    }

    @Override
    public synchronized void selectGlossaryTab(Glossary glossary) {
        if (glossary != null) {
            for (int i = 0; i < getGlossaryTabs().getTabCount(); i++) {
                GlossaryPane gp = (GlossaryPane) getGlossaryTabs()
                        .getComponentAt(i);
                if (gp.getGlossary() == glossary) {
                    getGlossaryTabs().setSelectedIndex(i);
                    return;
                }
            }
        }
    }

}
