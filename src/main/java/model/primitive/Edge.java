/** 
  *  ===============================================================
  *  Edge.java: Create edge objects for 2d modeling package ...
  *
  *  Constructor methods
  *
  *  -- public Edge();
  *
  *  Written by: Mark Austin                              June, 2012
  *  ===============================================================
  */ 

package model.primitive;

import model.*;

public class Edge extends AbstractFeature {
    protected double x1, y1, x2, y2;
    protected int thickness;
    protected Point end1;
    protected Point end2;
    

    // Constructor method ....

    public Edge ( String sName, Point end1, Point end2 ) { 
       super();
       this.sName = sName;
       this.x1 = end1.getX();
       this.y1 = end1.getY();
       this.x2 = end2.getX();
       this.y2 = end2.getY();
       this.end1 = end1;
       this.end2 = end2;
       setX( 0.0 );
       setY( 0.0 );
       be.addPoint ( x1, y1 );
       be.addPoint ( x2, y2 );
    }

    // Retrieve parameter values ....

    public void setThickness( int thickness ) {
       this.thickness = thickness;
    }

    public int getThickness() {
       return thickness;
    }

    // Retrieve parameter values ....

    public double getX1() {
       return x1;
    }

    public double getY1() {
       return y1;
    }

    public double getX2() {
       return x2;
    }

    public double getY2() {
       return y2;
    }

    public String getName() {
       return sName;
    }

    // Create clone (i.e., deep copy) of Edge object ...

    public Edge clone() {
       try {
           Edge cloned = (Edge) super.clone();
           return cloned;
       } catch ( CloneNotSupportedException e) {
           System.out.println("*** Error in Edge.clone() ... ");
           return null;
       }
    }

    // Create string representation for edge object ....

    public String toString() {
       String s1 = String.format("Edge: (x1,y1) = (%5.1f, %5.1f)", x1, y1 );
       String s2 = String.format(" : (x2,y2) = (%5.1f, %5.1f)", x2, y2 );
       String s3 = String.format(" : thickness = %3d", thickness );
       String s = s1 + s2 + s3;

       return s;
    }
}
