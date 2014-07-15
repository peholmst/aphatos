package net.pkhsolutions.aphatos.domain;

/**
 * An abstract adapter class for receiving glossary manager events. The methods
 * in this class are empty. This class exists as convenience for creating
 * {@link GlossaryManagerListener} objects.
 *
 * @author Petter Holmström
 */
public class GlossaryManagerAdapter implements GlossaryManagerListener {
    @Override
    public void currentGlossaryChange(GlossaryManager sender,
                                      Glossary currentGlossary) {
    }

    @Override
    public boolean addingGlossary(GlossaryManager sender, Glossary glossary) {
        return true;
    }

    @Override
    public void glossaryAdded(GlossaryManager sender, Glossary glossary) {
    }

    @Override
    public boolean removingGlossary(GlossaryManager sender,
                                    Glossary glossary) {
        return true;
    }

    @Override
    public void glossaryRemoved(GlossaryManager sender, Glossary glossary) {
    }

    @Override
    public void allGlossariesRemoved(GlossaryManager sender,
                                     int remainingGlossaries) {
    }

    @Override
    public void removingAllGlossaries(GlossaryManager sender) {
    }
}
