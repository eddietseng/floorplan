package demo;

import model.FloorplanModel;
import model.floorplan.FloorplanUtil;
import model.floorplan.GridLineModel;
import model.floorplan.Space2DModel;
import model.util.CoordinatePair;

import com.vividsolutions.jts.io.ParseException;

public class TestFloorplan3 {
	
	public static void main(String args[]) throws ParseException{
		FloorplanModel floorplanModel = new FloorplanModel();
		
		CoordinatePair box1 = new CoordinatePair();
		box1.setCoordinatePair( 10, 10, 20, 20 );
		floorplanModel.addSpaceBlock( box1 );
		
		CoordinatePair box2 = new CoordinatePair();
		box2.setCoordinatePair( 20, 10, 30, 20 );
		floorplanModel.addSpaceBlock( box2 );
		
		CoordinatePair box3 = new CoordinatePair();
		box3.setCoordinatePair( 9, 9, 11, 21 );
		floorplanModel.addWallCorner( box3 );
		
		CoordinatePair box4 = new CoordinatePair();
		box4.setCoordinatePair( 19, 9, 21, 21 );
		floorplanModel.addWallCorner( box4 );
		
		CoordinatePair box5 = new CoordinatePair();
		box5.setCoordinatePair( 29, 9, 31, 21 );
		floorplanModel.addWallCorner( box5 );
		
		CoordinatePair box6 = new CoordinatePair();
		box6.setCoordinatePair( 8, 8, 32, 22 );
		floorplanModel.addExteriorWall( box6 );
		
		Space2DModel sp1 = (Space2DModel) floorplanModel.getSpaceList().get( 0 );
		Space2DModel sp2 = (Space2DModel) floorplanModel.getSpaceList().get( 1 );
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** I : Create testline from GridLineModel ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		  
		GridLineModel testline1 = (GridLineModel) sp1.findGridLineModel( 20, true );
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** II : Space area with out components ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		
		double area1 = FloorplanUtil.getArea(floorplanModel, sp1);
		System.out.println("Center Area 1 = " + area1 );
		
		double area2 = FloorplanUtil.getArea(floorplanModel, sp2);
		System.out.println("Center Area 2 = " + area2 );
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** III : Move center line ( 2, 0 ) ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		
		testline1.move( 2, 0 );
		
		System.out.println("*** ");
		System.out.println("*** ======================================================= ");
		System.out.println("*** IV: Print refined Space and update the rTree... ");
		System.out.println("*** ======================================================= ");
		System.out.println("*** ");

		System.out.println(sp1.toString());
		System.out.println(sp2.toString());
		
		FloorplanUtil.updateWholeRTree(floorplanModel);
		
		System.out.println("*** ");
		System.out.println("*** =========================================================== ");
		System.out.println("*** V : Space area with out components ...");
	    System.out.println("*** =========================================================== ");
		System.out.println("*** ");
		
		double afterArea1 = FloorplanUtil.getArea(floorplanModel, sp1);
		System.out.println("Center Area after move 1 = " + afterArea1 );
		
		double afterArea2 = FloorplanUtil.getArea(floorplanModel, sp2);
		System.out.println("Center Area after move 2 = " + afterArea2 );
		
	}

}
