package model.floorplan;

import java.util.HashMap;

public class UtilityModel {
	private double ratio = 1;
	private double priceRatio = 1;
	private int numberOfOccupants = 1;
	private String constructionType;
	private String group;
	
	public final static String TYPE_I_A    = "Type I A";
	public final static String TYPE_I_B    = "Type I B";
	public final static String TYPE_II_A   = "Type II A";
	public final static String TYPE_II_B   = "Type II B";
	public final static String TYPE_III_A  = "Type III A";
	public final static String TYPE_III_B  = "Type III B";
	public final static String TYPE_IV     = "Type IV";
	public final static String TYPE_V_A    = "Type V A";
	public final static String TYPE_V_B    = "Type V B";
	public final static String EDUCATION   = "Education";
	public final static String BUSINESS    = "Business";
	public final static String R_2         = "Resident 2";
	public final static String R_3         = "Resident 3";
	
	private HashMap<String, HashMap<String, Double>> typeMap;
	
	public UtilityModel(){
		defineMap();
	}

	public void setRatio( double ratio )
	{
		if( ratio != 0 )
			this.ratio = ratio;
	}
	
	public double getRatio()
	{
		return this.ratio;
	}
	
	public void setPriceRatio( double priceRatio )
	{
		if( priceRatio != 0 )
			this.priceRatio = priceRatio;
	}
	
	public double getPriceRatio()
	{
		return this.priceRatio;
	}
	
	public void setNumberOfOccupants( int numberOfOccupants )
	{
		if( numberOfOccupants != 0 )
			this.numberOfOccupants = numberOfOccupants;
	}
	
	public int getNumberOfOccupants()
	{
		return this.numberOfOccupants;
	}

	public void setConstructionType( String constructionType ) {
		System.out.println( "UtilityModel.setConstructionType() called" );
		if( constructionType.equals( TYPE_I_A ) )
			this.constructionType = TYPE_I_A;
		else if( constructionType.equals( TYPE_I_B ) )
			this.constructionType = TYPE_I_B;
		else if( constructionType.equals( TYPE_II_A ) )
			this.constructionType = TYPE_II_A;
		else if( constructionType.equals( TYPE_II_B ) )
			this.constructionType = TYPE_II_B;
		else if( constructionType.equals( TYPE_III_A ) )
			this.constructionType = TYPE_III_A;
		else if( constructionType.equals( TYPE_III_B ) )
			this.constructionType = TYPE_III_B;
		else if( constructionType.equals( TYPE_IV ) )
			this.constructionType = TYPE_IV;
		else if( constructionType.equals( TYPE_V_A ) )
			this.constructionType = TYPE_V_A;
		else if( constructionType.equals( TYPE_V_B ) )
			this.constructionType = TYPE_V_B;
		else
			this.constructionType = "";
	}

	public String getConstructionType() {
		return constructionType;
	}
	
	public void setGroup( String group ) {
		this.group = group;
	}
	
	public String getGroup() {
		return group;
	}
	
	private void defineMap() {
		this.typeMap = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> groupBMap = new HashMap<String, Double>();
		groupBMap.put( TYPE_I_A,   153.33 );
		groupBMap.put( TYPE_I_B,   147.81 );
		groupBMap.put( TYPE_II_A,  143.08 );
		groupBMap.put( TYPE_II_B,  136.34 );
		groupBMap.put( TYPE_III_A, 124.01 );
		groupBMap.put( TYPE_III_B, 119.35 );
		groupBMap.put( TYPE_IV,    131.00 );
		groupBMap.put( TYPE_V_A,   108.67 );
		groupBMap.put( TYPE_V_B,   104.20 );
		typeMap.put( BUSINESS, groupBMap );
		
		HashMap<String, Double> groupEMap = new HashMap<String, Double>();
		groupEMap.put( TYPE_I_A,   168.14 );
		groupEMap.put( TYPE_I_B,   162.47 );
		groupEMap.put( TYPE_II_A,  157.86 );
		groupEMap.put( TYPE_II_B,  150.98 );
		groupEMap.put( TYPE_III_A, 141.50 );
		groupEMap.put( TYPE_III_B, 134.27 );
		groupEMap.put( TYPE_IV,    145.99 );
		groupEMap.put( TYPE_V_A,   124.54 );
		groupEMap.put( TYPE_V_B,   119.84 );
		typeMap.put( EDUCATION, groupEMap );
		
		HashMap<String, Double> groupR2Map = new HashMap<String, Double>();
		groupR2Map.put( TYPE_I_A,   130.60 );
		groupR2Map.put( TYPE_I_B,   125.33 );
		groupR2Map.put( TYPE_II_A,  121.35 );
		groupR2Map.put( TYPE_II_B,  115.49 );
		groupR2Map.put( TYPE_III_A, 106.19 );
		groupR2Map.put( TYPE_III_B, 102.65 );
		groupR2Map.put( TYPE_IV,    116.67 );
		groupR2Map.put( TYPE_V_A,    93.92 );
		groupR2Map.put( TYPE_V_B,    89.32 );
		typeMap.put( R_2, groupR2Map );
		
		HashMap<String, Double> groupR3Map = new HashMap<String, Double>();
		groupR3Map.put( TYPE_I_A,   123.28 );
		groupR3Map.put( TYPE_I_B,   119.90 );
		groupR3Map.put( TYPE_II_A,  116.97 );
		groupR3Map.put( TYPE_II_B,  113.77 );
		groupR3Map.put( TYPE_III_A, 109.66 );
		groupR3Map.put( TYPE_III_B, 106.79 );
		groupR3Map.put( TYPE_IV,    111.84 );
		groupR3Map.put( TYPE_V_A,   102.72 );
		groupR3Map.put( TYPE_V_B,    96.83 );
		typeMap.put( R_3, groupR3Map );
	}
	
	public double getPricePerSquareFoot()
	{
		return this.getPricePerSquareFoot( this.group, this.constructionType );
	}
	
	public double getPricePerSquareFoot( String groupType, String type ){
		HashMap<String, Double> constructionTypeMap = this.typeMap.get( groupType );
		double pricePerSquareFoot = constructionTypeMap.get( type );
		this.setPriceRatio( pricePerSquareFoot );
		return pricePerSquareFoot;
	}
}
