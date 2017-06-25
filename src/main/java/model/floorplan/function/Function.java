
package model.floorplan.function;

import view.PrintViewFloorplanResultVisitor;

public abstract class Function {
	
	public double function(double x[]){
		return 0;
	}
	
	public String getReqName(){
		return new String( "123" );
	}
	
	public boolean isValid(){
		return true;
	}
	
	public void setStatus( double x ){}
	
	public void accept( PrintViewFloorplanResultVisitor visitor ){
		visitor.visit( this );
	}
}
