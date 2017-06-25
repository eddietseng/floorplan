package model.primitive;

import model.*;

public class Arc extends AbstractFeature{
	private double dRadius;
	protected int thickness;
	protected double x1, y1, x2, y2;
	protected int startA, extA;
    protected Point end1;
    protected Point end2;

	// Constructor methods ...
	
	public Arc(double dX, double dY){
		super(dX, dY);
	}
	
	public Arc(String sName, double dX, double dY){
		super(dX, dY);
		setName(sName);
	}
	
	public void setExtenet(Point end1, Point end2){
	    this.x1 = end1.getX();
	    this.y1 = end1.getY();
	    this.x2 = end2.getX();
	    this.y2 = end2.getY();
	    this.end1 = end1;
	    this.end2 = end2;
	    this.dRadius = Math.abs(x2-x1);
	    if(getX() == x1){
	    	if(x1 < x2){
	    		if(y1 > y2){
	    			be.addPoint ( getX()+dRadius, getY()+dRadius );
	    			this.startA =   0;
	    			this.extA   = -90;
	    		}else{
	    			be.addPoint ( getX()+dRadius, getY()-dRadius );
	    			this.startA =  0;
	    			this.extA   = 90;
	    		}
	    	}else{
	    		if(y1 > y2){
	    			be.addPoint ( getX()-dRadius, getY()+dRadius );
	    			this.startA =  180;
	    			this.extA   =  90;
	    		}else{
	    			be.addPoint ( getX()-dRadius, getY()-dRadius );
	    			this.startA =  90;
	    			this.extA   =  90;
	    		}
	    	}
	    }
	    if(getY() == y1){
	    	if(x1 < x2){
	    		if(y1 > y2){
	    			be.addPoint ( getX()-dRadius, getY()-dRadius );
	    			this.startA =  180;
	    			this.extA   =  -90;
	    		}else{
	    			be.addPoint ( getX()-dRadius, getY()+dRadius );
	    			this.startA =  180;
	    			this.extA   =   90;
	    		}
	    	}else{
	    		if(y1 > y2){
	    			be.addPoint ( getX()+dRadius, getY()-dRadius );
	    			this.startA =   0;
	    			this.extA   =  90;
	    		}else{
	    			be.addPoint ( getX()+dRadius, getY()+dRadius );
	    			this.startA =  0;
	    			this.extA   = -90;
	    		}
	    	}
	    }
	    
	    setWidth(  dRadius );
	    setHeight( dRadius );
	}
	
	// Retrieve parameter values ....

    public void setThickness( int thickness ) {
       this.thickness = thickness;
    }

    public int getThickness() {
       return thickness;
    }
	
    public double getRadius () {
        return dRadius;
    }
    
    public int getstartA(){
    	return startA;
    }
    
    public int getextA(){
    	return extA;
    }

    // Create clone (i.e., deep copy) of Arc object ...

    public Arc clone(){
    	try {
    		Arc cloned = (Arc) super.clone();
            return cloned;
        } catch ( CloneNotSupportedException e) {
            	System.out.println("*** Error in Arc.clone() ... ");
                return null;
        }
    }

    // Create string representation for a Arc ....

    public String toString() {
    	String s = "Arc( " + sName + "): (x,y) = (" + getX() + "," + getY() + ") radius = " + dRadius 
    			   + " Starting angle = " + startA + " Extending angle = " + extA;
        return s;
    }

}
