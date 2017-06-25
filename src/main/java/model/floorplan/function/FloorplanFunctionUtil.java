package model.floorplan.function;

import java.util.ArrayList;

import model.floorplan.FloorplanResultModel;
import model.floorplan.FloorplanUtil;
import model.floorplan.RoomModel;
import model.floorplan.UtilityModel;

public class FloorplanFunctionUtil {
	public static final boolean DEBUG = true; 
	
	/**
	 * Transform general matrix to BCMaxUAMatrix
	 * @param model FloorplanResultModel the result of the test case
	 * @return double[] for BCMaxUnitAreaFunction
	 */
	public static double[] generalToBCMaxUAMatrix( FloorplanResultModel model ){
		double[] x = new double[3];
		UtilityModel util = model.getUtilityModel();
		
		if( util.getGroup().equals( UtilityModel.BUSINESS ) )
			x[0] = 1;
		else if( util.getGroup().equals( UtilityModel.EDUCATION ) )
			x[0] = 2;
		else if( util.getGroup().equals( UtilityModel.R_2 ) )
			x[0] = 3;
		else if( util.getGroup().equals( UtilityModel.R_3 ) )
			x[0] = 4;
		
		if( util.getConstructionType().equals( UtilityModel.TYPE_I_A) )
			x[1] = 1;
		else if( util.getConstructionType().equals( UtilityModel.TYPE_I_B ) )
			x[1] = 2;
		else if( util.getConstructionType().equals( UtilityModel.TYPE_II_A ) )
			x[1] = 3;
		else if( util.getConstructionType().equals( UtilityModel.TYPE_II_B ) )
			x[1] = 4;
		else if( util.getConstructionType().equals( UtilityModel.TYPE_III_A ) )
			x[1] = 5;
		else if( util.getConstructionType().equals( UtilityModel.TYPE_III_B ) )
			x[1] = 6;
		else if( util.getConstructionType().equals( UtilityModel.TYPE_IV ) )
			x[1] = 7;
		else if( util.getConstructionType().equals( UtilityModel.TYPE_V_A ) )
			x[1] = 8;
		else if( util.getConstructionType().equals( UtilityModel.TYPE_V_B ) )
			x[1] = 9;
		
		x[2] = model.getUsableArea();
		
		if( DEBUG && x.length == 3 )
		{
			System.out.println("FloorplanFunctionUtil.generalToBCMaxUAMatrix() Group: " + x[0]
					+ " Type: " + x[1] + " Area: " + x[2] );
		}
		
		return x;
	}
	
	/**
	 * Transform general matrix to BCMKWMatrix
	 * @param model the result of the test case
	 * @return double[] for BCMinKitchenWidthFunction
	 */
	public static double[] generalToBCMKWMatrix( FloorplanResultModel model ){
		ArrayList<Double> tempX = new ArrayList<Double>();
		double ratio = Math.sqrt( model.getUtilityModel().getRatio() ); // the square root of area ratio equals the length ratio
		if( DEBUG )
			System.out.println("FloorplanFunctionUtil.generalToBCMKWMatrix() ratio : " + ratio );
		
		for( int i = 0; i< model.getRoomList().size(); i++ ){
			RoomModel room = model.getRoomList().get( i );
			if( room.getRole().equals( RoomModel.KITCHEN ) ){
				ArrayList<Double> lengths = room.getLength();
				for( int j = 0; j < lengths.size(); j++ ){
					tempX.add( lengths.get(j) * ratio );
					
					if( DEBUG )
						System.out.println("FloorplanFunctionUtil.generalToBCMKWMatrix() length : " + lengths.get(j) * ratio );
				}
			}
		}
		double[] x = new double[ tempX.size() ];
		for( int i = 0; i < tempX.size(); i++ ){
			x[i] = tempX.get(i).doubleValue();
		}
		return x;
	}
	
	/**
	 * Transform general matrix to BCMLAMatrix
	 * @param model FloorplanResultModel the result of the test case
	 * @return double[] for BCMinLivingRoomAreaFunction
	 */
	public static double[] generalToBCMLAMatrix( FloorplanResultModel model ){
		double[] x = new double[] { 0, 0, 0, 0, 0 };  // Maximum living room = 2 
		int count = 3;
		
		for( int i = 0; i< model.getRoomList().size(); i++ ){
			RoomModel room = model.getRoomList().get( i );
			
			if( room.getRole().equals( RoomModel.LIVING_ROOM ) ){
				x[count - 2] = room.getOccupants();
				x[count] = room.getArea();
				count = count + 1;
			}
		}
		x[0] = count - 3;
		
		if( DEBUG && x.length == 5 )
		{
			System.out.println("FloorplanFunctionUtil.generalToBCMLAMatrix() LivingRoomCounts : " + x[0]
					+ " Occupants 1 : " + x[1] + " Occupants 2 :" + x[2] + " Size :" + x[3] + " Size :" + x[4] );
		}
		
		return x;
	}
	
	/**
	 * Transform general matrix to BCMRWMatrix
	 * @param model the result of the test case
	 * @return double[] for BCMinRoomWidthFunction
	 */
	public static double[] generalToBCMRWMatrix( FloorplanResultModel model ){
		ArrayList<Double> tempX = new ArrayList<Double>();
		double ratio = Math.sqrt( model.getUtilityModel().getRatio() ); // the square root of area ratio equals the length ratio
		
		for( int i = 0; i< model.getRoomList().size(); i++ ){
			RoomModel room = model.getRoomList().get( i );
			if( room.getRole().equals( RoomModel.BED_ROOM ) ){
				for( int j = 0; j < room.getLength().size(); j++ ){
					tempX.add( room.getLength().get(j) * ratio );
				}
			}
		}
		double[] x = new double[ tempX.size() ];
		for( int i = 0; i < tempX.size(); i++ ){
			x[i] = tempX.get(i).doubleValue();
		}
		
		if( DEBUG )
			System.out.println("FloorplanFunctionUtil.generalToBCMRWMatrix() ratio: " + ratio + " Size :" + x.length );
		
		return x;
	}
	
	/**
	 * Transform general matrix to BCMinUAMatrix
	 * @param model FloorplanResultModel the result of the test case
	 * @return double[] for BCMinUnitAreaFunction
	 */
	public static double[] generalToBCMinUAMatrix( FloorplanResultModel model ){
		double[] x = new double[] { 0, 0, 0, 0, 0, 0, 0 }; // Maximum bed room = 6
		int count = 1;
		for( int i = 0; i< model.getRoomList().size(); i++ ){
			RoomModel room = model.getRoomList().get( i );
			if( room.getRole().equals( RoomModel.BED_ROOM ) ){
				x[count] = room.getArea();
				count = count + 1;
			}
		}
		x[0] = count - 1;
		
		if( DEBUG && x.length == 7 )
		{
			System.out.println("FloorplanFunctionUtil.generalToBCMinUAMatrix() RoomCounts : " + x[0]
					+ " Size :" + x[1] + " Size :" + x[2] + " Size :" + x[3] + " Size :" + x[4] + " Size :" + x[5] + " Size :" + x[6] );
		}
		
		return x;
	}
	
	/**
	 * Transform general matrix to MCMBAMatrix
	 * @param model FloorplanResultModel the result of the test case
	 * @return double[] for MCMinBedroomAreaFunction
	 */
	public static double[] generalToMCMBAMatrix( FloorplanResultModel model ){
		double[] x = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // Maximum bed room = 7
		int count = 1;
		
		for( int i = 0; i< model.getRoomList().size(); i++ ){
			RoomModel room = model.getRoomList().get( i );
			
			if( room.getRole().equals( RoomModel.BED_ROOM ) )
			{
				x[count] = room.getOccupants();
				count++;
				x[count] = room.getArea();
				count++;
			}
		}
		x[0] = (count - 1)/2;
		
		if( DEBUG && x.length == 15 )
		{
			System.out.println("FloorplanFunctionUtil.generalToMCMBAMatrix() RoomCounts : " + x[0] + " Occupant :" + x[1]
					 + " Size :" + x[2] + " Occupant :" + x[3] + " Size :" + x[4] + " Occupant :" + x[5] + " Size :" + x[6]
							 + " Occupant :" + x[7] + " Size :" + x[8] + " Occupant :" + x[9] + " Size :" + x[10] + " Occupant :" + x[11]
									 + " Size :" + x[12] + " Occupant :" + x[13] + " Size :" + x[14] );
		}
		
		return x;
	}
	
	/**
	 * Transform general matrix to MCMKAMatrix
	 * @param model FloorplanResultModel the result of the test case
	 * @return double[] for MCMinKitchenAreaFunction
	 */
	public static double[] generalToMCMKAMatrix( FloorplanResultModel model ){
		double[] x = new double[] { 0, 0 }; // Assume only one kitchen in a unit
		
		for( int i = 0; i< model.getRoomList().size(); i++ ){
			RoomModel room = model.getRoomList().get( i );
			
			if( room.getRole().equals( RoomModel.KITCHEN ) ){
				x[0] = room.getOccupants();
				x[1] = room.getArea();
			}
		}
		
		if( DEBUG && x.length == 2 )
		{
			System.out.println("FloorplanFunctionUtil.generalToMCMKAMatrix() Occupant :" + x[0]
					 + " Kitchen Size :" + x[1]  );
		}
		
		return x;
	}
	
	/**
	 * Transform general matrix to MCMLAMatrix
	 * @param model FloorplanResultModel the result of the test case
	 * @return double[] for MCMinLivingrroomAreaFunction
	 */
	public static double[] generalToMCMLAMatrix( FloorplanResultModel model ){
		double[] x = new double[] { 0, 0, 0, 0, 0 }; // Maximum living room = 2
		int count = 1;
		
		for( int i = 0; i< model.getRoomList().size(); i++ ){
			RoomModel room = model.getRoomList().get( i );
			
			if( room.getRole().equals( RoomModel.LIVING_ROOM ) ){
				x[count] = room.getOccupants();
				count++;
				x[count] = room.getArea();
				count++;
			}
		}
		x[0] = ( count - 1 ) /2;
		
		if( DEBUG && x.length == 5 )
		{
			System.out.println("FloorplanFunctionUtil.generalToBCMLAMatrix() RoomCounts : " + x[0]
					+ " Occupants : " + x[1] + " Size : " + x[2] + " Occupants : " + x[3] + " Size :" + x[4] );
		}
		
		return x;
	}

}
