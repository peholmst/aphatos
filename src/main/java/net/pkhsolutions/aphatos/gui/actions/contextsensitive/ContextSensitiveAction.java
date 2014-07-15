package net.pkhsolutions.aphatos.gui.actions.contextsensitive;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

/**
 * A class that wraps an {@link Action} and changes the source of any
 * {@link ActionEvent}s received to a specified object instance before
 * forwarding them to the delegate object.
 * <p/>
 * This class is based on an example from <a href="http://www.javalobby.org/java/forums/t19442.html"/>Javalobby</a>.
 *
 * @author Petter Holmström
 *         <p/>
 *         TODO Test me!
 */
class ContextSensitiveAction implements Action {

    private Action delegate;

    private Object source;

    /**
     * Creates a new <code>ContextSensitiveAction</code> with the specified
     * delegate and source objects.
     *
     * @param delegate the delegate action, never <code>null</code>.
     * @param source   the source of the action events, never <code>null</code>.
     */
    public ContextSensitiveAction(Action delegate, Object source) {
        assert delegate != null : "delegate must not be null";
        assert source != null : "source must not be null";
        this.delegate = delegate;
        this.source = source;
    }

    /**
     * Gets the delegate action that this wrapper forwards all method calls to.
     *
     * @return the delegate action (never <code>null</code>).
     */
    Action getDelegate() {
        return delegate;
    }

    /**
     * Gets the source object that will be set as the source of all
     * {@link ActionEvent}s received.
     *
     * @return the source object (never <code>null</code>).
     */
    Object getSource() {
        return source;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getDelegate().addPropertyChangeListener(listener);
    }

    @Override
    public Object getValue(String key) {
        return getDelegate().getValue(key);
    }

    @Override
    public boolean isEnabled() {
        return getDelegate().isEnabled();
    }

    @Override
    public void setEnabled(boolean b) {
        getDelegate().setEnabled(b);
    }

    @Override
    public void putValue(String key, Object value) {
        getDelegate().putValue(key, value);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getDelegate().removePropertyChangeListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getDelegate().actionPerformed(
                new ActionEvent(getSource(), e.getID(), e.getActionCommand(), e
                        .getWhen(), e.getModifiers()));
    }

}
