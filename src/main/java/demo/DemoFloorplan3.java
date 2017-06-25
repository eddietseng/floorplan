package demo;

import java.util.Iterator;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import model.*;
import model.floorplan.Corner2DModel;
import model.floorplan.GridLineModel;
import model.floorplan.GridSystem;
import model.floorplan.Space2DModel;
import model.floorplan.Wall2DModel;

public class DemoFloorplan3 {
	
	public static void main(String args[]) throws ParseException{

	      System.out.println("*** Starting DemoFloorplan.main() ... ");
	      System.out.println("*** ");
	      System.out.println("*** ======================================= ");
	      System.out.println("*** STEP 1: Initialization ... ");
	      System.out.println("*** ======================================= ");
	      System.out.println("*** ");
		   
	      CompositeHierarchy workspace = new CompositeHierarchy( 0.0, 0.0, 0.0 );
	      GridSystem archGrid = new GridSystem();
	      
	      // Initialize grid system workspace ...

	      archGrid.setWorkspace ( workspace );
	      
	      System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 2: Create and print test space ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		  
		  Space2DModel sp1 = new Space2DModel( "Test space 1", archGrid, 10, 10, 20, 20 );
	  	  Space2DModel sp3 = new Space2DModel( "Test space 3", archGrid, 20, 10, 30, 20 );
		  
		  System.out.println( sp1.toString() );
		   
		  // Add spatial coodinates to architectural grid ... 

		  archGrid.add ( sp1 );
			   
		  Iterator iterator7 = sp1.xGridLine.iterator();
		  while ( iterator7.hasNext() != false) {
			  GridLineModel gl = (GridLineModel) iterator7.next();
		      System.out.println(gl.toString());
		  }

		  Iterator iterator8 = sp1.yGridLine.iterator();
		  while ( iterator8.hasNext() != false) {
		      GridLineModel gl = (GridLineModel) iterator8.next();
	          System.out.println(gl.toString());
		  }
		   
		  System.out.println( sp3.toString() );
			   
		  // Add spatial coodinates to architectural grid ... 

		  archGrid.add ( sp3 );
			   
		  Iterator iterator9 = sp3.xGridLine.iterator();
		  while ( iterator9.hasNext() != false) {
		     GridLineModel gl = (GridLineModel) iterator9.next();
		     System.out.println(gl.toString());
		  }

		  Iterator iterator10 = sp3.yGridLine.iterator();
		  while ( iterator10.hasNext() != false) {
		     GridLineModel gl = (GridLineModel) iterator10.next();
	         System.out.println(gl.toString());
		  }
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 3: Refine & getNeighbors ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		  
		  sp1.refine( archGrid );
		  sp1.getNeighbors ( archGrid );
		  workspace.add ( sp1 );
		      
		  sp3.refine( archGrid );
		  sp3.getNeighbors ( archGrid );
	      workspace.add ( sp3 );
	      
	      System.out.println("*** ");
		  System.out.println("*** ======================================== ");
		  System.out.println("*** STEP 4: Create and print test corner ... ");
		  System.out.println("*** ======================================== ");
		  System.out.println("*** ");
		  
		  Corner2DModel cr1 = new Corner2DModel( "Test corner 1", archGrid, 18, 18, 22, 22 );
		  Corner2DModel cr2 = new Corner2DModel( "Test corner 2", archGrid, 8, 18, 12, 22 );
		  Corner2DModel cr3 = new Corner2DModel( "Test corner 3", archGrid, 18, 8, 22, 12 );
		  Corner2DModel cr7 = new Corner2DModel( "Test corner 7", archGrid, 28, 18, 32, 22 );
		  
		  Corner2DModel cr8 = new Corner2DModel( "Test corner 8", archGrid, 8, 8, 12, 12 );
		  Corner2DModel cr10 = new Corner2DModel( "Test corner 10", archGrid, 28, 8, 32, 12 );
		  
		  cr1.setAssociatePoint(20, 20);
		  System.out.println( cr1.toString() );
		  cr1.define();
		  workspace.add ( cr1 );
		   
		  cr2.setAssociatePoint(10, 20);
		  System.out.println( cr2.toString() );
		  cr2.define();
		  workspace.add ( cr2 );
		   
		  cr3.setAssociatePoint(20, 10);
		  System.out.println( cr3.toString() );
		  cr3.define();
		  workspace.add ( cr3 );
		   
		  cr7.setAssociatePoint(30, 20);
		  System.out.println( cr7.toString() );
		  cr7.define();
		  workspace.add ( cr7 );
		  
		  cr8.setAssociatePoint(10, 10);
		  cr8.define();
		  System.out.println( cr8.toString() );
		  workspace.add ( cr8 );
		  
		  cr10.setAssociatePoint(30, 10);
		  cr10.define();
		  System.out.println( cr10.toString() );
		  workspace.add ( cr10 );
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================== ");
		  System.out.println("*** STEP 5: Create and print test wall ... ");
		  System.out.println("*** ======================================== ");
		  System.out.println("*** ");
		  
		  Wall2DModel wall1 = new Wall2DModel( "Test wall 1", archGrid, 18, 12, 22, 18);
		  Wall2DModel wall2 = new Wall2DModel( "Test wall 2", archGrid, 12, 18, 18, 22);
		  Wall2DModel wall5 = new Wall2DModel( "Test wall 5", archGrid, 22, 18, 28, 22);
		  
		  Wall2DModel wall6 = new Wall2DModel( "Test wall 6", archGrid, 8, 12, 12, 18);
		  Wall2DModel wall7 = new Wall2DModel( "Test wall 7", archGrid, 12, 8, 18, 12);
		  Wall2DModel wall10 = new Wall2DModel( "Test wall 10", archGrid, 22, 8, 28, 12);
		  Wall2DModel wall11 = new Wall2DModel( "Test wall 11", archGrid, 28, 12, 32, 18);
		  
		  wall1.setAssociatePoint(20, 15);
	      wall1.associateCorner.add( cr1 );
	      wall1.associateCorner.add( cr3 );
	      wall1.define();
	  	  workspace.add( wall1 );
	      System.out.println( wall1.toString() );
	  	      
	      System.out.println("*** ======================================== ");
	  	      
	  	  wall2.setAssociatePoint(15, 20);
	      wall2.associateCorner.add( cr1 );
	      wall2.associateCorner.add( cr2 );
	      wall2.define();
	  	  workspace.add( wall2 );
	      System.out.println( wall2.toString() );
	       
	      System.out.println("*** ======================================== ");
	       
	  	  wall5.setAssociatePoint(25, 20);
	      wall5.associateCorner.add( cr1 );
	      wall5.associateCorner.add( cr7 );
	      wall5.define();
	  	  workspace.add( wall5 );
	      System.out.println( wall5.toString() );
	      
	      System.out.println("*** ======================================== ");
	      
	      wall6.setAssociatePoint(10, 15);
	      wall6.associateCorner.add( cr8 );
	      wall6.associateCorner.add( cr2 );
	      wall6.define();
	 	  workspace.add( wall6 );
	      System.out.println( wall6.toString() );
	 	      
	      System.out.println("*** ======================================== ");
	 	   
	 	  wall7.setAssociatePoint(15, 10);
	      wall7.associateCorner.add( cr8 );
	      wall7.associateCorner.add( cr3 );
	      wall7.define();
	 	  workspace.add( wall7 );
	      System.out.println( wall7.toString() );
	      
	      System.out.println("*** ======================================== ");
	      
	      wall10.setAssociatePoint(25, 10);
	      wall10.associateCorner.add( cr3 );
	      wall10.associateCorner.add( cr10 );
	      wall10.define();
	 	  workspace.add( wall10 );
	      System.out.println( wall10.toString() );
	 	      
	      System.out.println("*** ======================================== ");
	 	   
	 	  wall11.setAssociatePoint(30, 15);
	      wall11.associateCorner.add( cr7 );
	      wall11.associateCorner.add( cr10 );
	      wall11.define();
	 	  workspace.add( wall11 );
	      System.out.println( wall11.toString() );
	      
	      System.out.println("*** ");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** STEP 6: Create testline from GridLineModel. Add listener ...");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** ");
		  
		  GridLineModel testline1 = (GridLineModel) sp1.findGridLineModel( 20, false );
		  GridLineModel testline2 = (GridLineModel) sp1.findGridLineModel( 20, true  );
		   
		  GridLineModel testline4 = (GridLineModel) sp3.findGridLineModel( 20, false );
		  GridLineModel testline3 = (GridLineModel) sp3.findGridLineModel( 20, true  );
		   
		  if( testline1 == testline4 )
			  System.out.println(" DemoFloorplan3  Same y Grid line found! ");
		  else
			  System.out.println(" DemoFloorplan3  ERROR: BUG FOUND (y-gridline) ");
		   
		  if( testline2 == testline3 )
			  System.out.println(" DemoFloorplan3  Same x Grid line found! ");
		  else
			  System.out.println(" DemoFloorplan3  ERROR: BUG FOUND (x-gridline) ");
			   
		  testline1.addFloorplanModelListener( sp1 );
		  testline1.addFloorplanModelListener( cr1 );
		  testline1.addFloorplanModelListener( cr2 );

		  testline1.addFloorplanModelListener( sp3 );
		  testline1.addFloorplanModelListener( cr7 );
		   
		  testline2.addFloorplanModelListener( sp1 );
		  testline2.addFloorplanModelListener( cr1 );
		  testline2.addFloorplanModelListener( cr3 );
		   
		  testline2.addFloorplanModelListener( sp3 );
		   
		  cr1.addFloorplanModelListener( wall1 );
		  cr3.addFloorplanModelListener( wall1 );
		      
		  cr1.addFloorplanModelListener( wall2 );
		  cr2.addFloorplanModelListener( wall2 );
		   
		  cr1.addFloorplanModelListener( wall5 );
		  cr7.addFloorplanModelListener( wall5 );
		  
		  cr2.addFloorplanModelListener( wall6 );
		  cr8.addFloorplanModelListener( wall6 );
		  
		  cr3.addFloorplanModelListener( wall7 );
		  cr8.addFloorplanModelListener( wall7 );
		  
		  cr3.addFloorplanModelListener( wall10 );
		  cr10.addFloorplanModelListener( wall10 );
		  
		  cr7.addFloorplanModelListener( wall11 );
		  cr10.addFloorplanModelListener( wall11 );
		  
		  System.out.println("*** ");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** STEP A: Print details of testline (before move) ...         ");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** ");
		   
		  System.out.println( testline2.toString() );
		  
		  System.out.println("*** ");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** STEP 7: Print details of testline (before move) ...         ");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** ");
		   
		  System.out.println( testline2.toString() );
		  
		  System.out.println("*** =================== Area Calculation ===================== ");
		  
		  GeometryFactory fact = new GeometryFactory();
		  WKTReader wktRdr = new WKTReader(fact);
		    
		  String sp1wkt = sp1.getPolygonString();
		  
		  String cr1wkt = cr1.getPolygonString();
		  String cr2wkt = cr2.getPolygonString();
		  String cr3wkt = cr3.getPolygonString();
		  String cr8wkt = cr8.getPolygonString();
		  
		  String wall1wkt = wall1.getPolygonString();
		  String wall2wkt = wall2.getPolygonString();
		  String wall6wkt = wall6.getPolygonString();
		  String wall7wkt = wall7.getPolygonString();
		  
		  Geometry sp1Geo = wktRdr.read( sp1wkt );
		  
		  Geometry cr1Geo = wktRdr.read( cr1wkt );
		  Geometry cr2Geo = wktRdr.read( cr2wkt );
		  Geometry cr3Geo = wktRdr.read( cr3wkt );
		  Geometry cr8Geo = wktRdr.read( cr8wkt );
		  
		  Geometry wall1Geo = wktRdr.read( wall1wkt );
		  Geometry wall2Geo = wktRdr.read( wall2wkt );
		  Geometry wall6Geo = wktRdr.read( wall6wkt );
		  Geometry wall7Geo = wktRdr.read( wall7wkt );
		  
		  Geometry A = cr1Geo.union( wall1Geo ).union( cr3Geo ).union( wall7Geo ).union( cr8Geo ).union( wall6Geo ).union( cr2Geo ).union( wall2Geo );
	      Geometry B = A.union( sp1Geo );
	      Geometry C = B.difference(A);
	      System.out.println("========================================================");
	      System.out.println("A = " + A);
	      System.out.println("Corners & Walls area = " + A.getArea());
	      System.out.println("========================================================");
	      System.out.println("B = " + B);
	      System.out.println("Total cover area = " + B.getArea());
	      System.out.println("========================================================");
	      System.out.println("C = " + C);
	      System.out.println("Center Area = " + C.getArea());
	      
	      System.out.println("*** ======================================== ");
	      
	      String sp3wkt = sp3.getPolygonString();
		  
		  cr1wkt = cr1.getPolygonString();
		  cr3wkt = cr3.getPolygonString();
		  String cr7wkt = cr7.getPolygonString();
		  String cr10wkt = cr10.getPolygonString();
		  
		  wall1wkt = wall1.getPolygonString();
		  String wall5wkt = wall5.getPolygonString();
		  String wall10wkt = wall10.getPolygonString();
		  String wall11wkt = wall11.getPolygonString();
		  
		  Geometry sp3Geo = wktRdr.read( sp3wkt );
		  
		  cr1Geo = wktRdr.read( cr1wkt );
		  cr3Geo = wktRdr.read( cr3wkt );
		  Geometry cr7Geo = wktRdr.read( cr7wkt );
		  Geometry cr10Geo = wktRdr.read( cr10wkt );
		  
		  wall1Geo = wktRdr.read( wall1wkt );
		  Geometry wall5Geo = wktRdr.read( wall5wkt );
		  Geometry wall10Geo = wktRdr.read( wall10wkt );
		  Geometry wall11Geo = wktRdr.read( wall11wkt );
		  
		  Geometry G = cr1Geo.union( wall5Geo ).union( cr7Geo ).union( wall11Geo ).union( cr10Geo ).union( wall10Geo ).union( cr3Geo ).union( wall1Geo );
	      Geometry H = G.union( sp3Geo );
	      Geometry I = H.difference(G);
	      System.out.println("========================================================");
	      System.out.println("G = " + G);
	      System.out.println("Corners & Walls area = " + G.getArea());
	      System.out.println("========================================================");
	      System.out.println("H = " + H);
	      System.out.println("Total cover area = " + H.getArea());
	      System.out.println("========================================================");
	      System.out.println("I = " + I);
	      System.out.println("Center Area = " + I.getArea());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 8: testline.move( 5, 0 ) ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");

		  testline2.move( 5, 0 );
		      
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 9: Print Testline after moved ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");

		  System.out.println( testline2.toString() );
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 10-1: Print refined Space ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");

		  System.out.println(sp1.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 10-3: Print refined Space ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");

		  System.out.println(sp3.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 11-1: Print refined Corner ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(cr1.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 11-2: Print refined Corner ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(cr2.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 11-3: Print refined Corner ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(cr3.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 11-7: Print refined Corner ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(cr7.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 12-1: Print refined Wall ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(wall1.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 12-2: Print refined Wall ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(wall2.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 12-5: Print refined Wall ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(wall5.toString());
		  
		  System.out.println("*** =================== Area Calculation ===================== ");
		  
		  sp1wkt = sp1.getPolygonString();
		  
		  cr1wkt = cr1.getPolygonString();
		  cr2wkt = cr2.getPolygonString();
		  cr3wkt = cr3.getPolygonString();
		  cr8wkt = cr8.getPolygonString();
		  
		  wall1wkt = wall1.getPolygonString();
		  wall2wkt = wall2.getPolygonString();
		  wall6wkt = wall6.getPolygonString();
		  wall7wkt = wall7.getPolygonString();
		  
		  sp1Geo = wktRdr.read( sp1wkt );
		  
		  cr1Geo = wktRdr.read( cr1wkt );
		  cr2Geo = wktRdr.read( cr2wkt );
		  cr3Geo = wktRdr.read( cr3wkt );
		  cr8Geo = wktRdr.read( cr8wkt );
		  
		  wall1Geo = wktRdr.read( wall1wkt );
		  wall2Geo = wktRdr.read( wall2wkt );
		  wall6Geo = wktRdr.read( wall6wkt );
		  wall7Geo = wktRdr.read( wall7wkt );
		  
		  A = cr1Geo.union( wall1Geo ).union( cr3Geo ).union( wall7Geo ).union( cr8Geo ).union( wall6Geo ).union( cr2Geo ).union( wall2Geo );
	      B = A.union( sp1Geo );
	      C = B.difference(A);
	      System.out.println("========================================================");
	      System.out.println("A = " + A);
	      System.out.println("Corners & Walls area = " + A.getArea());
	      System.out.println("========================================================");
	      System.out.println("B = " + B);
	      System.out.println("Total cover area = " + B.getArea());
	      System.out.println("========================================================");
	      System.out.println("C = " + C);
	      System.out.println("Center Area = " + C.getArea());
	      
	      System.out.println("*** ======================================== ");
	      
	      sp3wkt = sp3.getPolygonString();
		  
		  cr1wkt = cr1.getPolygonString();
		  cr3wkt = cr3.getPolygonString();
		  cr7wkt = cr7.getPolygonString();
		  cr10wkt = cr10.getPolygonString();
		  
		  wall1wkt = wall1.getPolygonString();
		  wall5wkt = wall5.getPolygonString();
		  wall10wkt = wall10.getPolygonString();
		  wall11wkt = wall11.getPolygonString();
		  
		  sp3Geo = wktRdr.read( sp3wkt );
		  
		  cr1Geo = wktRdr.read( cr1wkt );
		  cr3Geo = wktRdr.read( cr3wkt );
		  cr7Geo = wktRdr.read( cr7wkt );
		  cr10Geo = wktRdr.read( cr10wkt );
		  
		  wall1Geo = wktRdr.read( wall1wkt );
		  wall5Geo = wktRdr.read( wall5wkt );
		  wall10Geo = wktRdr.read( wall10wkt );
		  wall11Geo = wktRdr.read( wall11wkt );
		  
		  G = cr1Geo.union( wall5Geo ).union( cr7Geo ).union( wall11Geo ).union( cr10Geo ).union( wall10Geo ).union( cr3Geo ).union( wall1Geo );
	      H = G.union( sp3Geo );
	      I = H.difference(G);
	      System.out.println("========================================================");
	      System.out.println("G = " + G);
	      System.out.println("Corners & Walls area = " + G.getArea());
	      System.out.println("========================================================");
	      System.out.println("H = " + H);
	      System.out.println("Total cover area = " + H.getArea());
	      System.out.println("========================================================");
	      System.out.println("I = " + I);
	      System.out.println("Center Area = " + I.getArea());
		  
		  System.out.println("*** ================================= ");
	      System.out.println("*** Finished! ");
	}

}
