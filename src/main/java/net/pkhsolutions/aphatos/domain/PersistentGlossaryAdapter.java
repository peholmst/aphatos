package net.pkhsolutions.aphatos.domain;

/**
 * An abstract adapter class for receiving persistent glossary events. The
 * methods in this class are empty. This class exists as convenience for
 * creating {@link PersistentGlossaryListener} objects.
 *
 * @author Petter Holmström
 */
public class PersistentGlossaryAdapter extends GlossaryAdapter implements
        PersistentGlossaryListener {
    @Override
    public void glossaryLoaded(PersistentGlossary sender) {
    }

    @Override
    public void glossarySaved(PersistentGlossary sender) {
    }

}
