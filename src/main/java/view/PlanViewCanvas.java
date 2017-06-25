/*
 * ============================================================================
 * PlanViewCanvas.java:
 *
 * ============================================================================
 */ 

package view;

import java.awt.*;
import java.awt.geom.*;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.event.MouseInputAdapter;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.List;
import java.lang.Math;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import model.*;
import model.util.*;
import model.CompositeHierarchy;

public class PlanViewCanvas extends JPanel {
    private boolean DEBUG   = false;
    private Dimension          size;
    private PlanViewPanel       pvp;
    public  AffineTransform      at;
    public  Graphics2D          g2D;
    int     width, height;
    int     iBorder = 25;
    private boolean drawDisabled     = false;
    private boolean queryEnabled     = false;
    private boolean lineEnabled      = false;
    private boolean rectangleEnabled = false;
    private boolean selectAndMove    = false;
    public int startX    = 0;
    public int startY    = 0;
    public int currentX  = 0;
    public int currentY  = 0;
    public boolean firstPoint = true;

    public static int paintCount = 0;

    // Workspace data ....

    private HashMap grid = null;
    
    // Tag Display
    private boolean displayTag = false;

    // Workspace composite hierarchies ....

    private CompositeHierarchy  gridWorkspace = null;
    private CompositeHierarchy metroWorkspace = null;
    private CompositeHierarchy trackWorkspace = null;
    private CompositeHierarchy  areaWorkspace = null;
    private CompositeHierarchy trainWorkspace = null;

    private CompositeHierarchy  buildingWorkspace;
    private CompositeHierarchy floorplanWorkspace;

    // Default coordinates and gridsize ....

    private double MinX =  -300.0;
    private double MinY =  -150.0;
    private double MaxX =   600.0;
    private double MaxY =   500.0;

    private int GridSize = 40;

    // Constructor method ....
    
    public PlanViewCanvas( PlanViewPanel pvp ) {
       this.pvp = pvp;
    }

    // ==================================================================
    // Set composite hierarchy data ...
    // ==================================================================

    public void setMetroGridData ( HashMap grid ) {
       this.grid = grid;
    }
    
    public void setDisplayTag ( boolean displayTag ) {
    	System.out.println( "PlanViewCanvas.setDisplayTag " + displayTag );
    	this.displayTag = displayTag;
    	System.out.println( "PlanViewCanvas.setDisplayTag " + this.displayTag );
    }
    
    // ==================================================================
    // Set composite hierarchy workspaces ...
    // ==================================================================

    public void setMetroGridWorkspace ( CompositeHierarchy workspace ) {
       this.gridWorkspace = workspace;
    }

    public void setMetroSystemWorkspace ( CompositeHierarchy workspace ) {
       this.metroWorkspace = workspace;
    }

    public void setMetroTrackWorkspace ( CompositeHierarchy workspace ) {
       this.trackWorkspace = workspace;
    }

    public void setMetroAreaWorkspace ( CompositeHierarchy workspace ) {
       this.areaWorkspace = workspace;
    }

    public void setMetroTrainsWorkspace( CompositeHierarchy workspace ) {
       this.trainWorkspace = workspace;
    }

    public void setBuildingLayoutWorkspace ( CompositeHierarchy workspace ) {
       this.buildingWorkspace = workspace;
    }

    public void setFloorplanWorkspace ( CompositeHierarchy workspace ) {
       this.floorplanWorkspace = workspace;
    }

    // Draw details of metro system, metro area, train capacity and 
    // operating cost....

    public void drawTrainSystem() {
       PrintViewDetailsVisitor cpv = new PrintViewDetailsVisitor();
       trainWorkspace.accept( cpv );
    }

    public void drawTrainOperations() {
       PrintViewOperationsVisitor cpv = new PrintViewOperationsVisitor();
       trainWorkspace.accept( cpv );
    }

    // Set boolean variables for supporting graphical operations ...

    private boolean drawGrid            = false; // Flag for drawing grid ...
    private boolean drawMetroSystem     = false; // Flag for drawing metro system ...
    private boolean drawMetroArea       = false; // Flag for drawing metro area ...
    private boolean drawTrainSystem     = false; // Flag for drawing train system ...
    private boolean drawBuildingLayout  = false; // Flag for building-hvac layouts ...
    private boolean drawFloorplanLayout = false; // Flag for floorplan layouts ...

    // ======================================================
    // Methods to set drawgrid display ....
    // ======================================================

    public void setDrawGrid( boolean drawGrid ) {
        this.drawGrid = drawGrid;
    }

    // Set/get workspace display options ....

    public void setGridDisplay() {
       if( drawGrid == false )
           drawGrid = true;
       else
           drawGrid = false;

       repaint();
    }

    public boolean getGridDisplay() {
       return drawGrid;
    }

    // ======================================================
    // Methods to switch mode of canvas interactions ...
    // ======================================================

    public void setDrawDisabled() {
       this.drawDisabled     =  true;
       this.queryEnabled     = false;
       this.lineEnabled      = false;
       this.rectangleEnabled = false;
       this.selectAndMove    = false;
    }

    public void setQueryEnabled() {
       this.drawDisabled     = false;
       this.queryEnabled     =  true;
       this.lineEnabled      = false;
       this.rectangleEnabled =  true;
       this.selectAndMove    = false;
    }

    public void setLineEnabled() {
       this.queryEnabled     = false;
       this.lineEnabled      =  true;
       this.rectangleEnabled = false;
    }

    public boolean getLineEnabled() {
       return this.lineEnabled;
    }

    public void setRectangleEnabled() {
       this.rectangleEnabled =  true;
       this.lineEnabled      = false;
    }

    public boolean getRectangleEnabled() {
       return this.rectangleEnabled;
    }

    public void setSelectAndMove() {
    }
    
    // Change the status of the floorplan component and repaint 
    public void setFloorplanSelectedStatus ( AbstractCompoundFeature component ) {
       component.switchActivity();
       repaint();
    }

    // ======================================================
    // Methods to set/get applications display ....
    // ======================================================

    public void setMetroSystemDisplay() {
       if( drawMetroSystem == false )
           drawMetroSystem = true;
       else
           drawMetroSystem = false;

       repaint();
    }

    public boolean getMetroSystemDisplay() {
       return drawMetroSystem;
    }

    public void setMetroAreaDisplay() {
       if( drawMetroArea == false )
           drawMetroArea = true;
       else
           drawMetroArea = false;

       repaint();
    }

    public boolean getMetroAreaDisplay() {
       return drawMetroArea;
    }

    public void setTrainSystemDisplay() {
       if( drawTrainSystem == false )
           drawTrainSystem = true;
       else
           drawTrainSystem = false;

       repaint();
    }

    public boolean getTrainSystemDisplay() {
       return drawTrainSystem;
    }

    // ======================================================
    // Control of building layout display ....
    // ======================================================

    public void setBuildingLayoutDisplay() {
       if( drawBuildingLayout == false )
           drawBuildingLayout = true;
       else
           drawBuildingLayout = false;

       repaint();
    }

    public boolean getBuildingLayoutDisplay() {
       return drawBuildingLayout;
    }

    // ======================================================
    // Control of floorplan layout display ....
    // ======================================================

    public void setFloorplanDisplay() {
       if( drawFloorplanLayout == false )
           drawFloorplanLayout = true;
       else
           drawFloorplanLayout = false;

       repaint();
    }

    public boolean getFloorplanDisplay() {
       return drawFloorplanLayout;
    }

    // ======================================================
    // Paint panel ...
    // ======================================================

    public void paint() {
       Graphics g = getGraphics();
       super.paintComponent(g);
       paintComponent(g);
    }

    public void update(Graphics g) {}

    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        g2D = (Graphics2D) g.create();
        g2D.setBackground( Color.white );
        width  = getWidth();
        height = getHeight();

        // Transform origin to lower right-hand corner ..

        at = new AffineTransform();
        at.translate(  iBorder, height - iBorder );
        at.scale( 1, -1);
        at.translate( -MinX, -MinY );
        g2D.setTransform (at);

        gridWorkspace.setAffineTransform( at );
        gridWorkspace.setGraphics2D( g2D );

        // Draw metro area objects ...

        areaWorkspace.setAffineTransform( at );
        areaWorkspace.setGraphics2D( g2D );

        trackWorkspace.setAffineTransform( at );
        trackWorkspace.setGraphics2D( g2D );

        metroWorkspace.setAffineTransform( at );
        metroWorkspace.setGraphics2D( g2D );

      // Draw metro workspaces ...

      PlanViewVisitor pvg = new PlanViewVisitor( displayTag );
      
      //pvg.setTagDisplay( displayTag );

      if ( getGridDisplay() == true ) {
         gridWorkspace.accept( pvg );
      }

      if ( getMetroSystemDisplay() == true ) {
         metroWorkspace.accept( pvg );
         trackWorkspace.accept( pvg );
      }

      if ( getMetroAreaDisplay() == true ) {
         areaWorkspace.accept( pvg );
      }

      // Draw building layout workspace ...

      if ( getBuildingLayoutDisplay() == true ) {
         buildingWorkspace.accept( pvg );
      }

      // Draw floorplan workspace ...

      if ( getFloorplanDisplay() == true ) {
         floorplanWorkspace.accept( pvg );
      }

      // Draw dashed rectangle/line ... 

      if ( getRectangleEnabled() == true && firstPoint == false )
           drawRubberRectangle(at);

      if ( getLineEnabled() == true && firstPoint == false )
           drawRubberLine(at);
    }

    // ------------------------------------------------------
    // Get max/min graphics coordinates and grid size ...
    // ------------------------------------------------------

    public boolean getSnapToGrid() {
       boolean snap = false;

       if ( grid != null ) {
          Boolean temp = (Boolean) grid.get("SnapToGrid");
          snap = temp.booleanValue();
       }

       return snap;
    }

   public int getGridSize() {
      int size = GridSize;

      if ( grid != null ) {
         Integer temp = (Integer) grid.get("GridSize");
         size = temp.intValue();
      }

      return size;
   }

   public double getMinX() { return MinX; }
   public double getMinY() { return MinY; }
   public double getMaxX() { return MaxX; }
   public double getMaxY() { return MaxY; }

   // ------------------------------------------------------
   // Get composite hierarchy base ....
   // ------------------------------------------------------

   public CompositeHierarchy getCompositeHierarchyMetro() {
      return metroWorkspace;
   }

   public CompositeHierarchy getCompositeHierarchyArea() {
      return areaWorkspace;
   }

   public CompositeHierarchy getCompositeHierarchyBuilding() {
      return buildingWorkspace;
   }

   public PlanViewPanel getPlanViewPanel() {
      return this.pvp;
   }

   // ======================================================
   // Draw a rubber line, mouse down, tracks mouse
   // ======================================================

   public void drawRubberLine( AffineTransform at ) {

      if ( DEBUG == true ) {
         System.out.println("... Enter drawRubberLine( AffineTransform at ) ... ");
      }

      // Draw new dashed line: (x0T,y0T) to (x2T,y2T) ....

      int x0T = startX; int x1T = currentX;
      int y0T = startY; int y1T = currentY;

      g2D.setColor(Color.black);

      if ( y0T == y1T) { // Horizontal line .....
         for( int xc = Math.min( x0T, x1T); xc < Math.max(x0T, x1T) -3; xc=xc+8)
               g2D.drawLine(  xc, y0T, xc+4, y0T);
      } else if ( x0T == x1T) { // Vertical line ....
          for( int yc = Math.min( y0T, y1T); yc < Math.max(y0T, y1T) -3; yc=yc+8) 
               g2D.drawLine( x0T,  yc,  x0T, yc+4);
      } else {
          double theta  = Math.atan2( y1T-y0T, x1T-x0T );
          double length = Math.sqrt ( (x0T-x1T)*(x0T-x1T) + (y0T-y1T)*(y0T-y1T) );

          for( int i = 0; i <= length; i = i + 8 ) {
               int x1 = x0T + (int) (((double) i)*Math.cos(theta));
               int x2 = x0T + (int) (((double) (i+4))*Math.cos(theta));
               int y1 = y0T + (int) (((double) i)*Math.sin(theta));
               int y2 = y0T + (int) (((double) (i+4))*Math.sin(theta));

               g2D.drawLine( x1, y1, x2, y2 );
          }
      }
   }

   // ======================================================
   // Draw a rubber rectangle, mouse down, tracks mouse
   // ======================================================

   public void drawRubberRectangle( AffineTransform at ) {

      if ( DEBUG == true ) {
         System.out.println("... Enter drawRubberRectangle( AffineTransform at ) ... ");
      }

      // Draw new rubber rectangle

      int x0T = startX; int x1T = currentX;
      int y0T = startY; int y1T = currentY;

      g2D.setColor(Color.black);
      for( int xc = Math.min( x0T, x1T); xc < Math.max(x0T, x1T) -3; xc=xc+8) {
          g2D.drawLine(  xc, y0T, xc+4, y0T);
          g2D.drawLine(  xc, y1T, xc+4, y1T);
      }
      for( int yc = Math.min( y0T, y1T); yc < Math.max(y0T, y1T) -3; yc=yc+8) {
          g2D.drawLine( x0T,  yc,  x0T, yc+4);
          g2D.drawLine( x1T,  yc,  x1T, yc+4);
      }
   }
}

// ===============================================================
// Handle mouse events on the PlanView canvas ..... 
// ===============================================================

class PlanViewCanvasListener extends MouseInputAdapter {
   private boolean DEBUG = false;
   PlanViewCanvas                   pvc;
   CompositeHierarchy    metroWorkspace;
   CompositeHierarchy     areaWorkspace;
   CompositeHierarchy buildingWorkspace;
   int iBorder = 25;
   int startX    = 0;
   int startY    = 0;
   int currentX  = 0;
   int currentY  = 0;
   boolean firstPoint = true;

   public PlanViewCanvasListener( PlanViewCanvas pvc ) {
      this.pvc  = pvc;   
   }

   // Record start and current coordinates ....

   public void startMotion( int x, int y ) {
       pvc.startX = startX = x;
       pvc.startY = startY = y;
       pvc.currentX  = currentX  = x;
       pvc.currentY  = currentY  = y;
       pvc.firstPoint = firstPoint = false;
   }

   public  void currentMotion( int x, int y ) {
      currentX = x;  currentY = y;
   }

   // Snap (x,y) coord to nearest coord .....

   public void snapxy( int x[], int y[]) {
      int gridSize = (int) pvc.getGridSize();
      int xg, yg;

      if ( x[0] >= 0 )
         xg = (x[0]+gridSize/2)/gridSize;
      else
         xg = (x[0]-gridSize/2)/gridSize;

      if ( y[0] >= 0 )
         yg = (y[0]+gridSize/2)/gridSize;
      else
         yg = (y[0]-gridSize/2)/gridSize;

      x[0] = xg*gridSize;
      y[0] = yg*gridSize;
   }

   // ======================================================
   // Executed when the mouse is moved.
   // ======================================================

   public void mouseMoved(MouseEvent event ) {
      Point mousePoint = event.getPoint();
      boolean newOverShape = false;
      String overShapeName = null;
      double dVx, dVy;

      // Get point from mouse moved event and covert
      // to grid coordinates ....

      double dx = mousePoint.getX();
      double dy = mousePoint.getY();

      // Affine transformation for composite hierarchy search ...

      AffineTransform at = new AffineTransform();
      at.translate( iBorder, pvc.height - iBorder );
      at.scale( 1, -1 );
      at.translate( -pvc.getMinX(), -pvc.getMinY() );

      // Composite hierarchy searches ...

      metroWorkspace = pvc.getCompositeHierarchyMetro();
      if ( metroWorkspace != null && pvc.getMetroSystemDisplay() == true ) {
         metroWorkspace.setAffineTransform( at );
         metroWorkspace.search( at, (int) dx, (int) dy ); 
      }

      areaWorkspace = pvc.getCompositeHierarchyArea();
      if ( areaWorkspace != null && pvc.getMetroAreaDisplay() == true ) {
         areaWorkspace.setAffineTransform( at );
         areaWorkspace.search( at, (int) dx, (int) dy ); 
      }

      buildingWorkspace = pvc.getCompositeHierarchyBuilding();
      if ( buildingWorkspace != null && pvc.getBuildingLayoutDisplay() == true ) {
         buildingWorkspace.setAffineTransform( at );
         buildingWorkspace.search( at, (int) dx, (int) dy ); 
      }

      // Repaint the scene ....

      pvc.repaint();
   }

   public void mouseDragged (MouseEvent e) {
      int xx[] = new int[1];
      int yy[] = new int[1];
      xx[0] = e.getX();
      yy[0] = e.getY();

      double dx = e.getX();
      double dy = e.getY();

      double dLocalX = 0.0;
      double dLocalY = 0.0;

      if ( DEBUG == true ) {
         System.out.println("... In mouseDragged(): pvc.getSnapToGrid() = " + pvc.getSnapToGrid());
      }

      // Affine transformation for composite hierarchy search ...

      AffineTransform at = new AffineTransform();
      at.translate( iBorder, pvc.height - iBorder );
      at.scale( 1, -1 );
      at.translate( -pvc.getMinX(), -pvc.getMinY() );

      // Transform pixel coords to local coordinates ....
  
      try {
         double flatmatrix[] = new double[6];
         AffineTransform inverse = at.createInverse();
         inverse.getMatrix( flatmatrix );
         dLocalX = flatmatrix[0]*dx + flatmatrix[2]*dy + flatmatrix[4];
         dLocalY = flatmatrix[1]*dx + flatmatrix[3]*dy + flatmatrix[5];
      } catch (java.awt.geom.NoninvertibleTransformException e1 ){}

      xx[0] = (int) dLocalX;
      yy[0] = (int) dLocalY;

      // Snap cursor coordinate to grid ....

      if ( pvc.getSnapToGrid() == true )
           snapxy( xx, yy );

      // Record beginning of line/rectangle interaction ...

      if ( firstPoint == true )
           startMotion( xx[0], yy[0] );

      pvc.currentX = currentX = xx[0];
      pvc.currentY = currentY = yy[0];

      // Redraw the canvas ...

      pvc.repaint();
   }

   public void mouseReleased (MouseEvent e) {
      if ( pvc.firstPoint == false ) {
         System.out.printf("\n");
         System.out.printf("*** In MouseRelesed():   start (x,y) = (%6d,%6d)\n", startX, startY );
         System.out.printf("***                    current (x,y) = (%6d,%6d)\n", currentX, currentY );

         CoordinatePair pair = new CoordinatePair();
         pair.setX1 ( startX );
         pair.setY1 ( startY );
         pair.setX2 ( currentX );
         pair.setY2 ( currentY );

         if( pvc.getRectangleEnabled() == true )
             pair.setCoordinatePairType ( CoordinatePairType.Rectangle );
         if( pvc.getLineEnabled() == true )
             pair.setCoordinatePairType ( CoordinatePairType.LineSegment );

         pvc.getPlanViewPanel().getEngineeringController().changeFloorplanSelectionStatus( pair );
         pvc.firstPoint = firstPoint = true;
      }
   }

   public void mouseClicked (MouseEvent e) {}
   public void mousePressed (MouseEvent e) {}
   public void mouseEntered (MouseEvent e) {}
   public void mouseExited (MouseEvent e) {}
}
