package model.metro;

import java.lang.Math;
import java.util.ArrayList;

import model.Coordinate;
import model.metro.MetroStation;
import model.metro.MetroWayPoint;

public class MetroLineRegion {
   protected String name;
   private ArrayList<MetroWayPoint> pathway = null;
   private ArrayList<Coordinate> left      = null;
   private ArrayList<Coordinate> right     = null;
   private ArrayList<Coordinate> perimeter = null;
   private MetroStation      source = null;
   private MetroStation destination = null;
   private double pathwayLength;
   boolean DEBUG = false;

   public MetroLineRegion() {
      pathway   = new ArrayList<MetroWayPoint>();
      left      = new ArrayList<Coordinate>();
      right     = new ArrayList<Coordinate>();
      perimeter = new ArrayList<Coordinate>();
      pathwayLength = 0.0;
   }
    
   public MetroLineRegion( String sName ) {     
      pathway   = new ArrayList<MetroWayPoint>();
      left      = new ArrayList<Coordinate>();
      right     = new ArrayList<Coordinate>();
      perimeter = new ArrayList<Coordinate>();
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
        System.out.printf("*** ERROR in MetroLineRegion.pointAtDistance()");
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
        System.out.printf("*** ERROR in MetroLineRegion.angleAtDistance()");
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
   // Assemble Metro Pathway region 
   // =================================================

   public void assembleRegion ( int width ) {
      double  currentTheta = 0.0;
      double previousTheta = 0.0;
      double dThetaX = 0.0;
      double xPoint = 0.0;
      double yPoint = 0.0;

      if (DEBUG == true) {
         System.out.println("*** Enter assembleRegion(): width = " + width );
      }

      // Generate right-hand nodes for region boundary ....

      if (DEBUG == true) {
         System.out.println("");
         System.out.println("*** Assemble Right-Hand Points ...");
         System.out.println("*** =============================");
      }

      for ( int i = 1; i < pathway.size(); i = i + 1 ) {

          // Retrieve coordinates on current segment ...

          MetroWayPoint start  = pathway.get(i-1);
          MetroWayPoint finish = pathway.get(i);

          // Compute and return the slope.

          currentTheta = getAngle(finish.coord.getX() - start.coord.getX(),
                                  finish.coord.getY() - start.coord.getY());

          if ( i == 1 )
             previousTheta = currentTheta;

          // Compute boundary points ....

          if ( i == 1 ) {
             // First Segment .. 

             if (currentTheta <= Math.PI )
                 dThetaX = currentTheta + (3.0/2.0)*Math.PI;
             else
                 dThetaX = currentTheta - (1.0/2.0)*Math.PI;

             double L = width/2.0;
             xPoint = (double) (start.coord.getX() + L * Math.cos( dThetaX ));
             yPoint = (double) (start.coord.getY() + L * Math.sin( dThetaX ));
             right.add( new Coordinate ( xPoint, yPoint ));

          } else if ( i == (pathway.size() - 1) ) {
             // End Segment ....

             if ( currentTheta > previousTheta ) 
                dThetaX = (1.0/2.0)*(previousTheta + currentTheta + Math.PI );
             else
                dThetaX = (1.0/2.0)*(previousTheta + currentTheta - Math.PI );

             // Compute orientation of thetaX ....

             double dThetaC = (1.0/2.0)*(previousTheta - currentTheta);
             double L       = (width/2.0)*Math.sqrt( 1 + Math.tan(dThetaC)*Math.tan(dThetaC) );

             boolean antiClockwiseTurn = false;
             if ( previousTheta <= Math.PI && previousTheta < currentTheta &&
                  currentTheta < previousTheta + Math.PI ) { antiClockwiseTurn = true; }
             if ( previousTheta > Math.PI && ( previousTheta < currentTheta ||
                  currentTheta < previousTheta - Math.PI )) { antiClockwiseTurn = true; }

             if ( antiClockwiseTurn == true ) {
                xPoint = (double) (start.coord.getX() - L*Math.cos( dThetaX ));
                yPoint = (double) (start.coord.getY() - L*Math.sin( dThetaX ));
             } else {
                xPoint = (double) (start.coord.getX() + L*Math.cos( dThetaX ));
                yPoint = (double) (start.coord.getY() + L*Math.sin( dThetaX ));
             }

             right.add( new Coordinate ( xPoint, yPoint ));

             // Compute orientation of end point ...

             if (currentTheta <= Math.PI )
                 dThetaX = currentTheta + (3.0/2.0)*Math.PI;
             else
                 dThetaX = currentTheta - (1.0/2.0)*Math.PI;

             L = width/2.0;
             xPoint = (double) (finish.coord.getX() + L*Math.cos( dThetaX ));
             yPoint = (double) (finish.coord.getY() + L*Math.sin( dThetaX ));

             right.add( new Coordinate ( xPoint, yPoint ));
          } else {
             // Middle Segment .. 

             if ( currentTheta > previousTheta ) 
                dThetaX = (1.0/2.0)*(previousTheta + currentTheta + Math.PI );
             else
                dThetaX = (1.0/2.0)*(previousTheta + currentTheta - Math.PI );

             double dThetaC = (1.0/2.0)*(previousTheta - currentTheta);
             double L       = (width/2.0)*Math.sqrt( 1 + Math.tan(dThetaC)*Math.tan(dThetaC) );

             boolean antiClockwiseTurn = false;
             if ( previousTheta <= Math.PI && previousTheta < currentTheta &&
                  currentTheta < previousTheta + Math.PI ) { antiClockwiseTurn = true; }
             if ( previousTheta > Math.PI && ( previousTheta < currentTheta ||
                  currentTheta < previousTheta - Math.PI )) { antiClockwiseTurn = true; }

             if ( antiClockwiseTurn == true ) {
                xPoint = (double) (start.coord.getX() - L*Math.cos( dThetaX ));
                yPoint = (double) (start.coord.getY() - L*Math.sin( dThetaX ));
             } else {
                xPoint = (double) (start.coord.getX() + L*Math.cos( dThetaX ));
                yPoint = (double) (start.coord.getY() + L*Math.sin( dThetaX ));
             }

             right.add( new Coordinate ( xPoint, yPoint ));
          }

          // Update angle parameters ....

          previousTheta = currentTheta;
      }

      // Generate left-hand nodes for region boundary ....

      if (DEBUG == true) {
         System.out.println("");
         System.out.println("*** Assemble Left-Hand Points ...");
         System.out.println("*** =============================");
      }

      for ( int i = 1; i < pathway.size(); i = i + 1 ) {

          // Retrieve coordinates on current segment ...

          MetroWayPoint start  = pathway.get(i-1);
          MetroWayPoint finish = pathway.get(i);

          // Compute and return the slope.

          currentTheta = getAngle(finish.coord.getX() - start.coord.getX(),
                                  finish.coord.getY() - start.coord.getY());

          if ( i == 1 )
             previousTheta = currentTheta;


          // Compute boundary points ....

          if ( i == 1 ) {
             // First Segment .. 

             if (currentTheta <= Math.PI )
                 dThetaX = currentTheta + (3.0/2.0)*Math.PI;
             else
                 dThetaX = currentTheta - (1.0/2.0)*Math.PI;

             double L = width/2.0;
             xPoint = (double) (start.coord.getX() - L * Math.cos( dThetaX ));
             yPoint = (double) (start.coord.getY() - L * Math.sin( dThetaX ));

             left.add( new Coordinate ( xPoint, yPoint ));

          } else if ( i == (pathway.size() - 1) ) {
             // End segment ... 

             if ( currentTheta > previousTheta ) 
                dThetaX = (1.0/2.0)*(previousTheta + currentTheta + Math.PI );
             else
                dThetaX = (1.0/2.0)*(previousTheta + currentTheta - Math.PI );

             // Compute orientation of thetaX ....

             double dThetaC = (1.0/2.0)*(previousTheta - currentTheta);
             double L       = (width/2.0)*Math.sqrt( 1 + Math.tan(dThetaC)*Math.tan(dThetaC) );

             boolean antiClockwiseTurn = false;
             if ( previousTheta <= Math.PI && previousTheta < currentTheta &&
                  currentTheta < previousTheta + Math.PI ) { antiClockwiseTurn = true; }
             if ( previousTheta > Math.PI && ( previousTheta < currentTheta ||
                  currentTheta < previousTheta - Math.PI )) { antiClockwiseTurn = true; }

             if ( antiClockwiseTurn == true ) {
                xPoint = (double) (start.coord.getX() + L*Math.cos( dThetaX ));
                yPoint = (double) (start.coord.getY() + L*Math.sin( dThetaX ));
             } else {
                xPoint = (double) (start.coord.getX() - L*Math.cos( dThetaX ));
                yPoint = (double) (start.coord.getY() - L*Math.sin( dThetaX ));
             }

             left.add( new Coordinate ( xPoint, yPoint ));

             // Compute orientation of end point ...

             if (currentTheta <= Math.PI )
                 dThetaX = currentTheta + (3.0/2.0)*Math.PI;
             else
                 dThetaX = currentTheta - (1.0/2.0)*Math.PI;

             L = width/2.0;
             xPoint = (double) (finish.coord.getX() - L*Math.cos( dThetaX ));
             yPoint = (double) (finish.coord.getY() - L*Math.sin( dThetaX ));

             left.add( new Coordinate ( xPoint, yPoint ));

          } else {
             // Middle segment ....

             if ( currentTheta > previousTheta ) 
                dThetaX = (1.0/2.0)*(previousTheta + currentTheta + Math.PI );
             else
                dThetaX = (1.0/2.0)*(previousTheta + currentTheta - Math.PI );

             double dThetaC = (1.0/2.0)*(previousTheta - currentTheta);
             double L       = (width/2.0)*Math.sqrt( 1 + Math.tan(dThetaC)*Math.tan(dThetaC) );

             boolean antiClockwiseTurn = false;
             if ( previousTheta <= Math.PI && previousTheta < currentTheta &&
                  currentTheta < previousTheta + Math.PI ) { antiClockwiseTurn = true; }
             if ( previousTheta > Math.PI && ( previousTheta < currentTheta ||
                  currentTheta < previousTheta - Math.PI )) { antiClockwiseTurn = true; }

             if ( antiClockwiseTurn == true ) {
                xPoint = (double) (start.coord.getX() + L*Math.cos( dThetaX ));
                yPoint = (double) (start.coord.getY() + L*Math.sin( dThetaX ));
             } else {
                xPoint = (double) (start.coord.getX() - L*Math.cos( dThetaX ));
                yPoint = (double) (start.coord.getY() - L*Math.sin( dThetaX ));
             }

             left.add( new Coordinate ( xPoint, yPoint ));
          }

          // Update angle parameters ....

          previousTheta = currentTheta;
      }

      // ============================================================
      // Generate region boundary ....
      // ============================================================

      if (DEBUG == true) {
         System.out.println("");
         System.out.println("*** Generate Region Boundary ");
         System.out.println("*** -------------------------");
      }

      for ( int i = 0; i < left.size(); i = i + 1 ) {
          Coordinate c = left.get(i);
          perimeter.add ( new Coordinate( c.getX(), c.getY() ));
      }
      for ( int i = right.size() - 1; i >= 0; i = i - 1 ) {
          Coordinate c = right.get(i);
          perimeter.add ( new Coordinate( c.getX(), c.getY() ));
      }

      if (DEBUG == true) {
         System.out.println("*** Leave assembleRegion()");
      }
   }

   // =================================================
   // Retrieve arraylist of perimeter coordinates ...
   // =================================================

   public ArrayList<Coordinate> getRegion() {
      return perimeter;
   }

   // =================================================
   // Exercise methods in metroline pathway class ...
   // =================================================

   public static void main ( String args[] ) {
      System.out.println("Exercise MetroLineRegion methods ...");
      System.out.println("=====================================");

      // Create ficticious Metro Stations with coordinates ....

      MetroStation station01 = new MetroStation("Station 01",  0.0,  0.0 );
      MetroStation station02 = new MetroStation("Station 02", 10.0,  0.0 );
      MetroStation station03 = new MetroStation("Station 03", 10.0, 10.0 );
      MetroStation station04 = new MetroStation("Station 04", 15.0, 10.0 );
      MetroStation station05 = new MetroStation("Station 05", 20.0, 10.0 );
      MetroStation station06 = new MetroStation("Station 06", 20.0,  5.0 );
      MetroStation station07 = new MetroStation("Station 07", 10.0,  5.0 );
      MetroStation station08 = new MetroStation("Station 08", 10.0,  0.0 );

      // Define and assemble MetroLineRegion object ....

      MetroLineRegion testLine = new MetroLineRegion("Test");
      testLine.add( station01 );
      testLine.add( station02 );
      testLine.add( station03 );
      testLine.add( station04 );
      testLine.add( station05 );
      testLine.add( station06 );
      testLine.add( station07 );
      testLine.add( station08 );

      System.out.println("");
      System.out.printf("MetroLineRegion: %s\n", testLine.getName() );
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
      System.out.printf("-------------------------------------\n");
      System.out.printf("Assemble Metro Pathway Region        \n");
      System.out.printf("-------------------------------------\n");

      int width = 2;
      testLine.assembleRegion ( width );

      System.out.printf("------------------------------------------- \n");
      System.out.printf("Perimeter of Metro Track Region             \n");
      System.out.printf("------------------------------------------- \n");

      System.out.println( testLine.getRegion() );

      System.out.printf("------------------------------------------- \n");
   }
}
