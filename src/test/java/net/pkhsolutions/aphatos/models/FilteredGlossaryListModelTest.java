package net.pkhsolutions.aphatos.models;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.impl.SimpleGlossary;
import org.junit.Before;
import org.junit.Test;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import static org.junit.Assert.*;

/**
 * Test-case for {@link net.pkhsolutions.aphatos.models.FilteredGlossaryListModel}.
 *
 * @author Petter Holmström
 */
public class FilteredGlossaryListModelTest {

    private Glossary glossary;

    private FilteredGlossaryListModel model;

    private boolean contentsChanged = false;

    @Before
    public void initialize() {
        glossary = new SimpleGlossary();
        glossary.add("aaa", "aab", "aac", "aad", "bba", "bbb", "bbc", "bbd",
                "cca", "ccb", "ccc", "ccd", "ddd");
        model = new FilteredGlossaryListModel();
        model.setGlossary(glossary);
        contentsChanged = false;
        model.addListDataListener(new ListDataListener() {
            @Override
            public void contentsChanged(ListDataEvent e) {
                assertSame(e.getSource(), model);
                assertEquals(0, e.getIndex0());
                assertEquals(0, e.getIndex1());
                contentsChanged = true;
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                fail("no interval should have been added");
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                fail("no interval should have been removed");
            }
        });
    }

    @Test
    public void getDefaultFilter() {
        assertFalse(model.isFiltered());
        assertEquals("", model.getFilter());
        model.setFilter(null);
        assertEquals("", model.getFilter());
        assertFalse(model.isFiltered());
        assertEquals(13, model.getSize());
        assertEquals("aaa", model.getElementAt(0));
        assertEquals("aab", model.getElementAt(1));
        assertEquals("aac", model.getElementAt(2));
        assertEquals("aad", model.getElementAt(3));
        assertEquals("bba", model.getElementAt(4));
        assertEquals("bbb", model.getElementAt(5));
        assertEquals("bbc", model.getElementAt(6));
        assertEquals("bbd", model.getElementAt(7));
        assertEquals("cca", model.getElementAt(8));
        assertEquals("ccb", model.getElementAt(9));
        assertEquals("ccc", model.getElementAt(10));
        assertEquals("ccd", model.getElementAt(11));
        assertEquals("ddd", model.getElementAt(12));
    }

    @Test
    public void setFilter() {
        assertFalse(contentsChanged);
        model.setFilter("bb");
        assertEquals(4, model.getSize());
        assertEquals("bba", model.getElementAt(0));
        assertEquals("bbb", model.getElementAt(1));
        assertEquals("bbc", model.getElementAt(2));
        assertEquals("bbd", model.getElementAt(3));

        try {
            model.getElementAt(4);
            fail("no exception thrown");
        } catch (IndexOutOfBoundsException e) {
            // OK.
        }

        assertEquals("bb", model.getFilter());
        assertTrue(model.isFiltered());
        assertTrue(contentsChanged);
    }

    @Test
    public void setNonexistentFilter() {
        assertFalse(contentsChanged);
        model.setFilter("ee");
        assertEquals(0, model.getSize());

        try {
            model.getElementAt(0);
            fail("no exception thrown");
        } catch (IndexOutOfBoundsException e) {
            // OK.
        }

        assertEquals("ee", model.getFilter());
        assertTrue(model.isFiltered());
        assertTrue(contentsChanged);
    }

    @Test
    public void filterFirstElementsInList() {
        model.setFilter("aa");
        assertEquals(4, model.getSize());
        assertEquals("aaa", model.getElementAt(0));
    }

    @Test
    public void filterLastElementsInList() {
        model.setFilter("dd");
        assertEquals(1, model.getSize());
        assertEquals("ddd", model.getElementAt(0));
    }

    @Test
    public void filterOneElement() {
        model.setFilter("cca");
        assertEquals(1, model.getSize());
        assertEquals("cca", model.getElementAt(0));
    }

    @Test
    public void filteredWordAdded() {
        assertFalse(contentsChanged);
        model.setFilter("aa");
        assertEquals(4, model.getSize());
        glossary.add("aae");
        assertEquals(5, model.getSize());
        assertEquals("aae", model.getElementAt(4));
        assertTrue(contentsChanged);
    }

    @Test
    public void filteredWordRemoved() {
        assertFalse(contentsChanged);
        model.setFilter("aa");
        assertEquals(4, model.getSize());
        glossary.delete(glossary.indexOf("aaa"));
        assertEquals(3, model.getSize());
        assertEquals("aab", model.getElementAt(0));
        assertTrue(contentsChanged);
    }

}
