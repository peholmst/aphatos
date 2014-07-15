package net.pkhsolutions.aphatos.gui.components.completion;

import net.pkhsolutions.aphatos.domain.Glossary;
import net.pkhsolutions.aphatos.domain.GlossaryManager;
import net.pkhsolutions.aphatos.domain.GlossaryManagerAdapter;
import net.pkhsolutions.aphatos.domain.GlossaryManagerListener;
import net.pkhsolutions.aphatos.models.FilteredGlossaryListModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * FIXME Document and test me!
 *
 * @author petter
 */
public abstract class WordCompletionView {

    protected final Log logger = LogFactory.getLog(getClass());

    private JTextArea textArea;

    private JList<String> completionList;

    public WordCompletionView(JTextArea textArea, JList<String> completionList,
                              GlossaryManager glossaryManager) {
        assert textArea != null : "textArea must not be null";
        assert completionList != null : "completionList must not be null";
        assert glossaryManager != null : "glossaryManager must not be null";

        this.textArea = textArea;
        this.completionList = completionList;
        this.completionList.setModel(new FilteredGlossaryListModel());
        GlossaryManagerListener listener = new GlossaryManagerAdapter() {
            @Override
            public void currentGlossaryChange(GlossaryManager sender,
                                              Glossary currentGlossary) {
                getCompletionListModel().setGlossary(currentGlossary);
            }
        };
        glossaryManager.addListener(listener);
    }

    public final JTextArea getTextArea() {
        return textArea;
    }

    public final JList getCompletionList() {
        return completionList;
    }

    public final FilteredGlossaryListModel getCompletionListModel() {
        return (FilteredGlossaryListModel) completionList.getModel();
    }

    public void hideCompletionList() {
        if (logger.isDebugEnabled())
            logger.debug("Hiding word completion list");
        updateCompletionBox(false, 0);
    }

    public void showCompletionList(int position) {
        if (logger.isDebugEnabled())
            logger
                    .debug("Showing word completion list at position "
                            + position);
        if (completionList.getModel().getSize() > 0) {
            completionList.setSelectedIndex(0);
            updateCompletionBox(true, position);
        } else {
            if (logger.isDebugEnabled())
                logger.debug("No words to show in list, hiding it");
            completionList.setSelectedIndex(-1);
            updateCompletionBox(false, 0);
        }
    }

    public abstract boolean isCompletionListVisible();

    protected abstract void updateCompletionBox(boolean show, int position);

}
