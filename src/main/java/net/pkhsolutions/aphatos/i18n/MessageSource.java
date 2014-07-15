package net.pkhsolutions.aphatos.i18n;

import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class MessageSource {

    // TODO Test and document!

    private static final MessageSource INSTANCE = new MessageSource();
    private final Properties props;

    private MessageSource() {
        props = new Properties();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("messages.properties")))) {
            props.load(reader);
        } catch (IOException e) {
            LogFactory.getLog(MessageSource.class).error("An error occurred while loading messages", e);
        }
    }

    public static MessageSource getInstance() {
        return INSTANCE;
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public String get(String key) {
        String s = props.getProperty(key);
        return s == null ? key : s;
    }
}
