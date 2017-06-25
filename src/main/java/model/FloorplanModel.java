/*
 *  ====================================================================
 *  java-code-new.d/java-beans8.d/src/model/FloorplanModel.java
 *  ====================================================================
 */

package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.vividsolutions.jts.io.ParseException;

import controller.EngineeringController;

import model.rtree.HyperBoundingBox; // Classes for r-tree storage .... 
import model.rtree.HyperPoint;
import model.rtree.RTree;
import model.rtree.RTreeException;

import model.floorplan.*;
import model.util.*;

public class FloorplanModel extends AbstractModel {
   private Boolean DEBUG = true;
   private boolean displayTag = false;
   private CompositeHierarchy  workspace;
   HashMap<String,Object>         things;
   public ArrayList<String> addedKeys = new ArrayList<String>();
   private String currentTask;
   int iNoBlocks;
   int iNoSpaces;
   int iNoColumns;
   int iNoCorners;
   int iNoWalls;
   int iNoPortals;
   int iNoDoors;
   int iNoWindows;
   int iNoComponents;

   protected List<Space2DModel> spaceList  = new ArrayList<Space2DModel>();
   protected List<Column2DModel> columnList = new ArrayList<Column2DModel>();
   protected List<Corner2DModel> cornerList = new ArrayList<Corner2DModel>();
   protected List<Wall2DModel> wallList = new ArrayList<Wall2DModel>();
   protected List<Portal2DModel> portalList = new ArrayList<Portal2DModel>();
   protected List<Door2DModel> doorList = new ArrayList<Door2DModel>();
   protected List<Window2DModel> windowList = new ArrayList<Window2DModel>();
   
   protected ArrayList<RoomModel> roomList = new ArrayList<RoomModel>();
   protected ArrayList<FloorplanResultModel> resultsList = new ArrayList<FloorplanResultModel>();
   
   // Grid of architectural spaces....

   protected GridSystem archGrid = new GridSystem();

   // Parameters for r-tree storage ....

   protected RTree      ToplevelTree    = null;  //   Top Level Tree storage of spatial info ....
   protected RTree      SecondlevelTree = null;  //Second Level Tree storage of components ...
   protected RTree      ThirdlevelTree  = null;  // Third Level Tree storage of objects inside wall ...
   protected List<IndexArch> spaceindexList  = new ArrayList<IndexArch>();
   protected List<IndexArch> componentsindexList = new ArrayList<IndexArch>();
   
   // Utility Parameters
   protected UtilityModel utilityModel;
   
   // HashMap for storing the composite hierarchy from the wall component.
   protected HashMap< String, CompositeHierarchy> chMap = new HashMap< String, CompositeHierarchy>();
   
   protected ChangeListener listener = new ChangeListener(){

	@Override
	public void stateChanged(ChangeEvent e) 
	{
		if( e.getSource() instanceof Wall2DModel )
		{
			System.out.println( "FloorplanModel.stateChanged ....******" );
			Wall2DModel wall = (Wall2DModel)e.getSource();
			wall.removeChangeListener( listener );
			CompositeHierarchy ch = chMap.get( wall.getName() );
			System.out.println( "FloorplanModel.stateChanged ....ch(x,y) = ( "
					+ ch.getX() + ", " + ch.getY() + " )" );
			
			chMap.remove( ch );
			workspace.remove( ch );
			if( !wall.getChildList().isEmpty() )
			{
				for( AbstractCompoundFeature acf : wall.getChildList() )
				{
					if( acf instanceof Portal2DModel )
					{
						System.out.println( "FloorplanModel.stateChanged ...." + acf.getName() + "(x,y) = ( "
								+ acf.getX() + ", " + acf.getY() + " )" );
						
						if( chMap.get( wall.getName() ) == null )
						{
							chMap.put( wall.getName(), wall.getCompositeHierarchy() );
							workspace.add( wall.getCompositeHierarchy() );
						}
						//TODO bug
						wall.getCompositeHierarchy().add( acf );
						
				        System.out.println( "FloorplanModel.stateChanged ...." + acf.getName() + " Min(x,y) = ( "
							  + ((Portal2DModel)acf).getComponentBoundary().getBoundingBox().getMinX() + ", " 
				    		  + ((Portal2DModel)acf).getComponentBoundary().getBoundingBox().getMinY() + " ) ; Max(x,y) = ( "
				    		  + ((Portal2DModel)acf).getComponentBoundary().getBoundingBox().getMaxX() + ", " 
				    		  + ((Portal2DModel)acf).getComponentBoundary().getBoundingBox().getMaxY() + " )" );
						
				        for( Feature feature :wall.getCompositeHierarchy().getChildren() )
						{
							System.out.println( "Name: " + feature.getName() );
						}
					}
					else if( acf instanceof Door2DModel )
					{
						System.out.println( "FloorplanModel.stateChanged ...." + acf.getName() + "(x,y) = ( "
								+ acf.getX() + ", " + acf.getY() + " )" );
						
						chMap.put( wall.getName(), wall.getCompositeHierarchy() );
						workspace.add( wall.getCompositeHierarchy() );
						wall.getCompositeHierarchy().add( acf );
						
				        System.out.println( "FloorplanModel.stateChanged ...." + acf.getName() + " Min(x,y) = ( "
							  + ((Door2DModel)acf).getComponentBoundary().getBoundingBox().getMinX() + ", " 
				    		  + ((Door2DModel)acf).getComponentBoundary().getBoundingBox().getMinY() + " ) ; Max(x,y) = ( "
				    		  + ((Door2DModel)acf).getComponentBoundary().getBoundingBox().getMaxX() + ", " 
				    		  + ((Door2DModel)acf).getComponentBoundary().getBoundingBox().getMaxY() + " )" );
						
				        for( Feature feature :wall.getCompositeHierarchy().getChildren() )
						{
							System.out.println( "Name: " + feature.getName() );
						}
					}
					else if( acf instanceof Window2DModel )
					{
						System.out.println( "FloorplanModel.stateChanged ...." + acf.getName() + "(x,y) = ( "
								+ acf.getX() + ", " + acf.getY() + " )" );
						
						chMap.put( wall.getName(), wall.getCompositeHierarchy() );
						workspace.add( wall.getCompositeHierarchy() );
						wall.getCompositeHierarchy().add( acf );
						
				        System.out.println( "FloorplanModel.stateChanged ...." + acf.getName() + " Min(x,y) = ( "
							  + ((Window2DModel)acf).getComponentBoundary().getBoundingBox().getMinX() + ", " 
				    		  + ((Window2DModel)acf).getComponentBoundary().getBoundingBox().getMinY() + " ) ; Max(x,y) = ( "
				    		  + ((Window2DModel)acf).getComponentBoundary().getBoundingBox().getMaxX() + ", " 
				    		  + ((Window2DModel)acf).getComponentBoundary().getBoundingBox().getMaxY() + " )" );
						
				        for( Feature feature :wall.getCompositeHierarchy().getChildren() )
						{
							System.out.println( "Name: " + feature.getName() );
						}
					}
				}
			}
			wall.addChangeListener( listener );
			
			System.out.println( "FloorplanModel.stateChanged ....ch(x,y) = ( "
					+ wall.getCompositeHierarchy().getX() + ", " + wall.getCompositeHierarchy().getY() + " )" );
			
			System.out.println( "FloorplanModel.stateChanged ....******" );
		}	
	}};

   // Menu of floorplan editing tasks .....

   String[] taskLabels = {"Add Space Block",
                          "Add Support Column",
                          "Add Wall Corner",
                          "Add Exterior Wall",
                          "Add Portal",
                          "Add Door",
                          "Add Window" };
   // Numbers of Components inside rtree ...
   
   
   
   // Constructor Method ....

   public FloorplanModel() {
      System.out.println("");
      System.out.println("Create Model of Floorplan Entities ... ");
      System.out.println("===========================================================");

      // Initialize task and floorplan parameters ....

      iNoSpaces   = 0;
      iNoColumns  = 0;
      iNoCorners = 0;
      iNoWalls = 0;
      iNoPortals = 0;
      iNoDoors = 0;
      iNoWindows = 0;
      iNoComponents = 0;
      currentTask = taskLabels[0];
      
      

      // Initialize hashmaps and Workspace storage ...

      things    = new HashMap<String,Object>();
      workspace = new CompositeHierarchy( 0.0, 0.0, 0.0 );
      workspace.setName("Floorplan Workspace");
      
      // Initialize grid system workspace ...

      archGrid.setWorkspace ( workspace );

      // Construct persistent rtrees with dimension 2 and maxload 5
      // index file will be rtreeArch.idx and rtreeStruct.idx

      try {
         ToplevelTree    = new RTree(2, 5, "topleveltreeArch.idx"   );
         SecondlevelTree = new RTree(2, 5, "secondleveltreeArch.idx");
         ThirdlevelTree  = new RTree(2, 5, "thirdleveltreeArch.idx" );
      } catch (RTreeException e) {
         e.printStackTrace();
      }

      System.out.println("===========================================================");
      System.out.println("");
   }

   // Build model of Metro system area things ...

   public void initDefault() {
      setFloorplan();
      setFloorplanComposite();
   }
   
   /**
    * Returns the wall composite hierarchy map
    * @return HashMap< String, CompositeHierarchy>
    */
   public HashMap< String, CompositeHierarchy> getWallChMap()
   {
	   return chMap;
   }

   // ======================================================================
   // Add rectangular space block to the floorplan model ...
   // ======================================================================

   public void addSpaceBlock( CoordinatePair pair ) {

      System.out.println("*** Enter FloorplanModel.addSpaceBlock() ... ");
      System.out.println( pair.toString() );

      // Retrieve coordinates of input rectangle ...

      double dX1 = pair.getX1(); double dY1 = pair.getY1();
      double dX2 = pair.getX2(); double dY2 = pair.getY2();

      // Create temporary space block ....

      Space2DModel sp1 = new Space2DModel( dX1, dY1, dX2, dY2 );

      // Check for overlap in r-tree ...

      double A[] = new double[2];
      A[0] = Math.min ( dX1, dX2 );
      A[1] = Math.min ( dY1, dY2 );
      double B[] = new double[2];
      B[0] = Math.max ( dX1, dX2 );
      B[1] = Math.max ( dY1, dY2 );
      HyperPoint hpA = new HyperPoint ( A );
      HyperPoint hpB = new HyperPoint ( B );

      Object[] result = null;
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
          result = ToplevelTree.intersects( hp );
      } catch (RTreeException e) {
          // if problems occur - in the real world you have
          // to handle the exceptions :-)
          e.printStackTrace();
      }

      System.out.println("*** In FloorplanModel.addSpaceBlock(): flag 1: result.length = " + result.length);

      // Check that new space does not overlap currently defined spaces ...

      Integer temp;
      for(int i=0; i < result.length; i++) {

          temp = (Integer) result[i];
          IndexArch np = (IndexArch) spaceindexList.get( (temp.intValue()-1) );

          // Process architectural index ....

          if( np.getIndex() == IndexArch.SPACE ) {
              Space2DModel sp = (Space2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** ERROR: Spaces overlap!!");
                  return;
              } 
          } else { 
              System.out.println("ERROR!!! ");
          }
      }

      System.out.println("*** In FloorplanModel.addSpaceBlock(): flag 2 ... ");

      // ==========================================================
      // Add space to floorplan database ....
      // Add item to list of space-index pairs ...
      // ==========================================================

      iNoSpaces = iNoSpaces + 1;
      sp1.setName ( "sp-" + iNoSpaces );
      spaceList.add ( sp1 );
      spaceindexList.add ( new IndexArch ( new Integer( iNoSpaces ), sp1, IndexArch.SPACE ) );

      // ==============================================================
      // Insert new item into r-tree database ....
      // the Integer object represents the ID for the extern objects
      // ==============================================================

      try {
          double C[] = new double[2];
          C[0] = sp1.getBoundingBox().getMinX();
          C[1] = sp1.getBoundingBox().getMinY();
          double D[] = new double[2];
          D[0] = sp1.getBoundingBox().getMaxX();
          D[1] = sp1.getBoundingBox().getMaxY();

          System.out.println( "Box:" + sp1.getName() );
          System.out.println( sp1.getBoundingBox().toString() );

          HyperPoint hpC = new HyperPoint ( C );
          HyperPoint hpD = new HyperPoint ( D );
          ToplevelTree.insert( new Integer( iNoSpaces ), new HyperBoundingBox( hpC, hpD ) );
          sp1.setHyperBoundingBox( new HyperBoundingBox( hpC, hpD ) );
      } catch (RTreeException e) {
          e.printStackTrace();  // common exception of RTree implementation
      }		

      System.out.println("*** In FloorplanModel.addSpaceBlock(): flag 3 ... ");

      // Set Gridsystem
      sp1.setGridSystem( archGrid );
      
      // Add spatial coodinates to architectural grid ... 

      archGrid.add ( sp1 );

      // ===========================================
      // Refine neighbourhood spaces ....
      // ===========================================

      System.out.println("*** In FloorplanModel.addSpaceBlock(): flag 4 ... ");

      double delta = 1.0;
      double E[] = new double[2];
      E[0] = Math.min ( dX1, dX2 ) - delta;
      E[1] = Math.min ( dY1, dY2 ) - delta;
      double F[] = new double[2];
      F[0] = Math.max ( dX1, dX2 ) + delta;
      F[1] = Math.max ( dY1, dY2 ) + delta;
      HyperPoint hpE = new HyperPoint ( E );
      HyperPoint hpF = new HyperPoint ( F );

      result = null;
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpE, hpF );
          result = ToplevelTree.intersects( hp );
      } catch (RTreeException e) {
          e.printStackTrace();
      }

      // Process neighborhood spaces ....

      System.out.println("*** In FloorplanModel.addSpaceBlock(): process neighborhood spaces ... ");
      System.out.println("*** In FloorplanModel.addSpaceBlock(): result.length = " + result.length );

     for(int i=0; i < result.length; i++) {

         temp = (Integer) result[i];
         IndexArch np = (IndexArch) spaceindexList.get( (temp.intValue()-1) );

         // Process architectural index ....

         if( np.getIndex() == IndexArch.SPACE ) {
            Space2DModel sp = (Space2DModel) np.getSpace();
            System.out.println("Refining ID: " + sp.getName() );
            sp.refine( archGrid );
            sp.getNeighbors ( archGrid );
         } else { 
	    System.out.println("ERROR!!! ");
           }
      }

      // =============================================================
      // Update workspace and hashmap storage ....
      // =============================================================

      sp1.setFilledShape( true );
      sp1.setTextOffSetX( (int) (Math.max(dX1,dX2) - Math.min(dX1,dX2))/2 - 5 );
      sp1.setTextOffSetY( (int) (Math.max(dY1,dY2) - Math.min(dY1,dY2))/2 );
      sp1.setColor( Color.orange );
      workspace.add ( sp1 );
      things.put( sp1.getName(), sp1 );
      addedKeys.add( sp1.getName() );
      
      //Add Listener
      
      Iterator<GridLineModel> iterator1 = sp1.xGridLine.iterator();
	  while ( iterator1.hasNext() != false) {
		  GridLineModel gl = (GridLineModel) iterator1.next();
		  gl.addFloorplanModelListener(sp1);
          System.out.println(gl.toString());
      }

	  Iterator<GridLineModel> iterator2 = sp1.yGridLine.iterator();
	  while ( iterator2.hasNext() != false) {
		  GridLineModel gl = (GridLineModel) iterator2.next();
		  gl.addFloorplanModelListener(sp1);
          System.out.println(gl.toString());
      }

      System.out.println("*** Leave FloorplanModel.addSpaceBlock() ... ");
   }

   // ======================================================================
   // Add support columns to the floorplan model ...
   // ======================================================================

   public void addSupportColumn( CoordinatePair pair ) {

      System.out.println("*** Enter FloorplanModel.addSupportColumn() ... ");
      System.out.println( pair.toString() );

      // Retrieve coordinates of input rectangle ...

      double dX1 = pair.getX1(); double dY1 = pair.getY1();
      double dX2 = pair.getX2(); double dY2 = pair.getY2();

      double dMinX = Math.min(dX1, dX2 );
      double dMaxX = Math.max(dX1, dX2 );
      double dMinY = Math.min(dY1, dY2 );
      double dMaxY = Math.max(dY1, dY2 );

      // Create temporary working space from input rectangle ...

      Space2DModel sp1 = new Space2DModel( dX1, dY1, dX2, dY2 );

      // Check for overlap of input rectangle and spaces stored in r-tree ...

      double A[] = new double[2];
      A[0] = Math.min ( dX1, dX2 );
      A[1] = Math.min ( dY1, dY2 );
      double B[] = new double[2];
      B[0] = Math.max ( dX1, dX2 );
      B[1] = Math.max ( dY1, dY2 );
      HyperPoint hpA = new HyperPoint ( A );
      HyperPoint hpB = new HyperPoint ( B );

      // Create references to results on top- and component-level r-trees...

      Object[] result1 = null;  // level 1 (top level)
      Object[] result2 = null;  // level 2 (component level)

      // Retrieve intersections from component-level r-tree ...

      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
          result2 = SecondlevelTree.intersects( hp );
      } catch (RTreeException e) {
          // if problems occur - in the real world you have
          // to handle the exceptions :-)
          e.printStackTrace();
      }

      // If no of intersections is greater than zero, operation cannot proceed ...
      
      if( result2.length > 0 ) {
    	  System.out.println("*** ERROR: Columns overlap!!");
    	  return;
      }

      // Find intersection of input rectangle and previously defined spaces ... 
     
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
          result1 = ToplevelTree.intersects( hp );
      } catch (RTreeException e) {
          e.printStackTrace();
      }
    	  
      System.out.println("*** In FloorplanModel.addSupportColumn(): flag 1: result1.length = " + result1.length);

      if( result1.length == 0 ){
          System.out.println("*** In FloorplanModel.addSupportColumn(): flag 2 ....");
          System.out.println("*** ERROR: Input rectangle does not intersect spaces !!");
          return;
      }

      // Find list of corner point inside the working rectangle ...

      HashSet<Coordinate> cornerPoints = new HashSet<Coordinate>();
      Integer temp;
      for(int i=0; i < result1.length; i++) {

          temp = (Integer) result1[i];
          IndexArch np = (IndexArch) spaceindexList.get( (temp.intValue()-1) );

          // Process architectural index ....

          if( np.getIndex() == IndexArch.SPACE ) {
              Space2DModel sp = (Space2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {

                  System.out.println("*** Potential interesctions with: sp = " + sp.getName() );

            	  // Walk along vertices and find corners inside working rectangle ...

                  List vertex = sp.getComponentBoundary().getVertexList();
            	  for ( int j = 1; j <= vertex.size(); j = j + 1 ) {
            	      Coordinate cp = (Coordinate) vertex.get(j-1);
                      if ( dMinX < cp.getX() && cp.getX() < dMaxX &&
                      	   dMinY < cp.getY() && cp.getY() < dMaxY ) {

                           cornerPoints.add ( new Coordinate (cp.getX(), cp.getY() ) );
                           System.out.printf("*** Add coordinate (x,y) = (%3.1f %3.1f)\n", cp.getX(), cp.getY() );
                      }
                  }
              }
          }
      }

      System.out.println("*** In FloorplanModel.addSupportColumn(): flag 2 : CornerPoints.size= " + cornerPoints.size());
      
      // ==========================================================
      // Add column to floorplan database and add item to list of
      // space-index pairs only if the number of corner points is ONE.
      // ==========================================================
      
      Column2DModel sp2 = new Column2DModel( dX1, dY1, dX2, dY2 );
      
      // Condition 1: Working rectangle does not include any corner ...
      
      if(cornerPoints.size() == 0){
    	  System.out.println("*** ERROR: Input rectangle does not include any space vertex !!");
    	  return;
      }
      
      // Condition 2: Working rectangle includes only one corner ...
      
      if(cornerPoints.size() == 1){
    	  iNoColumns = iNoColumns + 1;
    	  iNoComponents = iNoColumns + iNoCorners + iNoWalls + iNoPortals + iNoDoors + iNoWindows;
          sp2.setName ( "cl-" + iNoColumns );
          columnList.add ( sp2 );
          componentsindexList.add ( new IndexArch ( new Integer( iNoComponents ), sp2, IndexArch.COLUMN ) );
      
      // ==============================================================
      // Insert new item into r-tree database ....
      // the Integer object represents the ID for the extern objects
      // ==============================================================

          try {
              double C[] = new double[2];
              C[0] = sp2.getBoundingBox().getMinX();
              C[1] = sp2.getBoundingBox().getMinY();
              double D[] = new double[2];
              D[0] = sp2.getBoundingBox().getMaxX();
              D[1] = sp2.getBoundingBox().getMaxY();

              System.out.println( "Box:" + sp2.getName() );
              System.out.println( sp2.getBoundingBox().toString() );

              HyperPoint hpC = new HyperPoint ( C );
              HyperPoint hpD = new HyperPoint ( D );
              SecondlevelTree.insert( new Integer( iNoComponents ), new HyperBoundingBox( hpC, hpD ) );
              } catch (RTreeException e) {
        	   e.printStackTrace();  // common exception of RTree implementation
          }
      
          System.out.println("*** In FloorplanModel.addSupportColumn(): flag 3 ... ");
          
          //Add Listener
          List<Coordinate> cornerPList = new ArrayList<Coordinate>(cornerPoints);
          Coordinate vx = (Coordinate) cornerPList.get(0);
          GridLineModel xGridline = archGrid.findXGridLine(vx);
          xGridline.addFloorplanModelListener(sp2);
          System.out.println("*** In FloorplanModel.addSupportColumn(): flag 3 ... xGridLine :" + xGridline);
          GridLineModel yGridline = archGrid.findYGridLine(vx);
          yGridline.addFloorplanModelListener(sp2);
          System.out.println("*** In FloorplanModel.addSupportColumn(): flag 3 ... yGridLine :" + yGridline);
          sp2.setAssociatePoint(vx.getX(), vx.getY());
      }
      
      // Condition 3: Working rectangle includes many corners ...
      
      if(cornerPoints.size() > 1){
    	  List<Coordinate> cornerPList = new ArrayList<Coordinate>(cornerPoints);
    	  
    	  for(int i = 0; i < cornerPList.size(); i = i+1){
    		  System.out.println("***");
    		  double length = Math.min(dMaxX - dMinX, dMaxY - dMinY);
        	  Coordinate vx = (Coordinate) cornerPList.get(i);
        	  vx.toString();
        	  Column2DModel sp3 = new Column2DModel( vx.getX() - length/2, vx.getY() - length/2,
        			  vx.getX() + length/2, vx.getY() + length/2 );
        	  sp3.setAssociatePoint(vx.getX(), vx.getY());
    		  iNoColumns = iNoColumns + 1;
    		  iNoComponents = iNoColumns + iNoCorners + iNoWalls + iNoPortals + iNoDoors + iNoWindows;
              sp3.setName ( "cl-" + iNoColumns );
              columnList.add ( sp3 );
              componentsindexList.add ( new IndexArch ( new Integer( iNoComponents ), sp3, IndexArch.COLUMN ) );
          
      // ==============================================================
      // Insert new item into r-tree database ....
      // the Integer object represents the ID for the extern objects
      // ==============================================================

              try {
                  double C[] = new double[2];
                  C[0] = sp3.getBoundingBox().getMinX();
                  C[1] = sp3.getBoundingBox().getMinY();
                  double D[] = new double[2];
                  D[0] = sp3.getBoundingBox().getMaxX();
                  D[1] = sp3.getBoundingBox().getMaxY();

                  System.out.println( "Box:" + sp3.getName() );
                  System.out.println( sp3.getBoundingBox().toString() );

                  HyperPoint hpC = new HyperPoint ( C );
                  HyperPoint hpD = new HyperPoint ( D );
                  SecondlevelTree.insert( new Integer( iNoComponents ), new HyperBoundingBox( hpC, hpD ) );
                  } catch (RTreeException e) {
            	   e.printStackTrace();  // common exception of RTree implementation
              }
          
              System.out.println("*** In FloorplanModel.addSupportColumn(): flag 3 ... ");
              
              //Add Listener
              
              GridLineModel xGridline = archGrid.findXGridLine(vx);
              xGridline.addFloorplanModelListener(sp3);
              
              GridLineModel yGridline = archGrid.findYGridLine(vx);
              yGridline.addFloorplanModelListener(sp3);
          
    	  }
      }
      
      
      
      System.out.println("*** In FloorplanModel.addSupportColumn(): flag 4 ... ");
      
      double delta = 1.0;
      double E[] = new double[2];
      E[0] = Math.min ( dX1, dX2 ) - delta;
      E[1] = Math.min ( dY1, dY2 ) - delta;
      double F[] = new double[2];
      F[0] = Math.max ( dX1, dX2 ) + delta;
      F[1] = Math.max ( dY1, dY2 ) + delta;
      HyperPoint hpE = new HyperPoint ( E );
      HyperPoint hpF = new HyperPoint ( F );

      result2 = null;
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpE, hpF );
          result2 = SecondlevelTree.intersects( hp );
      } catch (RTreeException e) {
          e.printStackTrace();
      }
      
      System.out.println("*** In FloorplanModel.addSupportColumn(): result2.length = " + result2.length );

      for(int i=0; i < result2.length; i++) {

         temp = (Integer) result2[i];
         IndexArch np = (IndexArch) componentsindexList.get( (temp.intValue()-1) );

         // Process architectural index ....

         if( np.getIndex() == IndexArch.COLUMN ) {
            Column2DModel sp = (Column2DModel) np.getSpace();
            System.out.println("Defining ID: " + sp.getName() );
            sp.define();
            
         // =============================================================
         // Update workspace and hashmap storage ....
         // =============================================================

            sp.setFilledShape( true );
            sp.setTextOffSetX( (int) (sp.width/2) - 5 );
            sp.setTextOffSetY( (int) (sp.height/2) );
            sp.setColor( Color.green );

            workspace.add ( sp );
            things.put( sp.getName(), sp );
            addedKeys.add( sp.getName() );
            
         } else { 
	    System.out.println("ERROR!!! ");
           }
      }

      System.out.println("*** Leave FloorplanModel.addSupportColumn() ... ");
   }

   // ======================================================================
   // Add Interior wall to the floorplan model ...
   // ======================================================================

   public void addWallCorner( CoordinatePair pair ) {

      System.out.println("*** Enter FloorplanModel.addWallCorner() ... ");
      System.out.println( pair.toString() );

      // Retrieve coordinates of input rectangle ...

      double dX1 = pair.getX1(); double dY1 = pair.getY1();
      double dX2 = pair.getX2(); double dY2 = pair.getY2();

      double dMinX = Math.min(dX1, dX2 );
      double dMaxX = Math.max(dX1, dX2 );
      double dMinY = Math.min(dY1, dY2 );
      double dMaxY = Math.max(dY1, dY2 );

      // Create temporary working space from input rectangle ...

      Space2DModel sp1 = new Space2DModel( dX1, dY1, dX2, dY2 );

      // Check for overlap of input rectangle and spaces stored in r-tree ...

      double A[] = new double[2];
      A[0] = Math.min ( dX1, dX2 );
      A[1] = Math.min ( dY1, dY2 );
      double B[] = new double[2];
      B[0] = Math.max ( dX1, dX2 );
      B[1] = Math.max ( dY1, dY2 );
      HyperPoint hpA = new HyperPoint ( A );
      HyperPoint hpB = new HyperPoint ( B );
      
      Object[] result1 = null;
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
          result1 = ToplevelTree.intersects( hp );
      } catch (RTreeException e) {
          // if problems occur - in the real world you have
          // to handle the exceptions :-)
          e.printStackTrace();
      }

      System.out.println("*** In FloorplanModel.addWallCorner(): flag 1: result1.length = " + result1.length);

      // Check for corners that include ...

      Integer temp;
      int count = 0;
      Coordinate cptemp;
      Set<Coordinate> cpset = new HashSet<Coordinate>();
      for(int i=0; i < result1.length; i++) {

          temp = (Integer) result1[i];
          IndexArch np = (IndexArch) spaceindexList.get( (temp.intValue()-1) );

          // Process architectural index ....

          if( np.getIndex() == IndexArch.SPACE ) {
              Space2DModel sp = (Space2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** Potential interesction with: sp = " + sp.getName() );

                  // Walk along vertices and find two corners inside working rectangle ...
                  
                  List vertex = sp.getComponentBoundary().getVertexList();
                  for ( int j = 1; j <= vertex.size(); j = j + 1 ) {
                        Coordinate cp = (Coordinate) vertex.get(j-1);
                        if ( dMinX < cp.getX() && cp.getX() < dMaxX &&
                             dMinY < cp.getY() && cp.getY() < dMaxY ) {
                        	count = count + 1;
                        	System.out.printf("*** Potential corner point %2d at (x,y) = (%3.1f %3.1f)\n", 
                        			count, cp.getX(), cp.getY() );
                        	cptemp = new Coordinate(cp.getX(), cp.getY());
                        	cpset.add(cptemp);
                        }
                  }
              }
          } else { 
              System.out.println("ERROR!!! ");
          }
      }

      if(cpset.size() < 1){
    	  System.out.println("*** ERROR no vertex include");
    	  return;
      }
      
      System.out.println("*** In FloorplanModel.addWallCorner(): flag 2 ... ");
      
      // Check for overlap of columns ...
      
      Object[] result2 = null;
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
          result2 = SecondlevelTree.intersects( hp );
      } catch (RTreeException e) {
          // if problems occur - in the real world you have
          // to handle the exceptions :-)
          e.printStackTrace();
      }
      
      System.out.println("*** In FloorplanModel.addWallCorner(): flag 2: result2.length = " + result2.length);
      
      Integer temp1;
      for(int i=0; i < result2.length; i++) {

          temp1 = (Integer) result2[i];
          IndexArch np = (IndexArch) componentsindexList.get( (temp1.intValue()-1) );

          // Process column index ....

          if( np.getIndex() == IndexArch.COLUMN ) {
              Column2DModel sp = (Column2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** Potential interesction with: Column = " + sp.getName() );

                  // Walk along vertices of working rectangle and find two corners inside column ...
                  List<Coordinate> cpcontain = new ArrayList<Coordinate>();
                  List vertex = sp1.getComponentBoundary().getVertexList();
                  for ( int j = 1; j <= vertex.size(); j = j + 1 ) {
                        Coordinate cp = (Coordinate) vertex.get(j-1);
                        double crMinX = sp.getBoundingBox().getMinX();
                        double crMinY = sp.getBoundingBox().getMinY();
                        double crMaxX = sp.getBoundingBox().getMaxX();
                        double crMaxY = sp.getBoundingBox().getMaxY();
                        if ( crMinX <= cp.getX() && crMinY <= cp.getY() && crMaxX >= cp.getX() && crMaxY >= cp.getY() ) {
                        	cpcontain.add(cp);
                        	System.out.printf("*** working rectangle point at (x,y) = (%3.1f %3.1f) is inside Column\n", 
                        			cp.getX(), cp.getY() );
                        }
                  }
                  
                  // If two corners of a working rectangle are inside a column, delete the corner vertex inside the list
                  
                  List<Coordinate> cplist = new ArrayList<Coordinate>(cpset);
                  for( int j =1; j<=cplist.size(); j = j + 1){
                	  Coordinate cp = (Coordinate) cplist.get(j-1);
                      boolean Contain = true;
                      Contain = sp.getComponentBoundary().contains(cp.getX(), cp.getY());
                      if ( Contain == true ) {
                    	cptemp = new Coordinate(cp.getX(), cp.getY());
                      	cpset.remove(cptemp);;
                      	System.out.printf("*** Corner at vertex point (x,y) = (%3.1f %3.1f) is inside Column\n", 
                      			cp.getX(), cp.getY() );
                      }
                  }
                  
                  // If there is only one working rectangle corner inside a column, the working rectangle might be off-side 
                  
                  if(cpcontain.size() <= 1){
                	  System.out.println("*** ERROR only one corner inside Column " + sp.getName() );
                	  return;
                  }
              }
          }
          
          // Process corner index ....
          
          if(np.getIndex() == IndexArch.CORNER){
        	  Corner2DModel sp = (Corner2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** Potential interesction with: Corner = " + sp.getName() );

                  // Walk along vertices and find two corners of working rectangle inside corner ...
                  List<Coordinate> cpcontain = new ArrayList<Coordinate>();
                  List vertex = sp1.getComponentBoundary().getVertexList();
                  for ( int j = 1; j <= vertex.size(); j = j + 1 ) {
                        Coordinate cp = (Coordinate) vertex.get(j-1);
                        double crMinX = sp.getBoundingBox().getMinX();
                        double crMinY = sp.getBoundingBox().getMinY();
                        double crMaxX = sp.getBoundingBox().getMaxX();
                        double crMaxY = sp.getBoundingBox().getMaxY();
                        if(crMinX <= cp.getX() && crMinY <= cp.getY() && crMaxX >= cp.getX() && crMaxY >= cp.getY()){
                        	cpcontain.add(cp);
                        	System.out.printf("*** working rectangle point at (x,y) = (%3.1f %3.1f) is inside Corner\n", 
                        			cp.getX(), cp.getY() );
                        }
                  }
                  
                  // If two corners of a working rectangle are inside a corner, delete the corner vertex inside the list
                  
                  List<Coordinate> cplist = new ArrayList<Coordinate>(cpset);
                  for( int j =1; j<=cplist.size(); j = j + 1){
                	  Coordinate cp = (Coordinate) cplist.get(j-1);
                      boolean Contain = true;
                      Contain = sp.getComponentBoundary().contains(cp.getX(), cp.getY());
                      if ( Contain == true ) {
                    	cptemp = new Coordinate(cp.getX(), cp.getY());
                      	cpset.remove(cptemp);;
                      	System.out.printf("*** Corner at vertex point (x,y) = (%3.1f %3.1f) is inside Corner\n", 
                      			cp.getX(), cp.getY() );
                      }
                  }
                  
                  // If there is only one working rectangle corner inside a corner, the working rectangle might be off-side 
                  
                  if(cpcontain.size() <= 1){
                	  System.out.println("*** ERROR only one working rectangle corner inside Corner " + sp.getName() );
                	  return;
                  }
              }
          }      
      }
      
      System.out.println("*** In FloorplanModel.addWallCorner(): flag 3 ... ");
      
      // ==========================================================
      // Create corners ...
      // Add corner to floorplan database ....
      // Add item to list of corner-index pairs ...
      // ==========================================================
      
      List<Coordinate> cplist = new ArrayList<Coordinate>(cpset);
      
      for( int k = 1; k <= cplist.size(); k = k + 1 ){
    	  double length = Math.min(dMaxX - dMinX, dMaxY - dMinY);
    	  Coordinate vx = (Coordinate) cplist.get(k-1);
    	  vx.toString();
    	  Corner2DModel sp2 = new Corner2DModel( vx.getX() - length/2, vx.getY() - length/2,
    			  vx.getX() + length/2, vx.getY() + length/2 );
    	  // Set Associate Point
    	  sp2.setAssociatePoint(vx.getX(), vx.getY());
    	  iNoCorners = iNoCorners + 1;
    	  iNoComponents = iNoColumns + iNoCorners + iNoWalls + iNoPortals + iNoDoors + iNoWindows;
          sp2.setName ( "cr-" + iNoCorners );
          cornerList.add ( sp2 );
          componentsindexList.add ( new IndexArch ( new Integer( iNoComponents ), sp2, IndexArch.CORNER ) );
          
          // ==============================================================
          // Insert new item into r-tree database ....
          // the Integer object represents the ID for the extern objects
          // ==============================================================
          
          try {
        	  double C[] = new double[2];
              C[0] = sp2.getBoundingBox().getMinX();
              C[1] = sp2.getBoundingBox().getMinY();
              double D[] = new double[2];
              D[0] = sp2.getBoundingBox().getMaxX();
              D[1] = sp2.getBoundingBox().getMaxY();

              System.out.println( "Corner:" + sp2.getName() );
              System.out.println( sp2.getBoundingBox().toString() );

              HyperPoint hpC = new HyperPoint ( C );
              HyperPoint hpD = new HyperPoint ( D );
              SecondlevelTree.insert( new Integer( iNoComponents ), new HyperBoundingBox( hpC, hpD ) );
              sp2.setHyperBoundingBox( new HyperBoundingBox( hpC, hpD ) );
              sp2.setNoComponent( iNoComponents );
          } catch (RTreeException e) {
              e.printStackTrace();  // common exception of RTree implementation
          }
          
          sp2.define();
          System.out.println("Defining ID: " + sp2.getName() );
      
          // =============================================================
          // Update workspace and hashmap storage ....
          // =============================================================

          sp2.setFilledShape( true );
          sp2.setTextOffSetX( (int) (sp2.width/2) - 5 );
          sp2.setTextOffSetY( (int) (sp2.height/2) );
          sp2.setColor( Color.blue );

          workspace.add ( sp2 );
          things.put( sp2.getName(), sp2 );
          addedKeys.add( sp2.getName() );
          
          //Add Listener
          
          GridLineModel xGridline = archGrid.findXGridLine(vx);
          xGridline.addFloorplanModelListener(sp2);
          
          GridLineModel yGridline = archGrid.findYGridLine(vx);
          yGridline.addFloorplanModelListener(sp2);
      }
      
      System.out.println("*** Leave FloorplanModel.addWallCorner() ... ");
   }

   // ======================================================================
   // Add Exterior wall to the floorplan model ...
   // ======================================================================

   public void addExteriorWall( CoordinatePair pair ) {

      System.out.println("*** Enter FloorplanModel.addExteriorWall() ... ");
      System.out.println( pair.toString() );

      // Retrieve coordinates of input rectangle ...

      double dX1 = pair.getX1(); double dY1 = pair.getY1();
      double dX2 = pair.getX2(); double dY2 = pair.getY2();

      double dMinX = Math.min(dX1, dX2 );
      double dMaxX = Math.max(dX1, dX2 );
      double dMinY = Math.min(dY1, dY2 );
      double dMaxY = Math.max(dY1, dY2 );

      // Create temporary working space from input rectangle ...

      Space2DModel sp1 = new Space2DModel( dX1, dY1, dX2, dY2 );

      // Check for overlap of input rectangle and spaces stored in r-tree ...

      double A[] = new double[2];
      A[0] = Math.min ( dX1, dX2 );
      A[1] = Math.min ( dY1, dY2 );
      double B[] = new double[2];
      B[0] = Math.max ( dX1, dX2 );
      B[1] = Math.max ( dY1, dY2 );
      HyperPoint hpA = new HyperPoint ( A );
      HyperPoint hpB = new HyperPoint ( B );
      
      Object[] result1 = null;
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
          result1 = ToplevelTree.intersects( hp );
      } catch (RTreeException e) {
          // if problems occur - in the real world you have
          // to handle the exceptions :-)
          e.printStackTrace();
      }

      System.out.println("*** In FloorplanModel.addExteriorWall(): flag 1: result1.length = " + result1.length);

      Set<Edge> EdgeInR = new HashSet<Edge>();
      
      // Add Exterior wall to space block corners inside working rectangle ...

      Integer temp1;
      for(int i=0; i < result1.length; i++) {

          temp1 = (Integer) result1[i];
          IndexArch np = (IndexArch) spaceindexList.get( (temp1.intValue()-1) );

          // Process architectural index ....

          if( np.getIndex() == IndexArch.SPACE ) {
              Space2DModel sp = (Space2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** Potential interesction with: sp = " + sp.getName() );

                  // Walk along edges and find edges inside working rectangle ...

                  List edge = sp.getComponentBoundary().getEdgeList();
                  for ( int j = 1; j <= edge.size(); j = j + 1 ) {
                	  Edge ed = (Edge) edge.get(j-1);
                      Coordinate cp1 = (Coordinate) ed.getVertex1();
                      Coordinate cp2 = (Coordinate) ed.getVertex2();
                        if ( dMinX < cp1.getX() && cp1.getX() < dMaxX &&
                             dMinY < cp1.getY() && cp1.getY() < dMaxY &&
                             dMinX < cp2.getX() && cp2.getX() < dMaxX &&
                             dMinY < cp2.getY() && cp2.getY() < dMaxY) {
                        	 EdgeInR.add(ed);
                             System.out.println("*** Edge " + ed.getName() + " of " + sp.getName() + " is insde working rectangle");
                             //System.out.println(ed.toString());
                             
                        }
                  }
              } 
          }
      }
      
      System.out.println("*** In FloorplanModel.addExteriorWall(): flag 2 ... ");
      
      // Create a Hyper Bounding Box for each edge that included in the working rectangle ...
      System.out.println("*** SIZE " + EdgeInR.size());
      List<Edge> EdgeInRList = new ArrayList<Edge>(EdgeInR);
      
      for(int i=0; i < EdgeInRList.size(); i++){
    	  Edge ed = (Edge) EdgeInRList.get(i);
    	  
    	  double C[] = new double[2];
          C[0] = Math.min ( ed.getVertex1().getX(), ed.getVertex2().getX() );
          C[1] = Math.min ( ed.getVertex1().getY(), ed.getVertex2().getY() );
          double D[] = new double[2];
          D[0] = Math.max ( ed.getVertex1().getX(), ed.getVertex2().getX() );
          D[1] = Math.max ( ed.getVertex1().getY(), ed.getVertex2().getY() );
          HyperPoint hpC = new HyperPoint ( C );
          HyperPoint hpD = new HyperPoint ( D );
      
          Object[] result2 = null;
          try {
        	  HyperBoundingBox hp = new HyperBoundingBox( hpC, hpD );
              result2 = SecondlevelTree.intersects( hp );
          } catch (RTreeException e) {
          // if problems occur - in the real world you have
          // to handle the exceptions :-)
              e.printStackTrace();
          }
      
          System.out.println("*** In FloorplanModel.addExteriorWall(): flag 2 <Edge " + i + " > : result2.length = " + result2.length);
          
          // Find the Max X, Max Y, Min X, and Min Y for each edge ...
          
          /*if(result2.length <=1){
        	  System.out.println("*** Can not create wall");
          }
          Need to add a way to jump out of loop*/
          
          double edMinX = 0, edMaxX = 0,  edMinY = 0,  edMaxY = 0;
          if(ed.getVertex1().getX() > ed.getVertex2().getX()){
    	      edMaxX = ed.getVertex1().getX();
    	      edMinX = ed.getVertex2().getX();
          }
          if(ed.getVertex1().getX() < ed.getVertex2().getX()){
    	      edMinX = ed.getVertex1().getX();
    	      edMaxX = ed.getVertex2().getX();
          }
          if(ed.getVertex1().getY() > ed.getVertex2().getY()){
    	      edMaxY = ed.getVertex1().getY();
    	      edMinY = ed.getVertex2().getY();
          }
          if(ed.getVertex1().getY() < ed.getVertex2().getY()){
    	      edMinY = ed.getVertex1().getY();
    	      edMaxY = ed.getVertex2().getY();
          }
      
          double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
          
          // Find the two items that intersected with the edge ...
      
          Integer temp2 = (Integer) result2[0];
          Integer temp3 = (Integer) result2[1];
          IndexArch np1 = (IndexArch) componentsindexList.get( (temp2.intValue()-1) );
          IndexArch np2 = (IndexArch) componentsindexList.get( (temp3.intValue()-1) );
          

          // Insure the intersected component is not wall ...

          if( np1.getIndex() == IndexArch.WALL ) {
              Wall2DModel sp = (Wall2DModel) np1.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** Potential interesction with: WALL = " + sp.getName() + "this is a bug");
              }
              temp2 = (Integer) result2[1];
              temp3 = (Integer) result2[2];
              np1 = (IndexArch) componentsindexList.get( (temp2.intValue()-1) );
              np2 = (IndexArch) componentsindexList.get( (temp3.intValue()-1) );
          }
          
          if( np2.getIndex() == IndexArch.WALL ) {
              Wall2DModel sp = (Wall2DModel) np2.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** Potential interesction with: WALL = " + sp.getName() + "this is a bug");
              }
              temp2 = (Integer) result2[0];
              temp3 = (Integer) result2[2];
              np1 = (IndexArch) componentsindexList.get( (temp2.intValue()-1) );
              np2 = (IndexArch) componentsindexList.get( (temp3.intValue()-1) );
          }

          // Check the edge to see if it's Horizontal or Vertical ...
          
          if(ed.getVertex1().getY() == ed.getVertex2().getY()){
        	  
        	  if(np1.getIndex() == IndexArch.COLUMN){
        		  Column2DModel spone = (Column2DModel) np1.getSpace();
        	      System.out.println("Component: " + spone.getName());
        	      if(edMinX < spone.getBoundingBox().getMinX() && spone.getBoundingBox().getMinX() < edMaxX){
        		      x2 = spone.getBoundingBox().getMinX();
        	      }
        	      if(edMinX < spone.getBoundingBox().getMaxX() && spone.getBoundingBox().getMaxX() < edMaxX){
        		      x1 = spone.getBoundingBox().getMaxX();
        	      }
        	  
        	      if(np2.getIndex() == IndexArch.COLUMN){
        		      Column2DModel sptwo = (Column2DModel) np2.getSpace();
        		      System.out.println("Component: " + sptwo.getName());
        		      if(edMinX < sptwo.getBoundingBox().getMinX() && sptwo.getBoundingBox().getMinX() < edMaxX){
        			      x2 = sptwo.getBoundingBox().getMinX();
        		      }
        		      if(edMinX < sptwo.getBoundingBox().getMaxX() && sptwo.getBoundingBox().getMaxX() < edMaxX){
        		          x1 = sptwo.getBoundingBox().getMaxX();
        	          }
        		      double dy1 = spone.getBoundingBox().getMaxY() - spone.getBoundingBox().getMinY();
                	  double dy2 = sptwo.getBoundingBox().getMaxY() - sptwo.getBoundingBox().getMinY();
                	  double dy = Math.min(dy1, dy2);
                	  y1 = ed.getVertex1().getY() - dy / 2;
                	  y2 = ed.getVertex1().getY() + dy / 2;
        	      }
        	      if(np2.getIndex() == IndexArch.CORNER){
        		      Corner2DModel sptwo = (Corner2DModel) np2.getSpace();
            	      System.out.println("Component: " + sptwo.getName());
            	      if(edMinX < sptwo.getBoundingBox().getMinX() && sptwo.getBoundingBox().getMinX() < edMaxX){
            		      x2 = sptwo.getBoundingBox().getMinX();
            	      }
            	      if(edMinX < sptwo.getBoundingBox().getMaxX() && sptwo.getBoundingBox().getMaxX() < edMaxX){
            		      x1 = sptwo.getBoundingBox().getMaxX();
            	      }
            	      double dy1 = spone.getBoundingBox().getMaxY() - spone.getBoundingBox().getMinY();
                	  double dy2 = sptwo.getBoundingBox().getMaxY() - sptwo.getBoundingBox().getMinY();
                	  double dy = Math.min(dy1, dy2);
                	  y1 = ed.getVertex1().getY() - dy / 2;
                	  y2 = ed.getVertex1().getY() + dy / 2;
        	      }
        	  }
        	  
        	  if(np1.getIndex() == IndexArch.CORNER){
        		  Corner2DModel spone = (Corner2DModel) np1.getSpace();
        	      System.out.println("Component: " + spone.getName());
        	      if(edMinX < spone.getBoundingBox().getMinX() && spone.getBoundingBox().getMinX() < edMaxX){
        		      x2 = spone.getBoundingBox().getMinX();
        	      }
        	      if(edMinX < spone.getBoundingBox().getMaxX() && spone.getBoundingBox().getMaxX() < edMaxX){
        		      x1 = spone.getBoundingBox().getMaxX();
        	      }
        	  
        	      if(np2.getIndex() == IndexArch.COLUMN){
        		      Column2DModel sptwo = (Column2DModel) np2.getSpace();
        		      System.out.println("Component: " + sptwo.getName());
        		      if(edMinX < sptwo.getBoundingBox().getMinX() && sptwo.getBoundingBox().getMinX() < edMaxX){
        			      x2 = sptwo.getBoundingBox().getMinX();
        		      }
        		      if(edMinX < sptwo.getBoundingBox().getMaxX() && sptwo.getBoundingBox().getMaxX() < edMaxX){
        		          x1 = sptwo.getBoundingBox().getMaxX();
        	          }
        		      double dy1 = spone.getBoundingBox().getMaxY() - spone.getBoundingBox().getMinY();
                	  double dy2 = sptwo.getBoundingBox().getMaxY() - sptwo.getBoundingBox().getMinY();
                	  double dy = Math.min(dy1, dy2);
                	  y1 = ed.getVertex1().getY() - dy / 2;
                	  y2 = ed.getVertex1().getY() + dy / 2;
        	      }
        	      if(np2.getIndex() == IndexArch.CORNER){
        		      Corner2DModel sptwo = (Corner2DModel) np2.getSpace();
            	      System.out.println("Component: " + sptwo.getName());
            	      if(edMinX < sptwo.getBoundingBox().getMinX() && sptwo.getBoundingBox().getMinX() < edMaxX){
            		      x2 = sptwo.getBoundingBox().getMinX();
            	      }
            	      if(edMinX < sptwo.getBoundingBox().getMaxX() && sptwo.getBoundingBox().getMaxX() < edMaxX){
            		      x1 = sptwo.getBoundingBox().getMaxX();
            	      }
            	      double dy1 = spone.getBoundingBox().getMaxY() - spone.getBoundingBox().getMinY();
                	  double dy2 = sptwo.getBoundingBox().getMaxY() - sptwo.getBoundingBox().getMinY();
                	  double dy = Math.min(dy1, dy2);
                	  y1 = ed.getVertex1().getY() - dy / 2;
                	  y2 = ed.getVertex1().getY() + dy / 2;
        	      }
        	  } 	  
          }
          
          if(ed.getVertex1().getX() == ed.getVertex2().getX()){
        	  
        	  if(np1.getIndex() == IndexArch.COLUMN){
        		  Column2DModel spone = (Column2DModel) np1.getSpace();
        		  System.out.println("Component: " + spone.getName());
        		  if(edMinY < spone.getBoundingBox().getMinY() && spone.getBoundingBox().getMinY() < edMaxY){
        			  y2 = spone.getBoundingBox().getMinY();
        	      }
        	      if(edMinY < spone.getBoundingBox().getMaxY() && spone.getBoundingBox().getMaxY() < edMaxY){
        		      y1 = spone.getBoundingBox().getMaxY();
        		  }
        	      
        	      if(np2.getIndex() == IndexArch.COLUMN){
        	    	  Column2DModel sptwo = (Column2DModel) np2.getSpace();
        	          System.out.println("Component: " + sptwo.getName());
        	          if(edMinY < sptwo.getBoundingBox().getMinY() && sptwo.getBoundingBox().getMinY() < edMaxY){
        	        	  y2 = sptwo.getBoundingBox().getMinY();
        	          }
        	          if(edMinY < sptwo.getBoundingBox().getMaxY() && sptwo.getBoundingBox().getMaxY() < edMaxY){
        		          y1 = sptwo.getBoundingBox().getMaxY();
        	          }
        	          double dx1 = spone.getBoundingBox().getMaxX() - spone.getBoundingBox().getMinX();
        	          double dx2 = sptwo.getBoundingBox().getMaxX() - sptwo.getBoundingBox().getMinX();
        	          double dx = Math.min(dx1, dx2);
        	          x1 = ed.getVertex1().getX() - dx / 2;
        	          x2 = ed.getVertex1().getX() + dx / 2;
        	      }
        	      if(np2.getIndex() == IndexArch.CORNER){
        	    	  Corner2DModel sptwo = (Corner2DModel) np2.getSpace();
        	    	  System.out.println("Component: " + sptwo.getName());
            	      if(edMinY < sptwo.getBoundingBox().getMinY() && sptwo.getBoundingBox().getMinY() < edMaxY){
            		      y2 = sptwo.getBoundingBox().getMinY();
            	      }
            	      if(edMinY < sptwo.getBoundingBox().getMaxY() && sptwo.getBoundingBox().getMaxY() < edMaxY){
            		      y1 = sptwo.getBoundingBox().getMaxY();
            	      }
            	  
            	      double dx1 = spone.getBoundingBox().getMaxX() - spone.getBoundingBox().getMinX();
            	      double dx2 = sptwo.getBoundingBox().getMaxX() - sptwo.getBoundingBox().getMinX();
            	      double dx = Math.min(dx1, dx2);
            	      x1 = ed.getVertex1().getX() - dx / 2;
            	      x2 = ed.getVertex1().getX() + dx / 2;
            	  }
        	  }
        	  
        	  if(np1.getIndex() == IndexArch.CORNER){
        		  Corner2DModel spone = (Corner2DModel) np1.getSpace();
        		  System.out.println("Component: " + spone.getName());
        		  if(edMinY < spone.getBoundingBox().getMinY() && spone.getBoundingBox().getMinY() < edMaxY){
        			  y2 = spone.getBoundingBox().getMinY();
        	      }
        	      if(edMinY < spone.getBoundingBox().getMaxY() && spone.getBoundingBox().getMaxY() < edMaxY){
        		      y1 = spone.getBoundingBox().getMaxY();
        		  }
        	      
        	      if(np2.getIndex() == IndexArch.COLUMN){
        	    	  Column2DModel sptwo = (Column2DModel) np2.getSpace();
        	          System.out.println("Component: " + sptwo.getName());
        	          if(edMinY < sptwo.getBoundingBox().getMinY() && sptwo.getBoundingBox().getMinY() < edMaxY){
        	        	  y2 = sptwo.getBoundingBox().getMinY();
        	          }
        	          if(edMinY < sptwo.getBoundingBox().getMaxY() && sptwo.getBoundingBox().getMaxY() < edMaxY){
        		          y1 = sptwo.getBoundingBox().getMaxY();
        	          }
        	          double dx1 = spone.getBoundingBox().getMaxX() - spone.getBoundingBox().getMinX();
        	          double dx2 = sptwo.getBoundingBox().getMaxX() - sptwo.getBoundingBox().getMinX();
        	          double dx = Math.min(dx1, dx2);
        	          x1 = ed.getVertex1().getX() - dx / 2;
        	          x2 = ed.getVertex1().getX() + dx / 2;
        	      }
        	      if(np2.getIndex() == IndexArch.CORNER){
        	    	  Corner2DModel sptwo = (Corner2DModel) np2.getSpace();
        	    	  System.out.println("Component: " + sptwo.getName());
            	      if(edMinY < sptwo.getBoundingBox().getMinY() && sptwo.getBoundingBox().getMinY() < edMaxY){
            		      y2 = sptwo.getBoundingBox().getMinY();
            	      }
            	      if(edMinY < sptwo.getBoundingBox().getMaxY() && sptwo.getBoundingBox().getMaxY() < edMaxY){
            		      y1 = sptwo.getBoundingBox().getMaxY();
            	      }
            	  
            	      double dx1 = spone.getBoundingBox().getMaxX() - spone.getBoundingBox().getMinX();
            	      double dx2 = sptwo.getBoundingBox().getMaxX() - sptwo.getBoundingBox().getMinX();
            	      double dx = Math.min(dx1, dx2);
            	      x1 = ed.getVertex1().getX() - dx / 2;
            	      x2 = ed.getVertex1().getX() + dx / 2;
            	  }
        	  }
          }
          
          Wall2DModel sp2 = new Wall2DModel(x1, y1, x2, y2);
          //Set Associate Point
          sp2.setAssociatePoint(sp2.getComponentBoundary().getCentroidX(), sp2.getComponentBoundary().getCentroidY());
          
          
          //Add Listener
          if( np1.getSpace() instanceof Corner2DModel)
          {
        	  sp2.associateCorner.add((Corner2DModel) np1.getSpace());
        	  ((Corner2DModel) np1.getSpace()).addFloorplanModelListener(sp2);
          }
          if( np2.getSpace() instanceof Corner2DModel)
          {
        	  sp2.associateCorner.add((Corner2DModel) np2.getSpace());
        	  ((Corner2DModel) np2.getSpace()).addFloorplanModelListener(sp2);
          }
          if( np1.getSpace() instanceof Column2DModel)
        	  sp2.associateColumn.add((Column2DModel) np1.getSpace() );
          if( np2.getSpace() instanceof Column2DModel)
        	  sp2.associateColumn.add((Column2DModel) np2.getSpace());
        	  
          
          iNoWalls = iNoWalls + 1;
          iNoComponents = iNoColumns + iNoCorners + iNoWalls + iNoPortals + iNoDoors + iNoWindows;
          sp2.setName ( "wall-" + iNoWalls );
          wallList.add ( sp2 );
          componentsindexList.add ( new IndexArch ( new Integer( iNoComponents ), sp2, IndexArch.WALL ) );

    	  
          // ==============================================================
          // Insert new item into r-tree database ....
          // the Integer object represents the ID for the extern objects
          // ==============================================================
          
          try {
              double E[] = new double[2];
              E[0] = sp2.getBoundingBox().getMinX();
              E[1] = sp2.getBoundingBox().getMinY();
              double F[] = new double[2];
              F[0] = sp2.getBoundingBox().getMaxX();
              F[1] = sp2.getBoundingBox().getMaxY();

              System.out.println( "Wall:" + sp2.getName() );
              System.out.println( sp2.getBoundingBox().toString() );

              HyperPoint hpE = new HyperPoint ( E );
              HyperPoint hpF = new HyperPoint ( F );
              SecondlevelTree.insert( new Integer( iNoComponents ), new HyperBoundingBox( hpE, hpF ) );
              sp2.setHyperBoundingBox( new HyperBoundingBox( hpE, hpF ) );
              sp2.setNoComponent( iNoComponents );
          } catch (RTreeException e) {
              e.printStackTrace();  // common exception of RTree implementation
          }
  
      }
      
      double delta = 1.0;
      double E[] = new double[2];
      E[0] = Math.min ( dX1, dX2 ) - delta;
      E[1] = Math.min ( dY1, dY2 ) - delta;
      double F[] = new double[2];
      F[0] = Math.max ( dX1, dX2 ) + delta;
      F[1] = Math.max ( dY1, dY2 ) + delta;
      HyperPoint hpE = new HyperPoint ( E );
      HyperPoint hpF = new HyperPoint ( F );

      Object[] result2 = null;
      result2 = null;
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpE, hpF );
          result2 = SecondlevelTree.intersects( hp );
      } catch (RTreeException e) {
          e.printStackTrace();
      }
      
      System.out.println("*** In FloorplanModel.addExteriorWall(): flag 4: result2.length = " + result2.length );

      Integer temp;
      for(int i=0; i < result2.length; i++) {

         temp = (Integer) result2[i];
         IndexArch np = (IndexArch) componentsindexList.get( (temp.intValue()-1) );

         // Process architectural index ....

         if( np.getIndex() == IndexArch.WALL ) {
            Wall2DModel sp = (Wall2DModel) np.getSpace();
            System.out.println("Refining ID: " + sp.getName() );
            
            boolean duplicate = false;
            for( Feature f : workspace.getChildren() )
            {
            	if( f instanceof Wall2DModel )
            	{
            		if( f.getName().equals( sp.getName() ) )
            			duplicate = true;
            	}
            }
            if( duplicate )
            	continue;
            
            sp.define();
            
            // =============================================================
            // Update workspace and hashmap storage ....
            // =============================================================

            sp.setFilledShape( true );
            sp.setTextOffSetX( (int) (Math.abs(sp.getX() - (sp.getX() + sp.height))/2) - 5 );
            sp.setTextOffSetY( (int) (Math.abs(sp.getY() - (sp.getY() + sp.width))/2) );
            sp.setColor( Color.blue );
            
            //Set the composite hierarchy location based on the walls' location
            sp.getCompositeHierarchy().setxOffset( sp.getX() );
            sp.getCompositeHierarchy().setyOffset( sp.getY() );
            sp.getCompositeHierarchy().setrotation( 0.0 );
            System.out.println( "FloorplanModel.addExteriorWall ....ch(x,y) = ( "
					+ sp.getCompositeHierarchy().getX() + ", " + sp.getCompositeHierarchy().getY() + " )" );
            
            chMap.put( sp.getName(), sp.getCompositeHierarchy() );
            sp.addChangeListener( listener );
            
            workspace.add ( sp );
            things.put( sp.getName(), sp );
            addedKeys.add( sp.getName() );
            
            workspace.add( sp.getCompositeHierarchy() );
         }
      }

      System.out.println("*** Leave FloorplanModel.addExteriorWall() ... ");
   }
   
   // ======================================================================
   // Add Portal to the floorplan model ...
   // ======================================================================

   public void addPortal( CoordinatePair pair ) {

      System.out.println("*** Enter FloorplanModel.addPortal() ... ");
      System.out.println( pair.toString() );

      // Retrieve coordinates of input rectangle ...

      double dX1 = pair.getX1(); double dY1 = pair.getY1();
      double dX2 = pair.getX2(); double dY2 = pair.getY2();

      double dMinX = Math.min(dX1, dX2 );
      double dMaxX = Math.max(dX1, dX2 );
      double dMinY = Math.min(dY1, dY2 );
      double dMaxY = Math.max(dY1, dY2 );
      
      // Create new coordinates pair for new composite hierarchy ...
      
      double ndX1 = 0; double ndY1 = 0;
      double ndX2 = 0; double ndY2 = 0;

      // Create temporary working space from input rectangle ...

      Space2DModel sp1 = new Space2DModel( dX1, dY1, dX2, dY2 );

      // Check for overlap of input rectangle and spaces stored in r-tree ...

      double A[] = new double[2];
      A[0] = Math.min ( dX1, dX2 );
      A[1] = Math.min ( dY1, dY2 );
      double B[] = new double[2];
      B[0] = Math.max ( dX1, dX2 );
      B[1] = Math.max ( dY1, dY2 );
      HyperPoint hpA = new HyperPoint ( A );
      HyperPoint hpB = new HyperPoint ( B );

      Object[] result2 = null;
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
          result2 = SecondlevelTree.intersects( hp );
      } catch (RTreeException e) {
          // if problems occur - in the real world you have
          // to handle the exceptions :-)
          e.printStackTrace();
      }

      System.out.println("*** In FloorplanModel.addPortal(): flag 1: result2.length = " + result2.length);

      // Add portal to wall ...
      
      Wall2DModel wall = null;

      Integer temp;
      for(int i=0; i < result2.length; i++) {

          temp = (Integer) result2[i];
          IndexArch np = (IndexArch) componentsindexList.get( (temp.intValue()-1) );

          // Process components index ....

          if( np.getIndex() == IndexArch.WALL ) {
              Wall2DModel sp = (Wall2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** Potential interesction with: wall = " + sp.getName() );

                  // Walk along vertices and find corners inside working rectangle ...

                  double spdMinX = sp.getBoundingBox().getMinX();
                  double spdMinY = sp.getBoundingBox().getMinY();
                  double spdMaxX = sp.getBoundingBox().getMaxX();
                  double spdMaxY = sp.getBoundingBox().getMaxY();
                  if ( dMinX == spdMinX && spdMaxX == dMaxX
                		  && spdMinY < dMinY && spdMaxY > dMaxY){
                	  System.out.println("*** Potential vertical portal in wall");
                	  
                	  ndX1 = dX1-spdMinX; ndY1 = dY1 - spdMinY;
                      ndX2 = dX2-spdMinX; ndY2 = dY2 - spdMinY;
                      wall = sp;
                      }
                  if ( dMinY == spdMinY && spdMaxY == dMaxY
                		  && spdMinX < dMinX && spdMaxX > dMaxX){
                	  System.out.println("*** Potential horizontal portal in wall");

                	  ndX1 = dX1-spdMinX; ndY1 = dY1 - spdMinY;
                      ndX2 = dX2-spdMinX; ndY2 = dY2 - spdMinY;
                      wall = sp;
                      }else { 
                          System.out.println("*** ERROR: Portal is not inside the wall!!! ");
                      }
                  }
          }
          if( np.getIndex() == IndexArch.COLUMN ) {
              Column2DModel sp = (Column2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** ERROR: Overlap with Column!!");
                  return;
              } 
          }
          if( np.getIndex() == IndexArch.CORNER ) {
              Corner2DModel sp = (Corner2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** ERROR: Overlap with Corner!!");
                  return;
              } 
          }
          else { 
              System.out.println("ERROR!!! ");
          }
      }
      
      Object[] result3 = null;
      try {
          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
          result3 = ThirdlevelTree.intersects( hp );
      } catch (RTreeException e) {
          // if problems occur - in the real world you have
          // to handle the exceptions :-)
          e.printStackTrace();
      }
      
      System.out.println("*** In FloorplanModel.addPortal(): flag 1: result3.length = " + result2.length);
      
      // Check that new portal does not overlap currently defined objects ...

      for(int i=0; i < result3.length; i++) {

          temp = (Integer) result3[i];
          IndexArch np = (IndexArch) componentsindexList.get( (temp.intValue()-1) );

          // Process architectural index ....

          if( np.getIndex() == IndexArch.PORTAL ) {
              Portal2DModel sp = (Portal2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** ERROR: Portals overlap!!");
                  return;
              } 
          } 
          if( np.getIndex() == IndexArch.DOOR ) {
              Door2DModel sp = (Door2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** ERROR: Doors overlap!!");
                  return;
              } 
          }
          if( np.getIndex() == IndexArch.WINDOW ) {
              Window2DModel sp = (Window2DModel) np.getSpace();
              if( sp1.overlap( sp ) == true ) {
                  System.out.println("*** ERROR: Overlap with Window!!");
                  return;
              } 
          }
          else { 
              System.out.println("ERROR!!! ");
          }
      }

      
      Portal2DModel sp2 = new Portal2DModel( ndX1, ndY1, ndX2, ndY2 );
      
      System.out.println("*** In FloorplanModel.addPortal(): flag 2 ... ");

      // ==========================================================
      // Add portal to floorplan database ....
      // Add item to list of portal-index pairs ...
      // ==========================================================

      iNoPortals = iNoPortals + 1;
      iNoComponents = iNoColumns + iNoCorners + iNoWalls + iNoPortals + iNoDoors + iNoWindows;
      sp2.setName ( "port-" + iNoPortals );
      portalList.add ( sp2 );
      componentsindexList.add ( new IndexArch ( new Integer( iNoComponents ), sp2, IndexArch.PORTAL ) );
      
      // ==============================================================
      // Insert new item into r-tree database ....
      // the Integer object represents the ID for the extern objects
      // ==============================================================

      try {
    	  double C[] = new double[2];
          C[0] = sp2.getBoundingBox().getMinX();
          C[1] = sp2.getBoundingBox().getMinY();
          double D[] = new double[2];
          D[0] = sp2.getBoundingBox().getMaxX();
          D[1] = sp2.getBoundingBox().getMaxY();
          
          System.out.println( "Box:" + sp2.getName() );
          System.out.println( sp2.getBoundingBox().toString() );
          
          HyperPoint hpC = new HyperPoint ( C );
          HyperPoint hpD = new HyperPoint ( D );
          ThirdlevelTree.insert( new Integer( iNoComponents ), new HyperBoundingBox( hpC, hpD ) );
          } catch (RTreeException e) {
        	  e.printStackTrace();  // common exception of RTree implementation
          }
      
      sp2.define();
          
      System.out.println("*** In FloorplanModel.addPortal(): flag 3 ... ");
      
      // =============================================================
      // Update workspace and hashmap storage ....
      // =============================================================
      wall.getChildList().add( sp2 );
      wall.getCompositeHierarchy().add(sp2);    
      
      System.out.println("*** Leave FloorplanModel.addPortal() ... ");
   }
   
   // ======================================================================
   // Add Door to the floorplan model ...
   // ======================================================================

   public void addDoor( CoordinatePair pair ) {

	      System.out.println("*** Enter FloorplanModel.addDoor() ... ");
	      System.out.println( pair.toString() );

	      // Retrieve coordinates of input rectangle ...

	      double dX1 = pair.getX1(); double dY1 = pair.getY1();
	      double dX2 = pair.getX2(); double dY2 = pair.getY2();

	      double dMinX = Math.min(dX1, dX2 );
	      double dMaxX = Math.max(dX1, dX2 );
	      double dMinY = Math.min(dY1, dY2 );
	      double dMaxY = Math.max(dY1, dY2 );
	      
	      // Create new coordinates pair for new composite hierarchy ...
	      
	      double ndX1 = 0; double ndY1 = 0;
	      double ndX2 = 0; double ndY2 = 0;

	      // Create temporary working space from input rectangle ...

	      Space2DModel sp1 = new Space2DModel( dX1, dY1, dX2, dY2 );

	      // Check for overlap of input rectangle and spaces stored in r-tree ...

	      double A[] = new double[2];
	      A[0] = Math.min ( dX1, dX2 );
	      A[1] = Math.min ( dY1, dY2 );
	      double B[] = new double[2];
	      B[0] = Math.max ( dX1, dX2 );
	      B[1] = Math.max ( dY1, dY2 );
	      HyperPoint hpA = new HyperPoint ( A );
	      HyperPoint hpB = new HyperPoint ( B );

	      Object[] result2 = null;
	      try {
	          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
	          result2 = SecondlevelTree.intersects( hp );
	      } catch (RTreeException e) {
	          // if problems occur - in the real world you have
	          // to handle the exceptions :-)
	          e.printStackTrace();
	      }

	      System.out.println("*** In FloorplanModel.addDoor(): flag 1: result2.length = " + result2.length);

	      // Add door to wall ...
	      
	      Wall2DModel wall = null;
	      
	      boolean horizon = true;

	      Integer temp;
	      for(int i=0; i < result2.length; i++) {

	          temp = (Integer) result2[i];
	          IndexArch np = (IndexArch) componentsindexList.get( (temp.intValue()-1) );

	          // Process architectural index ....
	          
	          if( np.getIndex() == IndexArch.WALL ) {
	        	  Wall2DModel sp = (Wall2DModel) np.getSpace();
	              
	              if( sp1.overlap( sp ) == true ) {
	                  System.out.println("*** Potential interesction with: wall = " + sp.getName() );

	                  // Walk along vertices and find corners inside working rectangle ...

	                  double spdMinX = sp.getBoundingBox().getMinX();
	                  double spdMinY = sp.getBoundingBox().getMinY();
	                  double spdMaxX = sp.getBoundingBox().getMaxX();
	                  double spdMaxY = sp.getBoundingBox().getMaxY();
	                  if ( dMinX == spdMinX && spdMaxX == dMaxX
	                		  && spdMinY < dMinY && spdMaxY > dMaxY){
	                	  System.out.println("*** Potential vertical door in wall");
	                	  /*wallcomposite.setxOffset(spdMinX);
	                	  wallcomposite.setyOffset(spdMinY);
	                	  wallcomposite.setrotation(0);*/
	                	  ndX1 = dX1-spdMinX; ndY1 = dY1 - spdMinY;
	                      ndX2 = dX2-spdMinX; ndY2 = dY2 - spdMinY;
	                      horizon = false;
	                      wall = sp;
	                      }
	                  if ( dMinY == spdMinY && spdMaxY == dMaxY
	                		  && spdMinX < dMinX && spdMaxX > dMaxX){
	                	  System.out.println("*** Potential horizontal door in wall");
	                	  /*wallcomposite.setxOffset(spdMinX);
	                	  wallcomposite.setyOffset(spdMinY);
	                	  wallcomposite.setrotation(0);*/
	                	  ndX1 = dX1-spdMinX; ndY1 = dY1 - spdMinY;
	                      ndX2 = dX2-spdMinX; ndY2 = dY2 - spdMinY;
	                      horizon = true;
	                      wall = sp;
	                      }
	                  }else { 
	                      System.out.println("ERROR!!! ");
	                  }
	          } 
	          if( np.getIndex() == IndexArch.PORTAL ) {
	              Portal2DModel sp = (Portal2DModel) np.getSpace();
	              if( sp1.overlap( sp ) == true ) {
	                  System.out.println("*** ERROR: Overlap with Portal!!");
	                  return;
	              } 
	          }
	          if( np.getIndex() == IndexArch.DOOR ) {
	              Door2DModel sp = (Door2DModel) np.getSpace();
	              if( sp1.overlap( sp ) == true ) {
	                  System.out.println("*** ERROR: Overlap with Door!!");
	                  return;
	              } 
	          }
	          if( np.getIndex() == IndexArch.WINDOW ) {
	              Window2DModel sp = (Window2DModel) np.getSpace();
	              if( sp1.overlap( sp ) == true ) {
	                  System.out.println("*** ERROR: Overlap with Window!!");
	                  return;
	              } 
	          }else { 
	              System.out.println("ERROR!!! ");
	          }
	      }
	      
	      boolean lefthinge;
	      boolean in;
	      
	      if(horizon == true){
	    	  if(dX1 < dX2){
	    		  if(dY1 > dY2){
	    			  lefthinge = true;
	    			  in = true;
	    		  }else{
	    			  lefthinge = true;
	    			  in = false;
	    		  }
		      }else{
		    	  if(dY1 > dY2){
	    			  lefthinge = false;
	    			  in = true;
	    		  }else{
	    			  lefthinge = false;
	    			  in = false;
	    		  }
		      }
	      }else{
	    	  if(dX1 < dX2){
	    		  if(dY1 > dY2){
	    			  lefthinge = false;
	    			  in = true;
	    		  }else{
	    			  lefthinge = true;
	    			  in = true;
	    		  }
		      }else{
		    	  if(dY1 > dY2){
	    			  lefthinge = true;
	    			  in = false;
	    		  }else{
	    			  lefthinge = false;
	    			  in = false;
	    		  }
		      }
	      }
	      
	      double ndMinX = Math.min(ndX1, ndX2 );
	      double ndMaxX = Math.max(ndX1, ndX2 );
	      double ndMinY = Math.min(ndY1, ndY2 );
	      double ndMaxY = Math.max(ndY1, ndY2 );
	      
	      Door2DModel sp2 = new Door2DModel( ndMinX, ndMinY, ndMaxX, ndMaxY, horizon, lefthinge, in );
	      
	      System.out.println("*** In FloorplanModel.addDoor(): flag 2 ... ");

	      // ==========================================================
	      // Add door to floorplan database ....
	      // Add item to list of portal-index pairs ...
	      // ==========================================================

	      iNoDoors = iNoDoors + 1;
	      iNoComponents = iNoColumns + iNoCorners + iNoWalls + iNoPortals + iNoDoors + iNoWindows;
	      sp2.setName ( "door-" + iNoDoors );
	      doorList.add ( sp2 );
	      componentsindexList.add ( new IndexArch ( new Integer( iNoComponents ), sp2, IndexArch.DOOR ) );
	      
	      // ==============================================================
	      // Insert new item into r-tree database ....
	      // the Integer object represents the ID for the extern objects
	      // ==============================================================

	      /*try {
	    	  double C[] = new double[2];
	          C[0] = sp2.getBoundingBox().getMinX();
	          C[1] = sp2.getBoundingBox().getMinY();
	          double D[] = new double[2];
	          D[0] = sp2.getBoundingBox().getMaxX();
	          D[1] = sp2.getBoundingBox().getMaxY();
	          
	          System.out.println( "Box:" + sp2.getName() );
	          System.out.println( sp2.getBoundingBox().toString() );
	          
	          HyperPoint hpC = new HyperPoint ( C );
	          HyperPoint hpD = new HyperPoint ( D );
	          SecondlevelTree.insert( new Integer( iNoComponents ), new HyperBoundingBox( hpC, hpD ) );
	          } catch (RTreeException e) {
	        	  e.printStackTrace();  // common exception of RTree implementation
	        	  }
	          
	      System.out.println("*** In FloorplanModel.addPortal(): flag 3 ... ");
	          
	      // Add component coordinates to component grid ... 

	      compGrid.add ( sp2 );*/
	      
	      // =============================================================
	      // Update workspace and hashmap storage ....
	      // =============================================================
	      sp2.define();
	      /*wallcomposite.add(sp2);
	      workspace.add(wallcomposite);*/
	      
	      wall.getChildList().add( sp2 );
	      wall.getCompositeHierarchy().add(sp2); 
	      
	      
	      System.out.println("*** Leave FloorplanModel.addPortal() ... ");
   }
   
   // ======================================================================
   // Add Window to the floorplan model ...
   // ======================================================================

   public void addWindow( CoordinatePair pair ) {

	   System.out.println("*** Enter FloorplanModel.addWindow() ... ");
	      System.out.println( pair.toString() );

	      // Retrieve coordinates of input rectangle ...

	      double dX1 = pair.getX1(); double dY1 = pair.getY1();
	      double dX2 = pair.getX2(); double dY2 = pair.getY2();

	      double dMinX = Math.min(dX1, dX2 );
	      double dMaxX = Math.max(dX1, dX2 );
	      double dMinY = Math.min(dY1, dY2 );
	      double dMaxY = Math.max(dY1, dY2 );
	      
	      // Create new coordinates pair for new composite hierarchy ...
	      
	      double ndX1 = 0; double ndY1 = 0;
	      double ndX2 = 0; double ndY2 = 0;

	      // Create temporary working space from input rectangle ...

	      Space2DModel sp1 = new Space2DModel( dX1, dY1, dX2, dY2 );

	      // Check for overlap of input rectangle and spaces stored in r-tree ...

	      double A[] = new double[2];
	      A[0] = Math.min ( dX1, dX2 );
	      A[1] = Math.min ( dY1, dY2 );
	      double B[] = new double[2];
	      B[0] = Math.max ( dX1, dX2 );
	      B[1] = Math.max ( dY1, dY2 );
	      HyperPoint hpA = new HyperPoint ( A );
	      HyperPoint hpB = new HyperPoint ( B );

	      Object[] result2 = null;
	      try {
	          HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
	          result2 = SecondlevelTree.intersects( hp );
	      } catch (RTreeException e) {
	          // if problems occur - in the real world you have
	          // to handle the exceptions :-)
	          e.printStackTrace();
	      }

	      System.out.println("*** In FloorplanModel.addWindow(): flag 1: result2.length = " + result2.length);

	      // Add window to wall ...
	      
	      Wall2DModel wall = null;

	      Integer temp;
	      for(int i=0; i < result2.length; i++) {

	          temp = (Integer) result2[i];
	          IndexArch np = (IndexArch) componentsindexList.get( (temp.intValue()-1) );

	          // Process architectural index ....

	          if( np.getIndex() == IndexArch.WALL ) {
	              Wall2DModel sp = (Wall2DModel) np.getSpace();
	              if( sp1.overlap( sp ) == true ) {
	                  System.out.println("*** Potential interesction with: wall = " + sp.getName() );

	                  // Walk along vertices and find corners inside working rectangle ...

	                  double spdMinX = sp.getBoundingBox().getMinX();
	                  double spdMinY = sp.getBoundingBox().getMinY();
	                  double spdMaxX = sp.getBoundingBox().getMaxX();
	                  double spdMaxY = sp.getBoundingBox().getMaxY();
	                  if ( dMinX == spdMinX && spdMaxX == dMaxX
	                		  && spdMinY < dMinY && spdMaxY > dMaxY){
	                	  System.out.println("*** Potential vertical window in wall");
	                	  /*wallcomposite.setxOffset(spdMinX);
	                	  wallcomposite.setyOffset(spdMinY);
	                	  wallcomposite.setrotation(0);*/
	                	  ndX1 = dX1-spdMinX; ndY1 = dY1 - spdMinY;
	                      ndX2 = dX2-spdMinX; ndY2 = dY2 - spdMinY;
	                      wall = sp;
	                      }
	                  if ( dMinY == spdMinY && spdMaxY == dMaxY
	                		  && spdMinX < dMinX && spdMaxX > dMaxX){
	                	  System.out.println("*** Potential horizontal window in wall");
	                	  /*wallcomposite.setxOffset(spdMinX);
	                	  wallcomposite.setyOffset(spdMinY);
	                	  wallcomposite.setrotation(0);*/
	                	  ndX1 = dX1-spdMinX; ndY1 = dY1 - spdMinY;
	                      ndX2 = dX2-spdMinX; ndY2 = dY2 - spdMinY;
	                      wall = sp;
	                      }
	                  }else { 
	                      System.out.println("ERROR!!! ");
	                  }
	          } 
	          if( np.getIndex() == IndexArch.PORTAL ) {
	              Portal2DModel sp = (Portal2DModel) np.getSpace();
	              if( sp1.overlap( sp ) == true ) {
	                  System.out.println("*** ERROR: Overlap with Portal!!");
	                  return;
	              } 
	          }
	          if( np.getIndex() == IndexArch.DOOR ) {
	              Door2DModel sp = (Door2DModel) np.getSpace();
	              if( sp1.overlap( sp ) == true ) {
	                  System.out.println("*** ERROR: Overlap with Door!!");
	                  return;
	              } 
	          }
	          if( np.getIndex() == IndexArch.WINDOW ) {
	              Window2DModel sp = (Window2DModel) np.getSpace();
	              if( sp1.overlap( sp ) == true ) {
	                  System.out.println("*** ERROR: Overlap with Window!!");
	                  return;
	              } 
	          }
	      }
	      
	      boolean horizon;
	      if(Math.abs( dX2 - dX1 ) > Math.abs( dY2 - dY1 )){
	    	  horizon = true;
	      }else{
	    	  horizon = false;
	      }
	      
	      double ndMinX = Math.min(ndX1, ndX2 );
	      double ndMaxX = Math.max(ndX1, ndX2 );
	      double ndMinY = Math.min(ndY1, ndY2 );
	      double ndMaxY = Math.max(ndY1, ndY2 );
	      
	      Window2DModel sp2 = new Window2DModel( ndMinX, ndMinY, ndMaxX, ndMaxY, horizon );
	      
	      System.out.println("*** In FloorplanModel.addWindow(): flag 2 ... ");

	      // ==========================================================
	      // Add portal to floorplan database ....
	      // Add item to list of portal-index pairs ...
	      // ==========================================================

	      iNoWindows = iNoWindows + 1;
	      iNoComponents = iNoColumns + iNoCorners + iNoWalls + iNoPortals + iNoDoors + iNoWindows;
	      sp2.setName ( "win-" + iNoWindows );
	      windowList.add ( sp2 );
	      componentsindexList.add ( new IndexArch ( new Integer( iNoComponents ), sp2, IndexArch.WINDOW ) );
	      
	      // ==============================================================
	      // Insert new item into r-tree database ....
	      // the Integer object represents the ID for the extern objects
	      // ==============================================================

	      /*try {
	    	  double C[] = new double[2];
	          C[0] = sp2.getBoundingBox().getMinX();
	          C[1] = sp2.getBoundingBox().getMinY();
	          double D[] = new double[2];
	          D[0] = sp2.getBoundingBox().getMaxX();
	          D[1] = sp2.getBoundingBox().getMaxY();
	          
	          System.out.println( "Box:" + sp2.getName() );
	          System.out.println( sp2.getBoundingBox().toString() );
	          
	          HyperPoint hpC = new HyperPoint ( C );
	          HyperPoint hpD = new HyperPoint ( D );
	          SecondlevelTree.insert( new Integer( iNoComponents ), new HyperBoundingBox( hpC, hpD ) );
	          } catch (RTreeException e) {
	        	  e.printStackTrace();  // common exception of RTree implementation
	        	  }
	          
	      System.out.println("*** In FloorplanModel.addPortal(): flag 3 ... ");
	          
	      // Add component coordinates to component grid ... 

	      compGrid.add ( sp2 );*/
	      
	      // =============================================================
	      // Update workspace and hashmap storage ....
	      // =============================================================
	      sp2.define();
	      wall.getChildList().add( sp2 );
	      wall.getCompositeHierarchy().add(sp2);
	      
	      System.out.println("*** Leave FloorplanModel.addWindow() ... ");
   
   
   }
   // ===============================================================
   // Propogate floorplan objects and composites to controller ....
   // ===============================================================

   public void setFloorplanTaskProperty( String task ) {
      System.out.println("*** Enter FloorplanModel.setFloorplanTaskProperty(): task = " + task );
      this.currentTask = task;
      System.out.println("*** Leave FloorplanModel.setFloorplanTaskProperty(): currentTask = " + currentTask );
   }

   public void setFloorplanSelectionProperty( CoordinatePair pair ) {
      System.out.println("*** Enter FloorplanModel.setFloorplanSelectionProperty(): ... " );

      if( currentTask.equals("Add Space Block") == true &&
          pair.getCoordinatePairType() == CoordinatePairType.Rectangle ) {
          addSpaceBlock( pair );
      }

      if( currentTask.equals("Add Support Column") == true &&
          pair.getCoordinatePairType() == CoordinatePairType.Rectangle ) {
          addSupportColumn( pair );
      }

      if( currentTask.equals("Add Wall Corner") == true &&
          pair.getCoordinatePairType() == CoordinatePairType.Rectangle ) {
          addWallCorner( pair );
      }
      
      if( currentTask.equals("Add Exterior Wall") == true &&
          pair.getCoordinatePairType() == CoordinatePairType.Rectangle ) {
          addExteriorWall( pair );
      }
      
      if( currentTask.equals("Add Portal") == true &&
              pair.getCoordinatePairType() == CoordinatePairType.Rectangle ) {
              addPortal( pair );
      }
      
      if( currentTask.equals("Add Door") == true &&
              pair.getCoordinatePairType() == CoordinatePairType.Rectangle ) {
              addDoor( pair );
      }
      
      if( currentTask.equals("Add Window") == true &&
              pair.getCoordinatePairType() == CoordinatePairType.Rectangle ) {
              addWindow( pair );
      }


      // Synchronize floorplan workspaces and data ...

      CompositeHierarchy copy01 = workspace.clone();
      firePropertyChange( EngineeringController.FLOORPLAN_WORKSPACE,
                          (CompositeHierarchy) null, copy01 );

      HashMap copy02 = new HashMap( things );
      firePropertyChange( EngineeringController.FLOORPLAN_HASHMAP, (HashMap) null, copy02 );
   }

   // ===============================================================
   // Propogate floorplan objects and composite to controller ....
   // ===============================================================

   public void setFloorplan() {
      HashMap copy = new HashMap( things );
      firePropertyChange( EngineeringController.FLOORPLAN_HASHMAP, (HashMap) null, copy );
   }

   public void setFloorplanComposite() {
      CompositeHierarchy copy = workspace.clone();
      firePropertyChange( EngineeringController.FLOORPLAN_WORKSPACE,
                          (CompositeHierarchy) null, copy );
   }
   
   public void setFloorplanSelectedPropertyStatus( AbstractFeature f ) {
	  System.out.println("*** Enter FloorplanModel.setFloorplanSelectedPropertyStatus() ... ");
	  
	  firePropertyChange( EngineeringController.FLOORPLAN_SELECTED_PROPERTY_STATUS,
              (AbstractFeature) null, f );
	  
	  CompositeHierarchy copy01 = workspace.clone();
      firePropertyChange( EngineeringController.FLOORPLAN_WORKSPACE,
                          (CompositeHierarchy) null, copy01 );
   }
   
   public void setFloorplanUtility( UtilityModel model ) {
	   System.out.println("*** Enter FloorplanModel.setFloorplanUtility() ... ");
	   this.utilityModel = model;
	   
	   if( !roomList.isEmpty() )
	   {
		   for( RoomModel room : roomList)
			   room.calculateArea( utilityModel.getRatio() );
	   }
	   updatePrice();
   }
   
   /**
    * Calculates and updates the room usable area
    * @param i is a dummy parameter for the invoke method to find the method 
    */
   public void setFloorplanPropertyUpdate( Integer i ) {
	   System.out.println("*** Enter FloorplanModel.setFloorplanUpdate() ... ");
	   FloorplanUtil.updateWholeRTree( this );
	   if( !roomList.isEmpty() )
	   {
		   for( RoomModel room : roomList)
		   {
			   room.calculateArea( utilityModel.getRatio() );
		   }
	   }
	   updatePrice();
   }
   
   /**
    * Deal with all the floor plan model trade off data calculations
    * @param model
    * @throws ParseException
    */
   public void setFloorplanSummary( FloorplanResultModel model ) throws ParseException {
	   System.out.println("*** Enter FloorplanModel.setFloorplanSummary() ... ");
	   if( !roomList.isEmpty() ) {
		   model.setRoomList( roomList );
		   model.setUtilityModel( utilityModel );
		   model.calculateAttributes();
		   firePropertyChange( EngineeringController.FLOORPLAN_SUMMARY,
		              (FloorplanResultModel) null, model );
		   resultsList.add( model );
		   System.out.println("*** Enter FloorplanModel.setFloorplanSummary() ... done");
	   }
	   else
		   System.out.println("*** FloorplanModel.setFloorplanSummary() there is no room define in this Floorplan Model... "); 
   }
   
   public void setFloorplanDisplayTag( Boolean displayTag ) {
	   System.out.println("*** Enter FloorplanModel.setFloorplanDisplayTag() ... ");
	   this.displayTag = displayTag;
	   firePropertyChange( EngineeringController.FLOORPLAN_TAG_PROPERTY, null, displayTag );
   }
   
   protected void updatePrice() {
	   UtilityModel model = this.utilityModel;
	   if( !model.getConstructionType().equals( new String("") ) )
	   {
		   if( !model.getGroup().equals( new String("") ) )
		   {
			   double priceRatio = model.getPricePerSquareFoot();
			   model.setPriceRatio( priceRatio );
		   }
		   else
			   System.out.println( "FloorplanModel.updatePrice() can't update the price ..." );
	   }
	   
	   if( !columnList.isEmpty() )
	   {
		   for( Object column : columnList )
			   if( column instanceof Column2DModel )
				   ((Column2DModel)column).setPrice( ((Column2DModel)column).getArea()
						   * model.getRatio() * model.getPriceRatio() );
	   }
	   if( !cornerList.isEmpty() )
	   {
		   for( Object corner : cornerList )
			   if( corner instanceof Corner2DModel )
				   ((Corner2DModel)corner).setPrice( ((Corner2DModel)corner).getArea() 
						   * model.getRatio() * model.getPriceRatio() );
	   }
	   if( !wallList.isEmpty() )
	   {
		   for( Object wall : wallList )
			   if( wall instanceof Wall2DModel )
				   ((Wall2DModel)wall).setPrice( ((Wall2DModel)wall).getArea() 
						   * model.getRatio() * model.getPriceRatio() );
	   }
   }
   
   public void setFloorplanDefineRoomProperty( RoomModel s ) {
	   System.out.println("*** Enter FloorplanModel.setFloorplanDefineRoomProperty() ... ");
	   RoomModel room = validateRoom( s );
	   
	   if( room != null )
	   {
		   room.calculateArea();
		   firePropertyChange( EngineeringController.FLOORPLAN_DEFINE_ROOM_PROPERTY,
				   (RoomModel)null, room );
	   }
	   else
		   System.out.println( "*** ERROR FloorplanModel.setFloorplanDefineRoomProperty() can't define room with one of " +
		   		"the selected space has been included in other room model");
   }
   
   public void setFloorplanResultSelected( FloorplanResultModel m ) {
	   System.out.println("*** Enter FloorplanModel.setFloorplanResultSelected() ... ");
	   if( m != null ){
		   firePropertyChange( EngineeringController.FLOORPLAN_SELECTED_SUMMARY,
		              (FloorplanResultModel) null, m );
	   }
	   else
		   System.out.println("*** ERROR FloorplanModel.setFloorplanResultSelected() can't get selected floorplan result model");
   }
   
   public void setFloorplanTradeOffCaseData( int[] caseNum )
   {
	   System.out.println("*** Enter FloorplanModel.setFloorplanTradeOffCaseData() ... ");
	   ArrayList<FloorplanResultModel> list = new ArrayList<FloorplanResultModel>();
	   if( resultsList != null )
	   {
		   if( caseNum[1] == 0 ) // One case being selected
		   {
			   for( FloorplanResultModel model : resultsList )
			   {
				   if( caseNum[0] != 0 && model.getCaseNumber() == caseNum[0] )
					   list.add( model );
			   }
			   
			   if( list.size() != 0 )
			   {
				   System.out.println("FLOORPLANMODEL FIRE CASE CITY");
				   firePropertyChange( EngineeringController.FLOORPLAN_CASE_CITY,
						   (ArrayList<FloorplanResultModel>)null, list );
		       }
		   }
		   else if( caseNum[2] == 0 || caseNum[3] == 0 ) // two/three cases being selected
		   {
			   for( FloorplanResultModel model : resultsList )
			   {
				   for( int i = 0; i < caseNum.length; i++ )
				   {
					   if( caseNum[i] != 0 && model.getCaseNumber() == caseNum[i] )
						   list.add( model );
				   }
			   }
			   
			   if( list.size() != 0 )
			   {
				   System.out.println("FLOORPLANMODEL FIRE MULTI CASES CITY");
				   firePropertyChange( EngineeringController.FLOORPLAN_MULTICASES,
						   (ArrayList<FloorplanResultModel>)null, list );
		       }
		   }
		   else
			   System.out.println("*** ERROR FloorplanModel.setFloorplanTradeOffCaseData() Can't process more than 3 cases");
	   }
   }
   
   public void setFloorplanExampleOne( Boolean b ) {
	   System.out.println("*** Enter FloorplanModel.setFloorplanExampleOne() ... ");
	   //Apt 1
	   CoordinatePair sp1 = new CoordinatePair();
	   sp1.setCoordinatePair( -200, 0, 0, 230 );
	   this.addSpaceBlock( sp1 );
	   
	   CoordinatePair sp2 = new CoordinatePair();
	   sp2.setCoordinatePair( 0, 0, 40, 130 );
	   this.addSpaceBlock( sp2 );
	   
	   CoordinatePair sp3 = new CoordinatePair();
	   sp3.setCoordinatePair( 0, 130, 40, 180 );
	   this.addSpaceBlock( sp3 );
	   
	   CoordinatePair sp4 = new CoordinatePair();
	   sp4.setCoordinatePair( 40, 0, 230, 180 );
	   this.addSpaceBlock( sp4 );
	   
	   CoordinatePair sp5 = new CoordinatePair();
	   sp5.setCoordinatePair( 0, 180, 140, 230 );
	   this.addSpaceBlock( sp5 );
	   
	   CoordinatePair sp6 = new CoordinatePair();
	   sp6.setCoordinatePair( 140, 180, 170, 230 );
	   this.addSpaceBlock( sp6 );
	   
	   CoordinatePair sp7 = new CoordinatePair();
	   sp7.setCoordinatePair( 170, 180, 230, 230 );
	   this.addSpaceBlock( sp7 );
	   
	   CoordinatePair sp8 = new CoordinatePair();
	   sp8.setCoordinatePair( -200, 230, 0, 360 );
	   this.addSpaceBlock( sp8 );
	   
	   CoordinatePair sp9 = new CoordinatePair();
	   sp9.setCoordinatePair( -200, 360, 0, 450 );
	   this.addSpaceBlock( sp9 );
	   
	   CoordinatePair sp10 = new CoordinatePair();
	   sp10.setCoordinatePair( 0, 230, 30, 270 );
	   this.addSpaceBlock( sp10 );
	   
	   CoordinatePair sp11 = new CoordinatePair();
	   sp11.setCoordinatePair( 30, 230, 90, 270 );
	   this.addSpaceBlock( sp11 );
	   
	   CoordinatePair sp12 = new CoordinatePair();
	   sp12.setCoordinatePair( 0, 270, 90, 450 );
	   this.addSpaceBlock( sp12 );
	   
	   CoordinatePair sp13 = new CoordinatePair();
	   sp13.setCoordinatePair( 90, 230, 230, 450 );
	   this.addSpaceBlock( sp13 );
	   
	   //Apt 2
	   CoordinatePair sp14 = new CoordinatePair();
	   sp14.setCoordinatePair( 230, 0, 420, 200 );
	   this.addSpaceBlock( sp14 );
	   
	   CoordinatePair sp15 = new CoordinatePair();
	   sp15.setCoordinatePair( 420, 0, 470, 150 );
	   this.addSpaceBlock( sp15 );
	   
	   CoordinatePair sp16 = new CoordinatePair();
	   sp16.setCoordinatePair( 420, 150, 470, 200 );
	   this.addSpaceBlock( sp16 );
	   
	   CoordinatePair sp17 = new CoordinatePair();
	   sp17.setCoordinatePair( 390, 230, 470, 300 );
	   this.addSpaceBlock( sp17 );
	   
	   CoordinatePair sp18 = new CoordinatePair();
	   sp18.setCoordinatePair( 390, 200, 470, 230 );
	   this.addSpaceBlock( sp18 );
	   
	   CoordinatePair sp19 = new CoordinatePair();
	   sp19.setCoordinatePair( 230, 200, 390, 300 );
	   this.addSpaceBlock( sp19 );
	   
	   CoordinatePair sp20 = new CoordinatePair();
	   sp20.setCoordinatePair( 470, 0, 540, 300 );
	   this.addSpaceBlock( sp20 );
	   
	   CoordinatePair sp21 = new CoordinatePair();
	   sp21.setCoordinatePair( 230, 300, 280, 450 );
	   this.addSpaceBlock( sp21 );
	   
	   CoordinatePair sp22 = new CoordinatePair();
	   sp22.setCoordinatePair( 280, 300, 540, 450 );
	   this.addSpaceBlock( sp22 );
	   
	   // Synchronize floorplan workspaces and data ...

	   CompositeHierarchy copy01 = workspace.clone();
	   firePropertyChange( EngineeringController.FLOORPLAN_WORKSPACE,
			   (CompositeHierarchy) null, copy01 );

	   HashMap copy02 = new HashMap( things );
	   firePropertyChange( EngineeringController.FLOORPLAN_HASHMAP, (HashMap) null, copy02 );
	   System.out.println("*** Import finished... Space size = " + spaceList.size() );
   }
   
   private RoomModel validateRoom( RoomModel s ) {
	   if( roomList.isEmpty() )
	   {
		   s.setFloorplanModel( this );
		   roomList.add( s );
		   return s;
	   }
	   else
	   {
		   for( RoomModel existRoom : roomList )
		   {
			   for( Space2DModel space : existRoom.getSpaces() )
			   {
				   System.out.println( "FloorplanModel.validateRoom() space name" + space.getName() );
				   for( Space2DModel selectedSpace : s.getSpaces() )
				   {
					   System.out.println( "FloorplanModel.validateRoom() selectedSpace name" + selectedSpace.getName() );
					   if( space.getName().equals( selectedSpace.getName() ) )
					   {
						   System.out.println( "FloorplanModel.validateRoom() the space " + space + " has been selected in " + existRoom );
						   return null;
					   }
				   }
			   }
		   }
		   s.setFloorplanModel( this );
		   roomList.add( s );
		   return s;
	   }
   }

/**
    * Get the grid system of this model
    * @return GridSystem
    */
   public GridSystem getGridSystem() {
	   return this.archGrid;
   }
   
   /**
    * Get the space list of this model
    * @return ArrayList
    */
   public ArrayList<Space2DModel> getSpaceList()
   {
	   return (ArrayList<Space2DModel>) this.spaceList;
   }
   
   /**
    * Get the column list of this model
    * @return ArrayList
    */
   public ArrayList<Column2DModel> getColumnList()
   {
	   return (ArrayList<Column2DModel>) this.columnList;
   }
   
   /**
    * Get the corner list of this model
    * @return ArrayList
    */
   public ArrayList<Corner2DModel> getCornerList()
   {
	   return (ArrayList<Corner2DModel>) this.cornerList;
   }
   
   /**
    * Get the wall list of this model
    * @return ArrayList
    */
   public ArrayList<Wall2DModel> getWallList()
   {
	   return (ArrayList<Wall2DModel>) this.wallList;
   }
   
   /**
    * Get the UtilityModel of this model
    * @return UtilityModel
    */
   public UtilityModel getUtilityModel()
   {
	   return this.utilityModel;
   }
   
   /**
    * Get the top level Rtree that stores the spaces
    * @return ToplevelTree
    */
   public RTree getToplevelTree()
   {
	   return this.ToplevelTree;
   }
   
   /**
    * Get the second level Rtree that stores the spaces
    * @return SecondlevelTree
    */
   public RTree getSecondlevelTree()
   {
	   return this.SecondlevelTree;
   }
   
   /**
    * Get the third level Rtree that stores the spaces
    * @return ThirdlevelTree
    */
   public RTree getThirdlevelTree()
   {
	   return this.ThirdlevelTree;
   }
   
   /**
    * Get the Components index list
    * @return componentsindexList
    */
   public List<IndexArch> getComponentsIndexList()
   {
	   return this.componentsindexList;
   }
}
