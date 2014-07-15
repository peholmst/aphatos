package net.pkhsolutions.aphatos.domain;

/**
 * A word converter that converts the word to either upper case or lower case.
 *
 * @author Petter Holmström
 */
public class CaseConverter implements WordConverter {

    private final boolean upper;

    /**
     * Creates a new <code>CaseConverter</code>.
     *
     * @param upper <code>true</code> to convert to upper case,
     *              <code>false</code> to convert to lower case.
     */
    public CaseConverter(boolean upper) {
        this.upper = upper;
    }

    @Override
    public String processWord(String word) {
        assert word != null : "word must not be null";
        if (upper)
            return word.toUpperCase();
        else
            return word.toLowerCase();
    }

}
