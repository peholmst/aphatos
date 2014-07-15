package net.pkhsolutions.aphatos.gui.actions.contextsensitive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Document and test me!
 * <p/>
 * This class is based on an example from <a href="http://www.javalobby.org/java/forums/t19448.html"/>Javalobby</a>.
 *
 * @author Petter Holmström
 */
public class GloballyContextSensitiveAction implements Action {

    private static final DelegateActionSourceSelectionStrategy DEFAULT_DELEGATE_SOURCE_SELECTION_STRATEGY = new DefaultDelegateActionSourceSelectionStrategy();
    private DelegateActionSourceSelectionStrategy delegateSourceSelectionStrategy = DEFAULT_DELEGATE_SOURCE_SELECTION_STRATEGY;
    private final Map<String, Object> values = new HashMap<>();
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final PropertyChangeListener delegateListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (isUseDelegateActionValues() || "enabled".equals(evt.getPropertyName()))
                propertyChangeSupport.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    };
    private Action delegate;
    private JComponent source;
    private boolean useDelegateActionValues = false;

    public GloballyContextSensitiveAction(String actionName, String name) {
        this(actionName, name, null);
    }

    protected GloballyContextSensitiveAction(String actionName, String name, Icon icon) {
        assert actionName != null : "actionName must not be null";
        assert name != null : "name must not be null";
        values.put(ACTION_COMMAND_KEY, actionName);
        values.put(NAME, name);
        if (icon != null)
            values.put(SMALL_ICON, icon);
        PropertyChangeListener keyboardFocusListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("focusOwner".equals(evt.getPropertyName())) {
                    if (evt.getNewValue() instanceof JComponent)
                        setFocusOwner((JComponent) evt.getNewValue());
                }
            }
        };
        getKeyboardFocusManager().addPropertyChangeListener(keyboardFocusListener);
    }

    boolean isUseDelegateActionValues() {
        return useDelegateActionValues;
    }

    public void setUseDelegateActionValues(boolean useDelegateActionValues) {
        this.useDelegateActionValues = useDelegateActionValues;
    }

    void setFocusOwner(JComponent focusOwner) {
        String actionName = (String) values.get(ACTION_COMMAND_KEY);
        JComponent source = getDelegateSourceSelectionStrategy().getDelegateActionSource(actionName, this.source, focusOwner);
        if (source != this.source) {
            if (this.delegate != null)
                this.delegate.removePropertyChangeListener(delegateListener);

            this.source = source;
            if (source == null)
                this.delegate = null;
            else
                this.delegate = source.getActionMap().get(actionName);

            if (this.delegate != null)
                this.delegate.addPropertyChangeListener(delegateListener);

            boolean enabled = this.delegate != null && this.delegate.isEnabled();
            propertyChangeSupport.firePropertyChange("enabled", !enabled, enabled);
        }
    }

    /**
     * Gets the keyboard focus manager that will notify this
     * context sensitive action when the currently focused component
     * changes.
     *
     * @return the keyboard focus manager (never <code>null</code>).
     */
    KeyboardFocusManager getKeyboardFocusManager() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager();
    }

    DelegateActionSourceSelectionStrategy getDelegateSourceSelectionStrategy() {
        return delegateSourceSelectionStrategy;
    }

    public void setDelegateSourceSelectionStrategy(DelegateActionSourceSelectionStrategy delegateSourceSelectionStrategy) {
        this.delegateSourceSelectionStrategy = delegateSourceSelectionStrategy == null ? DEFAULT_DELEGATE_SOURCE_SELECTION_STRATEGY : delegateSourceSelectionStrategy;
    }

    Action getDelegate() {
        return delegate;
    }

    JComponent getSource() {
        return source;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public Object getValue(String key) {
        if (getDelegate() == null || !isUseDelegateActionValues())
            return values.get(key);
        else
            return getDelegate().getValue(key);
    }

    @Override
    public boolean isEnabled() {
        return getDelegate() != null && getDelegate().isEnabled();
    }

    @Override
    public void setEnabled(boolean b) {
        if (getDelegate() != null)
            getDelegate().setEnabled(b);
    }

    @Override
    public void putValue(String key, Object value) {
        if (getDelegate() == null || !isUseDelegateActionValues()) {
            Object old = values.get(key);
            values.put(key, value);
            propertyChangeSupport.firePropertyChange(key, old, value);
        } else
            getDelegate().putValue(key, value);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getDelegate() != null)
            getDelegate().actionPerformed(new ActionEvent(getSource(), e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
    }

}
