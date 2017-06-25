package model.floorplan.function;

public class BCMinLivingroomAreaFunction extends Function{
	
	public final String CODE = "BC Section 1208.4 Living Room Area";
	private int st = 0;
	private boolean log = false;
	protected double[] temp;
	
	public BCMinLivingroomAreaFunction(){}

	// Section 1208.4 Efficiency dwelling unit Requirements table in Building code
	// combine Living and Dining room
	/**
	 * Used to validate the room
	 * @param x[0] = LivingRoom counts
	 * @param x[1] = Number of occupants for zoneA
	 * @param x[2] = Number of occupants for zoneB
	 * @param x[3] = Area for zoneA
	 * @param x[4] = Area for zoneB
	 * @return double 1 = true if satisfies the requirement
	 */
	@Override
	public double function(double[] x) {
		temp = (double[])x.clone();
		
		if( log == true ){
			for( int i = 0; i < x.length; i++ ){
				System.out.println("BCMinLivingroomAreaFunction input x["+ i + "] = "
						+ x[i] );
			}
		}
		
		boolean valid = false;
		if( x[0] >= 1 && x[0] < 3 ) // 0 ~ 2 living rooms
		{
			if( x[1] == 0 && x[2] == 0 )
			{
				return 1;
			}
			
			// 0 occupants
			if( x[1] == 0 )
				valid = true;
			// 1 ~ 2 occupants
			else if( x[1] <= 2 && x[1] >= 1 )
			{
				if( x[3] >= 220 || x[3] == 0 )
					valid = true;
				else
					return 0;
			}
			// more than 2 occupants
			else if( x[1] > 2 )
			{
				if( x[3] >= ( 220 + ( x[1] -2 ) * 100 ) || x[3] == 0 )
					valid = true;
				else
					return 0;
			}
			
			if( valid )
			{
				if( x[2] == 0 )
					return 1;
				// 1 ~ 2 occupants
				else if( x[2] <= 2 && x[2] >= 1 )
				{
					if( x[4] >= 220 || x[4] == 0 )
						return 1;
				}
				// more than 2 occupants
				else if( x[2] > 2 )
				{
					if( x[4] >= ( 220 + ( x[2] -2 ) * 100 ) || x[4] == 0 )
						return 1;
				}
			}
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
