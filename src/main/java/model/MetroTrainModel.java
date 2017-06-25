/*
 *  ====================================================================
 *  MetroTrainModel.java: Create model of train behavior ...
 *
 *  Written by: Mark Austin                                   June, 2012
 *  ====================================================================
 */

package model;

import java.lang.Math;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import java.beans.PropertyChangeEvent;

import model.metro.*;
import model.primitive.*;
import model.urban.*;
import controller.*;
import controller.EngineeringController;

public class MetroTrainModel extends AbstractModel {
   private Boolean DEBUG = true;
   private EngineeringController    controller;
   private CompositeHierarchy  workspace;

   HashMap<String,MetroTrain>         trains;
   HashMap<String,MetroLinePathway> pathways;

   // Constructor ....

   public MetroTrainModel( EngineeringController controller ) {
      this.controller = controller;

      System.out.println("Create Model of Train Behavior...      ");
      System.out.println("=======================================");

      trains    = new HashMap<String,MetroTrain>();
      workspace = new CompositeHierarchy( 0.0, 0.0, 0.0 );
      workspace.setName("Train Workspace");
   }

   // Default assembly of metro system and propagation to views ...

   public void initDefault() {

      // Build model of Washington DC Metro System and Trains ...

      buildTrainSystem();
      // buildTrains();

      // Transfer hashmap copies to registered views ...

      setMetroTrains();
      setMetroTrainsComposite();
   }

   // ======================================================================
   // Simplified model of trains: Thomas the Train Steam Team ....
   // ======================================================================

   public void buildTrainSystem() {

      // Create train objects ...

      MetroTrain tA = new MetroTrain( "Thomas" );
      tA.setPrice( 1.00 );
      tA.setCapacity( 100.0 );
      tA.setTrack( "Red" );
      tA.setStatus( "Parked" );

      MetroTrain tB = new MetroTrain( "Percy" );
      tB.setPrice( 2.00 );
      tB.setCapacity( 100.0 );
      tB.setTrack( "Red" );
      tB.setStatus( "Steaming" );

      MetroTrain tC = new MetroTrain( "Gordon" );
      tC.setPrice( 3.00 );
      tC.setCapacity( 150.0 );
      tC.setTrack( "Red" );
      tC.setStatus( "Stopped" );

      // Add trains to HashMap ...

      trains.put ( tA.getName(), tA );
      trains.put ( tB.getName(), tB );
      trains.put ( tC.getName(), tC );

      // Add trains to workspace coordinate hierarchy ...

      workspace.add( tA );
      workspace.add( tB );
      workspace.add( tC );

   }

   // ==========================================================
   // Draw trains on the red line pathway ....
   // ==========================================================

   public void buildTrains() {
      double smallDotSize = 8.0;

      Set keys = pathways.keySet();
      Iterator itr = keys.iterator();
      while ( itr.hasNext() ) {
         String key     = (String) itr.next();
         MetroLinePathway path = (MetroLinePathway) pathways.get(key);
         System.out.println("*** Pathway: " + key );

         System.out.println("");
         System.out.printf("MetroLinePathway: %s\n", path.getName() );
         System.out.printf("=====================================\n");
         System.out.printf( "Source : %s\n", path.getSource().getStationName() );
         System.out.printf( "Destination : %s\n", path.getDestination().getStationName() );
         System.out.printf( "Pathway length : %6.2f\n", path.getLength() );

         // Create trains equally space along the red line pathway ...

         for (double distance = 45.0; distance < path.getLength(); distance = distance + 260.0 ) { 

             // Draw train as a sequence of three circles : part 1 ....

             Coordinate point = path.pointAtDistance( distance );
             double angle     = path.angleAtDistance( distance );
             double xCoord    = point.getX() + 5*Math.sin(angle);
             double yCoord    = point.getY() - 5*Math.cos(angle);

             Circle node1 = new Circle( xCoord, yCoord );
             node1.setRadius( smallDotSize/2.0 );

             // Draw train as two circles : part 2 ....

             point  = path.pointAtDistance( distance + 6 );
             angle  = path.angleAtDistance( distance + 6 );
             xCoord = point.getX() + 5*Math.sin(angle);
             yCoord = point.getY() - 5*Math.cos(angle);

             Circle node2 = new Circle( xCoord, yCoord );
             node2.setRadius( smallDotSize/2.0 );

             // Draw train as two circles : part 3 ....

             point = path.pointAtDistance( distance + 12 );
             angle = path.angleAtDistance( distance + 12 );
             xCoord = point.getX() + 5*Math.sin(angle);
             yCoord = point.getY() - 5*Math.cos(angle);

             Circle node3 = new Circle( xCoord, yCoord );
             node3.setRadius( smallDotSize/2.0 );

             // Draw train as two circles : part 4 ....

             point = path.pointAtDistance( distance + 18 );
             angle = path.angleAtDistance( distance + 18 );
             xCoord = point.getX() + 5*Math.sin(angle);
             yCoord = point.getY() - 5*Math.cos(angle);

             Circle node4 = new Circle( xCoord, yCoord );
             node4.setRadius( smallDotSize/2.0 );
         }
      }
   }

   // ================================================================
   // Propogate metro train info to controller and registered views
   // ================================================================

   public void setMetroTrains() {
      HashMap copy = new HashMap(this.trains);
      firePropertyChange( EngineeringController.METRO_TRAIN_PROPERTY, (HashMap) null, copy );
   }

   public void setMetroTrainsComposite() {
      CompositeHierarchy copy = workspace.clone();
      firePropertyChange( EngineeringController.METRO_TRAIN_COMPOSITE_PROPERTY,
                          (CompositeHierarchy) null, copy );
   }

   // ===========================================================
   // Handle incoming PropertyChangeEvents sent by the controller
   // ===========================================================

   public void modelPropertyChange( PropertyChangeEvent e ) {

      if (e.getPropertyName().equals( controller.METRO_PATHWAY_HASHMAP ) ) {
          System.out.println("*** Case METRO_PATHWAY_HASHMAP !!!");
          pathways = (HashMap) (e.getNewValue());
      }

   }  
}
