/**
  *  =====================================================================
  *  GridPoint.java: GridPoints...
  * 
  *  Written by: Mark Austin                                     June 2006 
  *  =====================================================================
  */

package model.floorplan;

import java.util.List;
import java.util.ArrayList;

import model.Coordinate;

public class GridPoint extends Coordinate {
   protected String           gpName;                // name of grid point ...
   private GridPointType typeOfPoint;                // Stores gridpoint type ...
   public List adjacentSpaceList = new ArrayList(); // List of adjacent spaces ...

   // ====================================
   // Constructor methods ....
   // ====================================

   public GridPoint() {
      super( 0.0, 0.0 );
      this.gpName = "";
      this.setType ( GridPointType.Vertex );
   }

   public GridPoint( double dX, double dY ) {
      super( dX, dY );
      this.gpName = "";
      this.setType ( GridPointType.Vertex );
   }

   public GridPoint( String gpName ) {
      super( 0.0, 0.0 );
      this.gpName = gpName;
      this.setType ( GridPointType.Vertex );
   }

   public GridPoint( String gpName, double dX, double dY ) {
      super( dX, dY );
      this.gpName = gpName;
      this.setType ( GridPointType.Vertex );
   }

   public GridPoint ( Coordinate v, GridPointType typeOfPoint ) {
      super( v.getX(), v.getY() );
      this.setType ( typeOfPoint );
   }

   public GridPoint ( double dX, double dY, GridPointType typeOfPoint ) {
      super( dX, dY );
      this.setType ( typeOfPoint );
   }

   public GridPoint ( String gpName, double dX, double dY, GridPointType typeOfPoint ) {
      super( dX, dY );
      this.gpName = gpName;
      this.setType ( typeOfPoint );
   }

   // ==============================================================
   // Set/get grid point type ...
   // ==============================================================

   public void setType ( GridPointType typeOfPoint ) {
      this.typeOfPoint = typeOfPoint;
   }

   public GridPointType getType() {
      return this.typeOfPoint;
   }

   // ==============================================================
   // Set name for the node ...
   // ==============================================================

   public void setName( String gpName ) {
      this.gpName = gpName;
   }

   // ==============================================================
   // Add item to list of adjacent spaces....
   // ==============================================================

   public void add ( String spaceName ) {

      // Only add item to list if it doesn't already exist ....

      for ( int i = 0; i < adjacentSpaceList.size(); i = i + 1) {
         String adj = (String) adjacentSpaceList.get(i);
         if( adj.equals (spaceName) == true ) return;
      }

      // Add item to list....

      adjacentSpaceList.add( spaceName );
   }

   // ==============================================================
   // Convert grid node to a string ...
   // ==============================================================

   public String toString() {
      String s = "GridPoint(\"" + gpName + "\") is at (" + dX + "," + dY + ")\n";

      // Add reference type for grid point ....

      if( this.getType() == GridPointType.Vertex )
          s += "Type: Vertex .... \n";

      // Add neighboring spaces ...

      s += "Adjacent Spaces: ";
      for ( int i = 0; i < adjacentSpaceList.size(); i = i + 1 ) {
          String item = (String) adjacentSpaceList.get(i);
          s += item + " ";
      }
      s += "\n";

      return s;
   }

   // =========================================================
   // Exercise methods in the GridPoint class .....
   // =========================================================

   public static void main( String args[] ) {

      // Create and print "point 1", a node at coordinate (1,2)...

      GridPoint nA = new GridPoint();
      nA.dX = 1.0;
      nA.dY = 2.0;
      nA.sName  = "Point 1";

      System.out.println( nA.toString() );

      // Create and print "point 2", a node at coordinate (5,2)...

      GridPoint nB = new GridPoint ("Point 2", 5.0, 5.0 );
   }
}



