package model.floorplan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import model.*;
import model.primitive.*;
import model.primitive.Edge;
import model.rtree.HyperBoundingBox;

public class Wall2DModel extends AbstractCompoundFeature 
implements FloorplanModelListener
{
	/**
	 * Grid system to which the wall will be 
	 */
	protected GridSystem    grid;
	
	/**
	 * The component boundary of this wall object
	 */
    protected ComponentBoundary boundary;
    
    protected HyperBoundingBox hp;
    protected HyperBoundingBox oldHp;
    protected int iNoComponent;
    
    /**
     * The associate point of this wall object
     */
    public Coordinate associatePoint = new Coordinate();
    
    /**
     * The old associate point before the object being moved
     */
    public Coordinate oldAssociatePoint;
    
    /**
     * A List of corners that's associate with this wall
     */
    public List<Corner2DModel> associateCorner = new ArrayList<Corner2DModel>();
    
    /**
     * A List of column that's associate with this wall
     */
    public List<Column2DModel> associateColumn = new ArrayList<Column2DModel>();
    
    /**
     * An arraylist of grid point object that this wall has contained
     */
    public ArrayList<GridPoint> gridPointList = new ArrayList<GridPoint>();
    
    /**
     * The composite hierarchy in this wall
     */
    protected CompositeHierarchy subCH;
    
    protected ArrayList<AbstractCompoundFeature> childList = new ArrayList<AbstractCompoundFeature>();
    
    protected ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();
    
    /**
     * Default constructor
     */
    public Wall2DModel() {
       this.boundary = new ComponentBoundary();
       this.subCH = new CompositeHierarchy();
    }

    public Wall2DModel( String cName )
    {
       setName ( cName );
       this.boundary = new ComponentBoundary();
       this.subCH = new CompositeHierarchy();
    }
    
    public Wall2DModel( String cName, GridSystem grid, Coordinate firstCoordinate )
    {
        setName ( cName );
        this.grid     = grid;
        this.boundary = new ComponentBoundary();
        this.boundary.vertexList.add( firstCoordinate );
        this.subCH = new CompositeHierarchy();

        // Add point to container grid .....
        grid.add ( cName, firstCoordinate, GridPointType.Vertex );
    }

    /**
     * Create rectangular Wall defined by diagonally opposite
     * coordinate points
     * @param dX1 the x value of the first diagonal point 
     * @param dY1 the y value of the first diagonal point
     * @param dX2 the x value of the second diagonal point
     * @param dY2 the y value of the second diagonal point
     */
    public Wall2DModel ( double dX1, double dY1, double dX2, double dY2 )
    {
    	this.boundary = new ComponentBoundary( dX1, dY1, dX2, dY2 );
    	this.subCH = new CompositeHierarchy();
    }
     
    public Wall2DModel ( String cName, GridSystem grid, double dX1, double dY1,
           double dX2, double dY2 )
    {
    	setName ( cName );
    	this.grid     = grid;
    	this.boundary = new ComponentBoundary( cName, dX1, dY1, dX2, dY2 );
    	this.subCH = new CompositeHierarchy();

    	// Add points to container grid .....
    	grid.add ( cName, new Coordinate( dX1, dY1 ), GridPointType.Vertex );
    	grid.add ( cName, new Coordinate( dX1, dY2 ), GridPointType.Vertex );
    	grid.add ( cName, new Coordinate( dX2, dY1 ), GridPointType.Vertex );
    	grid.add ( cName, new Coordinate( dX2, dY2 ), GridPointType.Vertex );
    }

    
    /**
     * Wall defined by list of vertex coordinates
     * @param cName Name of the wall object
     * @param outerLoop list of vertex coordinates
     */
    public Wall2DModel ( String cName, List outerLoop )
    {
    	setName ( cName );
    	this.boundary = new ComponentBoundary( cName, outerLoop );
    	this.subCH = new CompositeHierarchy();
    }
    
    /**
     * Wall defined by list of vertex coordinates and Gridsystem
     * @param cName Name of the wall object
     * @param grid GridSystem
     * @param outerLoop list of vertex coordinates
     */
    public Wall2DModel ( String cName, GridSystem grid, List outerLoop )
    {
        setName ( cName );
        this.grid     = grid;
        this.boundary = new ComponentBoundary( cName, outerLoop );
        this.subCH = new CompositeHierarchy();

        // Add points to container grid .....

        for ( int i = 0; i < outerLoop.size(); i = i + 1 )
        {
            Coordinate v1 = (Coordinate) outerLoop.get(i);
            grid.add ( cName, v1 , GridPointType.Vertex );
        }
    }
     
    /**
     * Retrieve component boundary 
     * @return ComponentBoundary
     */
    public ComponentBoundary getComponentBoundary()
    {
       return this.boundary;
    }
    
    /**
     * Retrieve bounding box
     * @return BoundingBox
     */
    public BoundingBox getBoundingBox()
    {
       return this.boundary.box;
    }
    
    /**
     * Set the center point as the associate point
     * @param X the x value of associate point
     * @param Y the y value of associate point
     */
    public void setAssociatePoint(double X, double Y)
    {
    	double oldX = associatePoint.getX();
    	double oldY = associatePoint.getY();
    	this.oldAssociatePoint = new Coordinate( oldX, oldY);
        this.associatePoint.dX = X;
    	this.associatePoint.dY = Y;
    }
    
    /**
     * Print details of two-dimensional wall
     */
    public String toString()
    {
        String s = "Two-Dimensional Wall: " + this.getName() + "\n";
        s += "====================================== \n";
        s += "Associate Point: " + associatePoint.toString() + "\n";
        s += "====================================== \n";
        s += "Height: " + this.height + "\n";
        s += "====================================== \n";
        s += "Width:  " + this.width + "\n";
        s += "====================================== \n";
        s += boundary.toString() + "\n";
        s += "====================================== \n";

        return s;
    } 
    
    /**
     * Returns the x distance between two associate corner.
     * @return double 
     */
    public double cornerXDistance()
    {
    	double cornerXDistance = 0;
    	if( this.associateCorner.size() < 2)
    	{
    		if( this.associateColumn.size() == 2)
    		{
    			cornerXDistance = Math.abs( this.associateColumn.get(0).associatePoint.getX() 
    					- this.associateColumn.get(1).associatePoint.getX() );
    			return cornerXDistance;
    		}
    		else
    		{
    			double cornerMaxX = this.associateCorner.get(0).getX() +
			            this.associateCorner.get(0).getHeight();
    			double columnMaxX = this.associateColumn.get(0).getX() +
                        this.associateColumn.get(0).getHeight();
    			if( cornerMaxX < this.associateColumn.get(0).getX() )
    				return cornerXDistance = this.associateColumn.get(0).getX() - cornerMaxX;
    			else if( columnMaxX < this.associateCorner.get(0).getX() )
    				return cornerXDistance = this.associateCorner.get(0).getX() - columnMaxX;
    			else
    				return cornerXDistance;
    		}
    	}
    	double cornerMaxX1 = this.associateCorner.get(0).getX() +
	            this.associateCorner.get(0).getHeight();
		double cornerMaxX2 = this.associateCorner.get(1).getX() +
                this.associateCorner.get(1).getHeight();
		if( cornerMaxX1 < this.associateCorner.get(1).getX() )
			return cornerXDistance = this.associateCorner.get(1).getX() - cornerMaxX1;
		else if( cornerMaxX2 < this.associateCorner.get(0).getX() )
			return cornerXDistance = this.associateCorner.get(0).getX() - cornerMaxX2;
		else
			return cornerXDistance;
    }
    
    /**
     * Returns the y distance between two associate corner.
     * @return double
     */
    public double cornerYDistance()
    {
    	double  cornerYDistance = 0;
    	if( this.associateCorner.size() < 2)
    	{
    		if( this.associateColumn.size() == 2)
    		{
    			cornerYDistance = Math.abs( this.associateColumn.get(0).associatePoint.getY() 
    					- this.associateColumn.get(1).associatePoint.getY() );
    			return cornerYDistance;
    		}
    		else
    		{
    			double cornerMaxY = this.associateCorner.get(0).getY() +
    					            this.associateCorner.get(0).getWidth();
    			double columnMaxY = this.associateColumn.get(0).getY() +
			                        this.associateColumn.get(0).getWidth();
    			if( cornerMaxY < this.associateColumn.get(0).getY() )
    				return cornerYDistance = this.associateColumn.get(0).getY() - cornerMaxY;
    			else if( columnMaxY < this.associateCorner.get(0).getY() )
    				return cornerYDistance = this.associateCorner.get(0).getY() - columnMaxY;
    			else
    				return cornerYDistance;
    		}
    	}
    	double cornerMaxY1 = this.associateCorner.get(0).getY() +
	            this.associateCorner.get(0).getWidth();
		double cornerMaxY2 = this.associateCorner.get(1).getY() +
                this.associateCorner.get(1).getWidth();
		if( cornerMaxY1 < this.associateCorner.get(1).getY() )
			return cornerYDistance = this.associateCorner.get(1).getY() - cornerMaxY1;
		else if( cornerMaxY2 < this.associateCorner.get(0).getY() )
			return cornerYDistance = this.associateCorner.get(0).getY() - cornerMaxY2;
		else
			return cornerYDistance;
    }
    
    /**
     * Returns a Coordinate to store two corners relationship
     * @return Coordinate used as a vector of movement
     */
    public Coordinate associateCornersRelationship()
    {
    	Coordinate relationship = new Coordinate( this.cornerXDistance(), this.cornerYDistance() );
    	return relationship;
    }
    
    /**
     * The X distance of the moved associate point and the old associate point. 
     * @param b boolean for checking null old associate point
     * @return distance the X movement
     */
    public double dXAssociatePoint(boolean b)
    {
    	if(this.oldAssociatePoint == null)
    	{
    		b = false;
    		System.out.println("****** NO OLD ASSOCIATE POINT SAVED (Wall2DModel.dXAssociatePoint()) ******");
    		return 0;
    	}
    	b = true;
    	double distance =  Math.abs( this.associatePoint.getX() - this.oldAssociatePoint.getX() );
    	if( this.height >= cornerXDistance() )
    	{
    	return distance * (-1.0);
    	}
    	
    	return distance;
    }
    
    /**
     * The Y distance of the moved associate point and the old associate point. 
     * @param b boolean for checking null old associate point
     * @return distance the Y movement
     */
    public double dYAssociatePoint(boolean b)
    {
    	if(this.oldAssociatePoint == null)
    	{
    		b = false;
    		System.out.println("****** NO OLD ASSOCIATE POINT SAVED (Wall2DModel.dYAssociatePoint()) ******");
    		return 0;
    	}
    	b = true;
    	double distance = Math.abs( this.associatePoint.getY() - this.oldAssociatePoint.getY() );
    	if( this.width >= cornerYDistance() )
    	{
    	return distance * (-1.0);
    	}
    	return distance;
    }
    /**
     * Re-define the wall object by setting the bounding box of the wall
     */
    public void refine()
    {
    	boolean xNullFilter = true;
    	boolean yNullFilter = true;
    	double dX = dXAssociatePoint(xNullFilter);
    	double dY = dYAssociatePoint(yNullFilter);
    	if( xNullFilter == false)
    	{
    		System.out.println("***** ERROR Wall2DModel.refine() *****");
    		return;
    	}
    	
    	System.out.println( "***** DEBUG Wall2DModel.refine() start ... *****");
    	System.out.println( "Name : " + this.sName );
    	System.out.println( "cornerXDistance() = " +  cornerXDistance() );
    	System.out.println( "cornerYDistance() = " +  cornerYDistance() );
    	System.out.println( "dX = " + dX );
    	System.out.println( "dY = " + dY );
    	System.out.println( "width = " + width );
    	System.out.println( "height = " + height );
    	System.out.println( "associatePoint = " + this.associatePoint );
    	
    	if( this.oldAssociatePoint == this.associatePoint)
    	{
    		System.out.println("***** WARNING (Wall2DModel.refine())  new associate point and new associate point is the same*****");
    		return;
    	}
    	
    	if( cornerXDistance() == 0)
    	{
    		if( dX != 0 )
    		{
    			this.getBoundingBox().set( this.associatePoint.getX() + height/2,
    	    			this.associatePoint.getY() + width/2, 
    	    			this.associatePoint.getX() - height/2,
    	                this.associatePoint.getY() - width/2);
    	    	
    	    	this.getComponentBoundary().refineVertextList();
    	    	System.out.println("***** INFO Wall2DModel.refine()  Case 1 *****");
    	    }
    		else
    		{
    			this.getBoundingBox().set( this.associatePoint.getX() + height/2,
    					this.associatePoint.getY() + (width/2+dY),
    					this.associatePoint.getX() - height/2,
    					this.associatePoint.getY() - (width/2+dY));
    			
    			this.getComponentBoundary().refineVertextList();
    			System.out.println("***** INFO Wall2DModel.refine()  Case 2 *****");
    		}	
    	}
    	else
    	{
    		if( dX != 0 )
    		{
    			this.getBoundingBox().set( this.associatePoint.getX() + (height/2+dX),
    					this.associatePoint.getY() + width/2,
    					this.associatePoint.getX() - (height/2+dX),
    					this.associatePoint.getY() - width/2);

    			this.getComponentBoundary().refineVertextList();
    			System.out.println("***** INFO Wall2DModel.refine()  Case 3 *****");
    		}
    		else
    		{
    			this.getBoundingBox().set( this.associatePoint.getX() + height/2,
    	    			this.associatePoint.getY() + width/2, 
    	    			this.associatePoint.getX() - height/2,
    	                this.associatePoint.getY() - width/2);
    	    	
    	    	this.getComponentBoundary().refineVertextList();
    	    	System.out.println("***** INFO Wall2DModel.refine()  Case 4 *****");
    	    }
    	}

    	System.out.println( "***** DEBUG Wall2DModel.refine() end ...   *****");
    }
     
    /**
     * Define the Wall with primitives ...
     */
    public void define()
    {
        BoundingBox box = this.getBoundingBox();

        double x1 = box.getMinX(); double y1 = box.getMinY();
        double x2 = box.getMaxX(); double y2 = box.getMaxY();
        
        System.out.println( "***** DEBUG Wall2DModel.define() start ... *****");
        System.out.println("(x1, y1) : (" + x1 +", " + y1 + ")");
        System.out.println("(x2, y2) : (" + x2 +", " + y2 + ")");
        System.out.println( "***** DEBUG Wall2DModel.define() end ...   *****");
        
        // Save Wall reference point for shape ...
        setX( (double) x1 );
        setY( (double) y1 );
        setHeight( (double) (x2 - x1));
        setWidth(  (double) (y2 - y1));

        //Overwrite CH in wall
        this.setCompositeHierarchy( new CompositeHierarchy( this.getX(), this.getY(), 0.0) );

    	
        //Set associate point ...
        setAssociatePoint(x1 + (x2-x1)/2, y1 + (y2-y1)/2);

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
     
 	/**
 	 * Accept method for Feature Element visitor ...
 	 */
 	public void accept( FeatureElementVisitor visitor)
 	{
 		visitor.visit(this);
 	}
 	
	@Override
	/**
	 * The method being call when gets notified
	 */
	public void modelMoved(GridLineEvent evt)
	{
		System.out.println( "***** DEBUG Wall2DModel.modelMoved() start ... *****");
		System.out.println( "associateCorner.get(0).getName() : " + associateCorner.get(0).getName() );
		System.out.println( "associateCorner.get(0).getX() : " + associateCorner.get(0).getX() );
		if( associateCorner.size() > 1 )
		{
			System.out.println( "associateCorner.get(1).getName() : " + associateCorner.get(1).getName() );
			System.out.println( "associateCorner.get(1).getX() : " + associateCorner.get(1).getX() );
		} else{
			System.out.println( "associateColumn.get(0).getName() : " + associateColumn.get(0).getName() );
			System.out.println( "associateColumn.get(0).getX() : " + associateColumn.get(0).getX() );
		}
		System.out.println( "-------------------------------" );
		System.out.println( "associateCorner.get(0).getName() : " + associateCorner.get(0).getName() );
		System.out.println( "associateCorner.get(0).getY() : " + associateCorner.get(0).getY() );
		if( associateCorner.size() > 1 )
		{
			System.out.println( "associateCorner.get(1).getName() : " + associateCorner.get(1).getName() );
			System.out.println( "associateCorner.get(1).getY() : " + associateCorner.get(1).getY() );
		} else{
			System.out.println( "associateColumn.get(0).getName() : " + associateColumn.get(0).getName() );
			System.out.println( "associateColumn.get(0).getY() : " + associateColumn.get(0).getY() );
		}
		System.out.println( "-------------------------------" );
		System.out.println( "this.getX : " + this.getX() );
		System.out.println( "this.getY : " + this.getY() );
		System.out.println( "***** DEBUG Wall2DModel.modelMoved() end ...   *****" );
		
		if( associateCorner.size() > 1 )
		{
			if( (associateCornersRelationship().getX() == 0 && dXAssociatePoint(true) != 0) || (associateCornersRelationship().getY() == 0 && dYAssociatePoint(true) != 0) )
			{
				if((associateCorner.get(0).getX() == associateCorner.get(1).getX() &&
						associateCorner.get(0).getX() != this.getX())
						|| (associateCorner.get(0).getY() == associateCorner.get(1).getY() &&
								associateCorner.get(0).getY() != this.getY())){
					refine();
					define();
					System.out.println("***** INFO Wall2DModel.modelMoved()  Corner Case 1 *****");
				}
			}
			
			else if( (associateCornersRelationship().getX() == 0 && dXAssociatePoint(true) == 0) || (associateCornersRelationship().getY() == 0 && dYAssociatePoint(true) == 0) )
			{
				refine();
				define();
				System.out.println("***** INFO Wall2DModel.modelMoved()  Corner Case 2 *****");
			}
		}
		else if( associateCorner.size() == 1 )
		{
			if( (associateCornersRelationship().getX() == 0 && dXAssociatePoint(true) != 0) || (associateCornersRelationship().getY() == 0 && dYAssociatePoint(true) != 0) )
			{
				System.out.println("***** INFO Wall2DModel.modelMoved()  Column step 1.1 *****");
				if((associateCorner.get(0).getAssociatePoint().getX() == associateColumn.get(0).getAssociatePoint().getX() &&
						associateCorner.get(0).getX() != this.getX())
						|| (associateCorner.get(0).getY() == associateColumn.get(0).getY() &&
								associateCorner.get(0).getY() != this.getY())){
					refine();
					define();
					System.out.println("***** INFO Wall2DModel.modelMoved()  Column Case 1 *****");
				}
			}
			
			else if( (associateCornersRelationship().getX() == 0 && dXAssociatePoint(true) == 0) || (associateCornersRelationship().getY() == 0 && dYAssociatePoint(true) == 0) )
			{
				System.out.println("***** INFO Wall2DModel.modelMoved()  Column step 2.1 *****");
				refine();
				define();
				System.out.println("***** INFO Wall2DModel.modelMoved()  Column Case 2 *****");
			}
			
		}
		fireStateChanged();
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
    
    /**
     * Sets the composite hierarchy of the wall component
     * @param CompositeHierarchy ch
     */
    public void setCompositeHierarchy( CompositeHierarchy ch )
    {
    	if( ch != null && !this.subCH.equals( ch ) )
    		this.subCH = ch;
    }
    
    /**
     * Returns the CompositeHierarchy of the wall component
     * @return CompositeHierarchy
     */
    public CompositeHierarchy getCompositeHierarchy()
    {
    	return this.subCH;
    }
    
    public ArrayList<AbstractCompoundFeature> getChildList()
    {
    	return childList;
    }
    
    public void addChangeListener(ChangeListener l) 
    {
    	System.out.println( "Wall2DModel.addChangeListener() " );
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) 
    {
        listenerList.remove(ChangeListener.class, l);
    }
    
    protected void fireStateChanged() 
    {
    	System.out.println( "Wall2DModel.fireStateChanged() " );
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -=2 ) 
        {
            if (listeners[i] == ChangeListener.class) 
            {
                if (changeEvent == null) 
                {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }
}