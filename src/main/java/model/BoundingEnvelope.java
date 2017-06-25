/*
 *  =======================================================================
 *  BoundingEnvelope.java: Compute and store coordinates of an axis-aligned
 *  bounding envelope.
 *
 *  Written By: Mark Austin                                      June, 2012
 *  =======================================================================
 */

package model;

import java.lang.Math;
import java.awt.Point;

public class BoundingEnvelope implements Cloneable {
   private double dMinX, dMinY;
   private double dMaxX, dMaxY;

   // Constructor to create a null envelope ...

   public BoundingEnvelope() {
      init();
   }

   public BoundingEnvelope( double dX, double dY ) {
      init();
      addPoint( dX, dY );
   }

   // ======================================================================
   // Create an envelope for a region defined by maximum and minimum values.
   // ======================================================================

   public BoundingEnvelope(double x1, double x2, double y1, double y2) {
      setToNull();
      addPoint( Math.min( x1, x2), Math.min( y1, y2) );
      addPoint( Math.max( x1, x2), Math.max( y1, y2) );
   }

   // ==================================================================
   // Initialize the envelope ....
   // ==================================================================

   public void init() {
      setToNull();
   }

   // ==================================================================
   // Make a "null" envelope containing an empty geometry ...
   // ==================================================================

   public void setToNull() {
      dMinX =  0;
      dMaxX = -1;
      dMinY =  0;
      dMaxY = -1;
   }

   // ==================================================================
   // Simple coordinate test to detect a null envelope ...
   // ==================================================================

   public boolean isNull() {
      return dMaxX < dMinX;
   }

   // ==================================================================
   // Expand boundary envelope so that it contains a new point (x,y) ...
   // ==================================================================

   public void addPoint(double x, double y) {
      if (isNull() == true ) {
         dMinX = dMaxX = x;
         dMinY = dMaxY = y;
      } else {
         dMinX = Math.min ( dMinX, x );
         dMaxX = Math.max ( dMaxX, x );
         dMinY = Math.min ( dMinY, y );
         dMaxY = Math.max ( dMaxY, y );
      }
   }

   // ===========================================
   // Compute and retrieve the envelope width ...
   // ===========================================

   public double getWidth() {
      if ( isNull() == true ) {
         return 0;
      }

      return dMaxX - dMinX;
   }

   // ===========================================
   // Compute and retrieve the envelope height ...
   // ===========================================

   public double getHeight() {
      if (isNull() == true ) {
         return 0;
      }

      return dMaxY - dMinY;
   }

   // ========================================================================
   // Simple test for point containment ....
   // ========================================================================

   public boolean contains( Point pt ) {
      boolean insideX = ( pt.getX() > dMinX ) && ( pt.getX() < dMaxX );
      boolean insideY = ( pt.getY() > dMinY ) && ( pt.getY() < dMaxY );
      return ( insideX && insideY );
   }

   // ========================================================================
   // Create clone (i.e., deep copy) of BoundingEnvelope object ...
   // ========================================================================

   public Object clone() {
      try {
         BoundingEnvelope copy = (BoundingEnvelope) super.clone();
         return copy; 
      } catch (CloneNotSupportedException e) {
         return null;
      }
   }

   // ========================================================================
   // String representation for rectangular envelope ...
   // ========================================================================

   public String toString() {
      return "BoundingEnv[ x = (" + dMinX + "-" + dMaxX + ") y = ( " + dMinY + "-" + dMaxY + ")]";
   }
}

