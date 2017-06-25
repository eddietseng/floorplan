/**
  *  =====================================================================
  *  SimplePolygon.java: A simple polygon model ...
  *  =====================================================================
  */

package model.primitive;

import java.lang.Math;
import java.util.ArrayList;

import model.*;

public class SimplePolygon extends AbstractFeature {
   double dMinX, dMaxX, dMinY, dMaxY;
   double [] x;
   double [] y;

   public SimplePolygon( double xPoints[], double yPoints[] ) {
      super();

      // Make a copy of the incoming array coordinates ...

      x = (double[])xPoints.clone();
      y = (double[])yPoints.clone();

      // Compute max and min polygon coordinates ...

      dMinX = dMaxX = x[0];
      dMinY = dMaxY = y[0];

      for ( int k = 1; k < x.length; k++ ) {
         dMinX = Math.min ( dMinX, x[k] ); 
         dMaxX = Math.max ( dMaxX, x[k] ); 
         dMinY = Math.min ( dMinY, y[k] ); 
         dMaxY = Math.max ( dMaxY, y[k] ); 
      }

      // Save anchor reference point for shape ...

      setWidth(  dMaxX - dMinX );
      setHeight( dMaxY - dMinY );
      be.addPoint ( dMinX, dMinY );
      be.addPoint ( dMaxX, dMaxY );
   }

   // Retrieve arrays of x- and y- coordinates ...

   public double [] getXcoord() {
      return x;
   }

   public double [] getYcoord() {
      return y;
   }

   // Clone (i.e., deep copy) simple polygon object ...

   public SimplePolygon clone() {
       try {
           SimplePolygon cloned = (SimplePolygon) super.clone();
           return cloned;
       } catch ( CloneNotSupportedException e) {
           System.out.println("*** Error in SimplePolygon.clone() ... ");
           return null;
       }
   }

   // Create string representation of a point ...
    
   public String toString() {
      String s = "SimplePolygon(" + getName() + ")\n";
      s = s + "-- Anchor (x,y) = (" + getX() + ", " + getY() + ")\n";
      s = s + "-- Width " + getWidth() + " height = " + getHeight();
      return s;
   }
}
