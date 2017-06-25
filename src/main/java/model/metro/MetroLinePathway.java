package model.metro;

import java.lang.Math;
import java.util.ArrayList;

import model.Coordinate;
import model.metro.MetroStation;
import model.metro.MetroWayPoint;

public class MetroLinePathway {
   protected String name;
   private ArrayList<MetroWayPoint> pathway = null;
   private MetroStation      source = null;
   private MetroStation destination = null;
   private double pathwayLength;

   public MetroLinePathway() {
      pathway = new ArrayList<MetroWayPoint>();
      pathwayLength = 0.0;
   }
    
   public MetroLinePathway( String sName ) {     
      this.pathway = new ArrayList<MetroWayPoint>();
      this.name = sName;
      pathwayLength = 0.0;
   }

   // Set/get name of metroline pahtway ...

   public String getName() {
      return name;
   }

   public void setName( String name ) {
      this.name = name;
   }

   // Retrieve source and destination metrostations ....

   public MetroStation getSource() {
      return source;
   }

   public MetroStation getDestination() {
      return destination;
   }

   // Retrieve length of pathway ....

   public double getLength() {
      return pathwayLength;
   }

   // Compute length of segment (x0,y0) -- (x1,y1)...

   public double segmentLength( double dx0, double dy0, double dx1, double dy1 ) {
      return Math.sqrt( (dx1 - dx0)*(dx1 - dx0) + (dy1 - dy0)*(dy1 - dy0) );
   }

   // Compute angle for coordinates in four quadrants ....

   public double getAngle( double dX, double dY ) {
       double angle = 0.0;

       if ( dY >= 0.0 && dX >= 0.0 )
            angle = Math.atan( dY/dX );
       if ( dY >= 0.0 && dX < 0.0 )
            angle = Math.PI + Math.atan( dY/dX );
       if ( dY  < 0.0 && dX < 0.0 )
            angle = Math.PI + Math.atan( dY/dX );
       if ( dY  < 0.0 && dX >= 0.0 )
            angle = 2*Math.PI + Math.atan( dY/dX );

       return angle;
   }

   // =================================================
   // Add a metro station to end of pathway ...
   // =================================================

   public void add ( MetroStation newStation ) {

      // Create waypoint and add to arraylist ...

      MetroWayPoint point = new MetroWayPoint ( newStation );
      pathway.add ( point );

      // Update source and destination metro stations ...

      if ( pathway.size() == 1 )
        this.source = this.destination = newStation;
      else
        this.destination = newStation;

      // Add pathway length to latest waypoint ...

      if ( pathway.size() >= 2 ) {
         double dX0 = pathway.get( pathway.size() - 2 ).coord.getX();
         double dY0 = pathway.get( pathway.size() - 2 ).coord.getY();
         double dX1 = pathway.get( pathway.size() - 1 ).coord.getX();
         double dY1 = pathway.get( pathway.size() - 1 ).coord.getY();
         pathwayLength = pathwayLength + segmentLength( dX0, dY0, dX1, dY1 );
         point.setDistanceFromOrigin ( pathwayLength );
      }
   }

   // ===================================================================
   // Return coordinate point that is a given distance along pathway.
   // ===================================================================

   public Coordinate pointAtDistance( double length ) {
      boolean foundSegment = false;
      MetroWayPoint start  = pathway.get(0);
      MetroWayPoint finish = pathway.get(0);

      // Check that the length value is valid ...

      if ( length < 0.0 || length > pathwayLength ) {
        System.out.printf("*** ERROR in MetroLinePathway.pointAtDistance()");
        System.out.printf("*** Invalid Input: length = %6.1f\n", length );
      }

      // Walk along waypoint list and find segment within which the point lies ...

      int j = 1;
      while ( j < pathway.size() && foundSegment == false) {
         start  = pathway.get(j-1);
         finish = pathway.get(j);
         if ( length >= start.getDistanceFromOrigin() &&
              length < finish.getDistanceFromOrigin() ) {
              foundSegment = true;
         }
         j = j + 1;
      }

      // Compute offset distance ....

      double offset = length - start.getDistanceFromOrigin();

      // Compute the slope.

      double theta = getAngle(finish.coord.getX() - start.coord.getX(),
                              finish.coord.getY() - start.coord.getY());

      double xPoint = (double) (start.coord.getX() + offset * Math.cos(theta));
      double yPoint = (double) (start.coord.getY() + offset * Math.sin(theta));

      return new Coordinate( xPoint, yPoint );
   }


   // ===================================================================
   // Return angle that is a given distance along pathway.
   // ===================================================================

   public double angleAtDistance( double length ) {
      boolean foundSegment = false;
      MetroWayPoint start  = pathway.get(0);
      MetroWayPoint finish = pathway.get(0);

      // Check that the length value is valid ...

      if ( length < 0.0 || length > pathwayLength ) {
        System.out.printf("*** ERROR in MetroLinePathway.angleAtDistance()");
        System.out.printf("*** Invalid Input: length = %6.1f\n", length );
      }

      // Walk along waypoint list and find segment within which the point lies ...

      int j = 1;
      while ( j < pathway.size() && foundSegment == false) {
         start  = pathway.get(j-1);
         finish = pathway.get(j);
         if ( length >= start.getDistanceFromOrigin() &&
              length < finish.getDistanceFromOrigin() ) {
              foundSegment = true;
         }
         j = j + 1;
      }

      // Compute offset distance ....

      double offset = length - start.getDistanceFromOrigin();

      // Compute and return the slope.

      double theta = getAngle(finish.coord.getX() - start.coord.getX(),
                              finish.coord.getY() - start.coord.getY());

      return theta;
   }

   // =================================================
   // Exercise methods in metroline pathway class ...
   // =================================================

   public static void main ( String args[] ) {
      System.out.println("Exercise MetroLinePathway methods ...");
      System.out.println("=====================================");

      // Create ficticious Metro Stations with coordinates ....

      MetroStation station01 = new MetroStation("Station 01",  0.0,  0.0 );
      MetroStation station02 = new MetroStation("Station 02", 10.0,  0.0 );
      MetroStation station03 = new MetroStation("Station 03", 10.0, 10.0 );
      MetroStation station04 = new MetroStation("Station 04", 20.0, 20.0 );
      MetroStation station05 = new MetroStation("Station 05", 20.0,  0.0 );

      // Define and assemble MetroLinePathway object ....

      MetroLinePathway testLine = new MetroLinePathway("Test");
      testLine.add( station01 );
      testLine.add( station02 );
      testLine.add( station03 );
      testLine.add( station04 );
      testLine.add( station05 );

      System.out.println("");
      System.out.printf("MetroLinePathway: %s\n", testLine.getName() );
      System.out.printf("=====================================\n");
      System.out.printf( "Source : %s\n", testLine.getSource().getStationName() );
      System.out.printf( "Destination : %s\n", testLine.getDestination().getStationName() );
      System.out.printf( "Pathway length : %6.2f\n", testLine.getLength() );

      System.out.printf("");
      System.out.printf("-------------------------------------\n");
      System.out.printf("Arraylist of Metro Stations          \n");
      System.out.printf("-------------------------------------\n");
      System.out.println( testLine.pathway );

      System.out.printf("");
      System.out.printf("------------------------------------------- \n");
      System.out.printf("Test distance to coordinate and angle ...   \n");
      System.out.printf("------------------------------------------- \n");

      for (double distance = 0.0; distance < testLine.getLength(); distance = distance + 5.0 ) {
         Coordinate point = testLine.pointAtDistance( distance );
         double angle     = testLine.angleAtDistance( distance );
         System.out.printf(" *** Dist = %5.2f : (x,y) = ( %5.1f, %5.1f ) angle = %8.4f\n",
                             distance, point.getX(), point.getY(), angle );
      }

      System.out.printf("------------------------------------------- \n");
   }
}
