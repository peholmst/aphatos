package net.pkhsolutions.aphatos;

import org.apache.commons.logging.LogFactory;

import java.util.Properties;

/**
 * Utility class that provides access to the application version information
 * (stored in <code>/version.properties</code>, a file generated during
 * build-time).
 *
 * @author Petter Holmström
 */
public class AppVersion {

    private static final Properties props;

    static {
        props = new Properties();
        try {
            props.load(AppVersion.class
                    .getResourceAsStream("/version.properties"));
        } catch (Exception e) {
            LogFactory.getLog(AppVersion.class).debug("An exception occurred while reading the application version", e);
        }
    }

    /**
     * Gets the version of this program.
     *
     * @return the version string.
     */
    public static String getVersion() {
        return props.getProperty("application.version", "N/A");
    }
}
