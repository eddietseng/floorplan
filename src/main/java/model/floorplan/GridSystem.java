/*
 *  ===========================================================================================
 *  GridSystem.java: Definition and management of objects attached to objects along grid lines. 
 *
 *  Written by: Mark Austin                                                        January 2013
 *  ===========================================================================================
 */

package model.floorplan;

import java.lang.Math.*;
import java.applet.*;
import java.util.*;
import java.io.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.font.*;
import java.awt.image.*;
import java.awt.geom.*;   // Needed for affine transformation....

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.*;
 
public class GridSystem {
   static boolean showGrid = false;
   private CompositeHierarchy workspace;

   // Array list of grid coordinates along the x- and y- axes

   protected List xCoords = new ArrayList();
   protected List yCoords = new ArrayList();
   
   // Array list of x- and y- Grid line model
   
   protected Set xGridLines = new HashSet();
   protected Set yGridLines = new HashSet();
   
   public ArrayList<GridLineModel> addedLines = new ArrayList<GridLineModel>();

   // ===========================================================
   // Initialize grid system workspace ...
   // ===========================================================

   public void setWorkspace ( CompositeHierarchy workspace ) {
      this.workspace = workspace;
   }

   // ===========================================================
   // Find node in grid system ....
   // ===========================================================

   public GridPoint findPoint ( Coordinate v ) {
      return findPoint ( v.getX(), v.getY() ); 
   }

   public GridPoint findPoint ( double dX, double dY ) {
      GridPoint gp1 = null;
      
      // Re-sort lists .....

      Collections.sort( xCoords, new xCompare() );
      Collections.sort( yCoords, new yCompare() );
      
      // Find x coordinate along horizontal axis ...

      Iterator iterator1 = xCoords.iterator();
      while ( iterator1.hasNext() != false ) {
         GridLineModel cp = (GridLineModel) iterator1.next();
         if ( cp.getAnchorX() == dX ) {
              gp1 = cp.findPoint ( new Coordinate ( dX, dY ) );
              return gp1;
         }

         if ( cp.getAnchorX() > dX ) {
              return null;
         }
      }

      return gp1;
   }

   // ===========================================================
   // Add vertex to grid ...
   // ===========================================================

   public void add ( Coordinate v ) {
      add ( new GridPoint ( v.getX(), v.getY(), GridPointType.Vertex ) );
   }

   public void add ( String sName, Coordinate v ) {
      GridPoint gp = new GridPoint ( v.getX(), v.getY(), GridPointType.Vertex );
      gp.add ( sName );
      add ( gp );
   }

   public void add ( Coordinate v, GridPointType gpType ) {
      add ( new GridPoint ( v.getX(), v.getY(), gpType ) );
   }

   public void add ( String sName, Coordinate v, GridPointType gpType ) {

      // Add vertex to grid system .....

      add ( new GridPoint ( v.getX(), v.getY(), gpType ) );

      // Add name to adjacency lists in grid system....

      GridPoint gp1 = findPoint ( v );
      gp1.add ( sName );
   }

   // ===========================================================
   // Add collection of points in a Space2D object to grid ...
   // ===========================================================

   public void add ( Space2DModel sp ) {
	   xGridLines = new HashSet();
	   yGridLines = new HashSet();
       Iterator iterator1 = sp.boundary.vertexList.iterator();
       while ( iterator1.hasNext() != false ) {
    	   Coordinate vA = (Coordinate) iterator1.next();

           // Add vertex to grid system .....

           add ( new GridPoint ( vA.getX(), vA.getY(), GridPointType.Vertex ) );

           // Add name to adjacency lists in grid system....

           GridPoint   gp1 = findPoint ( vA );
           gp1.add ( sp.getName() );
           }
       System.out.println("Numbers of X GridLineModels: " + xGridLines.size());
       System.out.println("Numbers of Y GridLineModels: " + yGridLines.size());
       sp.xGridLine = xGridLines;
       sp.yGridLine = yGridLines;
       Iterator iterator2 = sp.xGridLine.iterator();
       while ( iterator2.hasNext() != false ){
    	   GridLineModel gl = (GridLineModel) iterator2.next();
    	   System.out.println("X - GridLine = " + gl.toString());
    	   gl.AssociateSpaceList.add(sp);
       }
       Iterator iterator3 = sp.yGridLine.iterator();
       while ( iterator3.hasNext() != false ){
    	   GridLineModel gl = (GridLineModel) iterator3.next();
    	   System.out.println("Y - GridLine = " + gl.toString());
    	   gl.AssociateSpaceList.add(sp);
       }
       
   }

   // ===========================================================
   // Add GridPoint to grid system ....
   // ===========================================================

   public void add ( GridPoint gp ) {

      // Get coordinate list items for dX and dY...

      GridLineModel line1 = findXGridLine ( new Coordinate( gp.getX(),       0.0 ));
      GridLineModel line2 = findYGridLine ( new Coordinate(       0.0, gp.getY() ));

      // Link new coordinate into list of coordinate lines ...

      if ( line1 == null ) {
           GridLineModel line3 = new GridLineModel( "Column 1", GridLineDirection.ParallelToYAxis );
           line3.setAnchorPoint( new Coordinate( gp.getX(),  0.0) );
           line3.add ( gp );
           xCoords.add ( line3 );
           xGridLines.add(line3);
           addedLines.add(line3);
      }else{
    	  xGridLines.add(line1);
    	  }

      if ( line2 == null ) {
           GridLineModel line4 = new GridLineModel( "Frame 1", GridLineDirection.ParallelToXAxis );
           line4.setAnchorPoint( new Coordinate( 0.0, gp.getY() ) );
           line4.add ( gp );
           yCoords.add ( line4 );
           yGridLines.add(line4);
           addedLines.add(line4);
      }else{
    	  yGridLines.add(line2);
    	  }

      // Add vertices to coordinate lines ....

      if ( line1 != null ) 
           line1.add ( gp );
      if ( line2 != null )
           line2.add ( gp );

      // Re-sort lists .....

      Collections.sort( xCoords, new xCompare() );
      Collections.sort( yCoords, new yCompare() );

      // Re-set names of frame and column lines ....

      int i = 1;
      Iterator iterator1 = xCoords.iterator();
      while ( iterator1.hasNext() != false ) {
         GridLineModel cp = (GridLineModel) iterator1.next();
         cp.setName( "c-" + i );
         workspace.add(cp);
         i = i + 1;
         
      }

      int j = 1;
      Iterator iterator2 = yCoords.iterator();
      while ( iterator2.hasNext() != false ) {
         GridLineModel cp = (GridLineModel) iterator2.next();
         cp.setName( "f-" + j );
         workspace.add(cp);
         j = j + 1;
         
      }
   }

   // Set grid visibility ...

   public void setVisibility() {
     if ( showGrid == false )
          showGrid = true;
     else
          showGrid = false;
   }

   public boolean getVisibility() { return showGrid; }

   // ==============================================================
   // Retrieve list of vertices positioned between two endpoints...
   // ==============================================================

   public GridLineModel findGridLineModel ( Coordinate v1, Coordinate v2 ) {
       GridLineModel line1 = null;

       // Gridline is parallel to the y-axis ....
       if ( v1.getX() == v2.getX() ) 
          line1 = findXGridLine ( new Coordinate( v1.getX(), 0.0 ));

       // Gridline is parallel to the x-axis ....
       if ( v1.getY() == v2.getY() ) 
          line1 = findYGridLine ( new Coordinate( 0.0, v1.getY() ));

       return line1;
   }

   public ArrayList findPoints ( Coordinate v1, Coordinate v2 ) {
       ArrayList gridpointList = new ArrayList();

       // Find grid line along which the points will lie

       GridLineModel line = findGridLineModel( v1, v2 );

       // Create sublist of points ....
       // Traverse line and add points between end-points ...

       int iCounter = 0;
       Iterator iterator1 = line.gridPointList.iterator();
       while ( iterator1.hasNext() != false ) {
           GridPoint gp = (GridPoint) iterator1.next();

           if( ( v1.getX() == gp.getX()) && ( v1.getY() == gp.getY()) ) 
              iCounter = iCounter + 1;
           if( ( v2.getX() == gp.getX()) && ( v2.getY() == gp.getY()) )
              iCounter = iCounter + 1;

           if ( iCounter == 1 ) {
                gridpointList.add ( gp );
           }

           if ( iCounter == 2) {
                gridpointList.add ( gp );
                return gridpointList;
           }
       }

       return gridpointList;
   }

   // ==============================================================
   // Retrieve vertex from a specific coordinate line ....
   // ==============================================================

   public GridLineModel findXGridLine ( Coordinate v ) {
       Iterator iterator1 = xCoords.iterator();
       
       while ( iterator1.hasNext() != false ) {
            GridLineModel cp = (GridLineModel) iterator1.next();
            if( ( cp.getAnchorX() == v.getX()) )
               return cp; 
       }
       return null;
   }

   public GridLineModel findYGridLine ( Coordinate v ) {
       Iterator iterator1 = yCoords.iterator();
       while ( iterator1.hasNext() != false ) {
            GridLineModel cp = (GridLineModel) iterator1.next();
            if( ( cp.getAnchorY() == v.getY()) )
               return cp; 
       }
       return null;
   }

   // Comparator classes for x- and y- directions ....

   class xCompare implements Comparator {
      public int compare( Object o1, Object o2) {
         GridLineModel c1 = (GridLineModel) o1;
         GridLineModel c2 = (GridLineModel) o2;
         if ( c1.getAnchorX() == c2.getAnchorX() )
              return  0;
         else if ( c1.getAnchorX() > c2.getAnchorX() )
              return  1;
         else
              return -1;
      }
   }

   class yCompare implements Comparator {
      public int compare( Object o1, Object o2) {
         GridLineModel c1 = (GridLineModel) o1;
         GridLineModel c2 = (GridLineModel) o2;
         if ( c1.getAnchorY() == c2.getAnchorY() )
              return  0;
         else if ( c1.getAnchorY() > c2.getAnchorY() )
              return  1;
         else
              return -1;

      }
   }

   // -------------------------------------------------
   // Create string description of grid coordinates ...
   // -------------------------------------------------

   public String toString() {
      String s = "";
      s += "Grid System                                    \n";
      s += "===============================================\n";

      // Walk along grid lines parallel to y- axis...

      Iterator iterator1 = xCoords.iterator();
      while ( iterator1.hasNext() != false ) {
          GridLineModel cp = (GridLineModel) iterator1.next();
          s += cp.toString() + "\n";
      }

      // Walk along grid lines parallel to x- axis...

      Iterator iterator2 = yCoords.iterator();
      while ( iterator2.hasNext() != false ) {
          GridLineModel cp = (GridLineModel) iterator2.next();
          s += cp.toString() + "\n";
      }

      s += "===============================================\n";
      
      return s;
   }

   // ================================================================
   // Exercise development of a coordinate grid ....
   // ================================================================

   public static void main ( String args [] ) {

      // Create small grid of architectural nodes ...

      GridSystem arch = new GridSystem();
      arch.exercise1();
      System.out.println( arch.toString() );

      // Retrieve list of nodes between two end points ...

      ArrayList gp = arch.findPoints( new Coordinate ( 2.0, 0.0 ),
                                      new Coordinate ( 2.0, 4.0 ) );

      System.out.println("List of points along segment");
      System.out.println("============================");

      System.out.println( gp.toString() );

      System.out.println("Reverse ordering of list ... ");
      System.out.println("===========================================");

      Collections.reverse( gp );
      System.out.println( gp.toString() );

      // Find and print a grid point ...

      System.out.println("*** Find a point in the grid() ....");
      System.out.println("*** ===============================");
      System.out.println("*** Looking for:...");

      Coordinate v3 = new Coordinate ( 4.0, 3.0 );
      System.out.println( v3.toString() );

      GridPoint gp2 = arch.findPoint ( v3 );

      System.out.println("*** Result:...");
      System.out.println( gp2.toString() );
   }

   public void exercise1() {

       Coordinate v1 = new Coordinate ( 2.0, 0.0 );
       Coordinate v2 = new Coordinate ( 0.0, 0.0 );
       Coordinate v3 = new Coordinate ( 2.0, 4.0 );
       Coordinate v4 = new Coordinate ( 0.0, 4.0 );
       Coordinate v5 = new Coordinate ( 2.0, 3.0 );
       Coordinate v6 = new Coordinate ( 2.0, 1.0 );
       Coordinate v7 = new Coordinate ( 4.0, 3.0 );
       Coordinate v8 = new Coordinate ( 4.0, 1.0 );

       GridPoint gp1 = new GridPoint ( v1, GridPointType.Vertex );
       GridPoint gp2 = new GridPoint ( v2, GridPointType.Vertex );
       GridPoint gp3 = new GridPoint ( v3, GridPointType.Vertex );
       GridPoint gp4 = new GridPoint ( v4, GridPointType.Vertex );
       GridPoint gp5 = new GridPoint ( v5, GridPointType.Vertex );
       GridPoint gp6 = new GridPoint ( v6, GridPointType.Vertex );
       GridPoint gp7 = new GridPoint ( v7, GridPointType.Vertex );
       GridPoint gp8 = new GridPoint ( v8, GridPointType.Vertex );

       add ( gp1 );
       add ( gp2 );
       add ( gp3 );
       add ( gp4 );
       add ( gp5 );
       add ( gp6 );
       add ( gp7 );
       add ( gp8 );
   }

}

