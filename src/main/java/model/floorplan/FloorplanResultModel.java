package model.floorplan;

import java.util.ArrayList;

import view.PrintViewFloorplanResultVisitor;

import model.floorplan.function.BCMaxUnitAreaFunction;
import model.floorplan.function.BCMinKitchenWidthFunction;
import model.floorplan.function.BCMinLivingroomAreaFunction;
import model.floorplan.function.BCMinRoomWidthFunction;
import model.floorplan.function.BCMinUnitAreaFunction;
import model.floorplan.function.FloorplanFunctionUtil;
import model.floorplan.function.Function;
import model.floorplan.function.MCMinBedroomAreaFunction;
import model.floorplan.function.MCMinKitchenAreaFunction;
import model.floorplan.function.MCMinLivingroomAreaFunction;

import com.vividsolutions.jts.io.ParseException;

public class FloorplanResultModel {
	
	private ArrayList<RoomModel> roomList;
	private double totalArea;
	private double usableArea;
	private double zone1Area;
	private double zone2Area;
	private double totalPrice;
	private double coolingLoadWhole;
	private double coolingLoadZone1;
	private double coolingLoadZone2;
	private UtilityModel utility;
	private boolean selected = false;
	
	private int caseNumber = 0;
	private City city;
	
	/**
	 * Requirements
	 */
	private ArrayList<Function> requirements;
	private BCMaxUnitAreaFunction bCMaxUA = new BCMaxUnitAreaFunction();
	private BCMinKitchenWidthFunction bCMKW = new BCMinKitchenWidthFunction();
	private BCMinLivingroomAreaFunction bCMLA = new BCMinLivingroomAreaFunction();
	private BCMinRoomWidthFunction bCMRW = new BCMinRoomWidthFunction();
	private BCMinUnitAreaFunction bCMinUA = new BCMinUnitAreaFunction();
	
	private MCMinBedroomAreaFunction mCMBA = new MCMinBedroomAreaFunction();
	private MCMinKitchenAreaFunction mCMKA = new MCMinKitchenAreaFunction();
	private MCMinLivingroomAreaFunction mCMLA = new MCMinLivingroomAreaFunction();
	
	
	
	public FloorplanResultModel( int caseNumber, City city ){
		this.requirements = new ArrayList<Function>();
		this.requirements.add( bCMaxUA );
		this.requirements.add( bCMKW );
		this.requirements.add( bCMLA );
		this.requirements.add( bCMRW );
		this.requirements.add( bCMinUA );
		this.requirements.add( mCMBA );
		this.requirements.add( mCMKA );
		this.requirements.add( mCMLA );
		
		this.caseNumber = caseNumber;
		this.city = city;
	}
	
	public FloorplanResultModel( ArrayList<RoomModel> roomlist, int caseNumber, City city ) {
		this.roomList = roomlist;
		this.requirements = new ArrayList<Function>();
		this.requirements.add( bCMaxUA );
		this.requirements.add( bCMKW );
		this.requirements.add( bCMLA );
		this.requirements.add( bCMRW );
		this.requirements.add( bCMinUA );
		this.requirements.add( mCMBA );
		this.requirements.add( mCMKA );
		this.requirements.add( mCMLA );
		
		this.caseNumber = caseNumber;
		this.city = city;
	}
	
	public int getCaseNumber(){
		return this.caseNumber;
	}
	
	public void setCity( City city ){
		this.city = city;
	}
	
	public City getCity(){
		return this.city;
	}
	
	public void setRoomList( ArrayList<RoomModel> roomlist ){
		this.roomList = roomlist;
	}
	
	public ArrayList<RoomModel> getRoomList(){
		return this.roomList;
	}
	
	public void setTotalArea( double totalArea ){
		this.totalArea = totalArea;
	}
	
	public double getTotalArea(){
		return this.totalArea;
	}
	
	public void setUsableArea( double usableArea ){
		this.usableArea = usableArea;
	}
	
	public double getUsableArea(){
		return this.usableArea;
	}
	
	public void setZone1Area( double zone1Area ){
		this.zone1Area = zone1Area;
	}
	
	public double getZone1Area(){
		return this.zone1Area;
	}
	
	public void setZone2Area( double zone2Area ){
		this.zone2Area = zone2Area;
	}
	
	public double getZone2Area(){
		return this.zone2Area;
	}
	
	public void setTotalPrice( double totalPrice ){
		this.totalPrice = totalPrice;
	}
	
	public double getTotalPrice(){
		return this.totalPrice;
	}
	
	public void setCoolingLoadWhole( double coolingLoadWhole ){
		this.coolingLoadWhole = coolingLoadWhole;
	}
	
	public double getCoolingLoadWhole(){
		return this.coolingLoadWhole;
	}
	
	public void setCoolingLoadZone1( double coolingLoadZone1 ){
		this.coolingLoadZone1 = coolingLoadZone1;
	}
	
	public double getCoolingLoadZone1(){
		return this.coolingLoadZone1;
	}
	
	public void setCoolingLoadZone2( double coolingLoadZone2 ){
		this.coolingLoadZone2 = coolingLoadZone2;
	}
	
	public double getCoolingLoadZone2(){
		return this.coolingLoadZone2;
	}
	
	public void setUtilityModel( UtilityModel utility ){
		this.utility = utility;
	}
	
	public UtilityModel getUtilityModel(){
		return this.utility;
	}
	
	public void setSelected( boolean selected ){
		this.selected = selected;
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	
	@SuppressWarnings("unchecked")
	public void setRequirements( ArrayList<Function> requirements ){
		if( requirements != null ){
			this.requirements = (ArrayList<Function>) requirements.clone();
		}
	}
	
	public void addRequirement( Function requirement ){
		this.requirements.add( requirement );
	}
	
	public ArrayList<Function> getRquirements(){
		System.out.println("FloorplanResultModel.getRquirements() Size : " + requirements.size() );
		return this.requirements;
	}
	
	public void calculateAttributes() throws ParseException {
		if( !roomList.isEmpty() )
		{
			//get(0) is just to used for getting the whole floor plan area
			this.setTotalArea( FloorplanUtil.getTotalArea( roomList.get(0).getFloorplanModel(), true ) 
					* roomList.get(0).getFloorplanModel().getUtilityModel().getRatio() );
			double totalUsableArea = 0;
			double totalZone1Area  = 0;
			double totalZone2Area  = 0;
			
			for( RoomModel room : roomList ) 
			{
				totalUsableArea = totalUsableArea + room.getArea();
				
				if( room.getZone().equals( RoomModel.ZONE_1 ) )
					totalZone1Area = totalZone1Area + room.getArea();
				else if( room.getZone().equals( RoomModel.ZONE_2 ) )
					totalZone2Area = totalZone2Area + room.getArea();
			}
			//Whole floor plan
			this.setUsableArea( totalUsableArea );
			
			//Zoned floor plan
			this.setZone1Area( totalZone1Area );
			this.setZone2Area( totalZone2Area );
			
			//Set heat pump for different zoning
			this.setCoolingLoadWhole( setHeatPump( this.city, this.usableArea ) );
			this.setCoolingLoadZone1( setHeatPump( this.city, this.zone1Area ) );
			this.setCoolingLoadZone2( setHeatPump( this.city, this.zone2Area ) );
			
			double price = this.totalArea * utility.getPriceRatio();
			this.setTotalPrice( price );
			validateRequirement();	
		}
		else
			System.out.println( "FloorplanResultModel.calculateAttributes() Can't calculate due to roomList == null" );
		
	}
	
	private double setHeatPump( City city, double usableArea ) {
		double tons = 0;
		
		switch( city )
		{
		  case SEATTLE:
				if( 0 <= usableArea && usableArea <= 840 )
					tons = 1;
				else if( 841 <= usableArea && usableArea <= 1120 )
					tons = 1.5;
				else if( 1121 <= usableArea && usableArea <= 1280 )
					tons = 1.5;
				else if( 1281 <= usableArea && usableArea <= 1440 )
					tons = 1.5;
				else if( 1441 <= usableArea && usableArea <= 1680 )
					tons = 2;
				else if( 1681 <= usableArea && usableArea <= 1960 )
					tons = 2;
				else if( 1961 <= usableArea && usableArea <= 2240 )
					tons = 2.5;
				else if( 2241 <= usableArea && usableArea <= 2520 )
					tons = 2.5;
				else if( 2521 <= usableArea && usableArea <= 2760 )
					tons = 2.5;
				else if( 2761 <= usableArea && usableArea <= 3000 )
					tons = 2.5;
				else
					tons = 0;
			  break;
			  
		  case LOS_ANGLES:
				if( 0 <= usableArea && usableArea <= 840 )
					tons = 1;
				else if( 841 <= usableArea && usableArea <= 1120 )
					tons = 1.5;
				else if( 1121 <= usableArea && usableArea <= 1280 )
					tons = 1.5;
				else if( 1281 <= usableArea && usableArea <= 1440 )
					tons = 1.5;
				else if( 1441 <= usableArea && usableArea <= 1680 )
					tons = 2;
				else if( 1681 <= usableArea && usableArea <= 1960 )
					tons = 2;
				else if( 1961 <= usableArea && usableArea <= 2240 )
					tons = 2.5;
				else if( 2241 <= usableArea && usableArea <= 2520 )
					tons = 2.5;
				else if( 2521 <= usableArea && usableArea <= 2760 )
					tons = 3;
				else if( 2761 <= usableArea && usableArea <= 3000 )
					tons = 3;
				else
					tons = 0;
			  break;
			  
		  case WASHINGTON:
				if( 0 <= usableArea && usableArea <= 840 )
					tons = 1.5;
				else if( 841 <= usableArea && usableArea <= 1120 )
					tons = 2;
				else if( 1121 <= usableArea && usableArea <= 1280 )
					tons = 2;
				else if( 1281 <= usableArea && usableArea <= 1440 )
					tons = 2;
				else if( 1441 <= usableArea && usableArea <= 1680 )
					tons = 2.5;
				else if( 1681 <= usableArea && usableArea <= 1960 )
					tons = 2.5;
				else if( 1961 <= usableArea && usableArea <= 2240 )
					tons = 3;
				else if( 2241 <= usableArea && usableArea <= 2520 )
					tons = 3;
				else if( 2521 <= usableArea && usableArea <= 2760 )
					tons = 3.5;
				else if( 2761 <= usableArea && usableArea <= 3000 )
					tons = 3.5;
				else
					tons = 0;
			  break;
			  
		  case MIAMI:
				if( 0 <= usableArea && usableArea <= 840 )
					tons = 2;
				else if( 841 <= usableArea && usableArea <= 1120 )
					tons = 2;
				else if( 1121 <= usableArea && usableArea <= 1280 )
					tons = 2.5;
				else if( 1281 <= usableArea && usableArea <= 1440 )
					tons = 2.5;
				else if( 1441 <= usableArea && usableArea <= 1680 )
					tons = 3;
				else if( 1681 <= usableArea && usableArea <= 1960 )
					tons = 3;
				else if( 1961 <= usableArea && usableArea <= 2240 )
					tons = 3.5;
				else if( 2241 <= usableArea && usableArea <= 2520 )
					tons = 4;
				else if( 2521 <= usableArea && usableArea <= 2760 )
					tons = 4;
				else if( 2761 <= usableArea && usableArea <= 3000 )
					tons = 4;
				else
					tons = 0;
			  break;
			  
		  case DALLAS:
				if( 0 <= usableArea && usableArea <= 840 )
					tons = 2;
				else if( 841 <= usableArea && usableArea <= 1120 )
					tons = 2.5;
				else if( 1121 <= usableArea && usableArea <= 1280 )
					tons = 2.5;
				else if( 1281 <= usableArea && usableArea <= 1440 )
					tons = 3;
				else if( 1441 <= usableArea && usableArea <= 1680 )
					tons = 3.5;
				else if( 1681 <= usableArea && usableArea <= 1960 )
					tons = 3.5;
				else if( 1961 <= usableArea && usableArea <= 2240 )
					tons = 4;
				else if( 2241 <= usableArea && usableArea <= 2520 )
					tons = 4.5;
				else if( 2521 <= usableArea && usableArea <= 2760 )
					tons = 4.5;
				else if( 2761 <= usableArea && usableArea <= 3000 )
					tons = 5;
				else
					tons = 0;
			  break;
			  
		  default:
			  break;
		
		}
		return tons;
	}

	public void validateRequirement(){
		System.out.println("FloorplanResultModel.validateRequirement() start...");
		
		syncOccupants();
		
		for( int i = 0; i < this.requirements.size(); i++ ){
			Function req = requirements.get( i );
			if( req instanceof BCMaxUnitAreaFunction )
				((BCMaxUnitAreaFunction)req).setStatus( ( (BCMaxUnitAreaFunction)req).function( FloorplanFunctionUtil.generalToBCMaxUAMatrix( this ) ) );
			else if( req instanceof BCMinKitchenWidthFunction )
				((BCMinKitchenWidthFunction)req).setStatus( ((BCMinKitchenWidthFunction)req).function( FloorplanFunctionUtil.generalToBCMKWMatrix( this ) ) );
			else if( req instanceof BCMinLivingroomAreaFunction )
				((BCMinLivingroomAreaFunction)req).setStatus( ((BCMinLivingroomAreaFunction)req).function( FloorplanFunctionUtil.generalToBCMLAMatrix( this ) ) );
			else if( req instanceof BCMinRoomWidthFunction )
				((BCMinRoomWidthFunction)req).setStatus( ((BCMinRoomWidthFunction)req).function( FloorplanFunctionUtil.generalToBCMRWMatrix( this ) ) );
			else if( req instanceof BCMinUnitAreaFunction )
				((BCMinUnitAreaFunction)req).setStatus( ((BCMinUnitAreaFunction)req).function( FloorplanFunctionUtil.generalToBCMinUAMatrix( this ) ) );
			else if( req instanceof MCMinBedroomAreaFunction )
				((MCMinBedroomAreaFunction)req).setStatus( ((MCMinBedroomAreaFunction)req).function( FloorplanFunctionUtil.generalToMCMBAMatrix( this ) ) );
			else if( req instanceof MCMinKitchenAreaFunction )
				((MCMinKitchenAreaFunction)req).setStatus( ((MCMinKitchenAreaFunction)req).function( FloorplanFunctionUtil.generalToMCMKAMatrix( this ) ) );
			else if( req instanceof MCMinLivingroomAreaFunction )
				((MCMinLivingroomAreaFunction)req).setStatus( ((MCMinLivingroomAreaFunction)req).function( FloorplanFunctionUtil.generalToMCMLAMatrix( this ) ) );
			else
				System.out.println("FloorplanResultModel.validateRequirement() can't validate requirement");
		}
		System.out.println("FloorplanResultModel.validateRequirement() done...");
	}
	
	private void syncOccupants(){
		int z1o = 0;
		int z2o = 0;
		
		for( RoomModel r : roomList )
		{
			if( r.getRole().equals( RoomModel.BED_ROOM ) )
			{
				if( r.getZone().equals( RoomModel.ZONE_1 ) )
					z1o = z1o + r.getOccupants();
				else if( r.getZone().equals( RoomModel.ZONE_2 ) )
					z2o = z2o + r.getOccupants();
				else
					System.out.println( "FloorplanResultModel.syncOccupants() failed" );
			}
		}
		
		for( RoomModel r : roomList )
		{
			if( r.getRole().equals( RoomModel.LIVING_ROOM ) )
			{
				if( r.getZone().equals( RoomModel.ZONE_1 ) )
				{
					if( r.getOccupants() != z1o )
					{
						System.out.println( r.getName() + " has " + r.getOccupants() + " occupants. Sync to " + z1o + " number of occupants" );
						r.setOccupants( z1o );
					}
				}
				else if( r.getZone().equals( RoomModel.ZONE_2 ) )
				{
					if( r.getOccupants() != z2o )
					{
						System.out.println( r.getName() + " has " + r.getOccupants() + " occupants. Sync to " + z2o + " number of occupants" );
						r.setOccupants( z2o );
					}
				}
				else
					System.out.println( "FloorplanResultModel.syncOccupants() failed" );
			}
			else if( r.getRole().equals( RoomModel.KITCHEN ) )
			{
				if( r.getZone().equals( RoomModel.ZONE_1 ) )
				{
					if( r.getOccupants() != z1o )
					{
						System.out.println( r.getName() + " has " + r.getOccupants() + " occupants. Sync to " + z1o + " number of occupants" );
						r.setOccupants( z1o );
					}
				}
				else if( r.getZone().equals( RoomModel.ZONE_2 ) )
				{
					if( r.getOccupants() != z2o )
					{
						System.out.println( r.getName() + " has " + r.getOccupants() + " occupants. Sync to " + z2o + " number of occupants" );
						r.setOccupants( z2o );
					}
				}
				else
					System.out.println( "FloorplanResultModel.syncOccupants() failed" );
			}
		}
	}
	
	public void accept( PrintViewFloorplanResultVisitor visitor ){
		// Step 1: Initialize parameters for visitor actions ...
		
		visitor.start ( this );
		
		// Step 2: Visit General info of floorplan result model .... 
		
		visitor.visit( this );
		
		// Step 3: Visit Room List of floorplan result model ....
		
		for ( RoomModel room : roomList ) {
			room.accept( visitor ); 
		}

	    // Step 4: Visit Requirements of floorplan result model ....

	    for ( Function requirement : requirements ) {
	    	requirement.accept( visitor );
	    }

	    // Step 5: Wrap-up parameters for visitor actions ...
	    
	    visitor.finish ( this );
	}
	/*TEST
	public static void main( String arg[] )
	{
		FloorplanResultModel r = new FloorplanResultModel( 1, City.DALLAS );
		
		System.out.println( "Tons: " + r.setHeatPump( r.city, 1671) );
	}*/
}
