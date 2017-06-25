package model.floorplan.function;

public class MCMinLivingroomAreaFunction extends Function{
	
	public final String CODE = "MC Section 404.5 Living Room";
	private int st = 0;
	protected double[] temp;
	
	public MCMinLivingroomAreaFunction(){}

	// Section 404.5 Minimum Area Requirements table in Maintenance code
	// combine Living and Dining room
	/**
	 * Used to validate the room
	 * @param double[] x x[0] = LivingRoom counts
	 * @param double[] x x[1],x[3] = Number of occupants
	 * @param double[] x x[2],x[4] = Room area
	 * @return double 1 = true if satisfies the requirement
	 */
	@Override
	public double function(double[] x) {
		temp = (double[])x.clone();
		boolean valid = false; 
		if( x[0] > 0 && x[0] < 3 )
		{
			for( int j = 1; j<= 4; j=j+2)
			{
				double y[] = { x[j], x[j+1] };
				
				if( y[1] == 0 )
					valid = true;
				// 1 ~ 2 occupants no requirements
				else if( y[0] > 0 && y[0] <= 2 ){
					valid = true;
				}
				// 3 ~ 5 occupants
				else if( y[0] >= 3 && y[0] <= 5 ){
					if( y[1] >= 200 )
						valid = true;
					else
						return 0;
				}
				// more than 6 occupant
				else if( y[0] >= 6){
					if( y[1] >= 250 )
						valid = true;
					else
						return 0;
				}
			}
		}
		
		if( valid )
			return 1;
			
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
	/*
	public static void main( String arg[] )
	{
		MCMinLivingroomAreaFunction r = new MCMinLivingroomAreaFunction();
		double x[] = { 2, 6, 230, 3, 200 };
		System.out.println( "Result: " + r.function( x ) );
	}*/
}
