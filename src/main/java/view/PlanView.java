/**
  *  ==============================================================================
  *  PlanView.java: Infrastructure for drawing plan views of engineering systems.
  *
  *  Written By: Mark Austin                                             July, 2012
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
import model.AbstractCompoundFeature;
import model.CompositeHierarchy;
import model.MetroSystemModel;
import model.metro.MetroStation;
import model.metro.MetroLineModel;
import model.metro.MetroLinePathway;
import model.Coordinate;

import view.shape.*;          // Import shape classes 

public class PlanView extends AbstractView {
   boolean DEBUG = true;
   private EngineeringController         controller;

   private CompositeHierarchy      gridWorkspace;
   private CompositeHierarchy     metroWorkspace;
   private CompositeHierarchy     trackWorkspace;
   private CompositeHierarchy      areaWorkspace;
   private CompositeHierarchy    trainsWorkspace;
   private CompositeHierarchy  buildingWorkspace;
   private CompositeHierarchy floorplanWorkspace;

   private HashMap<String,Object>               grid;
   private HashMap<String,Object>             things;
   private HashMap<String,MetroStation>     stations;
   private HashMap<String,MetroLineModel>     tracks;
   private HashMap<String,Object>              lines;
   private HashMap<String,MetroLinePathway> pathways;
   private HashMap<String,Object>          buildings;
   private HashMap<String,Object>         floorplans;
   
   private boolean displayTag = false;

   private PlanViewPanel pvp;

   public PlanView( EngineeringController controller ) {
      this.controller = controller;
   }

   public void display() {

       // Create graphics panel ...

       pvp = new PlanViewPanel( controller );
       pvp.initComponents();

       // Display frame ... 

       JFrame frame = new JFrame("Plan View");
       frame.getContentPane().setLayout( new BorderLayout() );
       frame.getContentPane().add(  pvp, BorderLayout.CENTER );
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
          System.out.println("*** Enter PlanView().modelPropertyChange(): case METRO_STATION_PROPERTY !!!");
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

          // canvas.setMetroSystemWorkspace( metroWorkspace );

          System.out.println("*** Leaving PrintView().modelPropertyChange()");
          System.out.println("");
      }

      // Deal with grid, station, track and pathway hashmaps ....

      if (e.getPropertyName().equals( controller.METRO_GRID_HASHMAP ) ) {
          System.out.println("*** Inside PrintView(): case METRO_GRID_HASHMAP !!!");
          grid = (HashMap) (e.getNewValue());
          pvp.setMetroGridData( grid );
      }
      
      if(e.getPropertyName().equals( controller.FLOORPLAN_TAG_PROPERTY ) ) {
    	  System.out.println("*** Inside PrintView(): case FLOORPLAN_TAG_PROPERTY !!!");
          displayTag = (Boolean) (e.getNewValue());
          pvp.setDisplayTag( displayTag );
      }

      if (e.getPropertyName().equals( controller.METRO_STATION_HASHMAP ) ) {
          System.out.println("*** Inside METRO_STATION_HASHMAP !!!");
          stations = (HashMap) (e.getNewValue());
      }

      if (e.getPropertyName().equals( controller.METRO_AREA_HASHMAP ) ) {
          System.out.println("*** Inside METRO_AREA_HASHMAP !!!");
          things = (HashMap) (e.getNewValue());
      }

      if (e.getPropertyName().equals( controller.METRO_LINE_HASHMAP ) ) {
          System.out.println("*** Case METRO_LINE_HASHMAP !!!");
          tracks = (HashMap) (e.getNewValue());
      }

      if (e.getPropertyName().equals( controller.METRO_PATHWAY_HASHMAP ) ) {
          System.out.println("*** Case METRO_PATHWAY_HASHMAP !!!");
          pathways = (HashMap) (e.getNewValue());
      }

      // Deal with building-hvac hashmap ....

      if (e.getPropertyName().equals( controller.BUILDING_LAYOUT_HASHMAP ) ) {
          System.out.println("*** Inside BUILDING_LAYOUT_HASHMAP !!!");
          buildings = (HashMap) (e.getNewValue());
      }

      // Deal with floorplan hashmap and composite hierarchy ....

      if (e.getPropertyName().equals( controller.FLOORPLAN_HASHMAP ) ) {
          System.out.println("*** Inside controller.FLOORPLAN_HASHMAP !!!");
          floorplans = (HashMap) (e.getNewValue());
      }

      if (e.getPropertyName().equals( controller.FLOORPLAN_WORKSPACE ) ) {
          System.out.println("*** Inside controller.FLOORPLAN_WORKSPACE !!!");
          floorplanWorkspace = (CompositeHierarchy) (e.getNewValue());
          pvp.setFloorplanWorkspace( floorplanWorkspace );
      }

      // Deal with metro system composite hierarchies ....

      if (e.getPropertyName().equals( controller.METRO_GRID_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_GRID_COMPOSITE_PROPERTY !!!");
          gridWorkspace = (CompositeHierarchy) (e.getNewValue());
          pvp.setMetroGridWorkspace( gridWorkspace );
      }

      if (e.getPropertyName().equals( controller.METRO_SYSTEM_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_SYSTEM_COMPOSITE_PROPERTY !!!");
          metroWorkspace = (CompositeHierarchy) (e.getNewValue());
          pvp.setMetroSystemWorkspace( metroWorkspace );
      }

      if (e.getPropertyName().equals( controller.METRO_LINE_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_LINE_COMPOSITE_PROPERTY !!!");
          trackWorkspace = (CompositeHierarchy) (e.getNewValue());
          pvp.setMetroTrackWorkspace( trackWorkspace );
      }

      if (e.getPropertyName().equals( controller.METRO_AREA_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_AREA_COMPOSITE_PROPERTY !!!");
          areaWorkspace = (CompositeHierarchy) (e.getNewValue());
          pvp.setMetroAreaWorkspace( areaWorkspace );
      }

      if (e.getPropertyName().equals( controller.METRO_TRAIN_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside METRO_TRAIN_COMPOSITE_PROPERTY !!!");
          trainsWorkspace = (CompositeHierarchy) (e.getNewValue());
          pvp.setMetroTrainsWorkspace( trainsWorkspace );
      }

      // Deal with building-hvac system composite hierarchy ....

      if (e.getPropertyName().equals( controller.BUILDING_LAYOUT_COMPOSITE_PROPERTY ) ) {
          System.out.println("*** Inside BUILDING_LAYOUT_COMPOSITE_PROPERTY !!!");
          buildingWorkspace = (CompositeHierarchy) (e.getNewValue());
          pvp.setBuildingLayoutWorkspace( buildingWorkspace );
      }
      
      if (e.getPropertyName().equals( controller.FLOORPLAN_SELECTED_PROPERTY_STATUS ) ) {
    	  System.out.println("*** Inside FLOORPLAN_SELECTED_PROPERTY_STATUS !!!");
    	  AbstractCompoundFeature newObject = (AbstractCompoundFeature) (e.getNewValue());
    	  pvp.setFloorplanSelectedStatus( newObject ); 
      }
   }  
}
