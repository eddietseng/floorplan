package model.floorplan.function;

public class BCMinUnitAreaFunction extends Function{
	
	public final String CODE = "BC Section 1208.3 Min Unit Area";
	private int st = 0;
	protected double[] temp;
	
	public BCMinUnitAreaFunction(){}

	// Section 1208.3 Minimum Room Area Requirements in Building code
	
	/**
	 * Used to validate the floorplan
	 * @param double[] x x[0] = Number of rooms
	 * @param double[] x x[1] ~ x[5] = Room area
	 * @return double 1 = true if satisfies the requirement
	 */
	@Override
	public double function(double[] x) {
		temp = (double[])x.clone();
		
		boolean valid = false;
		if( x[0] >= 1 && x[0] <= 5 )
		{
			for( int i = 1; i < x.length; i = i + 1)
			{
				if( x[0] == 1 && x[1] >= 120 ) // 1 room
					valid = true;
				else if( x[0] >= 2 && i >= 1 ) // more than 1 room
				{
					if( x[i] >= 70 || x[i] == 0 )
						valid = true;
					else
						return 0;
				}
				else
					return 0;
			}
			if( valid )
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
	/* TEST
	public static void main( String arg[] )
	{
		double x[] = { 5, 342, 293, 156, 183, 406, 0 };
		BCMinUnitAreaFunction f = new BCMinUnitAreaFunction();
		System.out.println( "Result: " + f.function( x ) );
	}*/
}
