package model.floorplan;

import java.util.ArrayList;

public enum City 
{
	SEATTLE( 282, 2956, 9, 0.0877, "Seattle, WA" ),
	LOS_ANGLES( 1630, 1070, 11, 0.1622, "Los Angles, CA" ),
	WASHINGTON( 1320, 2061, 25, 0.1284, "Washington, DC" ),
	MIAMI( 3931, 265, 41 , 0.1198, "Miami, FL"),
	DALLAS( 1926, 1343, 65, 0.1179, "Dallas, TX" );
	
	private final int summerUsage;
	private final int winterUsage;
	private final int groupNumber;
	private final double energyCost ;
	private final String location;
	
	City( int summerUsage, int winterUsage, int groupNumber, double energyCost, String location )
	{
		this.summerUsage = summerUsage;
		this.winterUsage = winterUsage;
		this.groupNumber = groupNumber;
		this.energyCost = energyCost;
		this.location = location;
	}
	
	public int getSummerUsage()
	{
		return this.summerUsage;
	}
	
	public int getWinterUsage()
	{
		return this.winterUsage;
	}
	
	public int getGroupNumber()
	{
		return this.groupNumber;
	}
	
	public double getEnergyCost()
	{
		return this.energyCost;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public static ArrayList<City> getDefaultCities()
	{
		ArrayList<City> cities = new ArrayList<City>();
		cities.add( SEATTLE );
		cities.add( LOS_ANGLES );
		cities.add( WASHINGTON );
		cities.add( MIAMI );
		cities.add( DALLAS );
		
		return cities;
	}
}
