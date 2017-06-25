/*
 *  ================================================================
 *  Coordinate.java: Store (x,y,z) location of a geographic feature.
 *  ================================================================
 */

package model;

public class Coordinate implements Cloneable {
   public String  sName;
   public double dX, dY;

   // Constructor methods ....

   public Coordinate() { } 

   public Coordinate( double dX, double dY ) {
      this.dX = dX;
      this.dY = dY;
   } 

   public Coordinate( String sName, double dX, double dY ) {
      this.sName = sName;
      this.dX = dX;
      this.dY = dY;
   } 

   // Return x- and y- coordinates ...

   public double getX() {
      return dX;
   }

   public double getY() {
      return dY;
   }

   public String getName() {
      return sName;
   }

   // ------------------------------------------------------------
   // String representation of coordinate ....
   // ------------------------------------------------------------

   public String toString() {
      String s="";
      s += s.format("Coordinate(x,y) = ( %6.2f, %6.2f )\n", getX(), getY() );
      return s;
   }
   
   public int hashCode() {
	   int x = (int)dX;
	   int y = (int)dY;
	   return (x * 31) + y;
	  }
   
   public boolean equals(Object o) {
	    if (o instanceof Coordinate) {
	      Coordinate other = (Coordinate) o;
	      return (dX == other.dX && dY == other.dY);
	    }
	    return false;
	  }

   // ------------------------------------------------------------
   // Create clone (i.e., deep copy) of Coordinate.java object ...
   // ------------------------------------------------------------

   public Object clone() {
      try {
         Coordinate coord = (Coordinate) super.clone();
         return coord; 
      } catch (CloneNotSupportedException e) {
         return null;
      }
   }
}
