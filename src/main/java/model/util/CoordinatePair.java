/*
 *  ===============================================================
 *  CoordinatePair.java: .....
 *  
 *  Written By: Mark Austin                            January 2013  
 *  ===============================================================
 */

package model.util;

public class CoordinatePair {
   private double dX1, dY1;
   private double dX2, dY2;
   private CoordinatePairType type;

   // =======================================
   // Set/get coordinate pair type ...
   // =======================================

   public void setCoordinatePairType( CoordinatePairType type ) {
      this.type = type;
   }

   public CoordinatePairType getCoordinatePairType() {
      return this.type;
   }

   // =======================================
   // Set/get variable X1 ....
   // =======================================

   public void setX1( double dX1 ) {
      this.dX1 = dX1;
   }

   public double getX1() {
      return dX1;
   }

   // Set/get variable Y1 ....

   public void setY1( double dY1 ) {
      this.dY1 = dY1;
   }

   public double getY1() {
      return dY1;
   }

   // =======================================
   // Set/get variable X2 ....
   // =======================================

   public void setX2( double dX2 ) {
      this.dX2 = dX2;
   }

   public double getX2() {
      return dX2;
   }

   // Set/get variable Y2 ....

   public void setY2( double dY2 ) {
      this.dY2 = dY2;
   }

   public double getY2() {
      return dY2;
   }
   
   public void setCoordinatePair( double x1, double y1, double x2, double y2 )
	{
		this.dX1 = x1;
		this.dY1 = y1;
		this.dX2 = x2;
		this.dY2 = y2;
	}

   // ------------------------------------------------------------
   // String representation of a coordinate pair ....
   // ------------------------------------------------------------

   public String toString() {
      String s="";
      s += s.format("Coordinate Pair: Type = " + type + "\n" );
      s += s.format("              (x1,y1) = ( %6.2f, %6.2f )\n", getX1(), getY1() );
      s += s.format("              (x2,y2) = ( %6.2f, %6.2f )\n", getX2(), getY2() );
      return s;
   }

} 
