package net.pkhsolutions.aphatos.domain;

import net.pkhsolutions.aphatos.domain.impl.SimpleGlossary;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Test-case for {@link net.pkhsolutions.aphatos.domain.GlossaryManager}.
 *
 * @author Petter Holmström
 */
public class GlossaryManagerTest {

    private GlossaryManager gloman;

    private GlossaryManagerListener listener;

    @Before
    public void initialize() {
        listener = createStrictMock(GlossaryManagerListener.class);
        gloman = new GlossaryManager();
        gloman.addListener(listener);
    }

    @Test
    public void initialState() {
        assertTrue(gloman.getGlossaries().isEmpty());
        assertNull(gloman.getCurrentGlossary());
    }

    @Test
    public void manualGlossaryEditing() {
        try {
            gloman.getGlossaries().add(new SimpleGlossary());
            fail("no exception thrown");
        } catch (RuntimeException e) {
            assertTrue(gloman.getGlossaries().isEmpty());
        }
    }

    @Test
    public void addAndRemoveGlossary() {
        Glossary glossary = new SimpleGlossary();
        expect(listener.addingGlossary(gloman, glossary)).andReturn(true);
        listener.glossaryAdded(gloman, glossary);
        listener.currentGlossaryChange(gloman, glossary);
        expect(listener.removingGlossary(gloman, glossary))
                .andReturn(true);
        listener.currentGlossaryChange(gloman, null);
        listener.glossaryRemoved(gloman, glossary);
        replay(listener);

        // Add glossary
        assertTrue(gloman.addGlossary(glossary));
        assertTrue(gloman.getGlossaries().contains(glossary));
        assertSame(glossary, gloman.getCurrentGlossary());

        // Add it again
        assertFalse(gloman.addGlossary(glossary));
        assertEquals(1, gloman.getGlossaries().size()); // No changes

        // Remove glossary
        assertTrue(gloman.removeGlossary(glossary));
        assertFalse(gloman.getGlossaries().contains(glossary));
        assertNull(gloman.getCurrentGlossary());

        // Remove it again
        assertFalse(gloman.removeGlossary(glossary)); // Nothing should happen

        verify(listener);
    }

    @Test
    public void addAndRemoveByIndex() {
        Glossary glossary = new SimpleGlossary();
        expect(listener.addingGlossary(gloman, glossary)).andReturn(true);
        listener.glossaryAdded(gloman, glossary);
        listener.currentGlossaryChange(gloman, glossary);
        expect(listener.removingGlossary(gloman, glossary))
                .andReturn(true);
        listener.currentGlossaryChange(gloman, null);
        listener.glossaryRemoved(gloman, glossary);
        replay(listener);

        // Add glossary
        assertTrue(gloman.addGlossary(glossary));
        assertTrue(gloman.getGlossaries().contains(glossary));
        assertSame(glossary, gloman.getCurrentGlossary());

        // Remove glossary
        assertTrue(gloman.removeGlossary(0));
        assertFalse(gloman.getGlossaries().contains(glossary));
        assertNull(gloman.getCurrentGlossary());

        verify(listener);
    }

    @Test
    public void addAndRemoveAborted() {
        Glossary glossary = new SimpleGlossary();
        expect(listener.addingGlossary(gloman, glossary)).andReturn(false);
        expect(listener.addingGlossary(gloman, glossary)).andReturn(true);
        listener.glossaryAdded(gloman, glossary);
        listener.currentGlossaryChange(gloman, glossary);
        expect(listener.removingGlossary(gloman, glossary)).andReturn(
                false);
        replay(listener);

        // Add glossary
        assertFalse(gloman.addGlossary(glossary)); // First attempt didn't work
        assertFalse(gloman.getGlossaries().contains(glossary));
        assertNull(gloman.getCurrentGlossary());

        // Try again
        assertTrue(gloman.addGlossary(glossary));
        assertTrue(gloman.getGlossaries().contains(glossary));
        assertSame(glossary, gloman.getCurrentGlossary());

        // Remove glossary
        assertFalse(gloman.removeGlossary(glossary)); // Won't work!
        assertTrue(gloman.getGlossaries().contains(glossary));
        assertSame(glossary, gloman.getCurrentGlossary());

        verify(listener);
    }

    @Test
    public void addAndRemoveByIndexAborted() {
        Glossary glossary = new SimpleGlossary();
        expect(listener.addingGlossary(gloman, glossary)).andReturn(true);
        listener.glossaryAdded(gloman, glossary);
        listener.currentGlossaryChange(gloman, glossary);
        expect(listener.removingGlossary(gloman, glossary)).andReturn(
                false);
        replay(listener);

        // Add glossary
        assertTrue(gloman.addGlossary(glossary));
        assertTrue(gloman.getGlossaries().contains(glossary));
        assertSame(glossary, gloman.getCurrentGlossary());

        // Remove glossary
        assertFalse(gloman.removeGlossary(0)); // Won't work!
        assertTrue(gloman.getGlossaries().contains(glossary));
        assertSame(glossary, gloman.getCurrentGlossary());

        verify(listener);
    }

    @Test
    public void removeAll() {
        Glossary glossary1 = new SimpleGlossary();
        Glossary glossary2 = new SimpleGlossary();
        expect(listener.addingGlossary(gloman, glossary1)).andReturn(true);
        listener.glossaryAdded(gloman, glossary1);
        listener.currentGlossaryChange(gloman, glossary1);
        expect(listener.addingGlossary(gloman, glossary2)).andReturn(true);
        listener.glossaryAdded(gloman, glossary2);

        listener.removingAllGlossaries(gloman);

        expect(listener.removingGlossary(gloman, glossary2)).andReturn(
                true);
        listener.glossaryRemoved(gloman, glossary2);
        expect(listener.removingGlossary(gloman, glossary1)).andReturn(
                true);
        listener.currentGlossaryChange(gloman, null);
        listener.glossaryRemoved(gloman, glossary1);

        listener.allGlossariesRemoved(gloman, 0);

        replay(listener);

        // Add glossaries
        assertTrue(gloman.addGlossary(glossary1));
        assertTrue(gloman.addGlossary(glossary2));

        // Remove all
        assertTrue(gloman.removeAll());
        assertNull(gloman.getCurrentGlossary());
        assertTrue(gloman.getGlossaries().isEmpty());

        verify(listener);
    }

    @Test
    public void removeAllAborted() {
        Glossary glossary1 = new SimpleGlossary();
        Glossary glossary2 = new SimpleGlossary();
        expect(listener.addingGlossary(gloman, glossary1)).andReturn(true);
        listener.glossaryAdded(gloman, glossary1);
        listener.currentGlossaryChange(gloman, glossary1);
        expect(listener.addingGlossary(gloman, glossary2)).andReturn(true);
        listener.glossaryAdded(gloman, glossary2);
        listener.currentGlossaryChange(gloman, glossary2);

        listener.removingAllGlossaries(gloman);

        expect(listener.removingGlossary(gloman, glossary2)).andReturn(
                true);
        listener.currentGlossaryChange(gloman, null);
        listener.glossaryRemoved(gloman, glossary2);
        expect(listener.removingGlossary(gloman, glossary1)).andReturn(
                false);
        listener.currentGlossaryChange(gloman, glossary1);

        listener.allGlossariesRemoved(gloman, 1);

        replay(listener);

        // Add glossaries
        assertTrue(gloman.addGlossary(glossary1));
        assertTrue(gloman.addGlossary(glossary2));
        gloman.setCurrentGlossary(glossary2);

        // Remove all
        assertFalse(gloman.removeAll());
        assertSame(glossary1, gloman.getCurrentGlossary());
        assertEquals(1, gloman.getGlossaries().size());

        verify(listener);
    }

    @Test
    public void currentGlossary() {
        Glossary glossary1 = new SimpleGlossary();
        Glossary glossary2 = new SimpleGlossary();
        expect(listener.addingGlossary(gloman, glossary1)).andReturn(true);
        listener.glossaryAdded(gloman, glossary1);
        listener.currentGlossaryChange(gloman, glossary1);
        expect(listener.addingGlossary(gloman, glossary2)).andReturn(true);
        listener.glossaryAdded(gloman, glossary2);
        listener.currentGlossaryChange(gloman, glossary2);
        expect(listener.removingGlossary(gloman, glossary2)).andReturn(
                true);
        listener.currentGlossaryChange(gloman, glossary1);
        listener.glossaryRemoved(gloman, glossary2);
        replay(listener);

        gloman.addGlossary(glossary1);
        gloman.addGlossary(glossary2);
        assertSame(glossary1, gloman.getCurrentGlossary());
        gloman.setCurrentGlossary(glossary2);
        assertSame(glossary2, gloman.getCurrentGlossary());
        gloman.removeGlossary(glossary2);
        assertSame(glossary1, gloman.getCurrentGlossary());
        gloman.setCurrentGlossary(glossary1); // Should not fire any event

        verify(listener);
    }

    @Test
    public void illegalCurrentGlossary() {
        try {
            gloman.setCurrentGlossary(new SimpleGlossary());
            fail("no exception thrown");
        } catch (RuntimeException e) {
            assertNull(gloman.getCurrentGlossary());
        }
    }

    @Test
    public void removeListener() {
        replay(listener);
        gloman.removeListener(listener);
        gloman.addGlossary(new SimpleGlossary());
        verify(listener);
    }
}
