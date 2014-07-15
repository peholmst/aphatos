package net.pkhsolutions.aphatos.gui.preferences;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * TODO Document me!
 *
 * @author Petter Holmström
 */
public abstract class AbstractPreferences {

    final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
