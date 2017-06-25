package model.floorplan;

import java.util.Iterator;

import model.*;
import model.primitive.*;
import model.primitive.Edge;

public class Door2DModel extends AbstractCompoundFeature{
	
	public boolean horizontal = true;
	public boolean left = true;
	public boolean in = true;
	protected ComponentBoundary boundary;
	
	public Door2DModel(double x1, double y1, double x2, double y2, boolean horizontal, boolean left, boolean in)
	{
		this.boundary = new ComponentBoundary( x1, y1, x2, y2 );
		// Setup the door ...
		
		this.horizontal = horizontal;
		this.left = left;
		this.in = in;
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
  		  System.out.println( "Door2DModel.define ... remove items" );
        }
	    
	    be.addPoint( x1, y1 );
	    be.addPoint( x2 - x1, y2 - y1 );

	    // Create vertices for portal schematic ...

	    int pSize = 2;

	    Point body01 = new Point( "b01", x1, y1, pSize, pSize );
	    Point body02 = new Point( "b02", x1, y2, pSize, pSize );
	    Point body03 = new Point( "b03", x2, y2, pSize, pSize );
	    Point body04 = new Point( "b04", x2, y1, pSize, pSize );
	    Point body05;
	    
	    // Connect edges to vertices ....

	    Edge edge01 = new Edge( "edge01",  body01, body02 );
	    edge01.setThickness( 2 );
	    Edge edge02 = new Edge( "edge02",  body02, body03 );
	    edge02.setThickness( 2 );
	    Edge edge03 = new Edge( "edge03",  body03, body04 );
	    edge03.setThickness( 2 );
	    Edge edge04 = new Edge( "edge04",  body04, body01 );
	    edge04.setThickness( 2 );
	    
	    // Create Arc ...
	    
	    Arc arc01;
	    Edge edge05;
	    
	    // Decide the door ...
	    
	    if(horizontal == true){
	    	if(left == true){
	    		if(in == true){
	    			body05 = new Point( "b05", x1, y2 + (x2 - x1), pSize, pSize );
	    			edge05 = new Edge( "edge05",  body02, body05 );
	    		    edge05.setThickness( 2 );
	    			arc01 = new Arc("a01", x1, y2);
	    			arc01.setExtenet(body05, body03);
	    		}else{
	    			body05 = new Point( "b05", x1, y1 - (x2 - x1), pSize, pSize );
	    			edge05 = new Edge( "edge05",  body01, body05 );
	    		    edge05.setThickness( 2 );
	    			arc01 = new Arc("a01", x1, y1);
	    			arc01.setExtenet(body05, body04);
	    		}
	    	}else{
	    		if(in == true){
	    			body05 = new Point( "b05", x2, y2 + (x2 - x1), pSize, pSize );
	    			edge05 = new Edge( "edge05",  body03, body05 );
	    		    edge05.setThickness( 2 );
	    			arc01 = new Arc("a01", x2, y2);
	    			arc01.setExtenet(body05, body02);
	    		}else{
	    			body05 = new Point( "b05", x2, y1 - (x2 - x1), pSize, pSize );
	    			edge05 = new Edge( "edge05",  body04, body05 );
	    		    edge05.setThickness( 2 );
	    			arc01 = new Arc("a01", x2, y1);
	    			arc01.setExtenet(body05, body01);
	    		}
	    	}
	    }
	    else{
	    	if(left == true){
	    		if(in == true){
	    			body05 = new Point( "b05", x1 - (y2 - y1), y1, pSize, pSize );
	    			edge05 = new Edge( "edge05",  body01, body05 );
	    		    edge05.setThickness( 2 );
	    			arc01 = new Arc("a01", x1, y1);
	    			arc01.setExtenet(body05, body02);
	    		}else{
	    			body05 = new Point( "b05", x2 + (y2 - y1), y1, pSize, pSize );
	    			edge05 = new Edge( "edge05",  body04, body05 );
	    		    edge05.setThickness( 2 );
	    			arc01 = new Arc("a01", x2, y1);
	    			arc01.setExtenet(body05, body03);
	    		}
	    	}else{
	    		if(in == true){
	    			body05 = new Point( "b05", x1 - (y2 - y1), y2, pSize, pSize );
	    			edge05 = new Edge( "edge05",  body02, body05 );
	    		    edge05.setThickness( 2 );
	    			arc01 = new Arc("a01", x1, y2);
	    			arc01.setExtenet(body05, body01);
	    		}else{
	    			body05 = new Point( "b05", x2 + (y2 - y1), y2, pSize, pSize );
	    			edge05 = new Edge( "edge05",  body03, body05 );
	    		    edge05.setThickness( 2 );
	    			arc01 = new Arc("a01", x2, y2);
	    			arc01.setExtenet(body05, body04);
	    		}
	    	}
	    }
	    
	    // Add vertices to model ...

	    items.put( body01.getName(), body01 );
	    items.put( body02.getName(), body02 );
	    items.put( body03.getName(), body03 );
	    items.put( body04.getName(), body04 );
	    items.put( body05.getName(), body05 );
	    
	    // Add edges to model ...

	    items.put( edge01.getName(), edge01 );
	    items.put( edge02.getName(), edge02 );
	    items.put( edge03.getName(), edge03 );
	    items.put( edge04.getName(), edge04 );
	    items.put( edge05.getName(), edge05 );
	    
	    // Add arc to model ...
	    
	    items.put(  arc01.getName(),  arc01 );
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
