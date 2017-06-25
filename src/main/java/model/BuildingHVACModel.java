/*
 *  ====================================================================
 *  java-code-new.d/java-beans8.d/src/model/BuildingLayoutModel.java
 *  ====================================================================
 */

package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import java.beans.PropertyChangeEvent;

import model.primitive.*;
import model.urban.*;
import model.hvac.*;
import controller.EngineeringController;

public class BuildingHVACModel extends AbstractModel {
   private Boolean DEBUG = true;
   private CompositeHierarchy  workspace;
   HashMap<String,Object>         things;

   // Constructor ....

   public BuildingHVACModel() {
      System.out.println("Create Building-HVAC System Layout Model");
      System.out.println("========================================");

      things    = new HashMap<String,Object>();
      workspace = new CompositeHierarchy( 0.0, 0.0, 0.0 );
      workspace.setName("Metro Area Workspace");
   }

   // Build model of Metro system area things ...

   public void initDefault() {
      buildBuildingLayout();
      setBuildingLayout();
      setBuildingLayoutComposite();
   }

   // ======================================================================
   // Create composite of Metro Area objects ...
   // ======================================================================

   public void buildBuildingLayout() {

      // =====================================================
      // Setup pipe to fan02 ....
      // =====================================================

      int width = 20;

      PipelineRegion region = new PipelineRegion( "pipe 01" );
      region.add( new Coordinate("P 01",   0.0,  -60.0 ) );
      region.add( new Coordinate("P 02",   0.0, -100.0 ) );
      region.add( new Coordinate("P 03", 300.0, -100.0 ) );
      region.add( new Coordinate("P 04", 300.0,  -25.0 ) );

      region.assembleRegion ( width );
      ArrayList<Coordinate> perimeter = region.getRegion();

      String pipeName = "Pipe01-Sec01";
      PipelineModel pipe01pt1 = new PipelineModel( pipeName, perimeter );
      pipe01pt1.setDisplayName( false );
      pipe01pt1.setPrice( 50.0 );

      region = new PipelineRegion( "pipe 01" );
      region.add( new Coordinate("P 05", 300.0,   25.0 ) );
      region.add( new Coordinate("P 06", 300.0,  100.0 ) );
      region.add( new Coordinate("P 07", 390.0,  100.0 ) );

      region.assembleRegion ( width );
      perimeter = region.getRegion();

      pipeName = "Pipe01-Sec02";
      PipelineModel pipe01pt2 = new PipelineModel( pipeName, perimeter );
      pipe01pt2.setDisplayName( false );
      pipe01pt2.setPrice( 50.0 );

      // =====================================================
      // Setup the "chiller" and "fan01" pipeline ....
      // =====================================================

      width = 20;

      region = new PipelineRegion( "chiller and fan" );
      region.add( new Coordinate("P 01",   0.0,  160.0 ) );
      region.add( new Coordinate("P 02",   0.0,  250.0 ) );
      region.add( new Coordinate("P 03",  50.0,  250.0 ) );

      region.assembleRegion ( width );
      perimeter = region.getRegion();

      pipeName = "Pipe02-Sec01";
      PipelineModel pipe02pt1 = new PipelineModel( pipeName, perimeter );
      pipe02pt1.setDisplayName( false );
      pipe02pt1.setPrice( 50.0 );

      region = new PipelineRegion( "chiller and fan" );
      region.add( new Coordinate("P 04", 150.0,  250.0 ) );
      region.add( new Coordinate("P 05", 200.0,  250.0 ) );
      region.add( new Coordinate("P 06", 200.0,  200.0 ) );
      region.add( new Coordinate("P 07", 250.0,  200.0 ) );

      region.assembleRegion ( width );
      perimeter = region.getRegion();

      pipeName = "Pipe02-Sec02";
      PipelineModel pipe02pt2 = new PipelineModel( pipeName, perimeter );
      pipe02pt2.setDisplayName( false );
      pipe02pt2.setPrice( 50.0 );

      region = new PipelineRegion( "chiller and fan" );
      region.add( new Coordinate("P 08", 300.0,  200.0 ) );
      region.add( new Coordinate("P 09", 340.0,  200.0 ) );
      region.add( new Coordinate("P 10", 390.0,  200.0 ) );

      region.assembleRegion ( width );
      perimeter = region.getRegion();

      pipeName = "Pipe02-Sec03";
      PipelineModel pipe02pt3 = new PipelineModel( pipeName, perimeter );
      pipe02pt3.setDisplayName( false );
      pipe02pt3.setPrice( 50.0 );

      // =====================================================
      // Define "controller" object ...
      // =====================================================

      ControllerModel controller01 = new ControllerModel( 50.0, 350.0, 150.0, 100.0 );
      controller01.setName("HVAC Control");
      controller01.setColor( Color.green );
      controller01.setFilledShape( true );
      controller01.setTextOffSetX(  30 );
      controller01.setTextOffSetY(  45 );
      controller01.setPrice( 1000.0 );

      // =====================================================
      // Define "sensor" objects for Room 1 ...
      // =====================================================

      SensorModel sensor01 = new SensorModel( -53.0, 134.0, 6.0 );
      sensor01.setName("Room 1, Sensor 1");
      sensor01.setDisplayName( false );
      sensor01.setColor( Color.orange );
      sensor01.setFilledShape( true );
      sensor01.setPrice( 10.0 );

      SensorModel sensor02 = new SensorModel(  47.0, 134.0, 6.0 );
      sensor02.setName("Room 1, Sensor 2");
      sensor02.setDisplayName( false );
      sensor02.setColor( Color.orange );
      sensor02.setFilledShape( true );
      sensor02.setPrice( 10.0 );

      SensorModel sensor03 = new SensorModel( -53.0,  47.0, 6.0 );
      sensor03.setName("Room 1, Sensor 3");
      sensor03.setDisplayName( false );
      sensor03.setColor( Color.orange );
      sensor03.setFilledShape( true );
      sensor03.setPrice( 10.0 );

      SensorModel sensor04 = new SensorModel(  47.0,  47.0, 6.0 );
      sensor04.setName("Room 1, Sensor 4");
      sensor04.setDisplayName( false );
      sensor04.setColor( Color.orange );
      sensor04.setFilledShape( true );
      sensor04.setPrice( 10.0 );

      SensorModel sensor05 = new SensorModel( -53.0, -40.0, 6.0 );
      sensor05.setName("Room 1, Sensor 5");
      sensor05.setDisplayName( false );
      sensor05.setColor( Color.orange );
      sensor05.setFilledShape( true );
      sensor05.setPrice( 10.0 );

      SensorModel sensor06 = new SensorModel(  47.0, -40.0, 6.0 );
      sensor06.setName("Room 1, Sensor 6");
      sensor06.setDisplayName( false );
      sensor06.setColor( Color.orange );
      sensor06.setFilledShape( true );
      sensor06.setPrice( 10.0 );

      // =====================================================
      // Define "sensor" objects for Room 2 ...
      // =====================================================

      SensorModel sensor07 = new SensorModel( 410.0, 75.0, 6.0 );
      sensor07.setName("Room 2, Sensor 1");
      sensor07.setDisplayName( false );
      sensor07.setColor( Color.orange );
      sensor07.setFilledShape( true );
      sensor07.setPrice( 10.0 );

      SensorModel sensor08 = new SensorModel( 410.0, 147.0, 6.0 );
      sensor08.setName("Room 2, Sensor 2");
      sensor08.setDisplayName( false );
      sensor08.setColor( Color.orange );
      sensor08.setFilledShape( true );
      sensor08.setPrice( 10.0 );

      SensorModel sensor09 = new SensorModel( 410.0, 222.0, 6.0 );
      sensor09.setName("Room 2, Sensor 3");
      sensor09.setDisplayName( false );
      sensor09.setColor( Color.orange );
      sensor09.setFilledShape( true );
      sensor09.setPrice( 10.0 );

      SensorModel sensor10 = new SensorModel( 584.0, 75.0, 6.0 );
      sensor10.setName("Room 2, Sensor 4");
      sensor10.setDisplayName( false );
      sensor10.setColor( Color.orange );
      sensor10.setFilledShape( true );
      sensor10.setPrice( 10.0 );

      SensorModel sensor11 = new SensorModel( 584.0, 147.0, 6.0 );
      sensor11.setName("Room 2, Sensor 5");
      sensor11.setDisplayName( false );
      sensor11.setColor( Color.orange );
      sensor11.setFilledShape( true );
      sensor11.setPrice( 10.0 );

      SensorModel sensor12 = new SensorModel( 584.0, 222.0, 6.0 );
      sensor12.setName("Room 2, Sensor 6");
      sensor12.setDisplayName( false );
      sensor12.setColor( Color.orange );
      sensor12.setFilledShape( true );
      sensor12.setPrice( 10.0 );


      // =====================================================
      // Define "chiller" and "fan" objects ...
      // =====================================================

      ChillerModel chiller01 = new ChillerModel(  50.0, 200.0, 100.0 );
      chiller01.setName("Big Chiller");
      chiller01.setFilledShape( true );
      chiller01.setTextOffSetX(   0 );
      chiller01.setTextOffSetY( 110 );
      chiller01.setColor( Color.green );
      chiller01.setPrice( 1000.0 );

      FanModel fan01 = new FanModel(  250.0, 175.0, 50.0 );
      fan01.setName("Fan 01");
      fan01.setFilledShape( true );
      fan01.setTextOffSetX(  0 );
      fan01.setTextOffSetY( 60 );
      fan01.setColor( Color.green );
      fan01.setPrice( 200.0 );

      FanModel fan02 = new FanModel(  0.0, 0.0, 50.0 );
      fan02.setName("Fan 02");
      fan02.setFilledShape( true );
      fan02.setTextOffSetX(  0 );
      fan02.setTextOffSetY( 60 );
      fan02.setColor( Color.green );
      fan02.setPrice( 200.0 );

      // ============================================================
      // Connect controller to fan and chiller objects with wires ...
      // ============================================================

      // Wire 1 ....

      width = 2;
      PipelineRegion wregion = new PipelineRegion( "wire 01" );
      wregion.add( new Coordinate("W 01", 200.0,  400.0 ) );
      wregion.add( new Coordinate("W 02", 325.0,  400.0 ) );
      wregion.add( new Coordinate("W 03", 325.0,  220.0 ) );
      wregion.add( new Coordinate("W 04", 300.0,  220.0 ) );

      wregion.assembleRegion ( width );
      ArrayList<Coordinate> wperimeter = wregion.getRegion();

      String wireName = "Wire 01";
      PipelineModel wire01pt1 = new PipelineModel( wireName, wperimeter );
      wire01pt1.setDisplayName( false );
      wire01pt1.setFilledShape( true );
      wire01pt1.setPrice( 5.0 );

      // Wire 2 ....

      wregion = new PipelineRegion( "wire 02" );
      wregion.add( new Coordinate("W 01", 200.0,  375.0 ) );
      wregion.add( new Coordinate("W 02", 225.0,  375.0 ) );
      wregion.add( new Coordinate("W 03", 225.0,  275.0 ) );
      wregion.add( new Coordinate("W 04", 150.0,  275.0 ) );

      wregion.assembleRegion ( width );
      wperimeter = wregion.getRegion();

      wireName = "Wire 02";
      PipelineModel wire02pt1 = new PipelineModel( wireName, wperimeter );
      wire02pt1.setDisplayName( false );
      wire02pt1.setFilledShape( true );
      wire02pt1.setPrice( 5.0 );

      // Wire 3 ....

      wregion = new PipelineRegion( "wire 03" );
      wregion.add( new Coordinate("W 01", 200.0,  425.0 ) );
      wregion.add( new Coordinate("W 02", 350.0,  425.0 ) );
      wregion.add( new Coordinate("W 03", 350.0,  215.0 ) );

      wregion.assembleRegion ( width );
      wperimeter = wregion.getRegion();

      wireName = "Wire 03";
      PipelineModel wire03pt1 = new PipelineModel( wireName, wperimeter );
      wire03pt1.setDisplayName( false );
      wire03pt1.setFilledShape( true );
      wire03pt1.setPrice( 5.0 );

      wregion = new PipelineRegion( "wire 03" );
      wregion.add( new Coordinate("W 01", 350.0,  185.0 ) );
      wregion.add( new Coordinate("W 02", 350.0,  150.0 ) );
      wregion.add( new Coordinate("W 03", 350.0,  115.0 ) );

      wregion.assembleRegion ( width );
      wperimeter = wregion.getRegion();

      wireName = "Wire 03";
      PipelineModel wire03pt2 = new PipelineModel( wireName, wperimeter );
      wire03pt2.setDisplayName( false );
      wire03pt2.setFilledShape( true );
      wire03pt2.setPrice( 5.0 );

      wregion = new PipelineRegion( "wire 03" );
      wregion.add( new Coordinate("W 01", 350.0,  85.0 ) );
      wregion.add( new Coordinate("W 02", 350.0,   0.0 ) );
      wregion.add( new Coordinate("W 03", 325.0,   0.0 ) );

      wregion.assembleRegion ( width );
      wperimeter = wregion.getRegion();

      wireName = "Wire 03";
      PipelineModel wire03pt3 = new PipelineModel( wireName, wperimeter );
      wire03pt3.setDisplayName( false );
      wire03pt3.setFilledShape( true );
      wire03pt3.setPrice( 5.0 );

      // =====================================================
      // Add wallplate object to the model ...
      // =====================================================

      WallPlateModel wallplate01 = new WallPlateModel(  -10.0, 140.0, 20.0 );
      wallplate01.setName("WallPlate 01");
      wallplate01.setFilledShape( true );
      wallplate01.setColor( Color.orange );
      wallplate01.setDisplayName( false );
      wallplate01.setPrice( 10.0 );

      WallPlateModel wallplate02 = new WallPlateModel(  -10.0, -60.0, 20.0 );
      wallplate02.setName("WallPlate 02");
      wallplate02.setFilledShape( true );
      wallplate02.setColor( Color.orange );
      wallplate02.setDisplayName( false );
      wallplate02.setPrice( 10.0 );

      WallPlateModel wallplate03 = new WallPlateModel(  0.0, 0.0, 20.0 );
      wallplate03.setName("WallPlate 03");
      wallplate03.setFilledShape( true );
      wallplate03.setColor( Color.orange );
      wallplate03.setDisplayName( false );
      wallplate03.setPrice( 10.0 );

      WallPlateModel wallplate04 = new WallPlateModel(  0.0, 0.0, 20.0 );
      wallplate04.setName("WallPlate 04");
      wallplate04.setFilledShape( true );
      wallplate04.setColor( Color.orange );
      wallplate04.setDisplayName( false );
      wallplate04.setPrice( 10.0 );

      // =====================================================
      // Mimic Room 01 with wall perimeters ....
      // =====================================================

      width = 20;

      region = new PipelineRegion( "room" );
      region.add( new Coordinate("R 01",   10.0,  -50.0 ) );
      region.add( new Coordinate("R 01",  150.0,  -50.0 ) );
      region.add( new Coordinate("R 01",  150.0,  150.0 ) );
      region.add( new Coordinate("R 01",   10.0,  150.0 ) );

      region.assembleRegion ( width );
      perimeter = region.getRegion();

      String roomName = "Room01 Pt01";
      PipelineModel room01pt1 = new PipelineModel( roomName, perimeter );
      room01pt1.setDisplayName( false );
      room01pt1.setFilledShape( true );
      room01pt1.setColor( Color.blue );
      room01pt1.setPrice( 100.0 );

      region = new PipelineRegion( "room" );
      region.add( new Coordinate("R 01",  -10.0,  150.0 ) );
      region.add( new Coordinate("R 01", -150.0,  150.0 ) );
      region.add( new Coordinate("R 01", -150.0,  -50.0 ) );
      region.add( new Coordinate("R 01",  -10.0,  -50.0 ) );

      width = 20;
      region.assembleRegion ( width );
      perimeter = region.getRegion();

      roomName = "Room01 Pt02";
      PipelineModel room01pt2 = new PipelineModel( roomName, perimeter );
      room01pt2.setDisplayName( false );
      room01pt2.setFilledShape( true );
      room01pt2.setColor( Color.blue );
      room01pt2.setPrice( 100.0 );

      // =====================================================
      // Mimic Room 02 with wall perimeters ....
      // =====================================================

      region = new PipelineRegion( "room 02" );
      region.add( new Coordinate("R 01",  400.0,  210.0 ) );
      region.add( new Coordinate("R 01",  400.0,  300.0 ) );
      region.add( new Coordinate("R 01",  600.0,  300.0 ) );
      region.add( new Coordinate("R 01",  600.0,  000.0 ) );
      region.add( new Coordinate("R 01",  400.0,  000.0 ) );
      region.add( new Coordinate("R 01",  400.0,   90.0 ) );

      width = 20;
      region.assembleRegion ( width );
      perimeter = region.getRegion();

      roomName = "Room02 Pt01";
      PipelineModel room02pt1 = new PipelineModel( roomName, perimeter );
      room02pt1.setDisplayName( false );
      room02pt1.setFilledShape( true );
      room02pt1.setColor( Color.blue );
      room02pt1.setPrice( 100.0 );

      region = new PipelineRegion( "room 02" );
      region.add( new Coordinate("R 01",  400.0,  110.0 ) );
      region.add( new Coordinate("R 01",  400.0,  150.0 ) );
      region.add( new Coordinate("R 01",  400.0,  190.0 ) );

      width = 20;
      region.assembleRegion ( width );
      perimeter = region.getRegion();

      roomName = "Room02 Pt02";
      PipelineModel room02pt2 = new PipelineModel( roomName, perimeter );
      room02pt2.setDisplayName( false );
      room02pt2.setFilledShape( true );
      room02pt2.setColor( Color.blue );
      room02pt2.setPrice( 100.0 );

      // Add objects to HashMap ...

      things.put (      "pipe01-pt1",   pipe01pt1 );
      things.put (      "pipe01-pt2",   pipe01pt2 );
      things.put (      "pipe02-pt1",   pipe02pt1 );
      things.put (      "pipe02-pt2",   pipe02pt2 );
      things.put (      "pipe02-pt3",   pipe02pt3 );
      things.put (       "chiller01",   chiller01 );
      things.put (      "wire01-pt1",   wire01pt1 );
      things.put (      "wire02-pt1",   wire02pt1 );
      things.put (      "wire03-pt1",   wire03pt1 );
      things.put (      "wire03-pt2",   wire03pt2 );
      things.put (      "wire03-pt3",   wire03pt3 );
      things.put (           "fan01",       fan01 );
      things.put (           "fan02",       fan02 );
      things.put (    "controller01", controller01 );
      things.put (        "sensor01",     sensor01 );
      things.put (        "sensor02",     sensor02 );
      things.put (        "sensor03",     sensor03 );
      things.put (        "sensor04",     sensor04 );
      things.put (        "sensor05",     sensor05 );
      things.put (        "sensor06",     sensor06 );
      things.put (        "sensor07",     sensor07 );
      things.put (        "sensor08",     sensor08 );
      things.put (        "sensor09",     sensor09 );
      things.put (        "sensor10",     sensor10 );
      things.put (        "sensor11",     sensor11 );
      things.put (        "sensor12",     sensor12 );
      things.put (     "wallplate01", wallplate01 );
      things.put (     "wallplate02", wallplate02 );
      things.put (     "wallplate03", wallplate03 );
      things.put (     "wallplate04", wallplate04 );
      things.put (   "room 01 part1",   room01pt1 );
      things.put (   "room 01 part2",   room01pt2 );
      things.put (   "room 02 part1",   room02pt1 );
      things.put (   "room 02 part2",   room02pt2 );

      System.out.println ( things );

      // Assemble composite hierarchy ...

      CompositeHierarchy area6 = new CompositeHierarchy(  325.0, -25.0, Math.PI/2.0 );
      area6.add( fan02 );

      CompositeHierarchy area7 = new CompositeHierarchy(  410.0,  90.0, Math.PI/2.0 );
      area7.add( wallplate03 );

      CompositeHierarchy area8 = new CompositeHierarchy(  410.0, 190.0, Math.PI/2.0 );
      area8.add( wallplate04 );

      // Assemble workspace hierarchy ....

      workspace.add( pipe01pt1 );
      workspace.add( pipe01pt2 );
      workspace.add( pipe02pt1 );
      workspace.add( pipe02pt2 );
      workspace.add( pipe02pt3 );
      workspace.add( wire01pt1 );
      workspace.add( wire02pt1 );
      workspace.add( wire03pt1 );
      workspace.add( wire03pt2 );
      workspace.add( wire03pt3 );
      workspace.add( chiller01 );
      workspace.add( fan01 );
      workspace.add( area6 );
      workspace.add( controller01 );
      workspace.add( sensor01 );
      workspace.add( sensor02 );
      workspace.add( sensor03 );
      workspace.add( sensor04 );
      workspace.add( sensor05 );
      workspace.add( sensor06 );
      workspace.add( sensor07 );
      workspace.add( sensor08 );
      workspace.add( sensor09 );
      workspace.add( sensor10 );
      workspace.add( sensor11 );
      workspace.add( sensor12 );
      workspace.add( wallplate01 );
      workspace.add( wallplate02 );
      workspace.add( area7 );
      workspace.add( area8 );
      workspace.add( room01pt1 );
      workspace.add( room01pt2 );
      workspace.add( room02pt1 );
      workspace.add( room02pt2 );
   }

   // ===============================================================
   // Propogate metro area objects and composite to controller ....
   // ===============================================================

   public void setBuildingLayout() {
      HashMap copy = new HashMap( things );
      firePropertyChange( EngineeringController.BUILDING_LAYOUT_HASHMAP, (HashMap) null, copy );
   }

   public void setBuildingLayoutComposite() {
      CompositeHierarchy copy = workspace.clone();
      firePropertyChange( EngineeringController.BUILDING_LAYOUT_COMPOSITE_PROPERTY,
                          (CompositeHierarchy) null, copy );
   }
}
