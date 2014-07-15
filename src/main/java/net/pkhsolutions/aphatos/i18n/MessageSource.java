package net.pkhsolutions.aphatos.i18n;

import java.io.IOException;
import java.util.Properties;

public class MessageSource {

    // TODO Test and document!

    private static final MessageSource INSTANCE = new MessageSource();
    private final Properties props;

    private MessageSource() {
        props = new Properties();
        try {
            // CHECKME Translations should be user-configurable
            props.load(getClass().getResourceAsStream("messages.properties"));
        } catch (IOException e) {
            e.printStackTrace();
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
