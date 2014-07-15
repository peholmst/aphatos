package net.pkhsolutions.aphatos.domain.impl;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryAdapter;
import net.pkhsolutions.aphatos.domain.PersistentGlossary;
import net.pkhsolutions.aphatos.domain.PersistentGlossaryListener;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * Test-case for {@link net.pkhsolutions.aphatos.domain.impl.SimplePersistentGlossary}.
 *
 * @author Petter Holmström
 */
public class SimplePersistableGlossaryTest extends SimpleGlossaryTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        listener = EasyMock.createStrictMock(PersistentGlossaryListener.class);
        glossary = new SimplePersistentGlossary();
        glossary.addGlossaryListener(listener);
    }

    @Override
    public void add() {
        glossary.addGlossaryListener(new GlossaryAdapter() {
            @Override
            public void wordAdded(Glossary sender, String word, int position) {
                assertTrue(((PersistentGlossary) sender).isDirty());
            }
        });
        assertFalse(((PersistentGlossary) glossary).isDirty());
        super.add();
        assertTrue(((PersistentGlossary) glossary).isDirty());
    }

    @Override
    public void addWithConverter() {
        glossary.addGlossaryListener(new GlossaryAdapter() {
            @Override
            public void wordAdded(Glossary sender, String word, int position) {
                assertTrue(((PersistentGlossary) sender).isDirty());
            }
        });
        assertFalse(((PersistentGlossary) glossary).isDirty());
        super.addWithConverter();
        assertTrue(((PersistentGlossary) glossary).isDirty());
    }

    @Override
    public void extractAndAdd() {
        glossary.addGlossaryListener(new GlossaryAdapter() {
            @Override
            public void wordAdded(Glossary sender, String word, int position) {
                assertTrue(((PersistentGlossary) sender).isDirty());
            }
        });
        assertFalse(((PersistentGlossary) glossary).isDirty());
        super.extractAndAdd();
        assertTrue(((PersistentGlossary) glossary).isDirty());
    }

    @Test
    public void loadAndRemove() throws Exception {
        PersistentGlossary glossary = (PersistentGlossary) this.glossary;

        listener.wordAdded(glossary, "hello", 0);
        ((PersistentGlossaryListener) listener).glossaryLoaded(glossary);
        listener.glossaryRefreshed(glossary);
        listener.wordDeleted(glossary, "dig", 0);
        EasyMock.replay(listener);

        glossary.add("hello");
        assertTrue(glossary.isDirty());

        String sep = (String) System.getProperties().get("line.separator");
        glossary.load(new StringReader("hej" + sep + "på" + sep + "dig"));

        assertEquals(3, glossary.getSize());
        assertEquals("dig", glossary.get(0));
        assertEquals("hej", glossary.get(1));
        assertEquals("på", glossary.get(2));

        assertFalse(glossary.isDirty());

        glossary.addGlossaryListener(new GlossaryAdapter() {
            @Override
            public void wordDeleted(Glossary sender, String word, int position) {
                assertTrue(((PersistentGlossary) sender).isDirty());
            }
        });
        glossary.delete(0);

        assertTrue(glossary.isDirty());

        EasyMock.verify(listener);
    }

    @Test
    public void save() throws Exception {
        PersistentGlossary glossary = (PersistentGlossary) this.glossary;

        listener.wordAdded(glossary, "dig", 0);
        listener.wordAdded(glossary, "hej", 1);
        listener.wordAdded(glossary, "på", 2);
        ((PersistentGlossaryListener) listener).glossarySaved(glossary);
        EasyMock.replay(listener);

        glossary.add("dig", "hej", "på");
        assertTrue(glossary.isDirty());

        String sep = (String) System.getProperties().get("line.separator");
        StringWriter sw = new StringWriter();
        glossary.save(sw);

        assertEquals("dig" + sep + "hej" + sep + "på" + sep, sw.toString());

        assertFalse(glossary.isDirty());

        EasyMock.verify(listener);
    }
}
