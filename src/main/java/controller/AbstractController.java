package controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import model.AbstractModel;
import view.AbstractView;

public abstract class AbstractController implements PropertyChangeListener {
   private boolean DEBUG = false;

   private ArrayList<AbstractView>       registeredViews;
   private ArrayList<AbstractModel>      registeredModels;

   public AbstractController() {
      registeredViews  = new ArrayList<AbstractView>();
      registeredModels = new ArrayList<AbstractModel>();
   }
   
   public ArrayList<AbstractView> getRegisteredViews() {
	  return registeredViews;
   }
   
   public ArrayList<AbstractModel> getRegisteredModels() {
	  return registeredModels;
   }

   public void addModel(AbstractModel model) {
      registeredModels.add( model );
      model.addPropertyChangeListener(this);
   }

   public void removeModel(AbstractModel model) {
      registeredModels.remove(model);
      model.removePropertyChangeListener(this);
   }

   public void addView(AbstractView view) {
      registeredViews.add(view);
   }

   public void removeView(AbstractView view) {
      registeredViews.remove(view);
   }

   //  =================================================================
   //  Use this to observe property changes from registered models
   //  and propagate them on to all the views.
   //  It gets fired when Fireproperty change method get called from the
   //  models; It contains both the old and the new values
   //  =================================================================

   public void propertyChange(PropertyChangeEvent evt) {

      PropertyChangeEvent e = new PropertyChangeEvent( this,
                                                       evt.getPropertyName(),
                                                       evt.getOldValue(),
                                                       evt.getNewValue() );

      // Send modelProperty to each of the registered views ....

      for (AbstractView view: registeredViews) {
          view.modelPropertyChange(e);     
      }
   }

   // =================================================================
   // This is a convenience method that subclasses can call upon
   // to fire property changes back to the models. This method
   // uses reflection to inspect each of the model classes
   // to determine whether it is the owner of the property
   // in question. If it isn't, a NoSuchMethodException is thrown,
   // which the method ignores.
   // @param propertyName = The name of the property.
   // @param newValue = An object that represents the new value
   // of the property.
   // =================================================================

   protected void setModelProperty(String propertyName, Object newValue ) {

      if(DEBUG == true) {
        System.out.println("*** In AbstractController().setModelProperty()...");
        System.out.println("*** In AbstractController(): propertyName = " + propertyName );
      }

      for (AbstractModel model: registeredModels) {

         if(DEBUG == true) {
            System.out.println("*** In AbstractController().loop on registered models ...");
         }

         try {

            if(DEBUG == true) {
               System.out.println("*** (pt1) :    model class: " + model.getClass() );
               System.out.println("*** (pt2) : newValue class: " + newValue.getClass() );
            }

            Method method = model.getClass().getMethod( "set"+propertyName, new Class[] { newValue.getClass() } );

            if(DEBUG == true) {
               System.out.println("*** (pt3) : method name = " + method.toString() );
            }

            method.invoke(model, newValue);
         } catch (Exception ex) {
            //  Handle exception
        	if(DEBUG == true) {
        	   System.out.println("*** In AbstractController().setModelProperty() This doesn't work");
        	}
         }
         
         for (Method method : model.getClass().getMethods()) {
             if ("setFloorplanSelectedPropertyStatus".equals( method.getName() )) {
                 if (method.getParameterTypes()[0].isAssignableFrom( newValue.getClass() )) {
                     try {
						method.invoke(model, newValue);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                 }
             }
         }
      }
   }
}
