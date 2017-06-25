package model.floorplan.function;

public class MCMinBedroomAreaFunction extends Function{
	
	public final String CODE = "MC Section 404.5 Bedroom";
	private int st = 0;
	private boolean log = false;
	protected double[] temp;
	
	public MCMinBedroomAreaFunction(){}

	// Section 404.4.1 Minimum Area Requirements table in Maintenance code
	/**
	 * Used to validate the room
	 * @param double[] x x[0] = Number of rooms
	 * @param double[] x x[1] = Number of occupants
	 * @param double[] x x[2] = Room area
	 * @return double 1 = true if satisfies the requirement
	 */
	@Override
	public double function(double[] x) {
		temp = (double[])x.clone();
		
		if( log == true ){
			for( int i = 0; i < x.length; i++ ){
				System.out.println("MCMinBedroomAreaFunction input x[ "+ i + " ] = "
						+ x[i] );
			}
		}
		
		boolean valid = false;
		if( x[0] > 0 && x[0] < 8 )
		{
			for( int j=1; j < x.length; j=j+2 )
			{
				double y[] = { x[j], x[j+1] };
				
				// 0 occupant
				if( y[0] == 0 )
					valid = true;
				// 1 occupant
				else if( y[0] == 1 )
				{
					if( y[1] >= 70 )
						valid = true;
					else
						return 0;
				}
				// more than 1 occupant
				else if( y[0] == 2 )
				{
					if( y[1] >= 120 )
						valid = true;
					else
						return 0;
				}
				// more than 2 occupants
				else if( y[0] >= 3 )
				{
					if( y[1] >= 150 ) // Assume the occupants in each room no more than 3
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
}
