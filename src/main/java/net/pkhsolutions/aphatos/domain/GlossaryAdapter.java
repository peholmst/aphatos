package net.pkhsolutions.aphatos.domain;

/**
 * An abstract adapter class for receiving glossary events. The methods in this
 * class are empty. This class exists as convenience for creating
 * {@link GlossaryListener} objects.
 *
 * @author Petter Holmström
 */
public class GlossaryAdapter implements GlossaryListener {
    @Override
    public void glossaryRefreshed(Glossary sender) {
    }

    @Override
    public void wordAdded(Glossary sender, String word, int position) {
    }

    @Override
    public void wordDeleted(Glossary sender, String word, int position) {
    }
}
