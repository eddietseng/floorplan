/*
 *  =======================================================================
 *  ComponentBoundary.java...
 * 
 *  Points to note:
 *
 *  1. Container boundaries correspond to one loops of edges
 *     oriented in a clockwise direction.
 *  2. Each edge points to: (1) the next edge in the list, (2) the opposite
 *     edge, and (3) to the parent space2D object.
 *
 *  Written By: Mark Austin                                       June 2006
 *  =======================================================================
 */

package model.floorplan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.*;

public class ComponentBoundary {
    protected String cName; // name of parent component ....
    protected BoundingBox box = new BoundingBox();
    protected List vertexList = new ArrayList();
    protected List   edgeList = new ArrayList();

    // Basic constructor....

    public ComponentBoundary() {}

    public ComponentBoundary( Coordinate firstCoordinate ) {
       vertexList.add( firstCoordinate );
    }

    // Create rectangular boundary defined by diagonally opposite
    // coordinate points .....

    public ComponentBoundary ( double dX1, double dY1, double dX2, double dY2 ) {

       Coordinate nA = new Coordinate( "n1", Math.min( dX1, dX2), Math.min ( dY1, dY2 ) );
       Coordinate nB = new Coordinate( "n2", Math.min( dX1, dX2), Math.max ( dY1, dY2 ) );
       Coordinate nC = new Coordinate( "n3", Math.max( dX1, dX2), Math.max ( dY1, dY2 ) );
       Coordinate nD = new Coordinate( "n4", Math.max( dX1, dX2), Math.min ( dY1, dY2 ) );

       // Assemble boundary data structure ....

       vertexList.add( nA );
       this.mev( "n1",  nB );
       this.mev( "n2",  nC );
       this.mev( "n3",  nD );
       this.mef( "n4", "n1" );
    }

    public ComponentBoundary ( String cName, double dX1, double dY1,
                                             double dX2, double dY2 ) {
       this.cName = cName;

       Coordinate nA = new Coordinate( "n1", Math.min( dX1, dX2), Math.min ( dY1, dY2 ) );
       Coordinate nB = new Coordinate( "n2", Math.min( dX1, dX2), Math.max ( dY1, dY2 ) );
       Coordinate nC = new Coordinate( "n3", Math.max( dX1, dX2), Math.max ( dY1, dY2 ) );
       Coordinate nD = new Coordinate( "n4", Math.max( dX1, dX2), Math.min ( dY1, dY2 ) );

       // Assemble boundary data structure ....

       vertexList.add( nA );
       this.mev( "n1",  nB );
       this.mev( "n2",  nC );
       this.mev( "n3",  nD );
       this.mef( "n4", "n1" );
    }

    // =======================================================
    // Space defined by list of vertex coordinates....
    // =======================================================

    public ComponentBoundary ( String cName, List outerLoop ) {
       int i;
       this.cName = cName;

       // Add first vertex to the space ....

       Coordinate v1 = (Coordinate) outerLoop.get(0);
       vertexList.add( v1 );

       // Create vertices along perimeter of space ...

       String nodeName = "";
       for ( i = 1; i < outerLoop.size(); i = i + 1 ) {
             v1 = (Coordinate) outerLoop.get(i);
             nodeName = "n" + i;
             this.mev( nodeName,  v1 );
       }

       nodeName = "n" + i;
       this.mef( nodeName, "n1" );
    }

    // ==================================================
    // Retrieve bounding box, vertex and edge lists
    // ==================================================

    public BoundingBox getBoundingBox() {
       return this.box;
    }

    public List getVertexList() {
       return this.vertexList;
    }

    public List getEdgeList() {
       return this.edgeList;
    }

    /*
     *  =========================================
     *  Create outer loop vertices and edges ....
     *  =========================================
     */

    public Coordinate createCoordinate( String sName, double x, double y ) {
        Coordinate newCoordinate = new Coordinate( sName, x, y );
        vertexList.add( newCoordinate );
        return newCoordinate;
    }

    public void createEdge( String eName, Coordinate vA, Coordinate vB ) {
        Edge newEdge = new Edge( eName, vA, vB );
        newEdge.setParent( cName );
        edgeList.add( newEdge );
    }

    /*
     *  ============================================
     *  Find vertices in exterior loop .....
     *  ============================================
     */

    public Coordinate findCoordinate( String sName ) {
       for ( int i = 0; i < vertexList.size(); i = i + 1 ) {
          Coordinate vA = (Coordinate) vertexList.get(i);
          if ( vA.getName().equals ( sName ) == true )
               return vA;
       }
       return null;
    }

    /*
     *  ===============================================
     *  Find edge in space description ...
     *  ===============================================
     */

    public Edge findEdge( String sName ) {
       for ( int i = 0; i < edgeList.size(); i = i + 1 ) {
          Edge eA = (Edge) edgeList.get(i);
          if ( eA.getName().equals ( sName ) == true )
               return eA;
       }
       return null;
    }
    
    /*
     *  ===============================================
     *  Refine ComponentBoundary ...
     *  ===============================================
     */
    
    public void refineVertextList(){
    	this.vertexList = new ArrayList();
    	this.edgeList = new ArrayList();
    	
    	Coordinate nA = new Coordinate( "n1", this.box.getMinX(), this.box.getMinY() );
        Coordinate nB = new Coordinate( "n2", this.box.getMinX(), this.box.getMaxY() );
        Coordinate nC = new Coordinate( "n3", this.box.getMaxX(), this.box.getMaxY() );
        Coordinate nD = new Coordinate( "n4", this.box.getMaxX(), this.box.getMinY() );

        // Assemble boundary data structure ....

        vertexList.add( nA );
        this.mev( "n1",  nB );
        this.mev( "n2",  nC );
        this.mev( "n3",  nD );
        this.mef( "n4", "n1" );

        
    }

    /*
     *  ============================================
     *  Euler operator mev (make edge, vertex).
     *  ============================================
     */

    public void mev( String vertexName, String newCoordinateName, double x, double y ) {

        // Retrieve Coordinate( vertexName ) ....

        Coordinate vA = findCoordinate ( vertexName );
        if ( vA == null) {
            System.out.println("mev: vertex (" + vertexName + ") not found" );
            System.exit(-1);
        }

        // Update boundingbox coordinates ....

        box.addCoordinate ( x, y );
       
        // Create one new edge and one new vertex ....

        Coordinate vB = createCoordinate( newCoordinateName, x, y );
        createEdge( "e"+ ( edgeList.size() + 1 ), vA, vB );
    }

    public void mev( String startingCoordinateName, Coordinate nextV ) {

        // Retrieve Coordinate( startingCoordinateName ) ....

        Coordinate vA = findCoordinate ( startingCoordinateName );
        if ( vA == null) {
            System.out.println("mev: vertex (" + startingCoordinateName + ") not found" );
            System.exit(-1);
        }

        // Update boundingbox coordinates ....

        box.addCoordinate ( nextV.getX(), nextV.getY() );

        // Create one new edge and one new vertex ....

        Coordinate vB = createCoordinate( nextV.getName(), nextV.getX(), nextV.getY() );
        createEdge( "e"+(edgeList.size() + 1), vA, vB );
    }

    /*
     *  ============================================
     *  Euler operator mef (make edge, face).
     *  ============================================
     */

    public void mef( String firstCoordinate, String secondCoordinate ) {

        Coordinate vA = findCoordinate (  firstCoordinate );
        Coordinate vB = findCoordinate ( secondCoordinate );

        if ( vA == null || vB == null ) {
            System.out.println("mef: vertex (" + firstCoordinate + ") not found" );
        }
        if ( vB == null ) {
            System.out.println("mef: vertex (" + secondCoordinate + ") not found" );
        }

        createEdge( "e"+(edgeList.size() + 1), vA, vB );
    }

    /*
     *  ========================================================
     *  Compute x- and y- coordinates of exterior space centroid
     *  ========================================================
     */

    public double getCentroidX() {
        double dCentroidX = 0;
        double dArea = area();
        double factor;

        Iterator iterator1 = edgeList.iterator();
        while ( iterator1.hasNext() != false ) {
            Edge eA = (Edge) iterator1.next();
            factor = eA.vertex2.dY*(   eA.vertex1.dX + 2*eA.vertex2.dX ) +
                     eA.vertex1.dY*( 2*eA.vertex1.dX +   eA.vertex2.dX );

            dCentroidX += ( eA.vertex2.dX - eA.vertex1.dX )*factor/6.0;
        }
        return dCentroidX/dArea;
    }

    public double getCentroidY() {
        double dCentroidY = 0;
        double dArea = area();
        double factor;

        Iterator iterator1 = edgeList.iterator();
        while ( iterator1.hasNext() != false ) {
            Edge eA = (Edge) iterator1.next();
            factor = eA.vertex1.dY * eA.vertex1.dY +
                     eA.vertex1.dY * eA.vertex2.dY +
                     eA.vertex2.dY * eA.vertex2.dY;
            dCentroidY += (eA.vertex2.dX - eA.vertex1.dX)*factor/6.0;
        }
        return dCentroidY/dArea;
    }
    
    public Coordinate getCentroid(){
    	return new Coordinate ( getCentroidX(), getCentroidY() );
    }

    /*
     *  ======================================================
     *  Compute area enclosed within boundary loop ....
     *  ======================================================
     */

    public double area() {
        double dArea = 0.0;

        Iterator iterator1 = edgeList.iterator();
        while ( iterator1.hasNext() != false ) {
            Edge eA = (Edge) iterator1.next();
            dArea += ( eA.vertex2.dX - eA.vertex1.dX ) *
                     ( eA.vertex1.dY + eA.vertex2.dY )/2.0;
        }

        return dArea;
    } 

    /*
     *  ======================================================
     *  Determine clockwise/anticlockwise ordering of vertices
     *  ======================================================
     */

    public boolean isClockwise() {
        if ( area() > 0 ) return true;
        else return false;
    }

    public boolean isCounterClockwise() {
        if ( area() < 0 ) return true;
        else return false;
    }

    /*
     *  ================================================================
     *  Is a point "p" contained inside the loop of edges ...
     *  
     *  Note.  If p is on boundary then 0 or 1 is returned,
     *         and p is in exactly one point of every partition of plane
     *  Reference: http://exaflop.org/docs/cgafaq/cga2.html
     *  ================================================================
     */

    public boolean contains( double x, double y ) {
        int crossings = 0;

        // Walk along edges and count crossings ....

        Iterator iterator1 = edgeList.iterator();
        while ( iterator1.hasNext() != false ) {
            Edge eA = (Edge) iterator1.next();

            boolean cond1 = (eA.vertex1.dY <= y) && (y < eA.vertex2.dY);
            boolean cond2 = (eA.vertex2.dY <= y) && (y < eA.vertex1.dY);
            boolean cond3 = x < (eA.vertex2.dX - eA.vertex1.dX) * (y - eA.vertex1.dY) /
                                (eA.vertex2.dY - eA.vertex1.dY ) +
                                 eA.vertex1.dX;
            if ((cond1 || cond2) && cond3)
                crossings++;
        }

        if (crossings % 2 == 1)
            return true;
        else
            return false; 
    }

    /*
     *  =========================================================
     *  overlap(). Test to see if two spaces overlap ...
     *  
     *  Spaces will not overlap if their rectangular bounding
     *  boxes are mutually disjoint.
     *  
     *  Note. This method does not compute the intersection.  
     *  =========================================================
     */

    public boolean overlap( BoundingBox box2 ) {

       // See any of the nodes in box2 lie inside box...

       if( this.box.overlap ( box2 ) == true ) { return true; }

       // Does box2 completely surround the current bounding box....

       if( box2.isInside (  box.getMinX(),  box.getMinY() ) == true &&
           box2.isInside (  box.getMinX(),  box.getMaxY() ) == true &&
           box2.isInside (  box.getMaxX(),  box.getMinY() ) == true &&
           box2.isInside (  box.getMaxX(),  box.getMaxY() ) == true ) {
           return true;
       }

       // Case where box and box2 touch .....

       if ( touch( box2 ) == true ) {
           if ( box.isAbove( box2 ) == true ||
                box.isBelow( box2 ) == true ||
                box.isLeft(  box2 ) == true ||
                box.isRight( box2 ) == true ) {
                return false;
           }
       }

       if( box.overlap ( box2 ) == false ) {
           if ( box.isAbove( box2 ) == true ||
                box.isBelow( box2 ) == true ||
                box.isLeft(  box2 ) == true ||
                box.isRight( box2 ) == true ) {
                return false;
           } else {
                return true;
           }
       }
       
       return false;
    }

    /*
     *  ===============================================================
     *  touch(). Test to see if component boundaries touch, either at a
     *           common vertex or perhaps a common edge fragment.
     *  ===============================================================
     */

    public boolean touch( BoundingBox box2 ) {

       // See if nodes for box2 lie on perimeter of current bounding box....

       if( box.isOnPerimeter ( box2.getMinX(), box2.getMinY() ) == true ||
           box.isOnPerimeter ( box2.getMinX(), box2.getMaxY() ) == true ||
           box.isOnPerimeter ( box2.getMaxX(), box2.getMinY() ) == true ||
           box.isOnPerimeter ( box2.getMaxX(), box2.getMaxY() ) == true ) {
           return true;
       }

       // Does current bounding box touch box2? ....

       if( box2.isOnPerimeter (  box.getMinX(),  box.getMinY() ) == true ||
           box2.isOnPerimeter (  box.getMinX(),  box.getMaxY() ) == true ||
           box2.isOnPerimeter (  box.getMaxX(),  box.getMinY() ) == true ||
           box2.isOnPerimeter (  box.getMaxX(),  box.getMaxY() ) == true ) {
           return true;
       }
       
       return false;
    }

    /*
     *  ======================================================
     *  Create string representation of component ...
     *  ======================================================
     */

    public String toString() {
       String s = "Boundary Edges\n";
       s += "-------------------------------------- \n";

       Iterator iterator1 = edgeList.iterator();
       while ( iterator1.hasNext() != false ) {
          Edge eA = (Edge) iterator1.next();
          s += eA.toString() + "\n";
       } 

       s += "-------------------------------------- \n";
       return s;
    }

    // Exercise methods in Component Boundary ...

    public static void main ( String args[] ) {
       ComponentBoundary b1 = new ComponentBoundary();
       System.out.println( b1.toString() );

       // Create an arraylist of vertices for spB...

        Coordinate nF = new Coordinate( "n1",  50.0, 25.0 );
        Coordinate nG = new Coordinate( "n2",  50.0, 75.0 );
        Coordinate nH = new Coordinate( "n3", 100.0, 75.0 );
        Coordinate nI = new Coordinate( "n4", 100.0, 25.0 );
        List outerLoop = new ArrayList();
        outerLoop.add ( nF );
        outerLoop.add ( nG );
        outerLoop.add ( nH );
        outerLoop.add ( nI );
        ComponentBoundary b2 = new ComponentBoundary( "spB", outerLoop );
        System.out.println( b2.toString() );
    }
}
