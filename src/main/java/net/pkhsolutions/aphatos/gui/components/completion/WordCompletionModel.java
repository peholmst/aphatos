package net.pkhsolutions.aphatos.gui.components.completion;

import net.pkhsolutions.aphatos.domain.Glossary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author petter
 *         FIXME Document and test me!
 */
public class WordCompletionModel implements DocumentListener {

    /**
     * Protected final logger.
     */
    private final Log logger = LogFactory.getLog(getClass());
    private final Set<WordCompletionModelListener> listeners = new HashSet<>();
    private String prefix = null;
    private int position = -1;

    /**
     * Gets the prefix to be used for word completion, if available.
     *
     * @return the current prefix, or <code>null</code> if none is available.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets the position at which the completion should be inserted.
     *
     * @return the completion position, or -1 if none is available.
     */
    public int getPosition() {
        return position;
    }

    void setPrefixAndPosition(String prefix, int position) {
        if (logger.isDebugEnabled())
            logger.debug("Setting prefix to [" + prefix + "] and position to [" + position + "]");
        this.prefix = prefix;
        this.position = position;
        for (WordCompletionModelListener l : listeners)
            l.wordCompletionPrefixChanged(this, prefix, position);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setPrefixAndPosition(null, -1);
    }

    public void addListener(WordCompletionModelListener listener) {
        assert listener != null : "listener must not be null";
        listeners.add(listener);
    }

    public void removeListener(WordCompletionModelListener listener) {
        assert listener != null : "listener must not be null";
        listeners.remove(listener);
    }

    @Override
    public void insertUpdate(DocumentEvent ev) {
        int pos = ev.getOffset();
        if (logger.isDebugEnabled())
            logger.debug("Insert update at position " + pos + " of length " + ev.getLength());
        if (ev.getLength() == 1) {
            String content;
            try {
                content = ev.getDocument().getText(0, pos + 1);
            } catch (BadLocationException e) {
                logger.error("Error getting document content", e);
                setPrefixAndPosition(null, -1);
                return;
            }

            // Find where the word starts and ends
            int begin;

            // CHECKME This is OK for now, but when the word separators become
            // user configurable, this line needs to change.
            String delimiters = new String(Glossary.DEFAULT_WORD_SEPARATORS);

            for (begin = pos; begin >= 0; begin--) {
                if (delimiters.indexOf(content.charAt(begin)) != -1)
                    break;
            }

            prefix = content.substring(begin + 1, pos + 1).toLowerCase();
            if (!prefix.isEmpty()) {
                setPrefixAndPosition(prefix, pos);
            } else {
                setPrefixAndPosition(null, -1);
            }
        } else {
            // We only do completion when one single character is changed at a time.
            setPrefixAndPosition(null, -1);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        setPrefixAndPosition(null, -1);
    }

}
