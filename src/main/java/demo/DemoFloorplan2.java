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

public class DemoFloorplan2 {
	
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
		  Space2DModel sp2 = new Space2DModel( "Test space 2", archGrid, 40, 10, 50, 20 );
		  
		  System.out.println( sp1.toString() );
		   
		  // Add spatial coodinates to architectural grid ... 

		  archGrid.add ( sp1 );
			   
		  Iterator iterator3 = sp1.xGridLine.iterator();
		  while ( iterator3.hasNext() != false) {
		     GridLineModel gl = (GridLineModel) iterator3.next();
		     System.out.println(gl.toString());
		  }

		  Iterator iterator4 = sp1.yGridLine.iterator();
		  while ( iterator4.hasNext() != false) {
		     GridLineModel gl = (GridLineModel) iterator4.next();
	         System.out.println(gl.toString());
		  }
		   
		  System.out.println( sp2.toString() );
			   
		  // Add spatial coodinates to architectural grid ... 

		  archGrid.add ( sp2 );
			   
		  Iterator iterator5 = sp2.xGridLine.iterator();
		  while ( iterator5.hasNext() != false) {
		     GridLineModel gl = (GridLineModel) iterator5.next();
		     System.out.println(gl.toString());
		  }

		  Iterator iterator6 = sp2.yGridLine.iterator();
		  while ( iterator6.hasNext() != false) {
		     GridLineModel gl = (GridLineModel) iterator6.next();
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
		      
		  sp2.refine( archGrid );
		  sp2.getNeighbors ( archGrid );
	      workspace.add ( sp2 );
		  
	      System.out.println("*** ");
		  System.out.println("*** ======================================== ");
		  System.out.println("*** STEP 4: Create and print test corner ... ");
		  System.out.println("*** ======================================== ");
		  System.out.println("*** ");
		  
		  Corner2DModel cr1 = new Corner2DModel( "Test corner 1", archGrid, 18, 18, 22, 22 );
		  Corner2DModel cr2 = new Corner2DModel( "Test corner 2", archGrid, 8, 18, 12, 22 );
		  Corner2DModel cr3 = new Corner2DModel( "Test corner 3", archGrid, 18, 8, 22, 12 );
		  Corner2DModel cr4 = new Corner2DModel( "Test corner 4", archGrid, 38, 18, 42, 22 );
		  Corner2DModel cr5 = new Corner2DModel( "Test corner 5", archGrid, 48, 18, 52, 22 );
		  Corner2DModel cr6 = new Corner2DModel( "Test corner 6", archGrid, 38, 8, 42, 12 );
		  
		  Corner2DModel cr8 = new Corner2DModel( "Test corner 8", archGrid, 8, 8, 12, 12 );
		  Corner2DModel cr9 = new Corner2DModel( "Test corner 9", archGrid, 48, 8, 52, 12 );
		  
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
		   
		  cr4.setAssociatePoint(40, 20);
		  System.out.println( cr4.toString() );
		  cr4.define();
		  workspace.add ( cr4 );
		      
		  cr5.setAssociatePoint(50, 20);
		  System.out.println( cr5.toString() );
		  cr5.define();
		  workspace.add ( cr5 );
		      
		  cr6.setAssociatePoint(40, 10);
		  System.out.println( cr6.toString() );
		  cr6.define();
		  workspace.add ( cr6 );
		  
		  cr8.setAssociatePoint(10, 10);
		  cr8.define();
		  System.out.println( cr8.toString() );
		  workspace.add ( cr8 );
		  
		  cr9.setAssociatePoint(50, 10);
		  cr9.define();
		  System.out.println( cr9.toString() );
		  workspace.add ( cr9 );
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================== ");
		  System.out.println("*** STEP 5: Create and print test wall ... ");
		  System.out.println("*** ======================================== ");
		  System.out.println("*** ");
		  
		  Wall2DModel wall1 = new Wall2DModel( "Test wall 1", archGrid, 18, 12, 22, 18);
		  Wall2DModel wall2 = new Wall2DModel( "Test wall 2", archGrid, 12, 18, 18, 22);
		  Wall2DModel wall3 = new Wall2DModel( "Test wall 3", archGrid, 38, 12, 42, 18);
		  Wall2DModel wall4 = new Wall2DModel( "Test wall 4", archGrid, 42, 18, 48, 22);
		  
		  Wall2DModel wall6 = new Wall2DModel( "Test wall 6", archGrid, 8, 12, 12, 18);
		  Wall2DModel wall7 = new Wall2DModel( "Test wall 7", archGrid, 12, 8, 18, 12);
		  Wall2DModel wall8 = new Wall2DModel( "Test wall 8", archGrid, 42, 8, 48, 12);
		  Wall2DModel wall9 = new Wall2DModel( "Test wall 9", archGrid, 48, 12, 52, 18);
		  
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
	       
	  	  wall3.setAssociatePoint(40, 15);
	      wall3.associateCorner.add( cr4 );
	      wall3.associateCorner.add( cr6 );
	      wall3.define();
	  	  workspace.add( wall3 );
	      System.out.println( wall3.toString() );
	  	      
	      System.out.println("*** ======================================== ");
	  	   
	  	  wall4.setAssociatePoint(45, 20);
	      wall4.associateCorner.add( cr4 );
	      wall4.associateCorner.add( cr5 );
	      wall4.define();
	  	  workspace.add( wall4 );
	      System.out.println( wall4.toString() );
	      
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
	      
	      wall8.setAssociatePoint(45, 10);
	      wall8.associateCorner.add( cr6 );
	      wall8.associateCorner.add( cr9 );
	      wall8.define();
	 	  workspace.add( wall8 );
	      System.out.println( wall8.toString() );
	 	      
	      System.out.println("*** ======================================== ");
	 	   
	 	  wall9.setAssociatePoint(50, 15);
	      wall9.associateCorner.add( cr5 );
	      wall9.associateCorner.add( cr9 );
	      wall9.define();
	 	  workspace.add( wall9 );
	      System.out.println( wall9.toString() );
	      
	      System.out.println("*** ");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** STEP 6: Create testline from GridLineModel. Add listener ...");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** ");
		  
		  GridLineModel testline1 = (GridLineModel) sp1.findGridLineModel( 20, false );
		  GridLineModel testline2 = (GridLineModel) sp1.findGridLineModel( 20, true  );
		  GridLineModel testline3 = (GridLineModel) sp2.findGridLineModel( 40, true );
		  GridLineModel testline4 = (GridLineModel) sp2.findGridLineModel( 20, false );
		   
		  if( testline1 == testline4 )
			  System.out.println("DemoFloorplan2  Same Grid line found! ");
		  else
			  System.out.println("DemoFloorplan2  ERROR: BUG FOUND ");
			   
		  testline1.addFloorplanModelListener( sp1 );
		  testline1.addFloorplanModelListener( cr1 );
		  testline1.addFloorplanModelListener( cr2 );

		  testline1.addFloorplanModelListener( sp2 );
		  testline1.addFloorplanModelListener( cr4 );
		  testline1.addFloorplanModelListener( cr5 );
		   
		  testline2.addFloorplanModelListener( sp1 );
		  testline2.addFloorplanModelListener( cr1 );
		  testline2.addFloorplanModelListener( cr3 );
		   
		  testline3.addFloorplanModelListener( sp2 );
		  testline3.addFloorplanModelListener( cr4 );
		  testline3.addFloorplanModelListener( cr6 );
		      
		  cr1.addFloorplanModelListener( wall1 );
		  cr3.addFloorplanModelListener( wall1 );
		      
		  cr1.addFloorplanModelListener( wall2 );
		  cr2.addFloorplanModelListener( wall2 );
		   
		  cr4.addFloorplanModelListener( wall3 );
		  cr6.addFloorplanModelListener( wall3 );
		   
		  cr4.addFloorplanModelListener( wall4 );
		  cr5.addFloorplanModelListener( wall4 );
		  
		  cr2.addFloorplanModelListener( wall6 );
		  cr8.addFloorplanModelListener( wall6 );
		  
		  cr3.addFloorplanModelListener( wall7 );
		  cr8.addFloorplanModelListener( wall7 );
		  
		  cr6.addFloorplanModelListener( wall8 );
		  cr9.addFloorplanModelListener( wall8 );
		  
		  cr5.addFloorplanModelListener( wall9 );
		  cr9.addFloorplanModelListener( wall9 );
		  
		  System.out.println("*** ");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** STEP 7: Print details of testline (before move) ...         ");
		  System.out.println("*** =========================================================== ");
		  System.out.println("*** ");
		   
		  System.out.println( testline1.toString() );
		  
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
	      
	      String sp2wkt = sp2.getPolygonString();
		  
		  String cr4wkt = cr4.getPolygonString();
		  String cr5wkt = cr5.getPolygonString();
		  String cr6wkt = cr6.getPolygonString();
		  String cr9wkt = cr9.getPolygonString();
		  
		  String wall3wkt = wall3.getPolygonString();
		  String wall4wkt = wall4.getPolygonString();
		  String wall8wkt = wall8.getPolygonString();
		  String wall9wkt = wall9.getPolygonString();
		  
		  Geometry sp2Geo = wktRdr.read( sp2wkt );
		  
		  Geometry cr4Geo = wktRdr.read( cr4wkt );
		  Geometry cr5Geo = wktRdr.read( cr5wkt );
		  Geometry cr6Geo = wktRdr.read( cr6wkt );
		  Geometry cr9Geo = wktRdr.read( cr9wkt );
		  
		  Geometry wall3Geo = wktRdr.read( wall3wkt );
		  Geometry wall4Geo = wktRdr.read( wall4wkt );
		  Geometry wall8Geo = wktRdr.read( wall8wkt );
		  Geometry wall9Geo = wktRdr.read( wall9wkt );
		  
		  Geometry D = cr4Geo.union( wall4Geo ).union( cr5Geo ).union( wall9Geo ).union( cr9Geo ).union( wall8Geo ).union( cr6Geo ).union( wall3Geo );
	      Geometry E = D.union( sp2Geo );
	      Geometry F = E.difference(D);
	      System.out.println("========================================================");
	      System.out.println("D = " + D);
	      System.out.println("Corners & Walls area = " + D.getArea());
	      System.out.println("========================================================");
	      System.out.println("E = " + E);
	      System.out.println("Total cover area = " + E.getArea());
	      System.out.println("========================================================");
	      System.out.println("F = " + F);
	      System.out.println("Center Area = " + F.getArea());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 8: testline.move( 0, 5 ) ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");

		  testline1.move( 0, 5 );
		      
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 9: Print Testline after moved ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");

		  System.out.println( testline1.toString() );
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 10-1: Print refined Space ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");

		  System.out.println(sp1.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 10-2: Print refined Space ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");

		  System.out.println(sp2.toString());
		  
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
		  System.out.println("*** STEP 11-4: Print refined Corner ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(cr4.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 11-5: Print refined Corner ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(cr5.toString());
		  
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
		  System.out.println("*** STEP 12-3: Print refined Wall ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(wall3.toString());
		  
		  System.out.println("*** ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** STEP 12-4: Print refined Wall ... ");
		  System.out.println("*** ======================================= ");
		  System.out.println("*** ");
		      
		  System.out.println(wall4.toString());
		  
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
	      
	      sp2wkt = sp2.getPolygonString();
		  
		  cr4wkt = cr4.getPolygonString();
		  cr5wkt = cr5.getPolygonString();
		  cr6wkt = cr6.getPolygonString();
		  cr9wkt = cr9.getPolygonString();
		  
		  wall3wkt = wall3.getPolygonString();
		  wall4wkt = wall4.getPolygonString();
		  wall8wkt = wall8.getPolygonString();
		  wall9wkt = wall9.getPolygonString();
		  
		  sp2Geo = wktRdr.read( sp2wkt );
		  
		  cr4Geo = wktRdr.read( cr4wkt );
		  cr5Geo = wktRdr.read( cr5wkt );
		  cr6Geo = wktRdr.read( cr6wkt );
		  cr9Geo = wktRdr.read( cr9wkt );
		  
		  wall3Geo = wktRdr.read( wall3wkt );
		  wall4Geo = wktRdr.read( wall4wkt );
		  wall8Geo = wktRdr.read( wall8wkt );
		  wall9Geo = wktRdr.read( wall9wkt );
		  
		  D = cr4Geo.union( wall4Geo ).union( cr5Geo ).union( wall9Geo ).union( cr9Geo ).union( wall8Geo ).union( cr6Geo ).union( wall3Geo );
	      E = D.union( sp2Geo );
	      F = E.difference(D);
	      System.out.println("========================================================");
	      System.out.println("D = " + D);
	      System.out.println("Corners & Walls area = " + D.getArea());
	      System.out.println("========================================================");
	      System.out.println("E = " + E);
	      System.out.println("Total cover area = " + E.getArea());
	      System.out.println("========================================================");
	      System.out.println("F = " + F);
	      System.out.println("Center Area = " + F.getArea());
		  
		  System.out.println("*** ================================= ");
	      System.out.println("*** Finished! ");
	      
	 }

}
