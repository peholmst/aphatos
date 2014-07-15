package net.pkhsolutions.aphatos.domain;

import net.pkhsolutions.aphatos.domain.impl.SimpleGlossary;
import net.pkhsolutions.aphatos.domain.impl.SimplePersistentGlossary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory that is used by the glossary application to create {@link Glossary}-instances.
 * This is the only allowed way of creating new glossaries!
 *
 * @author Petter Holmström
 */
public class GlossaryFactory {

    /**
     * Protected static logger.
     */
    private static final Log logger = LogFactory.getLog(GlossaryFactory.class);

    /**
     * Creates a new instance of the specified glossary interface.
     *
     * @param iface the glossary interface.
     * @return a new instance.
     */
    public static <T extends Glossary> T createGlossary(Class<T> iface) {
        assert iface != null : "iface must not be null";
        assert iface.isInterface() : "iface must be an interface";

        if (logger.isInfoEnabled())
            logger.info("Creating instance of " + iface);

        T glossary;
        if (iface == Glossary.class)
            glossary = iface.cast(new SimpleGlossary());
        else if (iface == PersistentGlossary.class)
            glossary = iface.cast(new SimplePersistentGlossary());
        else {
            if (logger.isErrorEnabled())
                logger.error("Unsupported glossary interface " + iface);
            throw new IllegalArgumentException("Unsupported glossary interface");
        }

        // CHECKME Separator chars should be made user configurable
        // CHECKME Word converters should be factored out and made user configurable
        glossary.setWordConverter(new CaseConverter(false));
        return glossary;
    }
}
