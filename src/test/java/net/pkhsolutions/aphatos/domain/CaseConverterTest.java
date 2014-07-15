package net.pkhsolutions.aphatos.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test-case for {@link net.pkhsolutions.aphatos.domain.CaseConverter}.
 *
 * @author Petter Holmström
 */
public class CaseConverterTest {

    @Test
    public void upperCaseConvertion() {
        WordConverter wc = new CaseConverter(true);
        assertEquals("HELLO", wc.processWord("hello"));
    }

    @Test
    public void lowerCaseConvertion() {
        WordConverter wc = new CaseConverter(false);
        assertEquals("hello", wc.processWord("HELLO"));
    }
}
