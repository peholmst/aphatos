package net.pkhsolutions.aphatos.domain.impl;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryListener;
import net.pkhsolutions.aphatos.domain.WordConverter;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test-case for {@link net.pkhsolutions.aphatos.domain.impl.AbstractGlossary}.
 *
 * @author Petter Holmström
 */
public class AbstractGlossaryTest {

    private final List<String> wordsAdded = new ArrayList<>();
    private AbstractGlossary glossary;
    private GlossaryListener listener;

    @Before
    public void initialize() {
        glossary = new AbstractGlossary() {
            @Override
            public void add(String... words) {
                Collections.addAll(wordsAdded, words);
            }

            @Override
            public boolean contains(String word) {
                fail("Not implemented");
                return false;
            }

            @Override
            public void delete(int position) throws IndexOutOfBoundsException {
                fail("Not implemented");
            }

            @Override
            public String get(int position) throws IndexOutOfBoundsException {
                fail("Not implemented");
                return null;
            }

            @Override
            public int getSize() {
                fail("Not implemented");
                return 0;
            }

            @Override
            public int indexOf(String word) {
                fail("Not implemented");
                return 0;
            }
        };
        listener = EasyMock.createMock(GlossaryListener.class);
    }

    @Test
    public void getDefaultWordConverter() {
        assertNull(glossary.getWordConverter());
    }

    @Test
    public void testSetWordConverter() {
        WordConverter wc = new WordConverter() {
            @Override
            public String processWord(String word) {
                return null;
            }
        };
        glossary.setWordConverter(wc);
        assertSame(wc, glossary.getWordConverter());
    }

    @Test
    public void getDefaultWordSeparators() {
        assertArrayEquals(Glossary.DEFAULT_WORD_SEPARATORS, glossary
                .getWordSeparators());
    }

    @Test
    public void setWordSeparators() {
        glossary.setWordSeparators('a', 'b', 'c');
        assertArrayEquals(new char[]{'a', 'b', 'c'}, glossary.getWordSeparators());
    }

    @Test
    public void extractAndAdd() {
        glossary.extractAndAdd("hej, mitt namn \"är\" pyrobjörn [jag] (är) med! i FBK? & jag:har;hjälm;och,mask.och-stövlar_halaren\n\t");
        assertTrue(wordsAdded.contains("hej"));
        assertTrue(wordsAdded.contains("mitt"));
        assertTrue(wordsAdded.contains("namn"));
        assertTrue(wordsAdded.contains("är"));
        assertTrue(wordsAdded.contains("pyrobjörn"));
        assertTrue(wordsAdded.contains("jag"));
        assertTrue(wordsAdded.contains("med"));
        assertTrue(wordsAdded.contains("i"));
        assertTrue(wordsAdded.contains("FBK"));
        assertTrue(wordsAdded.contains("har"));
        assertTrue(wordsAdded.contains("hjälm"));
        assertTrue(wordsAdded.contains("och"));
        assertTrue(wordsAdded.contains("mask"));
        assertTrue(wordsAdded.contains("stövlar"));
        assertTrue(wordsAdded.contains("halaren"));
        assertEquals(18, wordsAdded.size());
    }

    @Test
    public void notifyWordAdded() {
        listener.wordAdded(glossary, "hello", 1);
        EasyMock.replay(listener);

        glossary.addGlossaryListener(listener);
        glossary.notifyWordAdded("hello", 1);
        glossary.removeGlossaryListener(listener);
        glossary.notifyWordAdded("word", 2);

        EasyMock.verify(listener);
    }

    @Test
    public void notifyWordDeleted() {
        listener.wordDeleted(glossary, "hello", 1);
        EasyMock.replay(listener);

        glossary.addGlossaryListener(listener);
        glossary.notifyWordDeleted("hello", 1);
        glossary.removeGlossaryListener(listener);
        glossary.notifyWordDeleted("word", 2);

        EasyMock.verify(listener);
    }

    @Test
    public void notifyGlossaryRefreshed() {
        listener.glossaryRefreshed(glossary);
        EasyMock.replay(listener);

        glossary.addGlossaryListener(listener);
        glossary.notifyGlossaryRefreshed();
        glossary.removeGlossaryListener(listener);
        glossary.notifyGlossaryRefreshed();

        EasyMock.verify(listener);
    }

    @Test
    public void getName() {
        assertNotNull(glossary.getName());
    }

}
