package net.pkhsolutions.aphatos.gui.controller;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
import net.pkhsolutions.aphatos.domain.PersistentGlossary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Test-case for {@link net.pkhsolutions.aphatos.gui.controller.GlossaryController}.
 *
 * @author Petter Holmström
 */
public class GlossaryControllerTest {

    private GlossaryController controller;

    private GlossaryFileUI fileUI;

    private GlossaryTabsUI tabsUI;

    private SystemUI systemUI;

    private GlossaryManager manager;

    private Glossary glossary;

    private PersistentGlossary pGlossary;

    @Before
    public void initialize() {
        fileUI = createMock(GlossaryFileUI.class);
        tabsUI = createMock(GlossaryTabsUI.class);
        systemUI = createMock(SystemUI.class);
        glossary = createMock(Glossary.class);
        pGlossary = createMock(PersistentGlossary.class);
        manager = new GlossaryManager();
        controller = new GlossaryController(manager, fileUI, tabsUI, systemUI);
    }

    @After
    public void verifyMocks() {
        try {
            verify(fileUI);
        } catch (IllegalStateException e) {
            // OK.
        }
        try {
            verify(tabsUI);
        } catch (IllegalStateException e) {
            // OK.
        }
        try {
            verify(systemUI);
        } catch (IllegalStateException e) {
            // OK.
        }
        try {
            verify(glossary);
        } catch (IllegalStateException e) {
            // OK.
        }
    }

    @Test
    public void getters() {
        replay(fileUI);
        replay(tabsUI);
        replay(systemUI);

        assertSame(fileUI, controller.getGlossaryFileUI());
        assertSame(tabsUI, controller.getGlossaryTabsUI());
        assertSame(systemUI, controller.getSystemUI());
        assertSame(manager, controller.getGlossaryManager());
    }

    @Test
    public void addingGlossary() {
        replay(fileUI);
        replay(tabsUI);
        replay(systemUI);
        replay(glossary);

        assertTrue(controller.addingGlossary(manager, glossary));
    }

    @Test
    public void currentGlossaryChange() {
        tabsUI.selectGlossaryTab(glossary);
        replay(tabsUI);
        replay(fileUI);
        replay(systemUI);
        replay(glossary);

        controller.currentGlossaryChange(manager, glossary);
    }

    @Test
    public void glossaryAdded() {
        tabsUI.addGlossaryTab(glossary);
        replay(tabsUI);
        replay(fileUI);
        replay(systemUI);
        replay(glossary);

        controller.glossaryAdded(manager, glossary);
    }

    @Test
    public void glossaryRemoved() {
        tabsUI.removeGlossaryTab(glossary);
        replay(tabsUI);
        replay(fileUI);
        replay(systemUI);
        replay(glossary);

        controller.glossaryRemoved(manager, glossary);
    }

    @Test
    public void removingGlossary() {
        replay(fileUI);
        replay(tabsUI);
        replay(systemUI);
        replay(glossary);

        assertTrue(controller.removingGlossary(manager, glossary));
    }

    @Test
    public void removingDirtyPersistentGlossary() {
        expect(pGlossary.isDirty()).andReturn(true).times(2);
        replay(pGlossary);
        expect(fileUI.confirmUnsavedChanges(pGlossary)).andReturn(true);
        expect(fileUI.confirmUnsavedChanges(pGlossary)).andReturn(false);
        replay(fileUI);
        replay(tabsUI);
        replay(systemUI);

        // First try, user saves changes
        assertTrue(controller.removingGlossary(manager, pGlossary));

        // Second try, user cancels
        assertFalse(controller.removingGlossary(manager, pGlossary));
    }

    @Test
    public void removingCleanPersistentGlossary() {
        expect(pGlossary.isDirty()).andReturn(false);
        replay(pGlossary);
        replay(fileUI);
        replay(tabsUI);
        replay(systemUI);

        assertTrue(controller.removingGlossary(manager, pGlossary));
    }

    @Test
    public void windowClosingSuccess() {
        systemUI.exitApplication();
        replay(systemUI);
        replay(fileUI);
        replay(tabsUI);

        controller.windowClosing(null);
    }

    @Test
    public void windowClosingFailure() {
        replay(systemUI);
        replay(fileUI);
        replay(tabsUI);

        manager = new GlossaryManager() {
            @Override
            public synchronized boolean removeAll() {
                return false;
            }
        };

        controller = new GlossaryController(manager, fileUI, tabsUI, systemUI);
        controller.windowClosing(null);
    }
}
