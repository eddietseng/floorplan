/*
 *  ====================================================================
 *  MetroGridModel.java: ....
 *
 *  Written By: Mark Austin                                    July 2012
 *  ====================================================================
 */

package model;

import java.lang.Math.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.font.*;
import java.awt.image.*;

import java.beans.PropertyChangeEvent;

import model.primitive.Circle;
import controller.EngineeringController;

public class MetroGridModel extends AbstractModel {
   private Boolean DEBUG = true;
   private CompositeHierarchy  workspace;
   private HashMap<String,Object> things;
   boolean bSnapToGrid = false;  // snap mouse press/release/motion to grid

   // Define default fidelity for grid size....

   private int gridSize     = 40;
   private int smallDotSize =  2;

   // Working coordinate limits.

   private double MinX =  -300.0;
   private double MinY =  -150.0;
   private double MaxX =   600.0;
   private double MaxY =   500.0;

   // Retrieve max/min graphics coordinates ...

   public double getMinX() {
      return MinX;
   }

   public double getMinY() {
      return MinY;
   }

   public double getMaxX() {
      return MaxX;
   }

   public double getMaxY() {
      return MaxY;
   }

   // Constructor ....

   public MetroGridModel() {
      System.out.println("Create Model of Workspace Grid ");
      System.out.println("============================== ");

      workspace = new CompositeHierarchy( 0.0, 0.0, 0.0 );
      workspace.setName("Metro Grid Workspace");

      things = new HashMap<String,Object>();
   }

   // ====================================================
   // Build model of Metro system area things ...
   // ====================================================

   public void initDefault() {
      buildMetroGrid();
      setMetroGridComposite();
   }

   // ====================================================
   // Methods to adjust grid size and behaviors ...
   // ====================================================

   public void setSnapToGrid ( boolean bSnapToGrid ) {
      System.out.println("*** In model.MetroGridModel.setSnapToGrid(): snap to grid = " + bSnapToGrid );
      this.bSnapToGrid = bSnapToGrid;
      things.put( "SnapToGrid", new Boolean (bSnapToGrid) );
   }

   public void setGridSize ( int gridSize ) {
      this.gridSize = gridSize;

      System.out.println("*** In model.MetroGridModel.setGridSize(): gridSize = " + gridSize );

      // Automatically adjust smallDotSize for fine grids ...

      if ( gridSize >= 20 ) 
           smallDotSize = 4;
      else 
           smallDotSize = 2;

      // Update hashmap ....

      things.put( "GridSize", new Integer (gridSize) );
   }

   // ======================================================================
   // Create composite of grid objects ....
   // ======================================================================

   public void buildMetroGrid() {

      // Create rectangular grid of nodes ...

      System.out.printf( "*** Enter model.MetroGridModel.buildMetroGrid(): gridSize = %d\n", gridSize );

      double dMinX = -gridSize*Math.floor( -MinX/gridSize );
      double dMinY = -gridSize*Math.floor( -MinY/gridSize );
      double dMaxX =  gridSize*Math.floor(  MaxX/gridSize );
      double dMaxY =  gridSize*Math.floor(  MaxY/gridSize );

      for (double i = dMinX; i <=  dMaxX; i = i + gridSize )
      for (double j = dMinY; j <=  dMaxY; j = j + gridSize ) {

          // Create new grid point ....

          Circle node = new Circle( i, j );
          node.setRadius( smallDotSize/2.0 );
          node.setFilledShape( true );
 
          // Adjust color for x- and y- axes ....

          if ( i == 0 || j == 0 )
             node.setColor( Color.red );
          else
             node.setColor( Color.blue );

          // Add point to composite workspace ....

          workspace.add( node );
      }

      // Add grid data to things hashmap ....

      things.put ("MinX", new Double ( MinX ));
      things.put ("MinY", new Double ( MinY ));
      things.put ("MaxX", new Double ( MaxX ));
      things.put ("MaxY", new Double ( MaxY ));
      things.put ("SnapToGrid", new Boolean ( bSnapToGrid ) );
      things.put ("GridSize",   new Integer ( gridSize ));
   }

   // ===========================================================
   // Set status of grid properties ...
   // ===========================================================

   public void setMetroSnapProperty( Boolean bSnapToGrid ) {
      System.out.println("*** Enter MetroGridModel.setSnapProperty(): snap to grid = " + bSnapToGrid.booleanValue()  );
      setSnapToGrid ( bSnapToGrid.booleanValue()  );
      HashMap copy = new HashMap( things );
      firePropertyChange( EngineeringController.METRO_GRID_HASHMAP, (HashMap) null, copy );
   }

   public void setMetroGridProperty( Integer gridSize ) {

      System.out.println("*** Enter MetroGridModel.setMetroGridProperty(): gridsize = " + gridSize );

      setGridSize ( gridSize.intValue() );

      // Assemble revised workspace ....

      workspace = new CompositeHierarchy( 0.0, 0.0, 0.0 );
      buildMetroGrid();

      // Synchronize grid workspaces and data ...

      CompositeHierarchy copy01 = workspace.clone();
      firePropertyChange( EngineeringController.METRO_GRID_COMPOSITE_PROPERTY,
                          (CompositeHierarchy) null, copy01 );

      HashMap copy02 = new HashMap( things );
      firePropertyChange( EngineeringController.METRO_GRID_HASHMAP, (HashMap) null, copy02 );
   }

   // ===============================================================
   // Propogate metro grid composite to controller ....
   // ===============================================================

   public void setMetroGridComposite() {
      CompositeHierarchy copy = workspace.clone();
      firePropertyChange( EngineeringController.METRO_GRID_COMPOSITE_PROPERTY,
                          (CompositeHierarchy) null, copy );
   }
}


