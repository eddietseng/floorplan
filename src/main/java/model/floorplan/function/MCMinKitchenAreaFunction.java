package model.floorplan.function;

public class MCMinKitchenAreaFunction extends Function {
	
	public final String CODE = "MC Section 404.5 Kitchen";
	private int st = 0;
	protected double[] temp;
	
	public MCMinKitchenAreaFunction(){}

	// Section 404.2 Minimum width Requirements for kitchen
	/**
	 * Used to validate the room
	 * @param double[] x x[0] = Number of occupants
	 * @param double[] x x[1] = Room area
	 * @return double 1 = true if satisfies the requirement
	 */
	@Override
	public double function(double[] x) {
		temp = (double[])x.clone();
		
		if( x.length == 2 )
		{
			if( x[0] == 0 )
				return 1;
			// 1 ~ 2 occupants
			else if( ( x[0] >= 1 && x[0] <= 2) && x[1] >= 49 )
				return 1;
			
			// 3 ~ 5 occupants
		    else if( ( x[0] >= 3 && x[0] <= 5 ) && x[1] >= 50 )
				return 1;
						
			// more than 6 occupant
			else if( x[0] >= 6 && x[1] >= 60 )
				return 1;
		}
		return 0;
	}
	
	public String getReqName(){
		return CODE;
	}
	
	public boolean isValid(){
		if( st == 1 )
			return true;
		else
			return false;
	}
	
	public void setStatus( double x ){
		this.st = (int)x;
	}
	
	public double[] getInputMatrix(){
		return temp;
	}
}
