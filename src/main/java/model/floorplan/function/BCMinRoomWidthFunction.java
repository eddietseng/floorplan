package model.floorplan.function;

public class BCMinRoomWidthFunction extends Function{
	
	public final String CODE = "BC Section 1208.1 Room Width";
	private int st = 0 ;
	private boolean log = false;
	protected double[] temp;
	
	public BCMinRoomWidthFunction(){}

	// Section 1208.1 Minimum room widths in Building code
	/**
	 * Used to validate the room
	 * @param double[] x the Width of the room
	 * @return double 1 = true if satisfies the requirement
	 */
	@Override
	public double function(double[] x) {
		temp = (double[])x.clone();
		
		if( log == true ){
			for( int i = 0; i < x.length; i++ ){
				System.out.println("BCMinRoomWidthFunction input x["+ i + "] = "
						+ x[i] );
			}
		}
		
		int count = 0;
		if( x.length >= 4 )
		{
			for( int i = 0; i < x.length; i++ )
			{
				if( x[i] >= 7 )
					count = count + 1;
			}
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
