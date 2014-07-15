package net.pkhsolutions.aphatos.gui.components.completion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

/**
 * @author petter FIXME Document and test me!
 */
public class WordCompletionController {

    private static final String COMMIT_ACTION = "commit";
    private final Log logger = LogFactory.getLog(getClass());
    private final WordCompletionModelListener modelListener = new WordCompletionModelListener() {
        @Override
        public void wordCompletionPrefixChanged(WordCompletionModel sender,
                                                String prefix, int position) {
            if (prefix != null) {
                if (logger.isDebugEnabled())
                    logger.debug("Setting filter to: " + prefix);
                view.getCompletionListModel().setFilter(prefix);
                view.showCompletionList(position);
                listSelectionListener.valueChanged(null);
            } else {
                hideCompletionList();
            }
        }
    };
    private final ListSelectionListener listSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            // The model data could have changed before the selection, therefore
            // the check
            // is required.
            if (view.getCompletionList().getSelectedIndex() != -1
                    && view.getCompletionList().getSelectedIndex() < view
                    .getCompletionList().getModel().getSize()) {
                String w = (String) view.getCompletionList().getSelectedValue();
                if (logger.isDebugEnabled())
                    logger
                            .debug("Completion list selection changed, current value is: "
                                    + w);
                setWordToComplete(w);
            }
        }
    };
    private WordCompletionModel model;
    private WordCompletionView view;
    private String completion;

    public WordCompletionController(WordCompletionModel model,
                                    WordCompletionView view) {
        assert model != null : "model must not be null";
        assert view != null : "view must not be null";

        this.model = model;
        this.view = view;

        initialize();
    }

    private void initialize() {
        // Capture the Enter key for custom processing
        final InputMap im = view.getTextArea().getInputMap();
        final ActionMap am = view.getTextArea().getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
        am.put(COMMIT_ACTION, new CommitAction());

        // Register some listeners
        model.addListener(modelListener);
        view.getTextArea().getDocument().addDocumentListener(model);
        view.getCompletionList().getSelectionModel().addListSelectionListener(
                listSelectionListener);

        view.getTextArea().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideCompletionList();
            }
        });

        view.getTextArea().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_UP:
                        if (view.isCompletionListVisible()) {
                            view.getCompletionList().dispatchEvent(e);
                            break;
                        }
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_ESCAPE:
                        hideCompletionList();
                }
            }
        });

        view.getTextArea().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() != view.getCompletionList())
                    hideCompletionList();
            }
        });

        view.getCompletionList().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() != view.getTextArea())
                    hideCompletionList();
            }
        });

        view.getCompletionList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String word = (String) view.getCompletionList().getSelectedValue();
                    if (word != null) {
                        //completion = word;
                        am.get(COMMIT_ACTION).actionPerformed(null);
                    }
                }
            }
        });
    }

    private void hideCompletionList() {
        view.hideCompletionList();
    }

    public final WordCompletionModel getModel() {
        return model;
    }

    public final WordCompletionView getView() {
        return view;
    }

    private void setWordToComplete(String word) {
        if (word != null) {
            completion = word.substring(model.getPrefix().length());
            if (logger.isDebugEnabled())
                logger.debug("Suggesting completion: " + completion);
        }
    }

    private class CommitAction extends AbstractAction {
        private static final long serialVersionUID = 123L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (view.isCompletionListVisible()) {
                view.getTextArea().insert(completion + " ",
                        model.getPosition() + 1);
            } else {
                view.getTextArea().replaceSelection("\n");
            }
        }
    }

}
