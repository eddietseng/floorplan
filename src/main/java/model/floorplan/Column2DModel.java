package model.floorplan;

import java.util.ArrayList;
import java.util.List;
import model.*;
import model.primitive.*;
import model.primitive.Edge;

public class Column2DModel extends AbstractCompoundFeature implements FloorplanModelListener{
	protected GridSystem    grid; // Grid system to which the column will be 
    protected ComponentBoundary boundary;
    public Coordinate associatePoint = new Coordinate();
    
    // Constructor methods ....

    public Column2DModel() {
       this.boundary = new ComponentBoundary();
    }

    public Column2DModel( String cName ) {
       setName ( cName );
       this.boundary = new ComponentBoundary();
    }
    
    public Column2DModel( String cName, GridSystem grid, Coordinate firstCoordinate ) {
        setName ( cName );
        this.grid     = grid;
        this.boundary = new ComponentBoundary();
        this.boundary.vertexList.add( firstCoordinate );

        // Add point to container grid .....

        grid.add ( cName, firstCoordinate, GridPointType.Vertex );
    }

    // ======================================================================
    // Create rectangular column defined by diagonally opposite
    // coordinate points .....
    // ======================================================================

    public Column2DModel ( double dX1, double dY1, double dX2, double dY2 ) {
    	this.boundary = new ComponentBoundary( dX1, dY1, dX2, dY2 );
    }
     
    public Column2DModel ( String cName, GridSystem grid, double dX1, double dY1,
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
    // Column defined by list of vertex coordinates....
    // =======================================================
     
    public Column2DModel ( String cName, List outerLoop ) {
    	setName ( cName );
    	this.boundary = new ComponentBoundary( cName, outerLoop );
    }
     
    public Column2DModel ( String cName, GridSystem grid, List outerLoop ) {
        setName ( cName );
        this.grid     = grid;
        this.boundary = new ComponentBoundary( cName, outerLoop );

        // Add points to container grid .....

        for ( int i = 0; i < outerLoop.size(); i = i + 1 ) {
            Coordinate v1 = (Coordinate) outerLoop.get(i);
            grid.add ( cName, v1 , GridPointType.Vertex );
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
     
    // Define the column with primitives ...
    public void define(){
        BoundingBox box = this.getBoundingBox();

        double x1 = box.getMinX(); double y1 = box.getMinY();
        double x2 = box.getMaxX(); double y2 = box.getMaxY();

        // Save corner reference point for shape ...

        setX( (double) x1 );
        setY( (double) y1 );
        setHeight( (double) (x2 - x1));
        setWidth(  (double) (y2 - y1));

        // Initialize envelope ...

        be.addPoint( x1, y1 );
        be.addPoint( x2, y2 );
        
	    // Create vertices for portal schematic ...

	    int pSize = 2;

	    Point body01 = new Point( "b01", x1, y1, pSize, pSize );
	    Point body02 = new Point( "b02", x1, y2, pSize, pSize );
	    Point body03 = new Point( "b03", x2, y2, pSize, pSize );
	    Point body04 = new Point( "b04", x2, y1, pSize, pSize );
	    
	    // Connect edges to vertices ....

	    Edge edge01 = new Edge( "edge01",  body01, body02 );
	    edge01.setThickness( 2 );
	    Edge edge02 = new Edge( "edge02",  body02, body03 );
	    edge02.setThickness( 2 );
	    Edge edge03 = new Edge( "edge03",  body03, body04 );
	    edge03.setThickness( 2 );
	    Edge edge04 = new Edge( "edge04",  body04, body01 );
	    edge04.setThickness( 2 );
	    
	    // Add vertices to model ...

	    items.put( body01.getName(), body01 );
	    items.put( body02.getName(), body02 );
	    items.put( body03.getName(), body03 );
	    items.put( body04.getName(), body04 );
	    
	    // Add edges to model ...

	    items.put( edge01.getName(), edge01 );
	    items.put( edge02.getName(), edge02 );
	    items.put( edge03.getName(), edge03 );
	    items.put( edge04.getName(), edge04 );
	    
    }
    
    //Set the two reference gridlines intersection point as the associate point
    
    public void setAssociatePoint(double X, double Y){
    	this.associatePoint.dX = X;
    	this.associatePoint.dY = Y;
    }
    
    public Coordinate getAssociatePoint(){
    	return associatePoint;
    }
     
 	// Accept method for Feature Element visitor ...

 	public void accept( FeatureElementVisitor visitor) {
 		visitor.visit(this);
 	}

 	// ==============================================================
    // Method for notified Column2DModel when Space2DModel moved ...
    // ==============================================================
 	
	public void modelMoved(GridLineEvent evt) {
		// Auto-generated method stub
		
	}
	
	public String toString()
	{
		String s = "Two-Dimensional Column: " + this.getName() + "\n";
        s += "====================================== \n";
        s += "Height: " + height + " ; Width: " + width + "\n";
        s += "====================================== \n";
        s += boundary.toString() + "\n";
        s += "====================================== \n";
        
        return s;
	}
     
     
}


