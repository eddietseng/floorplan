package view.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import view.data.model.HeatPump;
import view.data.model.ScatterPlotDataModel;

import model.floorplan.City;
import model.floorplan.FloorplanResultModel;

public class TradeoffDataUtility {
	
	public static final String HEATPUMP_LOC = "xmlData/heatpumps.xml";
	
	public static final boolean COST_AVG = false;
	public static final boolean PV = true;
	
	public static final double ENERGY_COST_AVG = 0.1253;
	public static final double YEARS = 18.4;
	public static final double RATE = 0.04;
	
	
	protected static ArrayList<HeatPump> heatPumpDB = new ArrayList<HeatPump>();
	
	static
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(XmlWrapper.class, HeatPump.class );
			
			// Unmarshal
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        List<HeatPump> heatpumps 
	        	= unmarshal(unmarshaller, HeatPump.class, HEATPUMP_LOC );
	        
	        for( HeatPump heatpump : heatpumps )
	        {
	        	heatPumpDB.add( heatpump );
	        	System.out.println( heatpump.toString() );
	        }
		}
		catch( JAXBException e )
		{
			System.out.println("Import Xml failed" + e );
		}
	}
	
	public static void test(){}
	
	/**
     * Unmarshal XML to Wrapper and return List value.
     */
    private static <T> List<T> unmarshal(Unmarshaller unmarshaller,
            Class<T> clazz, String xmlLocation) throws JAXBException {
        StreamSource xml = new StreamSource(xmlLocation);
        XmlWrapper<T> wrapper = (XmlWrapper<T>) unmarshaller.unmarshal(xml,
        		XmlWrapper.class).getValue();
        return wrapper.getItems();
    }
	
	public static ArrayList<ScatterPlotDataModel> getEnergyComsumptionData( FloorplanResultModel model ) {
		ArrayList<HeatPump> zone1Hps = getHeatPumps( model.getCoolingLoadZone1() );
		ArrayList<HeatPump> zone2Hps = getHeatPumps( model.getCoolingLoadZone2() );
		ArrayList<HeatPump> wholeHps = getHeatPumps( model.getCoolingLoadWhole() );
		ArrayList<ScatterPlotDataModel> list = new ArrayList<ScatterPlotDataModel>();
		
		for( HeatPump z1hp : zone1Hps )
		{
			ScatterPlotDataModel data = calculateEnergyComsumptionFomula( model.getCity(), z1hp );
			data.setAreaSize( model.getZone1Area() );
			list.add( data );
		}
		
		for( HeatPump z2hp : zone2Hps )
		{
			ScatterPlotDataModel data = calculateEnergyComsumptionFomula( model.getCity(), z2hp );
			data.setAreaSize( model.getZone2Area() );
			list.add( data );
		}
		
		for( HeatPump whhp : wholeHps )
		{
			ScatterPlotDataModel data = calculateEnergyComsumptionFomula( model.getCity(), whhp );
			data.setAreaSize( model.getUsableArea() );
			list.add( data );
		}
		
		return list;
	}
	
	protected static ArrayList<HeatPump> getHeatPumps( double tons ) {
		ArrayList<HeatPump> heatpumps = new ArrayList<HeatPump>();
		
		if( tons == 4.5 ) // No 4.5 tons Heat pump model
			tons = 5;
		
		for( HeatPump hp : heatPumpDB )
		{
			if( hp.getTons() == tons )
				heatpumps.add( hp );
		}
		
		return heatpumps;
	}
	
	protected static ScatterPlotDataModel calculateEnergyComsumptionFomula( City city, HeatPump hp ) {
		double consumption = hp.getTons() * HeatPump.TONS_TO_BTU / 1000 
				* ( ( city.getSummerUsage() / hp.getSeer() ) 
						+ ( city.getWinterUsage() / hp.getHspf() ) );
		
		ScatterPlotDataModel data = new ScatterPlotDataModel( city, hp );
		double cost = 0; 
		
		if( PV )
		{
			if( COST_AVG )
				cost = calculatePresentValue( consumption, ENERGY_COST_AVG, YEARS, RATE ) + hp.getPrice();
			else
				cost = calculatePresentValue( consumption, city.getEnergyCost(), YEARS, RATE ) + hp.getPrice();
		}
		else
		{
			if( COST_AVG )
				cost = consumption * ENERGY_COST_AVG * YEARS + hp.getPrice();
			else
				cost = consumption * city.getEnergyCost() * YEARS + hp.getPrice();
		}
		
		data.setCity( city );
		data.setHeatPump( hp );
		data.setEnergyConsumption( consumption );
		data.setCost( cost );
		
		return data;
	}
	
	public static City findCity( String name )
	{
		if( name.equals( City.SEATTLE.getLocation() ) )
			return City.SEATTLE;
		else if( name.equals( City.LOS_ANGLES.getLocation() ) )
			return City.LOS_ANGLES;
		else if( name.equals( City.WASHINGTON.getLocation() ) )
			return City.WASHINGTON;
		else if( name.equals( City.MIAMI.getLocation() ) )
			return City.MIAMI;
		else if( name.equals( City.DALLAS.getLocation() ) )
			return City.DALLAS;
		else
			return null;
	}
	
	/**
	 * Annually recurring uniform amounts
	 * @param consumption
	 * @param energycost
	 * @param years
	 * @return the present value of the energy consumption cost
	 */
	private static double calculatePresentValue( double consumption,
			double energycost, double years, double rate )
	{
		double pv = ( consumption * energycost ) * ( Math.pow( ( 1 + rate ), years ) - 1 ) 
				/ ( rate * Math.pow( ( 1 + rate ), years ) );
		return pv;
	}
	
	/**
	 * Returns data for city based electricity trend heat pump comparison 
	 * @return list of electricity/heat pump/city datum
	 */
	public static HashMap<String,ArrayList<ScatterPlotDataModel>> getElectricityData()
	{
		HashMap<String,ArrayList<ScatterPlotDataModel>> map = new HashMap<String,ArrayList<ScatterPlotDataModel>>();
		
		ArrayList<HeatPump> hps = getHeatPumps( 3 );
		for( int i = 0; i < hps.size(); i++ )
		{
			if( hps.get( i ).getSeer() == 14 )
				hps.remove( i );
		}
		
		ArrayList<City> cities  = City.getDefaultCities();
		
		for( City city : cities )
		{
			ArrayList<ScatterPlotDataModel> data = new ArrayList<ScatterPlotDataModel>();
			for( HeatPump hp : hps )
			{
				for( int i = 0; i <= 50 ; i ++ )
				{
					ScatterPlotDataModel datum = calculateElectricityDatum( city, hp, i * 0.01 );
					data.add( datum );
				}
			}
			map.put( city.getLocation(), data );
		}
		
		return map;
	}
	
	protected static ScatterPlotDataModel calculateElectricityDatum( City city, HeatPump hp, double electricity )
	{
		double consumption = hp.getTons() * HeatPump.TONS_TO_BTU / 1000 
				* ( ( city.getSummerUsage() / hp.getSeer() ) 
						+ ( city.getWinterUsage() / hp.getHspf() ) );
		//System.out.println( "City: " + city.getLocation() + " , HP SEER: " + hp.getSeer() + " , Consumption : " + consumption );
		ScatterPlotDataModel data = new ScatterPlotDataModel( city, hp );
		data.setElectricityCost( electricity );
		data.setCost( consumption * electricity + hp.getPrice() );
		
		return data;
	}

	//TEST
	public static void main( String arg[] ) 
	{
		double a = 10, b = 10;
		double c = 5, d = 0.03;
		double pv = calculatePresentValue(a, b, c, d);
		System.out.println( "PV = " + pv );
		
		System.out.println( "========================" );
		ArrayList<City> cities = City.getDefaultCities();
		HeatPump hp13 = new HeatPump();
		HeatPump hp16 = new HeatPump();
		for( HeatPump hp : heatPumpDB )
		{
			if( hp.getTons() == 3.0 && hp.getSeer() == 13 )
				hp13 = hp;
			else if( hp.getTons() == 3.0 && hp.getSeer() == 16 )
				hp16 = hp;
		}
		
		
		for( City city : cities )
		{
			double consumption13 = 3 * 12000 / 1000 
					* ( ( city.getSummerUsage() / hp13.getSeer() ) 
							+ ( city.getWinterUsage() / hp13.getHspf() ) );
			
			System.out.println("City: " + city.getLocation() + " HP13 consumption: " + consumption13 );
			
			double consumption16 = 3 * 12000 / 1000 
					* ( ( city.getSummerUsage() / hp16.getSeer() ) 
							+ ( city.getWinterUsage() / hp16.getHspf() ) );
			
			System.out.println("City: " + city.getLocation() + " HP16 consumption: " + consumption16 );
			
			double x = ( 1100 - 1600 )/( consumption16 - consumption13 );
			System.out.println("Point: " + x );
		}
		
		
	}
}
