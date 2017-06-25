package model.floorplan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.*;
import model.primitive.*;
import model.primitive.Edge;
import model.rtree.HyperBoundingBox;

public class Corner2DModel extends AbstractCompoundFeature implements FloorplanModelListener{
	protected GridSystem    grid; // Grid system to which the Corner will be 
    protected ComponentBoundary boundary;
    protected HyperBoundingBox hp;
    protected HyperBoundingBox oldHp;
    protected int iNoComponent;
    public Coordinate oldAssociatePoint;
    public Coordinate associatePoint = new Coordinate();
    public ArrayList<GridPoint> gridPointList = new ArrayList<GridPoint>();
    public ArrayList<Wall2DModel> associateWallList = new ArrayList<Wall2DModel>();//Save the corner that is associate with this corner
    private List<FloorplanModelListener> listeners = new ArrayList<FloorplanModelListener>();
    
    
    // Constructor methods ....

    public Corner2DModel() {
       this.boundary = new ComponentBoundary();
    }

    public Corner2DModel( String cName ) {
       setName ( cName );
       this.boundary = new ComponentBoundary();
    }
    
    public Corner2DModel( String cName, GridSystem grid, Coordinate firstCoordinate ) {
        setName ( cName );
        this.grid     = grid;
        this.boundary = new ComponentBoundary();
        this.boundary.vertexList.add( firstCoordinate );

        // Add point to container grid .....

        grid.add ( cName, firstCoordinate, GridPointType.Vertex );
    }

    // ======================================================================
    // Create rectangular Corner defined by diagonally opposite
    // coordinate points .....
    // ======================================================================

    public Corner2DModel ( double dX1, double dY1, double dX2, double dY2 ) {
    	this.boundary = new ComponentBoundary( dX1, dY1, dX2, dY2 );
    }
     
    public Corner2DModel ( String cName, GridSystem grid, double dX1, double dY1,
           double dX2, double dY2 ) {
    	setName ( cName );
    	this.grid     = grid;
    	this.boundary = new ComponentBoundary( cName, dX1, dY1, dX2, dY2 );

    	// Add points to container grid .....
    	
    	GridPoint gp1 = new GridPoint(cName, new Coordinate( dX1, dY1 ).getX(), 
    			new Coordinate( dX1, dY1 ).getY(), GridPointType.Vertex);
    	GridPoint gp2 = new GridPoint(cName, new Coordinate( dX1, dY2 ).getX(), 
    			new Coordinate( dX1, dY2 ).getY(), GridPointType.Vertex);
    	GridPoint gp3 = new GridPoint(cName, new Coordinate( dX2, dY1 ).getX(), 
    			new Coordinate( dX2, dY1 ).getY(), GridPointType.Vertex);
    	GridPoint gp4 = new GridPoint(cName, new Coordinate( dX2, dY2 ).getX(), 
    			new Coordinate( dX2, dY2 ).getY(), GridPointType.Vertex);
    	
    	this.gridPointList.add( gp1 );
    	this.gridPointList.add( gp2 );
    	this.gridPointList.add( gp3 );
    	this.gridPointList.add( gp4 );
    	
    	Iterator iterator = gridPointList.iterator();
        while ( iterator.hasNext() != false) {
           GridPoint gp = (GridPoint) iterator.next();
    	grid.add ( gp );
        }
        
    }

    // =======================================================
    // Corner defined by list of vertex coordinates....
    // =======================================================
     
    public Corner2DModel ( String cName, List outerLoop ) {
    	setName ( cName );
    	this.boundary = new ComponentBoundary( cName, outerLoop );
    }
     
    public Corner2DModel ( String cName, GridSystem grid, List outerLoop ) {
        setName ( cName );
        this.grid     = grid;
        this.boundary = new ComponentBoundary( cName, outerLoop );

        // Add points to container grid .....

        for ( int i = 0; i < outerLoop.size(); i = i + 1 ) {
            Coordinate v1 = (Coordinate) outerLoop.get(i);
            GridPoint gp = new GridPoint(cName, v1.getX(), v1.getY(), GridPointType.Vertex);
            this.gridPointList.add( gp );
            grid.add ( gp );
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
    
    /*
     *  =========================================================
     *  Print details of two-dimensional corner....
     *  =========================================================
     */

    public String toString() {
        String s = "Two-Dimensional Corner: " + this.getName() + "\n";
        s += "====================================== \n";
        s += "Associate Point: " + associatePoint.toString() + "\n";
        s += "====================================== \n";
        s += "Height: " + height + " ; Width: " + width + "\n";
        s += "====================================== \n";
        s += boundary.toString() + "\n";
        s += "====================================== \n";

        return s;
    }    
    
    //Set the two reference gridlines intersection point as the associate point
    public void setAssociatePoint(double X, double Y){
    	double oldX = associatePoint.getX();
    	double oldY = associatePoint.getY();
    	this.oldAssociatePoint = new Coordinate( oldX, oldY);
        this.associatePoint.dX = X;
    	this.associatePoint.dY = Y;
    }
    
    public Coordinate getAssociatePoint(){
    	return associatePoint;
    }
    
    public Coordinate getOldAssociatePoint()
    {
    	if(this.oldAssociatePoint != null)
    		return this.oldAssociatePoint;
    	else
    		return null;
    }
    
    public void refine(){
    	this.getBoundingBox().set( this.associatePoint.getX() + width/2,
    			this.associatePoint.getY() + height/2, 
    			this.associatePoint.getX() - width/2,
                this.associatePoint.getY() - height/2);
    	
    	this.getComponentBoundary().refineVertextList();
    	
    	System.out.println("######################### THIS IS GRID POINT (Corner2DModel.refine()) #########################");
    	Iterator iterator = this.gridPointList.iterator();
        while ( iterator.hasNext() != false) {
           GridPoint gp = (GridPoint) iterator.next();
           System.out.println(gp.toString());
        }
        System.out.println("#########################   GRID POINT END (Corner2DModel.refine())  #########################");
    	
    }
    
     
    // Define the Corner with primitives ...
    public void define(){
        BoundingBox box = this.getBoundingBox();

        double x1 = box.getMinX(); double y1 = box.getMinY();
        double x2 = box.getMaxX(); double y2 = box.getMaxY();

        // Save corner reference point for shape ...

        setX( (double) x1 );
        setY( (double) y1 );
        setHeight( (double) (x2 - x1));
        setWidth(  (double) (y2 - y1));
        
        //Set associate point ...
        
        if(associatePoint.getX() != oldAssociatePoint.getX() && associatePoint.getY() != oldAssociatePoint.getY())
        	setAssociatePoint(x1 + (x2-x1)/2, y1 + (y2-y1)/2);

        // Initialize envelope ...

        be.addPoint( x1, y1 );
        be.addPoint( x2, y2 );
        
        System.out.println("Corner x1:" + x1);
        System.out.println("Corner y1:" + y1);
        System.out.println("Corner x2:" + x2);
        System.out.println("Corner y2:" + y2);
        
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
     
 	// Accept method for Feature Element visitor ...

 	public void accept( FeatureElementVisitor visitor) {
 		visitor.visit(this);
 	}
 	
 	public void addFloorplanModelListener( FloorplanModelListener listener) {
 	     if(listener instanceof Wall2DModel )
 	    	 this.associateWallList.add( (Wall2DModel) listener);
 		  listeners.add(listener);
 	  }

 	public void removeFloorplanModelListener( FloorplanModelListener listener) {
 		if(listener instanceof Wall2DModel )
 			this.associateWallList.remove( (Wall2DModel) listener);
 		listeners.remove(listener);
 	  }

	public void modelMoved(GridLineEvent evt) {
		refine();
		define();
		Iterator iterator = this.associateWallList.iterator();
		while(iterator.hasNext() != false){
	     	 Wall2DModel wall = (Wall2DModel) iterator.next();
	     	 
	     	 System.out.println("Corner2DModel.modelMoved() wall.sName :" + wall.getName() );
	     	 System.out.println("Corner2DModel.modelMoved() this.sName :" + this.sName );
	     	 System.out.println("Corner2DModel.modelMoved() wall.associatePoint.getX()" + wall.associatePoint.getX());
	     	 System.out.println("Corner2DModel.modelMoved() this.oldAssociatePoint.getX()" + this.oldAssociatePoint.getX());
	     	 System.out.println("Corner2DModel.modelMoved() this.associatePoint.getX()" + this.associatePoint.getX());
	     	 System.out.println("Corner2DModel.modelMoved() wall.associatePoint.getY()" + wall.associatePoint.getY());
	     	 System.out.println("Corner2DModel.modelMoved() this.oldAssociatePoint.getY()" + this.oldAssociatePoint.getY());
	     	 System.out.println("Corner2DModel.modelMoved() this.associatePoint.getY()" + this.associatePoint.getY());
	     	 
	     	 if(wall.associatePoint.getX() == this.oldAssociatePoint.getX())
	     	 {
	     		 if(this.oldAssociatePoint.getX() != this.associatePoint.getX())
	     		 {
	     			//Corner has x movement, and the wall is parallel with the y-axis
		     		 System.out.println("Corner2DModel.modelMoved() ... case 1");
		     		 if(wall.associatePoint.dX != this.associatePoint.getX())
		     			 wall.associatePoint.dX = this.associatePoint.getX();
	     		 }
	     		 else
	     		 {
	     			//Corner has y movement, and the wall is parallel with the y-axis
		     		 System.out.println("Corner2DModel.modelMoved() ... case 2");
		     		 wall.associatePoint.dY = wall.associatePoint.dY +
		     				 ((this.associatePoint.getY() - this.oldAssociatePoint.getY())/2 );
	     		 }
	     	 }
	     	if(wall.associatePoint.getY() == this.oldAssociatePoint.getY())
	     	 {
	     		if(this.oldAssociatePoint.getX() != this.associatePoint.getX())
	     		 {
	     			//Corner has x movement, and the wall is parallel with the x-axis
		     		 System.out.println("Corner2DModel.modelMoved() ... case 3");
		     		 wall.associatePoint.dX = wall.associatePoint.dX +
		     				 ((this.associatePoint.getX() - this.oldAssociatePoint.getX())/2 );
	     		 }
	     		 else
	     		 {
	     			//Corner has y movement, and the wall is parallel with the x-axis
		     		 System.out.println("Corner2DModel.modelMoved() ... case 4");
		     		 if(wall.associatePoint.dY != this.associatePoint.getY())
		     			 wall.associatePoint.dY = this.associatePoint.getY();
	     		 }
	     	 }
	      }
		notifyListeners();
	}

	private void notifyListeners() {
	     GridLineEvent evt = new GridLineEvent(this);
	     for (FloorplanModelListener aListener : listeners) {
	          aListener.modelMoved(evt);
	     }
	  }
	
    public void setHyperBoundingBox( HyperBoundingBox hbp ) {
    	oldHp = hp;
    	hp = hbp;
    }
    
    public HyperBoundingBox getHyperBoundingBox() {
    	return hp;
    }
    
    public void setNoComponent( int number){
    	this.iNoComponent = number;
    }
    
    public int getNoComponent(){
    	return iNoComponent;
    }
}
