package net.pkhsolutions.aphatos.domain.impl;

import net.pkhsolutions.aphatos.domain.GlossaryListener;
import net.pkhsolutions.aphatos.domain.WordConverter;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test-case for {@link net.pkhsolutions.aphatos.domain.impl.SimpleGlossary}.
 *
 * @author Petter Holmström
 */
public class SimpleGlossaryTest {

    GlossaryListener listener;

    SimpleGlossary glossary;

    private WordConverter converter;

    @Before
    public void setUp() throws Exception {
        listener = EasyMock.createStrictMock(GlossaryListener.class);
        converter = EasyMock.createMock(WordConverter.class);
        glossary = new SimpleGlossary();
        glossary.addGlossaryListener(listener);
    }

    @Test
    public void add() {
        // Also tests getSize() and get()
        listener.wordAdded(glossary, "world", 0);
        listener.wordAdded(glossary, "hello", 0);
        EasyMock.replay(listener);

        glossary.add("world", "hello", "hello", "123"); // No numbers included!
        assertEquals(2, glossary.getSize());
        assertEquals("hello", glossary.get(0));
        assertEquals("world", glossary.get(1));

        EasyMock.verify(listener);
    }

    @Test
    public void extractAndAdd() {
        // Also tests getSize() and get()
        listener.wordAdded(glossary, "hello", 0);
        listener.wordAdded(glossary, "world", 1);
        EasyMock.replay(listener);

        glossary.extractAndAdd("hello, world!");
        assertEquals(2, glossary.getSize());
        assertEquals("hello", glossary.get(0));
        assertEquals("world", glossary.get(1));

        EasyMock.verify(listener);
    }

    @Test
    public void addWithConverter() {
        // Also tests getSize() and get()
        EasyMock.expect(converter.processWord("world")).andReturn("WORLD");
        EasyMock.expect(converter.processWord("hello")).andReturn("HELLO")
                .times(2);
        EasyMock.replay(converter);
        listener.wordAdded(glossary, "WORLD", 0);
        listener.wordAdded(glossary, "HELLO", 0);
        EasyMock.replay(listener);

        glossary.setWordConverter(converter);
        glossary.add("world", "hello", "hello");
        assertEquals("HELLO", glossary.get(0));
        assertEquals("WORLD", glossary.get(1));

        EasyMock.verify(listener);
        EasyMock.verify(converter);
    }

    @Test
    public void remove() {
        listener.wordAdded(glossary, "world", 0);
        listener.wordAdded(glossary, "xyz", 1);
        listener.wordDeleted(glossary, "world", 0);
        EasyMock.replay(listener);

        glossary.add("world", "xyz");
        glossary.delete(0);
        assertEquals(1, glossary.getSize());

        EasyMock.verify(listener);
    }

    @Test
    public void indexOf() {
        listener.wordAdded(glossary, "world", 0);
        listener.wordAdded(glossary, "xyz", 1);
        EasyMock.replay(listener);

        glossary.add("world", "xyz");
        assertEquals(-1, glossary.indexOf("nonexistent"));
        assertEquals(0, glossary.indexOf("world"));
        assertEquals(1, glossary.indexOf("xyz"));

        EasyMock.verify(listener);
    }

    @Test
    public void contains() {
        listener.wordAdded(glossary, "world", 0);
        listener.wordAdded(glossary, "xyz", 1);
        EasyMock.replay(listener);

        glossary.add("world", "xyz");
        assertFalse(glossary.contains("nonexistent"));
        assertTrue(glossary.contains("world"));
        assertTrue(glossary.contains("xyz"));

        EasyMock.verify(listener);
    }
}
