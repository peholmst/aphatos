package net.pkhsolutions.aphatos.domain.utils;

import net.pkhsolutions.aphatos.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * TODO Document and test me!
 *
 * @author Petter Holmström
 */
public class GlossaryManagerPersister extends GlossaryManagerAdapter {

    private static final String OPEN_FILE_GLOSSARIES = "open-glossaries";

    private static final String FILE_COUNT = "fileCount";

    private static final String FILE_PREFIX = "file";

    private final Log logger = LogFactory.getLog(getClass());

    private final Preferences prefs;

    public GlossaryManagerPersister() {
        prefs = Preferences.userNodeForPackage(getClass()).node(
                OPEN_FILE_GLOSSARIES);
    }

    public void loadGlossaries(GlossaryManager destination) {
        if (logger.isInfoEnabled())
            logger.info("Loading filename information from preference store");

        int fileCount = prefs.getInt(FILE_COUNT, 0);
        for (int i = 0; i < fileCount; i++) {
            String fn = prefs.get(FILE_PREFIX + i, "");

            if (!fn.equals("")) {
                if (logger.isDebugEnabled())
                    logger.debug("Trying to open file " + fn);
                PersistentGlossary backend = GlossaryFactory
                        .createGlossary(PersistentGlossary.class);
                FileGlossary glossary = new FileGlossary(backend);
                glossary.setFile(new File(fn));
                try {
                    glossary.loadFromFile();
                    destination.addGlossary(glossary);
                } catch (IOException e) {
                    logger.warn("Could not load file " + fn);
                }
            }
        }
    }

    @Override
    public void removingAllGlossaries(GlossaryManager sender) {
        if (logger.isInfoEnabled())
            logger.info("Saving filename information to preference store");

        int i = 0;
        for (Glossary gl : sender.getGlossaries()) {
            if (gl instanceof FileGlossary
                    && ((FileGlossary) gl).getFile() != null) {
                File file = ((FileGlossary) gl).getFile();

                if (logger.isDebugEnabled())
                    logger.debug("Saving filename " + file.getAbsolutePath());

                prefs.put(FILE_PREFIX + (i++), file.getAbsolutePath());
            }
        }
        prefs.putInt(FILE_COUNT, i);
    }
}
