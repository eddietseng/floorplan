/*
 *  ==========================================================================================
 *  GridLineModel.java: Definition and managment of objects attached to a coordinate line segment.
 *
 *  Note. This initial implementation supports two types of
 *  coordinate line.
 *
 *    1. PARALLEL_TO_X-AXIS (i.e., coordinate line is horizontal).
 *    2. PARALLEL_TO_Y-AXIS (i.e., coordinate line is vertical).
 *
 *  Written By: Mark Austin                                                       January 2013
 *  ==========================================================================================
 */

package model.floorplan;

import java.lang.Math.*;
import java.applet.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.awt.Color;

import model.*;
import model.primitive.*;

public class GridLineModel extends AbstractCompoundFeature {
   Coordinate       anchor = new Coordinate(); // Anchor point ...
   public ArrayList gridPointList = new ArrayList();  // List of coordinates ...
   private GridLineDirection clType;           // Grid line type ...
   private ComponentBoundary boundary;         // Boundary of gridline anchor point ...
   String clName;                              // Name of grid line ....
   protected double shiftValue = 0;			   // Default shift value ...
   public ArrayList<Space2DModel> AssociateSpaceList = new ArrayList<Space2DModel>(); //Save the space that is associate with this GridLine
   public ArrayList<Corner2DModel> AssociateCornerList = new ArrayList<Corner2DModel>();//Save the corner that is associate with this GridLine
   public ArrayList<Column2DModel> AssociateColumnList = new ArrayList<Column2DModel>();//Save the column to make this line unmovable
   private List<FloorplanModelListener> listeners = new ArrayList<FloorplanModelListener>();

   private double MinX =  -300.0;
   private double MinY =  -150.0;
   private double MaxX =   600.0;
   private double MaxY =   500.0;

   // Constructor ...

   public GridLineModel() {}

   public GridLineModel ( String clName ) {
      setName ( clName );
      this.boundary = new ComponentBoundary();
   }

   public GridLineModel ( String clName, GridLineDirection clType ) {
      setName ( clName );
      this.boundary = new ComponentBoundary();
      this.setGridLineDirection ( clType );
   }

   // ==============================================================
   // Set/get "type" of coordinate line ...
   // ==============================================================

   public void setGridLineDirection ( GridLineDirection clType ) {
       this.clType = clType;
   }

   public GridLineDirection getGridLineDirection() {
      return this.clType;
   }
   
   // ==============================================================
   // Set/get shift value for the gridline ...
   // ==============================================================
   
   public void setShiftValue ( double value ) {
	   this.shiftValue = value;
   }
   
   public double getShiftValue () {
	   return this.shiftValue;
   }

   // ==============================================================
   // Set anchor point. Nodes will be ordered acording to
   // their distance from the anchor point ....
   // ==============================================================

   public void setAnchorPoint( Coordinate v ) {
      anchor.dX = v.dX; 
      anchor.dY = v.dY; 
   }

   public void setAnchorPoint( GridPoint gp ) {
      anchor.dX = gp.getX(); 
      anchor.dY = gp.getY(); 
   }

   public double getAnchorX() { return anchor.dX; }
   public double getAnchorY() { return anchor.dY; }

   // ==============================================================
   // Add Coordinate to coordinate list ....
   // ==============================================================

   public void add ( GridPoint gp ) {
      double dAnchorWidth  = 30.0;
      double dAnchorHeight = 20.0;
      double x1 = 0.0;
      double x2 = 0.0;
      double y1 = 0.0;
      double y2 = 0.0;
      double x3 = 0.0;
      double y3 = 0.0;
      double x4 = 0.0;
      double y4 = 0.0;

      // Don't add vertex if coordinates are already in the list ....

      if ( findPoint( new Coordinate ( gp.getX(), gp.getY()) ) != null ) {
           return;
      }

      // Add new vertex to list ....

      gridPointList.add( gp );

      // Resort lists .....

      if( this.getGridLineDirection() == GridLineDirection.ParallelToXAxis ) {
          Collections.sort( gridPointList, new xCompare() );
      }

      if( this.getGridLineDirection() == GridLineDirection.ParallelToYAxis ) {
          Collections.sort( gridPointList, new yCompare() );
      }

      // Save corner reference point for shape ...

      if( this.getGridLineDirection() == GridLineDirection.ParallelToXAxis ) {
          x1 = MinX - dAnchorWidth/2.0;
          y1 = gp.getY() - dAnchorHeight/2.0;
          x2 = MinX + dAnchorWidth/2.0;
          y2 = gp.getY() + dAnchorHeight/2.0;
          x3 = x2;
          y3 = (y1 + y2 )/2.0;
          x4 = x3 + 2*dAnchorWidth;
          y4 = y3;
      }

      if( this.getGridLineDirection() == GridLineDirection.ParallelToYAxis ) {
          x1 = gp.getX() - dAnchorWidth/2.0;
          y1 = MinY - dAnchorHeight/2.0;
          x2 = gp.getX() + dAnchorWidth/2.0;
          y2 = MinY + dAnchorHeight/2.0;
          x3 = (x1 + x2 )/2.0;
          y3 = y2;
          x4 = x3;
          y4 = y3 + 2*dAnchorWidth;
      }

      // Initialize envelope ...

      be.addPoint( x1, y1 );
      be.addPoint( x2, y2 );

      // Initialize grid model envelope ...

      setX( (double) x1 );
      setY( (double) y1 );
      setHeight( (double) (y2 - y1) );
      setWidth(  (double) (x2 - x1) );
      setColor(  Color.green );
      setTextOffSetX( 5 );
      setTextOffSetY( 5 );

      // Create vertices for centerline schematic ...

      int pSize = 2;
      Point node01 = new Point( "pt01",  x1, y1, pSize, pSize );
      Point node02 = new Point( "pt02",  x2, y1, pSize, pSize );
      Point node03 = new Point( "pt03",  x2, y2, pSize, pSize );
      Point node04 = new Point( "pt04",  x1, y2, pSize, pSize );
      Point node05 = new Point( "pt05",  x3, y3, pSize, pSize );
      Point node06 = new Point( "pt06",  x4, y4, pSize, pSize );

      // Connect edges to vertices ....

      model.primitive.Edge edge01 = new model.primitive.Edge( "edge01",  node01, node02 );
      edge01.setThickness( pSize );
      model.primitive.Edge edge02 = new model.primitive.Edge( "edge02",  node02, node03 );
      edge02.setThickness( pSize );
      model.primitive.Edge edge03 = new model.primitive.Edge( "edge03",  node03, node04 );
      edge03.setThickness( pSize );
      model.primitive.Edge edge04 = new model.primitive.Edge( "edge04",  node04, node01 );
      edge04.setThickness( pSize );
      model.primitive.Edge edge05 = new model.primitive.Edge( "edge05",  node05, node06 );
      edge05.setThickness( pSize );

      // Add points to the model ...

      items.put( node01.getName(), node01 );
      items.put( node02.getName(), node02 );
      items.put( node03.getName(), node03 );
      items.put( node04.getName(), node04 );
      items.put( node05.getName(), node05 );
      items.put( node06.getName(), node06 );

      // Add edges to the model ...

      items.put( edge01.getName(), edge01 );
      items.put( edge02.getName(), edge02 );
      items.put( edge03.getName(), edge03 );
      items.put( edge04.getName(), edge04 );
      items.put( edge05.getName(), edge05 );
   }

   // ==============================================================
   // Retrieve vertex from a specific coordinate line ....
   // 
   // Return null if the point does not exist....
   // ==============================================================

   public GridPoint findPoint ( Coordinate v ) {
       GridPoint gp = null;

       Iterator iterator1 = gridPointList.iterator();
       while ( iterator1.hasNext() != false ) {
            gp = (GridPoint) iterator1.next();
            if( (gp.getX() == v.getX()) && (gp.getY() == v.getY()) ) {
                return gp;
            }
       }

       return null;
   }

   class xCompare implements Comparator {
      public int compare( Object o1, Object o2) {
         GridPoint gp1 = (GridPoint) o1;
         GridPoint gp2 = (GridPoint) o2;
         if ( gp1.getX() == gp2.getX() )
              return  0;
         else if ( gp1.getX() > gp2.getX() )
              return  1;
         else
              return -1;
      }
   }

   class yCompare implements Comparator {
      public int compare( Object o1, Object o2) {
         GridPoint gp1 = (GridPoint) o1;
         GridPoint gp2 = (GridPoint) o2;
         if ( gp1.getY() == gp2.getY() )
              return  0;
         else if ( gp1.getY() > gp2.getY() )
              return  1;
         else
              return -1;
      }
  }

  // Create string description of coordinate line ..

  public String toString() {
     String s = "Grid Line: " + this.getName() + "\n";
     s += "Anchor Point: (x,y) = (" + anchor.getX() + "," + anchor.getY() + ")\n";

     if( this.getGridLineDirection() == GridLineDirection.ParallelToXAxis )
         s += "Type: Parallel to X Axis.... \n";
     if( this.getGridLineDirection() == GridLineDirection.ParallelToYAxis )
         s += "Type: Parallel to Y Axis.... \n";

     s += "--------------------------------------------\n";

     Iterator iterator1 = gridPointList.iterator();
     while ( iterator1.hasNext() != false ) {
         Coordinate vp = (Coordinate) iterator1.next();
         s += vp.toString() + "\n";
     }

     s += "--------------------------------------------\n";
     return s;
  }

  // ===================================================================
  // Accept method for Feature Element visitor ...
  // ===================================================================

  public void accept( FeatureElementVisitor visitor) {
     visitor.visit(this);
  }
  
  //===================================================================
  // Support for node listeners ...
  //===================================================================
  
  public void addFloorplanModelListener( FloorplanModelListener listener) {
	 if(listener instanceof Column2DModel )
    	 this.AssociateColumnList.add( (Column2DModel) listener);
     if(listener instanceof Corner2DModel )
    	 this.AssociateCornerList.add( (Corner2DModel) listener);
	  listeners.add(listener);
  }

  public void removeFloorplanModelListener( FloorplanModelListener listener) {
	  if(listener instanceof Column2DModel )
	    	 this.AssociateColumnList.remove( (Column2DModel) listener);
	  if(listener instanceof Corner2DModel )
		  this.AssociateCornerList.remove( (Corner2DModel) listener);
	  listeners.remove(listener);
  }
  
  // Check the associate column list. If there is a column attached to this gridline, the line is unmovable.
  public boolean isGridLineMoveable() {
	  if( !this.AssociateColumnList.isEmpty() )
		  return false;
	  else
		  return true;
  }
  
  // Move node a distance dx, dy, then notify all registered listeners.

  boolean DEBUG = true;
  
  public void move( double dx, double dy ) {

     if( DEBUG == true ) {
        System.out.println("*** Enter move(dx,dy) ... ");
     }

     // Move grid in the x-direction ...

     if( this.getGridLineDirection() == GridLineDirection.ParallelToYAxis && dx != 0.0){
         System.out.println("Vertical Grid Line(X-axis) moved: ... ");
         Iterator iterator1 = this.gridPointList.iterator();
         while ( iterator1.hasNext() != false ){
            GridPoint vA = (GridPoint) iterator1.next();
            vA.dX = vA.dX + dx;
         }
         Iterator iterator2 = this.AssociateSpaceList.iterator();
         while ( iterator2.hasNext() != false){
            Space2DModel sp = (Space2DModel) iterator2.next();
            if(sp.getBoundingBox().getMaxX() == anchor.dX )
               sp.getBoundingBox().set( anchor.dX + dx, sp.getBoundingBox().getMaxY(), 
                                        sp.getBoundingBox().getMinX(),
                                        sp.getBoundingBox().getMinY() );
            if(sp.getBoundingBox().getMinX() == anchor.dX )
               sp.getBoundingBox().set( sp.getBoundingBox().getMaxX(),
                                        sp.getBoundingBox().getMaxY(), 
                                        anchor.dX + dx, sp.getBoundingBox().getMinY() );
         }
         Iterator iterator3 = this.AssociateCornerList.iterator();
         while ( iterator3.hasNext() != false){
        	 Corner2DModel cr = (Corner2DModel) iterator3.next();
        	 if(cr.associatePoint.getX() == anchor.dX)
        		 cr.setAssociatePoint(anchor.dX + dx, cr.associatePoint.getY());
         }
         anchor.dX = anchor.dX + dx;
	  }

      // Move grid in the y-direction ...

      if( this.getGridLineDirection() == GridLineDirection.ParallelToXAxis && dy != 0.0){
	  System.out.println("Horizontal Grid Line(Y-axis) moved: ... ");
	  Iterator iterator = this.gridPointList.iterator();
	  while ( iterator.hasNext() != false ){
		  GridPoint vA = (GridPoint) iterator.next();
             vA.dY = vA.dY + dy;
	  }

	  Iterator iterator2 = this.AssociateSpaceList.iterator();
	  while ( iterator2.hasNext() != false){
		  Space2DModel sp = (Space2DModel) iterator2.next();
		  if(sp.getBoundingBox().getMaxY() == anchor.dY)
			  sp.getBoundingBox().set(sp.getBoundingBox().getMaxX(), anchor.dY + dy, 
				  sp.getBoundingBox().getMinX(), sp.getBoundingBox().getMinY());
		  if(sp.getBoundingBox().getMinY() == anchor.dY)
			  sp.getBoundingBox().set(sp.getBoundingBox().getMaxX(), sp.getBoundingBox().getMaxY(), 
				  sp.getBoundingBox().getMinX(), anchor.dY + dy);
	  }
	  Iterator iterator3 = this.AssociateCornerList.iterator();
      while ( iterator3.hasNext() != false){
     	 Corner2DModel cr = (Corner2DModel) iterator3.next();
     	 if(cr.associatePoint.getY() == anchor.dY)
     		 cr.setAssociatePoint(cr.associatePoint.getX(), anchor.dY + dy );
      }
	  anchor.dY = anchor.dY + dy;
     }

     if ( DEBUG == true ) {
        System.out.println("*** ");
        System.out.println("*** In GridLineMode.moved() ... start    ");
        System.out.println("*** =========================================== ");
        System.out.println("*** Printing: this.gridPointList.toString() ... ");
        System.out.println("*** ");

        System.out.println( this.gridPointList.toString() );

        System.out.println("*** ");
        System.out.println("*** =========================================== ");
        System.out.println("*** In GridLineMode.moved() ... end      ");
        System.out.println("*** ");
     }

     notifyListeners();
  }

  private void notifyListeners() {
     GridLineEvent evt = new GridLineEvent(this);
     for (FloorplanModelListener aListener : listeners) {
          aListener.modelMoved(evt);
     }
  }

  // ===================================================================
  // Exercise routines in the coordinate line ...
  // ===================================================================

  public static void main ( String args [] ) {

     // Small set of test vertices ....

     GridPoint gp1 = new GridPoint ( 1.0, 2.0, GridPointType.Vertex );
     GridPoint gp2 = new GridPoint ( 2.0, 2.0, GridPointType.Vertex );
     GridPoint gp3 = new GridPoint ( 4.0, 2.0, GridPointType.Vertex );

     // Create two coordinate lines ....

     GridLineModel line1 = new GridLineModel( "Frame 1", GridLineDirection.ParallelToXAxis );
     line1.setAnchorPoint( new Coordinate ( 0.0, 2.0) );
     line1.add( gp3 ); 
     line1.add( gp2 ); 
     line1.add( gp1 ); 
     System.out.println( line1.toString() );
  }
}

