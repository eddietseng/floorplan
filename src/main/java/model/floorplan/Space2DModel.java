/*
 *  =============================================================================
 *  Space2DModelModel.java: Model definition for a two-dimensional rectangular space.
 *
 *  This initial implementation supports five types of space:
 *
 *  1. BLOCK           (i.e., space occupied by an entire design block).
 *  2. PRIMARY         (i.e., primary spaces lie in the center of design blocks).
 *  3. BOUNDARY_CORNER (i.e., boundary space on a corner).
 *  4. BOUNDARY_EDGE   (i.e., boundary space lying along an edge).
 *  5. PORTAL          (i.e., fragment of boundary space that acts as a portal).
 *
 *  Written By : Mark Austin                                            July 2006
 *  =============================================================================
 */

package model.floorplan;

import java.lang.Math.*;
import java.util.*;
import java.io.*;
import java.text.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.*;
import model.primitive.*;
import model.rtree.HyperBoundingBox;

public class Space2DModel extends AbstractCompoundFeature implements FloorplanModelListener {
    protected GridSystem    grid; // Grid system to which the space will be 
    protected Space2DType spType; // Space2DModel type ...
    protected ComponentBoundary boundary;
    protected HyperBoundingBox hp;
    protected HyperBoundingBox oldHp;
    public Set<GridLineModel> xGridLine = new HashSet<GridLineModel>(); // Store the dependent X Grid Line Model
    public Set<GridLineModel> yGridLine = new HashSet<GridLineModel>(); // Store the dependent Y Grid Line Model

    // Constructor methods ....

    public Space2DModel() {
       this.boundary = new ComponentBoundary();
    }

    public Space2DModel( String cName ) {
       setName ( cName );
       this.boundary = new ComponentBoundary();
    }

    public Space2DModel( String cName, GridSystem grid, Coordinate firstCoordinate ) {
       setName ( cName );
       this.grid     = grid;
       this.boundary = new ComponentBoundary();
       this.boundary.vertexList.add( firstCoordinate );

       // Add point to container grid .....

       grid.add ( cName, firstCoordinate, GridPointType.Vertex );
    }

    // ======================================================================
    // Create rectangular space defined by diagonally opposite
    // coordinate points .....
    // ======================================================================

    public Space2DModel ( double dX1, double dY1, double dX2, double dY2 ) {
       this.boundary = new ComponentBoundary( dX1, dY1, dX2, dY2 );
    }

    public Space2DModel ( String cName, GridSystem grid, double dX1, double dY1,
                                                    double dX2, double dY2 ) {
       setName ( cName );
       this.grid     = grid;
       this.boundary = new ComponentBoundary( cName, dX1, dY1, dX2, dY2 );

       // Add points to container grid .....

       grid.add ( cName, new Coordinate( dX1, dY1 ), GridPointType.Vertex );
       grid.add ( cName, new Coordinate( dX1, dY2 ), GridPointType.Vertex );
       grid.add ( cName, new Coordinate( dX2, dY1 ), GridPointType.Vertex );
       grid.add ( cName, new Coordinate( dX2, dY2 ), GridPointType.Vertex );
    }

    // =======================================================
    // Space defined by list of vertex coordinates....
    // =======================================================

    public Space2DModel ( String cName, List outerLoop ) {
       setName ( cName );
       this.boundary = new ComponentBoundary( cName, outerLoop );
    }

    public Space2DModel ( String cName, GridSystem grid, List outerLoop ) {
       setName ( cName );
       this.grid     = grid;
       this.boundary = new ComponentBoundary( cName, outerLoop );

       // Add points to container grid .....

       for ( int i = 0; i < outerLoop.size(); i = i + 1 ) {
           Coordinate v1 = (Coordinate) outerLoop.get(i);
           grid.add ( cName, v1 , GridPointType.Vertex );
       }
    }

    public Space2DModel ( String cName, List outerLoop, Space2DType spType ) {
       setName ( cName );
       this.boundary = new ComponentBoundary( cName, outerLoop );
       this.spType   = spType;
    }

    public Space2DModel ( String cName, GridSystem grid, List outerLoop, Space2DType spType ) {
       setName ( cName );
       this.grid     = grid;
       this.boundary = new ComponentBoundary( cName, outerLoop );
       this.spType   = spType;

       // Add points to container grid .....

       for ( int i = 0; i < outerLoop.size(); i = i + 1 ) {
           Coordinate v1 = (Coordinate) outerLoop.get(i);
           grid.add ( v1 , GridPointType.Vertex );
       }
    }

    // ==============================================================
    // Retrieve component boundary and bounding box ...
    // ==============================================================

    public ComponentBoundary getComponentBoundary() {
       return this.boundary;
    }

    public BoundingBox getBoundingBox() {
       return this.boundary.box;
    }
    
    public void setGridSystem( GridSystem grid )
    {
    	this.grid = grid;
    }

    // ==============================================================
    // Set/get "type" of coordinate line ...
    // ==============================================================

    public void setSpace2DType ( Space2DType spType ) {
       this.spType = spType;
    }

    public Space2DType getSpace2DType() {
       return this.spType;
    }

    /*
     *  ===========================================================
     *  Refine edge/vertex descriptions to match underlying grid...
     * 
     *  Assumption. The bounding box describes the perimeter...
     *  ===========================================================
     */

    public void refine ( GridSystem grid ) {
       BoundingBox box = this.getBoundingBox();
       int i;

       double x1 = box.getMinX();
       double y1 = box.getMinY();
       double x2 = box.getMaxX();
       double y2 = box.getMaxY();

       Coordinate v1 = new Coordinate ( x1, y1 );
       Coordinate v2 = new Coordinate ( x1, y2 );
       Coordinate v3 = new Coordinate ( x2, y2 );
       Coordinate v4 = new Coordinate ( x2, y1 );

       // Save corner reference point for shape ...

       setX( (double) x1 );
       setY( (double) y1 );
       setHeight( (double) (x2 - x1));
       setWidth(  (double) (y2 - y1));

       // Initialize envelope ...

       be.addPoint( x1, y1 );
       be.addPoint( x2, y2 );

       // Retrieve and order list of columns that lies along the edge...

       ArrayList gp1 = grid.findPoints( v1, v2 );
       ArrayList gp2 = grid.findPoints( v2, v3 );
       ArrayList gp3 = grid.findPoints( v3, v4 );
       Collections.reverse( gp3 );
       ArrayList gp4 = grid.findPoints( v4, v1 );
       Collections.reverse( gp4 );

       // Create vertex and edge lists for boundary ...
       
       this.boundary.vertexList = new ArrayList();
       this.boundary.edgeList   = new ArrayList();

       // Add first vertex to the space ....

       GridPoint   gp = (GridPoint) gp1.get(0);
       gp.add ( this.getName() );
       Coordinate v5 = new Coordinate ( "n1", gp.getX(), gp.getY() );
       this.boundary.vertexList.add( v5 );

       // Create vertices along perimeter of space ...

       String nodeName = "";
       int NodeNo = 1;
       for ( i = 1; i < gp1.size(); i = i + 1 ) {
             gp = (GridPoint) gp1.get(i);
             gp.add ( this.getName() );
             NodeNo = NodeNo + 1;
             Coordinate v6 = new Coordinate ( "n" + NodeNo, gp.getX(), gp.getY() );
             this.boundary.mev( "n" + (NodeNo-1),  v6 );
       }

       for ( i = 1; i < gp2.size(); i = i + 1 ) {
             gp = (GridPoint) gp2.get(i);
             gp.add ( this.getName() );
             NodeNo = NodeNo + 1;
             Coordinate v6 = new Coordinate ( "n" + NodeNo, gp.getX(), gp.getY() );
             this.boundary.mev( "n" + (NodeNo-1),  v6 );
       }

       for ( i = 1; i < gp3.size(); i = i + 1 ) {
             gp = (GridPoint) gp3.get(i);
             gp.add ( this.getName() );
             NodeNo = NodeNo + 1;
             Coordinate v6 = new Coordinate ( "n" + NodeNo, gp.getX(), gp.getY() );
             this.boundary.mev( "n" + (NodeNo-1),  v6 );
       }

       for ( i = 1; i < gp4.size() - 1; i = i + 1 ) {
             gp = (GridPoint) gp4.get(i);
             gp.add ( this.getName() );
             NodeNo = NodeNo + 1;
             Coordinate v6 = new Coordinate ( "n" + NodeNo, gp.getX(), gp.getY() );
             this.boundary.mev( "n" + (NodeNo-1),  v6 );
       }

       // Close outer loop ...

       this.boundary.mef( "n" + NodeNo, "n1" );

       // ===========================================================
       // Add vertices to composite shape model ...
       // ===========================================================

       List vertexList = this.boundary.getVertexList();
       List   edgeList = this.boundary.getEdgeList();

       // Create an arraylist of point objects ....

       int pSize = 2;
       ArrayList pts = new ArrayList();
       for ( i = 1; i <= vertexList.size(); i = i + 1 ) {
           Coordinate c = (Coordinate) vertexList.get(i-1);

           String name;
           if ( i <= 9 ) {
               name = "pt0" + i;
           } else {
               name = "pt" + i;
           }

           Point node = new Point( name, c.getX(), c.getY(), pSize, pSize );
           pts.add( node );
       }

       // Add point to the model ....

       for ( i = 1; i <= vertexList.size(); i = i + 1 ) {
           Point node = (Point) pts.get(i-1);
           items.put( node.getName(), node );
       }

       // ===========================================================
       // Add edges to composite shape model ...
       // ===========================================================

       for ( i = 1; i <= edgeList.size(); i = i + 1 ) {
           Edge e = (Edge) edgeList.get(i-1);

           String name;
           if ( i <= 9 ) {
               name = "e0" + i;
           } else {
               name = "e" + i;
           }

           Point node01 = (Point) pts.get( (i-1) % edgeList.size() );
           Point node02 = (Point) pts.get( (i)   % edgeList.size() );

           model.primitive.Edge edge = new model.primitive.Edge( name,  node01, node02 );
           edge.setThickness( pSize );

           items.put( edge.getName(), edge );
           System.out.println( edge.toString() );
           
       }
    }

    /*
     *  =============================================================================
     *  getNeighbors(): For each edge retrieve the neighboring space ...
     *  
     *  Note. Each node in the grid stores a list of adjacent spaces.
     *  An edge will have at most two common space names the parent and the neighbor.
     *  =============================================================================
     */

    public void getNeighbors ( GridSystem gridsys ) {
        String neighbor = null;

        // Walk along edges and retrieve lists of adjacent spaces
        // from each endpoint.....

        String sParent = this.getName();

        Iterator iterator1 = boundary.edgeList.iterator();
        while ( iterator1.hasNext() != false ) {
            Edge eA = (Edge) iterator1.next();

            // Retrieve/print gridpoints for the end points ....

            Coordinate v1 = new Coordinate ( eA.vertex1.getX(), eA.vertex1.getY() );
            Coordinate v2 = new Coordinate ( eA.vertex2.getX(), eA.vertex2.getY() );

            GridPoint gp1 = gridsys.findPoint ( v1 );
            GridPoint gp2 = gridsys.findPoint ( v2 );

            neighbor = null;
            
            for ( int i = 0; i < gp1.adjacentSpaceList.size(); i = i + 1 ) {
                String item1 = (String) gp1.adjacentSpaceList.get(i);
                if ( item1.equals (this.getName()) == false ) {
                   for ( int j = 0; j < gp2.adjacentSpaceList.size(); j = j + 1 ) {
                       String item2 = (String) gp2.adjacentSpaceList.get(j);
                       if ( item1.equals (item2) == true )
                            neighbor = item1;
                   }
                }
            }

            // Set parent and neighbor spaces ....

            eA.parent   = sParent;
            eA.neighbor = neighbor;
        }
    }

    /*
     *  =========================================================
     *  overlap(). Test to see if two spaces overlap ...
     *  =========================================================
     */

    public boolean overlap( Space2DModel sp ) {
       BoundingBox box2 = sp.getBoundingBox();
       return boundary.overlap ( box2 );
    }
    
    public boolean overlap( Column2DModel sp ) {
       BoundingBox box2 = sp.getBoundingBox();
       return boundary.overlap ( box2 );
    }
    
    public boolean overlap( Corner2DModel sp ) {
       BoundingBox box2 = sp.getBoundingBox();
       return boundary.overlap ( box2 );
    }

    public boolean overlap( Wall2DModel sp ) {
        BoundingBox box2 = sp.getBoundingBox();
        return boundary.overlap ( box2 );
    }
    
    public boolean overlap( Portal2DModel sp ) {
        BoundingBox box2 = sp.getBoundingBox();
        return boundary.overlap ( box2 );
    }
    
    public boolean overlap( Door2DModel sp ) {
        BoundingBox box2 = sp.getBoundingBox();
        return boundary.overlap ( box2 );
    }
    
    public boolean overlap( Window2DModel sp ) {
        BoundingBox box2 = sp.getBoundingBox();
        return boundary.overlap ( box2 );
    }
    
    /*
     *  =========================================================
     *  touch(). Test to see if two spaces touch, either at a
     *           common vertex or perhaps a common edge fragment.
     *  =========================================================
     */

    public boolean touch( Space2DModel sp ) {
       BoundingBox box2 = sp.getBoundingBox();
       return boundary.touch ( box2 );
    }

    /*
     *  =========================================================
     *  Print details of two-dimensional space....
     *  =========================================================
     */

    public String toString() {
        String s = "Two-Dimensional Space: " + this.getName() + "\n";
        
        s += "====================================== \n";
        s += "Height: " + height + " ; Width: " + width + "\n";
        s += "====================================== \n";
        s += boundary.toString() + "\n";
        s += "====================================== \n";

        return s;
    }
    
    /**
     * Returns the GridLineModel of the designating value
     * @param value the location of the line
     * @param Vertical set to true if the line is x-gridline
     * @return GridLineModel
     */
    public GridLineModel findGridLineModel( double value, boolean Vertical ){
    	if( Vertical == true){
    		ArrayList xGridLineModel = new ArrayList( this.xGridLine );
    		for( int i=0; i < xGridLineModel.size(); i++ ){
    			GridLineModel gl = (GridLineModel) xGridLineModel.get(i);
    			if( gl.getAnchorX() == value )
    				return gl;
    		}	
    	}
    	if( Vertical != true ){
    		ArrayList yGridLineModel = new ArrayList( this.yGridLine );
    		for( int i=0; i < yGridLineModel.size(); i++ ){
    			GridLineModel gl = (GridLineModel) yGridLineModel.get(i);
    			if( gl.getAnchorY() == value )
    				return gl;
    		}	
    	}
    	
    	return null;
    }

    // ==============================================================
    // Accept method for Feature Element visitor ...
    // ==============================================================

    public void accept( FeatureElementVisitor visitor) {
        visitor.visit(this);
    }
    
    // ==============================================================
    // Method for notified Space2Dmodel when GridLineModel moved ...
    // ==============================================================    

    public void modelMoved( GridLineEvent evt) {
    	this.refine(grid);
    	this.getNeighbors(grid);
    }
    
    public void setHyperBoundingBox( HyperBoundingBox hbp )
    {
    	oldHp = hp;
    	hp = hbp;
    }
    
    public HyperBoundingBox getHyperBoundingBox()
    {
    	return hp;
    }

    // ==============================================================
    // Exercise methods in two-d space class ....
    // ==============================================================

    public static void main( String args[] ) {

        // Create an arraylist of vertices for spA...

        System.out.println("*** Start model.floorplan.Space2DModel.main() ... ");
        System.out.println("*** Part 1: Build spA ... ");
        System.out.println("*** ======================================= ");

        Coordinate nA = new Coordinate( "n1",  0.0,  0.0 );
        Coordinate nB = new Coordinate( "n2",  0.0, 50.0 );
        Coordinate nC = new Coordinate( "n3", 50.0, 50.0 );
        Coordinate nD = new Coordinate( "n4", 50.0, 30.0 );
        Coordinate nE = new Coordinate( "n5", 50.0,  0.0 );

        List outerLoop = new ArrayList();
        outerLoop.add ( nA );
        outerLoop.add ( nB );
        outerLoop.add ( nC );
        outerLoop.add ( nD );
        outerLoop.add ( nE );

        GridSystem grid = new GridSystem();
        Space2DModel spA     = new Space2DModel( "spA", grid, outerLoop );
        spA.setSpace2DType ( Space2DType.Block );

        System.out.println( spA.toString() );

        // Create an arraylist of vertices for spB...

        System.out.println("");
        System.out.println("*** Part 2: Build spB and spC ... ");
        System.out.println("*** ======================================= ");

        Coordinate nF = new Coordinate( "n1",  50.0, 25.0 );
        Coordinate nG = new Coordinate( "n2",  50.0, 75.0 );
        Coordinate nH = new Coordinate( "n3", 100.0, 75.0 );
        Coordinate nI = new Coordinate( "n4", 100.0, 25.0 );
        outerLoop = new ArrayList();
        outerLoop.add ( nF );
        outerLoop.add ( nG );
        outerLoop.add ( nH );
        outerLoop.add ( nI );

        Space2DModel spB = new Space2DModel( "spB", outerLoop, Space2DType.Block );
        System.out.println( spB.toString() );

        Coordinate nS = new Coordinate( "n1",  50.0, 25.0 );
        Coordinate nT = new Coordinate( "n2", 100.0, 25.0 );
        Coordinate nU = new Coordinate( "n3", 100.0, 00.0 );
        Coordinate nV = new Coordinate( "n4",  50.0, 00.0 );
        outerLoop = new ArrayList();
        outerLoop.add ( nS );
        outerLoop.add ( nT );
        outerLoop.add ( nU );
        outerLoop.add ( nV );

        Space2DModel spC = new Space2DModel( "spC", outerLoop, Space2DType.Block );
        System.out.println( spC.toString() );

        // Add coordinates for spA and spB to a grid system ...

        System.out.println("");
        System.out.println("*** Part 3: Add spA, spB and spC to archGrid ... ");
        System.out.println("*** ============================================ ");

        GridSystem archGrid = new GridSystem();
        archGrid.add ( spA );
        archGrid.add ( spB );
        archGrid.add ( spC );

        System.out.println( archGrid.toString() );

        System.out.println("");
        System.out.println("*** Part 4: Refine spA, spB, and spC ... ");
        System.out.println("*** ==================================== ");

        // Refine space description ....

        spA.refine( archGrid );
        spB.refine( archGrid );
        spC.refine( archGrid );

        // Get neighboring spaces along each outer edge ....

        spA.getNeighbors( archGrid );
        spB.getNeighbors( archGrid );
        spC.getNeighbors( archGrid );

        System.out.println( spA.toString() );
        System.out.println( spB.toString() );
        System.out.println( spC.toString() );

        System.out.println("");
        System.out.println("*** Part 5: Print refined grid ... ");
        System.out.println("*** ============================== ");

        System.out.println( archGrid.toString() );

    }
}
