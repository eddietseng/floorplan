package model.floorplan;

import java.util.Iterator;

import model.*;
import model.primitive.*;
import model.primitive.Edge;

public class Portal2DModel extends AbstractCompoundFeature{
	protected ComponentBoundary boundary;
	
	public Portal2DModel(double x1, double y1, double x2, double y2){
		this.boundary = new ComponentBoundary( x1, y1, x2, y2 );
	}
	
	public void define()
	{
		BoundingBox box = this.getBoundingBox();

        double x1 = box.getMinX(); double y1 = box.getMinY();
        double x2 = box.getMaxX(); double y2 = box.getMaxY();
        
     // Save corner reference point and width of shape ...
		
     		setX((double) x1);
     	    setY( (double) y1 );
     	    setWidth( (double) x2 - x1 );
     	    setHeight( (double) y2 - y1 );
     	    
     	   if( !items.isEmpty() )
           {
     		  Iterator it = items.entrySet().iterator();
     		  while (it.hasNext()) 
     		  {
  		    	items.remove( it.next() );
  		        it.remove(); // avoids a ConcurrentModificationException
  		      }
     		  System.out.println( "Portal2DModel.define ... remove items" );
           }
     	    
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
	
    // ==============================================================
    // Retrieve component boundary and bounding box ...
    // ==============================================================

    public ComponentBoundary getComponentBoundary() {
       return this.boundary;
    }

    public BoundingBox getBoundingBox() {
       return this.boundary.box;
    }
	
	// Accept method for Feature Element visitor ...

	public void accept( FeatureElementVisitor visitor) {
		visitor.visit(this);
	}
}
