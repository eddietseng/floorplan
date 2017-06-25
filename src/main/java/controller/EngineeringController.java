package controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import view.AbstractView;
import model.AbstractCompoundFeature;
import model.AbstractFeature;
import model.floorplan.FloorplanResultModel;
import model.floorplan.RoomModel;
import model.floorplan.Space2DModel;
import model.floorplan.UtilityModel;
import model.metro.MetroStation;
import model.metro.MetroTrain;
import model.util.*;

public class EngineeringController extends AbstractController {
   protected Boolean DEBUG = true;

   // Define vocabulary for interaction of controller with models and views ...

   public static final String METRO_STATION_PROPERTY = "MetroStationProperty";
   public static final String METRO_LINE_PROPERTY    = "MetroLineProperty";
   public static final String METRO_PATHWAY_PROPERTY = "MetroPathwaysProperty";
   public static final String METRO_AREA_PROPERTY    = "MetroAreaProperty";
   public static final String METRO_GRID_PROPERTY    = "MetroGridProperty";
   public static final String METRO_SNAP_PROPERTY    = "MetroSnapProperty";
   public static final String METRO_TRAIN_PROPERTY   = "MetroTrainsProperty";

   public static final String METRO_STATION_HASHMAP = "MetroStationHashMap";
   public static final String METRO_LINE_HASHMAP    = "MetroLineHashMap";
   public static final String METRO_PATHWAY_HASHMAP = "MetroPathwaysHashMap";
   public static final String METRO_AREA_HASHMAP    = "MetroAreaHashMap";
   public static final String METRO_GRID_HASHMAP    = "MetroGridHashMap";
   public static final String METRO_TRAIN_HASHMAP   = "MetroTrainsHashMap";

   public static final String METRO_SYSTEM_COMPOSITE_PROPERTY = "MetroSystemComposite";
   public static final String METRO_AREA_COMPOSITE_PROPERTY   = "MetroAreaComposite";
   public static final String METRO_LINE_COMPOSITE_PROPERTY   = "MetroLineComposite";
   public static final String METRO_GRID_COMPOSITE_PROPERTY   = "MetroGridComposite";
   public static final String METRO_TRAIN_COMPOSITE_PROPERTY  = "MetroTrainsComposite";

   // Vocabulary for building-hvac system ....

   public static final String FLOORPLAN_TASK_PROPERTY            = "FloorplanTaskProperty";
   public static final String FLOORPLAN_SELECTION_PROPERTY       = "FloorplanSelectionProperty";
   public static final String FLOORPLAN_SELECTED_PROPERTY_STATUS = "FloorplanSelectedPropertyStatus";
   public static final String FLOORPLAN_DEFINE_ROOM_PROPERTY     = "FloorplanDefineRoomProperty";
   public static final String FLOORPLAN_UPDATE                   = "FloorplanPropertyUpdate";
   public static final String FLOORPLAN_UTILITY                  = "FloorplanUtility";
   public static final String FLOORPLAN_SUMMARY                  = "FloorplanSummary";
   
   public static final String FLOORPLAN_HASHMAP                  = "FloorplanHashMap";
   public static final String FLOORPLAN_WORKSPACE                = "FloorplanWorkspace";
   public static final String FLOORPLAN_TAG_PROPERTY             = "FloorplanDisplayTag";
   public static final String FLOORPLAN_SELECTED_SUMMARY         = "FloorplanResultSelected";
   
   public static final String FLOORPLAN_EXAMPLE_1                = "FloorplanExampleOne";
   public static final String FLOORPLAN_TRADEOFF_CASE            = "FloorplanTradeOffCaseData";
   public static final String FLOORPLAN_CASE_CITY                = "FloorplanCaseCity";
   public static final String FLOORPLAN_MULTICASES               = "FloorplanMultiCases";
   
   public static final String BUILDING_LAYOUT_PROPERTY           = "BuildingLayoutProperty";
   public static final String BUILDING_LAYOUT_HASHMAP            = "BuildingLayoutHashMap";
   public static final String BUILDING_LAYOUT_COMPOSITE_PROPERTY = "BuildingLayoutComposite";

   

   // Callback methods for floorplan model .....

   public void changeFloorplanTaskStatus ( String task ) {
      System.out.println("*** In FloorplanController.changeFloorplanTaskStatus(): task = " + task );
      setModelProperty( FLOORPLAN_TASK_PROPERTY, task );
   }

   public void changeFloorplanSelectionStatus ( CoordinatePair pair ) {
      System.out.println("*** In FloorplanController.changeFloorplanSelectionStatus() ... " );
      setModelProperty( FLOORPLAN_SELECTION_PROPERTY, pair );
   }
   
   public void changeFloorplanComponentStatus ( AbstractFeature f ) {
	  System.out.println("*** In FloorplanController.changeFloorplanComponentStatus() ... " );
	  setModelProperty( FLOORPLAN_SELECTED_PROPERTY_STATUS, ((AbstractFeature)f) );
   }
   
   public void defineFloorplanRoomComponent ( RoomModel room ) {
	   System.out.println("*** In FloorplanController.defineFloorplanRoomComponent() ... " );
	   setModelProperty( FLOORPLAN_DEFINE_ROOM_PROPERTY, room );
   }
   
   public void updateFloorplanProperty( int n ) {
	   System.out.println("*** In FloorplanController.updateFloorplanProperty()");
	   setModelProperty( FLOORPLAN_UPDATE, n );
   }
   
   public void setFloorplanUtility( UtilityModel model ) {
	   System.out.println("*** In FloorplanController.setFloorplanUtility()");
	   setModelProperty( FLOORPLAN_UTILITY, model );
   }
   
   public void setFloorplanSummary( FloorplanResultModel model ) {
	   System.out.println("*** In FloorplanController.setFloorplanSummary()");
	   setModelProperty( FLOORPLAN_SUMMARY, model );
   }
   
   public void setFloorplanResultSelected( FloorplanResultModel model ) {
	   System.out.println("*** In FloorplanController.setFloorplanSummary()");
	   setModelProperty( FLOORPLAN_SELECTED_SUMMARY, model );
   }
   
   /**
    * Pass in the case number that will be used in tradeoff
    */
   public void setFloorplanCityTradeOffData( int[] caseNum ) {
	   System.out.println("*** In FloorplanController.setFloorplanCityTradeOffData()");
	   setModelProperty( FLOORPLAN_TRADEOFF_CASE, caseNum );
   }

   // Callback method for grid size model .....

   public void changeSnapToGridStatus ( Boolean newSnapToGrid ) {
      System.out.println("*** In MetroController.changeSnapToGridStatus() ... ");
      setModelProperty( METRO_SNAP_PROPERTY, newSnapToGrid );
   }
   
   public void changeDisplayTagStatus( boolean bDisplayTag ) {
	   System.out.println("*** In MetroController.changeDisplayTagStatus() ... " + bDisplayTag );
	   setModelProperty( FLOORPLAN_TAG_PROPERTY, bDisplayTag );
   }
   
   // Auto import example 1
   public void importExample1( boolean b ) {
	   System.out.println("*** In MetroController.importExample1() ... ");
	   setModelProperty( FLOORPLAN_EXAMPLE_1, b );
   }

   public void changeMetroGridStatus ( Integer newGridSize ) {
      System.out.println("*** In MetroController.changeMetroGridStatus() ... ");
      setModelProperty( METRO_GRID_PROPERTY, newGridSize );
   }

   // Callback method for metrostation model status .....

   public void changeMetroStationStatus ( MetroStation m ) {
      System.out.println("*** In MetroController.changeMetroStationStatus() ... ");
      setModelProperty( METRO_STATION_PROPERTY, m );
   }

   // Callback method for metroline status .....

   public void changeMetroLineStatus ( Hashtable s ) {
      setModelProperty( METRO_LINE_PROPERTY, s );
   }

   // Callback method for metro pathway status .....

   public void changeMetroPathwayStatus ( Hashtable s ) {
      setModelProperty( METRO_PATHWAY_PROPERTY, s );
   }

   // Callback method for metro train status .....

   public void changeMetroTrainStatus ( MetroTrain t ) {
      setModelProperty( METRO_TRAIN_PROPERTY, t );
   }

   // Callback method for metro area status .....

   public void changeMetroAreaStatus ( MetroTrain t ) {
      setModelProperty( METRO_AREA_PROPERTY, t );
   }

   public void modelPropertyChanged(PropertyChangeEvent e) {
      if( DEBUG == true ) {
          System.out.println("*** In MetroController().modelPropertyChanged() ... ");
      }
   }
}
