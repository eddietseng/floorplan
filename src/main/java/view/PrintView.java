/**
  *  ==============================================================================
  *  PrintView.java: Simple text area view for the Washington DC Metro System ...
  *  ==============================================================================
  */

package view;

import java.awt.*;
import java.awt.geom.*;
import java.awt.Point;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.List;
import java.lang.Math;
import javax.swing.*;

import controller.AbstractController;
import controller.EngineeringController;
import model.*;
import model.floorplan.FloorplanResultModel;
import model.metro.MetroStation;

public class PrintView extends AbstractView {
   boolean DEBUG = true;
   private EngineeringController controller;
   private PrintViewPanel      canvas;
   private HashMap<String,MetroStation>  stations;
   private HashMap<String,Object>          things;
   private CompositeHierarchy       gridWorkspace;
   private CompositeHierarchy      metroWorkspace;
   private CompositeHierarchy      trackWorkspace;
   private CompositeHierarchy       areaWorkspace;
   private CompositeHierarchy     trainsWorkspace;
   private CompositeHierarchy   buildingWorkspace;
   
   private FloorplanResultModel floorplanResultModel;

   public PrintView( EngineeringController controller ) {
      this.controller = controller;
   }

   public void display() {

       // Create print view panel ...

       canvas = new PrintViewPanel();
       canvas.setPreferredSize( new Dimension( 750, 600 ) );
       canvas.setBackground( Color.white );

       // Create buttons for panel along bottom of screen ...

       JFrame frame = new JFrame("Print View");
       frame.getContentPane().setLayout( new BorderLayout() );
       frame.getContentPane().add(  canvas, BorderLayout.CENTER );
       frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
       frame.pack();
       frame.setVisible(true);
   }

   // ===========================================================
   // Handle incoming PropertyChangeEvents sent by the controller
   // ===========================================================

   @Override
   public void modelPropertyChange( PropertyChangeEvent e ) {

      if (e.getPropertyName().equals( controller.METRO_STATION_PROPERTY ) ) {

          System.out.println("");
          System.out.println("*** Enter PrintView().modelPropertyChange(): case METRO_STATION_PROPERTY !!!");
          MetroStation m = (MetroStation) (e.getNewValue());
	  System.out.println( m.toString() );

          // Transfer updated metro station to hashmap ...

          stations.put ( m.toString(), m );

          // Update metro station in composite hierarchy ....

          MetroStation update = (MetroStation) metroWorkspace.find( m.toString() );
          if ( m.getSelection() == true )
             update.setSelection ( true );
          else
             update.setSelection ( false );

          if ( update != null )
             System.out.println( update.toString() );

          canvas.setMetroSystemWorkspace( metroWorkspace );

          System.out.println("*** Leaving PrintView().modelPropertyChange()");
          System.out.println("");
      }

      // Deal with hashmaps ....

      if (e.getPropertyName().equals( controller.METRO_STATION_HASHMAP ) ) {
          System.out.println("*** Inside METRO_STATION_HASHMAP !!!");
          stations = (HashMap) (e.getNewValue());
      }

      if (e.getPropertyName().equals( controller.METRO_AREA_HASHMAP ) ) {
          System.out.println("*** Inside METRO_AREA_HASHMAP !!!");
          things = (HashMap) (e.getNewValue());
      }

      // Deal with composite hierarchies ....

      if (e.getPropertyName().equals( controller.METRO_GRID_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_GRID_COMPOSITE_PROPERTY !!!");
          gridWorkspace = (CompositeHierarchy) (e.getNewValue());
          canvas.setMetroGridWorkspace( gridWorkspace );
      }

      if (e.getPropertyName().equals( controller.METRO_SYSTEM_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_SYSTEM_COMPOSITE_PROPERTY !!!");
          metroWorkspace = (CompositeHierarchy) (e.getNewValue());
          canvas.setMetroSystemWorkspace( metroWorkspace );
      }

      if (e.getPropertyName().equals( controller.METRO_AREA_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_AREA_COMPOSITE_PROPERTY !!!");
          areaWorkspace = (CompositeHierarchy) (e.getNewValue());
          canvas.setMetroAreaWorkspace( areaWorkspace );
      }

      if (e.getPropertyName().equals( controller.METRO_LINE_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_LINE_COMPOSITE_PROPERTY !!!");
          trackWorkspace = (CompositeHierarchy) (e.getNewValue());
          canvas.setMetroTrackWorkspace( trackWorkspace );
      }

      if (e.getPropertyName().equals( controller.METRO_TRAIN_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_TRAIN_COMPOSITE_PROPERTY !!!");
          trainsWorkspace = (CompositeHierarchy) (e.getNewValue());
          canvas.setMetroTrainsWorkspace( trainsWorkspace );
      }

      // Deal with building-hvac system composite hierarchy ....

      if (e.getPropertyName().equals( controller.BUILDING_LAYOUT_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside BUILDING_LAYOUT_COMPOSITE_PROPERTY !!!");
          buildingWorkspace = (CompositeHierarchy) (e.getNewValue());
          canvas.setBuildingLayoutWorkspace( buildingWorkspace );
      }
      
      // floorplan result model print view
      
      if(e.getPropertyName().equals( controller.FLOORPLAN_SELECTED_SUMMARY ) ) {
    	  System.out.println("*** Inside FLOORPLAN_SELECTED_SUMMARY !!!");
    	  floorplanResultModel = (FloorplanResultModel) (e.getNewValue());
    	  canvas.setFloorplanResultModel( floorplanResultModel );
      }
   }  
}
