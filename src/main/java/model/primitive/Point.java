/** 
  *  ===============================================================
  *  Point.java: Create two-dimensional (x,y) homogeneous point.
  *
  *  Parameters
  *
  *  -- protected double   x, y;
  *  -- protected double  width;
  *  -- protected double height;
  *
  *  Constructor methods
  *
  *  -- public Point()
  *  -- public Point( double x, double y );
  *
  *  Written by: Mark Austin                          November, 2004
  *  ===============================================================
  */ 

package model.primitive;

import model.*;

public class Point extends AbstractFeature {

    // Point constructor methods .....

    public Point() {
        super();
        setX( 0.0 );
        setY( 0.0 );
    }

    public Point(String sName, double x, double y ) {
        super();
        setName( sName );
        setX( x );
        setY( y );
    }

    public Point(double x, double y, double width, double height ) {
        super();
        setX( x );
        setY( y );
        setWidth( width );
        setHeight( height );
    }

    public Point(String sName, double x, double y, double width, double height ) {
        super();
        setName( sName );
        setX( x );
        setY( y );
        setWidth( width );
        setHeight( height );
    }

    // Compute norm of the point ....
 
    public double norm() {
        return Math.sqrt( getX() * getX() + getY() * getY() );
    }

    // Clone (i.e., deep copy) point object ...

    public Point clone() {
       try {
           Point cloned = (Point) super.clone();
           return cloned;
       } catch ( CloneNotSupportedException e) {
           System.out.println("*** Error in Point.clone() ... ");
           return null;
       }
    }

    // Create string representation of a point ...
    
    public String toString() {
       String s = String.format("Point: (x,y) = (%5.1f, %5.1f)", getX(), getY() );
       return s;
    }
}
