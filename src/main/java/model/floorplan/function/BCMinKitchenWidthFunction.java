package model.floorplan.function;

public class BCMinKitchenWidthFunction extends Function{
	
	public final String CODE = "BC Section 1208.1 Kitchen Width";
	private int st = 0;
	protected double[] temp;
	
	public BCMinKitchenWidthFunction(){}

	// Section 1208.1 Minimum kitchen clear passage way widths in Building code
	// with assumption the width of the counter top min 2.5 feet
	/**
	 * Used to validate the kitchen
	 * @param double[] x x[] = Width and Height
	 * @return double 1 = true if satisfies the requirement
	 */
	@Override
	public double function(double[] x) {
		temp = (double[])x.clone();
		
		if( x.length >= 4 )
		{
			int count = 0; // if satisfied + 1
			
			for( int i = 0; i < x.length; i++ )
			{
				if( x[i] >= 5.5 )
					count = count + 1;
			}
			// The length around the room will have one duplicate side
			// As long as we have 2 side satisfied we can assume the room satisfies
			if( count >= 3 )
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
