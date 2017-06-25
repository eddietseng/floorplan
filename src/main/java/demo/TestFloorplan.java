package demo;

import java.util.ArrayList;

import com.vividsolutions.jts.io.ParseException;

import model.FloorplanModel;
import model.floorplan.FloorplanUtil;
import model.floorplan.GridLineModel;
import model.floorplan.Space2DModel;
import model.util.CoordinatePair;

public class TestFloorplan {
	
	public static void main(String args[]) throws ParseException{
		FloorplanModel floorplanModel = new FloorplanModel();
		
		CoordinatePair box1 = new CoordinatePair();
		box1.setCoordinatePair( 10, 10, 20, 20 );
		
		floorplanModel.addSpaceBlock( box1 );
		
		CoordinatePair box2 = new CoordinatePair();
		box2.setCoordinatePair( 8, 8, 12, 22 );
		
		floorplanModel.addWallCorner( box2 );
		
		CoordinatePair box3 = new CoordinatePair();
		box3.setCoordinatePair( 18, 8, 22, 22 );
		
		floorplanModel.addWallCorner( box3 );
		
		CoordinatePair box4 = new CoordinatePair();
		box4.setCoordinatePair( 6, 6, 23, 23 );
		
		floorplanModel.addExteriorWall( box4 );
		/*
		CoordinatePair box4 = new CoordinatePair();
		box4.setCoordinatePair(6, 6, 14, 24);
		floorplanModel.addExteriorWall( box4 );
		CoordinatePair box5 = new CoordinatePair();
		box5.setCoordinatePair(6, 6, 24, 14);
		floorplanModel.addExteriorWall( box5 );
		CoordinatePair box6 = new CoordinatePair();
		box6.setCoordinatePair(16, 6, 24, 24);
		floorplanModel.addExteriorWall( box6 );
		*/
		Space2DModel sp1 = (Space2DModel) floorplanModel.getSpaceList().get( 0 );
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** I : Create testline from GridLineModel ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		  
		GridLineModel testline1 = (GridLineModel) sp1.findGridLineModel( 20, false );
		GridLineModel testline2 = (GridLineModel) sp1.findGridLineModel( 20, true  );
		
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** II : Space area with out components ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		
		double area = FloorplanUtil.getArea(floorplanModel, sp1);
		System.out.println("Center Area = " + area );
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** III : Move center line ( 0, -5 ) ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		
		testline1.move( 0, -5 );
		
		System.out.println("*** ");
		System.out.println("*** ======================================================= ");
		System.out.println("*** IV: Print refined Space and update the rTree... ");
		System.out.println("*** ======================================================= ");
		System.out.println("*** ");

		System.out.println(sp1.toString());
		
		FloorplanUtil.updateWholeRTree(floorplanModel);
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** V : Space area with out components ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		
		double afterArea = FloorplanUtil.getArea(floorplanModel, sp1);
		System.out.println("Center Area = " + afterArea );
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** VI : Move center line ( -5, 0 ) ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		testline2.move( -5, 0 );
		
		System.out.println(sp1.toString());
		
		FloorplanUtil.updateWholeRTree(floorplanModel);
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** VII : Space area with out components ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		
		double afterArea2 = FloorplanUtil.getArea(floorplanModel, sp1);
		System.out.println("Center Area = " + afterArea2 );	
		
		ArrayList<Space2DModel> spaces = new ArrayList<Space2DModel>();
		spaces.add( sp1 );
		FloorplanUtil.getLength(floorplanModel, spaces);
	}
}
