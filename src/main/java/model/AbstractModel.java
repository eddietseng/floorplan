package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractModel {
   private Boolean DEBUG = false;

   protected PropertyChangeSupport propertyChangeSupport;

   public AbstractModel() {
      propertyChangeSupport = new PropertyChangeSupport( this );
   }

   // Add/remove property change listeners from the observers list ...
    
   public void addPropertyChangeListener( PropertyChangeListener listener ) {

      if( DEBUG == true ) {
         System.out.println("*** In AbstractModel().addPropertyChangeListener() ... ");
      }

      propertyChangeSupport.addPropertyChangeListener(listener);
   }

   public void removePropertyChangeListener( PropertyChangeListener listener ) {
      propertyChangeSupport.removePropertyChangeListener(listener);
   }

   // ==========================================================================
   // Fire property change: Important note: a property change will only be fired
   // when the oldValue and newValue are different!!
   // ==========================================================================

   protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {

      if( DEBUG == true ) {
         System.out.println("*** In AbstractModel().firePropertyChange(): propertyName = " + propertyName );
         System.out.println("*** In AbstractModel(): has listeners == " + propertyChangeSupport.hasListeners(propertyName) );
      }

      propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
   }
}
