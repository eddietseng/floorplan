package model.floorplan.function;

import java.util.HashMap;

public class BCMaxUnitAreaFunction extends Function{
	
	public final String CODE = "MC Section 503 Max Unit Area";
	protected HashMap<Integer, HashMap<Integer, Double>> typeMap;
	protected double[] temp;
	private int st = 0; // Not satisfy the requirement
	
	public BCMaxUnitAreaFunction(){
		defineMap();
	}
	
	// Section 503 Building area table in Building code
	/**
	 * Used to validate the whole floorplan
	 * @param double[] x x[0] = Building type
	 * @param double[] x x[1] = Construction type
	 * @param double[] x x[2] = Floorplan area
	 * @return double 1 = true if satisfies the requirement
	 */
	@Override
	public double function(double[] x) {
		temp = (double[])x.clone();
		
		if( x.length == 3 )
		{
			HashMap<Integer, Double> a = typeMap.get( (int)x[0] );
			double b = a.get( (int)x[1] );
			if( x[2] <= b )
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
	
	private void defineMap() {
		this.typeMap = new HashMap<Integer, HashMap<Integer, Double>>();
		// Group Business type
		HashMap<Integer, Double> groupBMap = new HashMap<Integer, Double>();
		groupBMap.put( 1, (double) 100000000 );
		groupBMap.put( 2, (double) 100000000 );
		groupBMap.put( 3, (double) 37500 );
		groupBMap.put( 4, (double) 23000 );
		groupBMap.put( 5, (double) 28500 );
		groupBMap.put( 6, (double) 19000 );
		groupBMap.put( 7, (double) 36000 );
		groupBMap.put( 8, (double) 18000 );
		groupBMap.put( 9, (double)  9000 );
		typeMap.put( 1, groupBMap );
		
		// Group Education type
		HashMap<Integer, Double> groupEMap = new HashMap<Integer, Double>();
		groupEMap.put( 1, (double) 100000000 );
		groupEMap.put( 2, (double) 100000000 );
		groupEMap.put( 3, (double) 26500 );
		groupEMap.put( 4, (double) 14500 );
		groupEMap.put( 5, (double) 23500 );
		groupEMap.put( 6, (double) 14500 );
		groupEMap.put( 7, (double) 25500 );
		groupEMap.put( 8, (double) 18500 );
		groupEMap.put( 9, (double)  9500 );
		typeMap.put( 2, groupEMap );
		
		// Group Residential-2 type
		HashMap<Integer, Double> groupR2Map = new HashMap<Integer, Double>();
		groupR2Map.put( 1, (double) 100000000 );
		groupR2Map.put( 2, (double) 100000000 );
		groupR2Map.put( 3, (double) 24000 );
		groupR2Map.put( 4, (double) 16000 );
		groupR2Map.put( 5, (double) 24000 );
		groupR2Map.put( 6, (double) 16000 );
		groupR2Map.put( 7, (double) 20500 );
		groupR2Map.put( 8, (double) 12000 );
		groupR2Map.put( 9, (double)  7000 );
		typeMap.put( 3, groupR2Map );
		
		// Group Residential-3 type
		HashMap<Integer, Double> groupR3Map = new HashMap<Integer, Double>();
		groupR3Map.put( 1, (double) 100000000 );
		groupR3Map.put( 2, (double) 100000000 );
		groupR3Map.put( 3, (double) 100000000 );
		groupR3Map.put( 4, (double) 100000000 );
		groupR3Map.put( 5, (double) 100000000 );
		groupR3Map.put( 6, (double) 100000000 );
		groupR3Map.put( 7, (double) 100000000 );
		groupR3Map.put( 8, (double) 100000000 );
		groupR3Map.put( 9, (double) 100000000 );
		typeMap.put( 4, groupR3Map );
	}
	
	public static void main(String args[]){
		BCMaxUnitAreaFunction function = new BCMaxUnitAreaFunction();
		System.out.println("function :" + function.typeMap.get(1).get(5));
		
		double[] x = new double[3];
		x[0] = 1;
		x[1] = 2;
		x[2] = 750;
		System.out.println("function 2:" + function.function(x) );
	}
}
