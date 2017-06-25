/*
 *  ===========================================================================
 *  BoundingBox.java: Create/Manage a rectangular bounding box.
 *
 *  boolean isInside ( Coordinate v )      -- returns "true" when "v" is inside
 *                                        the rectangle.
 *  boolean isOutside ( Coordinate v )     -- returns "true" when "v" is outside
 *                                        the rectangle.
 *  boolean isOnPerimeter ( Coordinate v ) -- returns "true" when "v" sits on
 *                                        the perimeter of the rectangle.
 *  boolean isAbove ( Coordinate v )       -- returns "true" when "v" is strictly
 *                                        above the rectangle.
 *  boolean isBelow  ( Coordinate v )      -- returns "true" when "v" is strictly
 *                                        below the rectangle.
 *  boolean isLeft ( Coordinate v )   -- returns "true" when "v" is strictly to 
 *                                   the left of the rectangle.
 *  boolean isRight ( Coordinate v )  -- returns "true" when "v" is strictly to
 *                                   the right of the rectangle.
 *
 *  Written By : Mark Austin                                         April 2006
 *  ===========================================================================
 */

package model.floorplan;

import java.lang.Math;

import model.Coordinate;

public class BoundingBox {
   protected double dMinX, dMinY, dMaxX, dMaxY;

   // Constructor methods ....

   public BoundingBox() {
       dMinX = Double.NaN;
       dMinY = Double.NaN;
       dMaxX = Double.NaN;
       dMaxY = Double.NaN;
   }

   public BoundingBox( double dX1, double dY1, double dX2, double dY2 ) {
       dMinX = Math.min ( dX1, dX2 );
       dMinY = Math.min ( dY1, dY2 );
       dMaxX = Math.max ( dX1, dX2 );
       dMaxY = Math.max ( dY1, dY2 );
   }

   /*
    *  =======================================================
    *  Set Bounding box coordinates ...
    *  =======================================================
    */

   public void set( double dX1, double dY1, double dX2, double dY2 ) {
       dMinX = Math.min ( dX1, dX2 );
       dMinY = Math.min ( dY1, dY2 );
       dMaxX = Math.max ( dX1, dX2 );
       dMaxY = Math.max ( dY1, dY2 );
   }

   /*
    *  =======================================================
    *  Retrieve boundingbox coordinates 
    *  =======================================================
    */

   public double getMinX() { return dMinX; }
   public double getMaxX() { return dMaxX; }
   public double getMinY() { return dMinY; }
   public double getMaxY() { return dMaxY; }

   /*
    *  =======================================================
    *  Add vertex to the bounding box ....
    *  =======================================================
    */

   public void addCoordinate( double dX, double dY ) {
       if ( isDefined() == false ) {
          dMinX = dMaxX = dX;
          dMinY = dMaxY = dY;
       } else {
          dMinX = Math.min ( dMinX, dX );
          dMaxX = Math.max ( dMaxX, dX );
          dMinY = Math.min ( dMinY, dY );
          dMaxY = Math.max ( dMaxY, dY );
       }
   }

   /*
    *  =======================================================
    *  Check to see if bounding box coordinates are defined ..
    *  =======================================================
    */

   public boolean isDefined() {
      if( Double.isNaN( dMinX ) == false ||
          Double.isNaN( dMinY ) == false ||
          Double.isNaN( dMaxX ) == false ||
          Double.isNaN( dMaxY ) == false ) 
             return true;
      else 
             return false;
   }

   // ======================================================
   // Create string representation for bounding box....
   // ======================================================

   public String toString() {
      String s = "";

      if( isDefined() == true ) {
          s += "BoundingBox(): Min(X) = " + dMinX + " ";
          s += " Min(Y) = " + dMinY + "\n";
          s += "               Max(X) = " + dMaxX + " ";
          s += " Max(Y) = " + dMaxY + "\n";
      } else
          s += "BoundingBox coordinates undefined\n";
 
      return s;
   }

   /*
    *  ======================================================
    *  Evaluate (x,y) coordinate relative to the bounding box
    *  ======================================================
    */

   public boolean isInside ( double dX, double dY ) {
      return isInside ( new Coordinate ( dX, dY ) );
   }

   public boolean isInside ( Coordinate v ) {
      if ( dMinX < v.dX && v.dX < dMaxX &&
           dMinY < v.dY && v.dY < dMaxY )
         return true;
      else
         return false;
   }

   public boolean isOutside ( double dX, double dY ) {
      return isOutside ( new Coordinate ( dX, dY ) );
   }

   public boolean isOutside ( Coordinate v ) {
      if ( dMinX > v.dX || v.dX > dMaxX ||
           dMinY > v.dY || v.dY > dMaxY )
         return true;
      else
         return false;
   }

   public boolean isOnPerimeter ( double dX, double dY ) {
      return isOnPerimeter ( new Coordinate ( dX, dY ) );
   }

   public boolean isOnPerimeter ( Coordinate v ) {
      if ( isInside  ( v ) == false &&
           isOutside ( v ) == false )
         return true;
      else
         return false;
   }

   public boolean isAbove ( Coordinate v ) {
      if ( dMaxY <= v.dY )
         return true;
      else
         return false;
   }

   public boolean isBelow  ( Coordinate v ) {
      if ( dMinY >= v.dY ) return true;
      else return false;
   }

   public boolean isLeft ( Coordinate v ) {
      if ( dMinX >= v.dX ) return true;
      else return false;
   }

   public boolean isRight ( Coordinate v ) {
      if ( dMaxX <= v.dX ) return true;
      else return false;
   } 

   /*
    *  ======================================================
    *  Evaluate relative positioning of two bounding boxes
    *  ======================================================
    */

    public boolean overlap( BoundingBox box ) {

       // See if nodes for box lie inside the current bounding box....

       if( this.isInside  ( box.getMinX(), box.getMinY() ) == true ||
           this.isInside  ( box.getMinX(), box.getMaxY() ) == true ||
           this.isInside  ( box.getMaxX(), box.getMinY() ) == true ||
           this.isInside  ( box.getMaxX(), box.getMaxY() ) == true )
           return true;
       else
           return false;
    }

    public boolean touch( BoundingBox box ) {

       // See if nodes for box2 lie on perimeter of current bounding box....

       if( this.isOnPerimeter ( box.getMinX(), box.getMinY() ) == true ||
           this.isOnPerimeter ( box.getMinX(), box.getMaxY() ) == true ||
           this.isOnPerimeter ( box.getMaxX(), box.getMinY() ) == true ||
           this.isOnPerimeter ( box.getMaxX(), box.getMaxY() ) == true )
           return true;
       else
           return false;
    }

    public boolean isAbove( BoundingBox box ) {
       if( this.getMaxY() <= box.getMinY() ) 
           return true;
       else
           return false;
    }

    public boolean isBelow( BoundingBox box ) {
       if( this.getMinY() >= box.getMaxY() )
           return true;
       else
           return false;
    }

    public boolean isRight( BoundingBox box ) {
       if( this.getMaxX() <= box.getMinX() )
           return true;
       else
           return false;
    }

    public boolean isLeft( BoundingBox box ) {
       if( this.getMinX() >= box.getMaxX() )
           return true;
       else
           return false;
    }

   // ============================================
   // Exercise methods in the Rectangle class ....
   // ============================================

   public static void main ( String args[] ) {

      System.out.println("BoundingBox test program   ");
      System.out.println("===========================");

      // Setup and print details of a small boundingbox

      BoundingBox bA = new BoundingBox( 1.0, 1.0, 3.0, 4.0 );
      System.out.println( bA.toString() );
      bA.addCoordinate(  2.0, 2.0 );
      bA.addCoordinate( -2.0, 2.0 );
      bA.addCoordinate(  2.0, 6.5 );
      System.out.println( bA.toString() );

      // ===============================================
      // Create and print test points ....
      // 
      // Point v1 is outside, below and to the left ....
      // Point v2 is inside
      // Point v3 is one the perimeter ...
      // ===============================================

      Coordinate v1 = new Coordinate( 0.0, 0.0 );
      System.out.println ( v1.toString() );
      Coordinate v2 = new Coordinate( 2.0, 2.0 );
      System.out.println ( v2.toString() );
      Coordinate v3 = new Coordinate( 2.0, 1.0 );
      System.out.println ( v3.toString() );

      if ( bA.isInside( v1 ) == true )
           System.out.println("Coordinate v1 is \"inside\" bA");
      else
           System.out.println("Coordinate v1 is not \"inside\" bA");

      if ( bA.isInside( v2 ) == true )
           System.out.println("Coordinate v2 is \"inside\" bA");
      else
           System.out.println("Coordinate v2 is not \"inside\" bA");

      if ( bA.isInside( v3 ) == true )
           System.out.println("Coordinate v3 is \"inside\" bA");
      else
           System.out.println("Coordinate v3 is not \"inside\" bA");

      if ( bA.isOnPerimeter( v3 ) == true )
           System.out.println("Coordinate v3 is \"on the perimeter\" of bA");
      else
           System.out.println("Coordinate v3 is not \"on the perimeter\" of bA");

   }
}
