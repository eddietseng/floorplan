package model.hvac;

import java.lang.Math;
import java.util.ArrayList;

import model.Coordinate;

public class PipelineRegion {
   protected String name;
   private ArrayList<Coordinate> pathway   = null;
   private ArrayList<Coordinate> left      = null;
   private ArrayList<Coordinate> right     = null;
   private ArrayList<Coordinate> perimeter = null;
   private Coordinate      source = null;
   private Coordinate destination = null;
   private double pathwayLength;
   boolean DEBUG = false;

   public PipelineRegion() {
      pathway   = new ArrayList<Coordinate>();
      left      = new ArrayList<Coordinate>();
      right     = new ArrayList<Coordinate>();
      perimeter = new ArrayList<Coordinate>();
      pathwayLength = 0.0;
   }
    
   public PipelineRegion( String sName ) {     
      pathway   = new ArrayList<Coordinate>();
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

   public Coordinate getSource() {
      return source;
   }

   public Coordinate getDestination() {
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

   public void add ( Coordinate point ) {

      // Create waypoint and add to arraylist ...

      pathway.add ( point );

      // Update source and destination metro stations ...

      if ( pathway.size() == 1 )
        this.source = this.destination = point;
      else
        this.destination = point;

      // Add pathway length to latest waypoint ...

      if ( pathway.size() >= 2 ) {
         double dX0 = pathway.get( pathway.size() - 2 ).getX();
         double dY0 = pathway.get( pathway.size() - 2 ).getY();
         double dX1 = pathway.get( pathway.size() - 1 ).getX();
         double dY1 = pathway.get( pathway.size() - 1 ).getY();
         pathwayLength = pathwayLength + segmentLength( dX0, dY0, dX1, dY1 );
      }
   }

   // =================================================
   // Assemble Pipeline Pathway region 
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

          Coordinate start  = pathway.get(i-1);
          Coordinate finish = pathway.get(i);

          // Compute and return the slope.

          currentTheta = getAngle(finish.getX() - start.getX(),
                                  finish.getY() - start.getY());

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
             xPoint = (double) (start.getX() + L * Math.cos( dThetaX ));
             yPoint = (double) (start.getY() + L * Math.sin( dThetaX ));
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
                xPoint = (double) (start.getX() - L*Math.cos( dThetaX ));
                yPoint = (double) (start.getY() - L*Math.sin( dThetaX ));
             } else {
                xPoint = (double) (start.getX() + L*Math.cos( dThetaX ));
                yPoint = (double) (start.getY() + L*Math.sin( dThetaX ));
             }

             right.add( new Coordinate ( xPoint, yPoint ));

             // Compute orientation of end point ...

             if (currentTheta <= Math.PI )
                 dThetaX = currentTheta + (3.0/2.0)*Math.PI;
             else
                 dThetaX = currentTheta - (1.0/2.0)*Math.PI;

             L = width/2.0;
             xPoint = (double) (finish.getX() + L*Math.cos( dThetaX ));
             yPoint = (double) (finish.getY() + L*Math.sin( dThetaX ));

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
                xPoint = (double) (start.getX() - L*Math.cos( dThetaX ));
                yPoint = (double) (start.getY() - L*Math.sin( dThetaX ));
             } else {
                xPoint = (double) (start.getX() + L*Math.cos( dThetaX ));
                yPoint = (double) (start.getY() + L*Math.sin( dThetaX ));
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

          Coordinate start  = pathway.get(i-1);
          Coordinate finish = pathway.get(i);

          // Compute and return the slope.

          currentTheta = getAngle(finish.getX() - start.getX(),
                                  finish.getY() - start.getY());

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
             xPoint = (double) (start.getX() - L * Math.cos( dThetaX ));
             yPoint = (double) (start.getY() - L * Math.sin( dThetaX ));

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
                xPoint = (double) (start.getX() + L*Math.cos( dThetaX ));
                yPoint = (double) (start.getY() + L*Math.sin( dThetaX ));
             } else {
                xPoint = (double) (start.getX() - L*Math.cos( dThetaX ));
                yPoint = (double) (start.getY() - L*Math.sin( dThetaX ));
             }

             left.add( new Coordinate ( xPoint, yPoint ));

             // Compute orientation of end point ...

             if (currentTheta <= Math.PI )
                 dThetaX = currentTheta + (3.0/2.0)*Math.PI;
             else
                 dThetaX = currentTheta - (1.0/2.0)*Math.PI;

             L = width/2.0;
             xPoint = (double) (finish.getX() - L*Math.cos( dThetaX ));
             yPoint = (double) (finish.getY() - L*Math.sin( dThetaX ));

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
                xPoint = (double) (start.getX() + L*Math.cos( dThetaX ));
                yPoint = (double) (start.getY() + L*Math.sin( dThetaX ));
             } else {
                xPoint = (double) (start.getX() - L*Math.cos( dThetaX ));
                yPoint = (double) (start.getY() - L*Math.sin( dThetaX ));
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
      System.out.println("Exercise PipelineRegion methods ...");
      System.out.println("=====================================");

      // Create centerline points for a pipeline pathway ...

      Coordinate c01 = new Coordinate("C 01",  0.0,  0.0 );
      Coordinate c02 = new Coordinate("C 02", 10.0,  0.0 );
      Coordinate c03 = new Coordinate("C 03", 10.0, 10.0 );
      Coordinate c04 = new Coordinate("C 04", 15.0, 10.0 );
      Coordinate c05 = new Coordinate("C 05", 20.0, 10.0 );
      Coordinate c06 = new Coordinate("C 06", 20.0,  5.0 );
      Coordinate c07 = new Coordinate("C 07", 10.0,  5.0 );
      Coordinate c08 = new Coordinate("C 08", 10.0,  0.0 );

      // Define and assemble PipelineRegion object ....

      PipelineRegion testLine = new PipelineRegion("Test");
      testLine.add( c01 );
      testLine.add( c02 );
      testLine.add( c03 );
      testLine.add( c04 );
      testLine.add( c05 );
      testLine.add( c06 );
      testLine.add( c07 );
      testLine.add( c08 );

      System.out.println("");
      System.out.printf("PipelineRegion: %s\n", testLine.getName() );
      System.out.printf("=====================================\n");
      System.out.printf( "Source : %s\n",            testLine.getSource().getName() );
      System.out.printf( "Destination : %s\n",       testLine.getDestination().getName() );
      System.out.printf( "Pathway length : %6.2f\n", testLine.getLength() );

      System.out.printf("");
      System.out.printf("-------------------------------------\n");
      System.out.printf("Assemble Pipeline Pathway Region     \n");
      System.out.printf("-------------------------------------\n");

      int width = 2;
      testLine.assembleRegion ( width );

      System.out.printf("------------------------------------------- \n");
      System.out.printf("Perimeter of Pipeline Region                \n");
      System.out.printf("------------------------------------------- \n");

      System.out.println( testLine.getRegion() );

      System.out.printf("------------------------------------------- \n");
   }
}
