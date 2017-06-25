/*
 * ===================================================================
 * Tree.java: Create a tree ...
 * ===================================================================
 */

package model.primitive;

import model.*;

public class Circle extends AbstractFeature {
   private double dRadius;

   // Constructor methods ...

   public Circle ( double dX, double dY ) {
      super ( dX, dY );
   }

   public Circle ( String sName, double dX, double dY ) {
      super ( dX, dY );
      setName ( sName );
   }

   // Methods to set/get circle radius ....

   public void setRadius ( double radius ) {
      this.dRadius = radius;
      be.addPoint ( getX()-dRadius, getY()-dRadius );
      be.addPoint ( getX()+dRadius, getY()+dRadius );
      setWidth(  2.0*dRadius );
      setHeight( 2.0*dRadius );
   }

   public double getRadius () {
      return dRadius;
   }

   // Create clone (i.e., deep copy) of Circle object ...

   public Circle clone() {
      try {
         Circle cloned = (Circle) super.clone();
         return cloned;
      } catch ( CloneNotSupportedException e) {
         System.out.println("*** Error in Circle.clone() ... ");
         return null;
      }
   }

   // Create string representation for a circle ....

   public String toString() {
      String s = "Circle( " + sName + "): (x,y) = (" + getX() + "," + getY() + ") radius = " + dRadius;
      return s;
   }
}

