package net.pkhsolutions.aphatos.domain;

/**
 * Interface defining a word converter that is used by a {@link Glossary} to
 * perform any last minute modifications to a word before it is added to the
 * glossary.
 *
 * @author Petter Holmström
 */
public interface WordConverter {

    /**
     * Processes <code>word</code>.
     *
     * @param word the original word.
     * @return the converted word.
     */
    public String processWord(String word);

}
