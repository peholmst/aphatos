package net.pkhsolutions.aphatos.models;

import net.pkhsolutions.aphatos.domain.Glossary;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import static org.junit.Assert.*;

/**
 * Test-case for {@link net.pkhsolutions.aphatos.models.GlossaryListModel}.
 *
 * @author Petter Holmström
 */
public class GlossaryListModelTest {

    private GlossaryListModel model;

    private Glossary glossary;


    @Before
    public void initialize() {
        glossary = EasyMock.createMock(Glossary.class);
        model = new GlossaryListModel();
    }

    @Test
    public void getDefaultGlossary() {
        assertNull(model.getGlossary());
    }

    @Test
    public void getSizeNoGlossary() {
        try {
            model.getSize();
            fail("no exception thrown");
        } catch (IllegalStateException e) {
            // Ok.
        }
    }

    @Test
    public void getElementAtNoGlossary() {
        try {
            model.getElementAt(1);
            fail("no exception thrown");
        } catch (IllegalStateException e) {
            // Ok.
        }
    }

    @Test
    public void setGlossary() {
        Glossary glossary2 = EasyMock.createMock(Glossary.class);

        glossary.addGlossaryListener(model);
        glossary.removeGlossaryListener(model);
        glossary.addGlossaryListener(model);
        glossary.removeGlossaryListener(model);
        glossary2.addGlossaryListener(model);
        EasyMock.replay(glossary);
        EasyMock.replay(glossary2);

        model.setGlossary(glossary);
        assertSame(glossary, model.getGlossary());
        model.setGlossary(null);
        assertNull(model.getGlossary());
        model.setGlossary(glossary);
        model.setGlossary(glossary2);

        EasyMock.verify(glossary);
        EasyMock.verify(glossary2);
    }


    @Test
    public void getSize() {
        glossary.addGlossaryListener(model);
        EasyMock.expect(glossary.getSize()).andReturn(1);
        EasyMock.replay(glossary);

        model.setGlossary(glossary);
        assertEquals(1, model.getSize());

        EasyMock.verify(glossary);
    }

    @Test
    public void getElementAt() {
        glossary.addGlossaryListener(model);
        EasyMock.expect(glossary.get(2)).andReturn("hello");
        EasyMock.replay(glossary);

        model.setGlossary(glossary);
        assertEquals("hello", model.getElementAt(2));

        EasyMock.verify(glossary);
    }

    @Test
    public void glossaryRefreshed() {
        final boolean[] called = new boolean[1];
        model.addListDataListener(new ListDataListener() {
            @Override
            public void contentsChanged(ListDataEvent e) {
                assertSame(model, e.getSource());
                assertEquals(0, e.getIndex0());
                assertEquals(9, e.getIndex1());
                assertEquals(ListDataEvent.CONTENTS_CHANGED, e.getType());
                called[0] = true;
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                fail("no items added");
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                fail("no items removed");
            }
        });

        glossary.addGlossaryListener(model);
        EasyMock.expect(glossary.getSize()).andReturn(10);
        EasyMock.replay(glossary);

        model.setGlossary(glossary);
        model.glossaryRefreshed(glossary);
        assertTrue(called[0]);

        EasyMock.verify(glossary);
    }

    @Test
    public void wordAdded() {
        final boolean[] called = new boolean[1];
        model.addListDataListener(new ListDataListener() {
            @Override
            public void contentsChanged(ListDataEvent e) {
                fail("no contents change");
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                assertSame(model, e.getSource());
                assertEquals(5, e.getIndex0());
                assertEquals(5, e.getIndex1());
                assertEquals(ListDataEvent.INTERVAL_ADDED, e.getType());
                called[0] = true;
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                fail("no items removed");
            }
        });

        glossary.addGlossaryListener(model);
        EasyMock.replay(glossary);

        model.setGlossary(glossary);
        model.wordAdded(glossary, "hello", 5);
        assertTrue(called[0]);

        EasyMock.verify(glossary);
    }

    @Test
    public void wordDeleted() {
        final boolean[] called = new boolean[1];
        model.addListDataListener(new ListDataListener() {
            @Override
            public void contentsChanged(ListDataEvent e) {
                fail("no contents change");
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                fail("no items added");
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                assertSame(model, e.getSource());
                assertEquals(5, e.getIndex0());
                assertEquals(5, e.getIndex1());
                assertEquals(ListDataEvent.INTERVAL_REMOVED, e.getType());
                called[0] = true;
            }
        });

        glossary.addGlossaryListener(model);
        EasyMock.replay(glossary);

        model.setGlossary(glossary);
        model.wordDeleted(glossary, "hello", 5);
        assertTrue(called[0]);

        EasyMock.verify(glossary);
    }
}
