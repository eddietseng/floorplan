/*
 * =======================================================================
 * AbstractView.java:
 *
 * Created on January 22, 2007, 9:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 * =======================================================================
 */

package view;

import java.beans.PropertyChangeEvent;
import javax.swing.JPanel;

/**
 * ===================================================================================
 * Provide an abstract view ....
 *
 * Called by the controller when it needs to pass along a property change from a model.
 * @param evt The property change event from the model
 * ===================================================================================
 */

public abstract class AbstractView extends JPanel {

    /**
     * Called by the controller when it needs to pass along a property change from a model.
     * @param evt The property change event from the model
     */

    public abstract void modelPropertyChange(PropertyChangeEvent evt);
}
