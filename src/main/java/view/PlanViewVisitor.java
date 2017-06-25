/*
 *  ==========================================================================
 *  PlanViewVisitor.java: Visit composite hierarchies and create 2-dimensional
 *  views....
 *
 *  Written by: Mark Austin                                         July, 2012
 *  ==========================================================================
 */

package view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.awt.*;
import java.awt.geom.*;  // Needed for affine transformation....
import java.awt.Point;
import java.awt.BasicStroke;

import model.*;
import model.primitive.*;
import view.shape.ArcShape;
import view.shape.CircleShape;
import view.shape.PointShape;
import view.shape.PolygonShape;
import view.shape.LineShape;
 
public class PlanViewVisitor implements FeatureElementVisitor {
   private AffineTransform at;
   private Graphics2D     g2D;
   private boolean DEBUG = false;
   protected boolean displayTag;

   // Constructor method ....

   public PlanViewVisitor() {}
   
   public PlanViewVisitor( boolean displayTag )
   {
	  this.displayTag = displayTag;
   }

   // Visitor methods ....

   public void visit( AbstractCompoundFeature compound ) {
      draw ( (AbstractCompoundFeature) compound );
   }

   public void visit( AbstractFeature poly ) {
      draw ( (AbstractFeature) poly );
   }

   public void visit( CompositeHierarchy composite ) {
      draw ( composite );
   }

   // Increment coordinate system at beginning of composite hieararchy.

   public void start( CompositeHierarchy composite ) {

      // Set affine tranformation and 2D graphics handle ..

      this.at  = composite.at;
      this.g2D = composite.g2D;

      if ( DEBUG == true ) {
         System.out.println("");
         System.out.println("*** Start Composite Hierarchy ... ");
      }

      // Increment transformation parameters ...

      composite.transformationIncrement();
   }

   // Decrement coordinate system at end of composite hieararchy.

   public void finish( CompositeHierarchy composite ) {
      composite.transformationDecrement();

      if ( DEBUG == true ) {
         System.out.println("*** Finish Composite Hierarchy ... ");
         System.out.println("");
      }
   }


   // ============================================================
   // Draw details of primative shapes ...
   // ============================================================

   public void draw( AbstractFeature af ) {

      if(DEBUG == true) {
         System.out.println("*** Enter PlanViewVisitor.draw( AbstractFeature af )...");
         System.out.println("*** af.getWidth() = " + af.getWidth() );
      }

      // Print local and global coordinates of anchor point ....

      double flatmatrix[] = new double[6];
      model.CompositeHierarchy.at.getMatrix( flatmatrix );
      double dGlobalX = flatmatrix[0]*af.getX() + flatmatrix[2]*af.getY() + flatmatrix[4];
      double dGlobalY = flatmatrix[1]*af.getX() + flatmatrix[3]*af.getY() + flatmatrix[5];

      g2D.translate( af.getX(), af.getY() );

      // Highlight shape when "cursor over shape" ....

      if (af.getShapeActive() == false)
         g2D.setColor( af.getColor() );
      else 
         g2D.setColor( Color.red );

      // Draw a circle .....

      if(af instanceof model.primitive.Circle) {
         Circle c = (model.primitive.Circle) af;
         CompoundShape ps = new CircleShape( (int) 0, (int) 0, (int) c.getRadius() );

         // Set flag for filled shape ...

         if (af.getFilledShape() == false)
            g2D.draw( ps.getPath() );
         else
            g2D.fill( ps.getPath() );
      }

      // Draw an edge .....

      if(af instanceof model.primitive.Edge) {
         model.primitive.Edge e = (model.primitive.Edge) af;
      }

      // Draw a primitive point .....

      if(af instanceof model.primitive.Point) {
         model.primitive.Point p = (model.primitive.Point) af;
         CompoundShape ps = new PointShape( (int) (-p.getWidth()/2.0),
                                            (int) (-p.getHeight()/2.0),
                                            (int) p.getWidth(),
                                            (int) p.getHeight() );

         // Set flag for filled shape ...

         if (af.getFilledShape() == false)
            g2D.draw( ps.getPath() );
         else
            g2D.fill( ps.getPath() );

      }

      // Draw a primitive simple polygon .....

      if(af instanceof model.primitive.SimplePolygon) {
         model.primitive.SimplePolygon sp = (model.primitive.SimplePolygon) af;
         double x[] = sp.getXcoord();
         double y[] = sp.getYcoord();

         int xcoord[] = new int [ x.length ];
         int ycoord[] = new int [ y.length ];
         for (int i = 0; i < x.length; i = i + 1 ) {
            xcoord[i] = (int) x[i];
            ycoord[i] = (int) y[i];
         }

         CompoundShape ps = new PolygonShape( xcoord, ycoord );

         // Set flag for filled shape ...

         if (af.getFilledShape() == false)
            g2D.draw( ps.getPath() );
         else
            g2D.fill( ps.getPath() );
      }

      // Add label to shape ....

      if (af.getName() != null && displayTag ) {
          g2D.translate(  af.getTextOffSetX(),  af.getTextOffSetY() );
          g2D.scale( 1, -1 );
          g2D.drawString( af.getName(), 0, 0 );
          g2D.scale( 1, -1 );
          g2D.translate( -af.getTextOffSetX(), -af.getTextOffSetY() );
      }

      g2D.translate( -af.getX(),  -af.getY() );

      // Reset affine transformation ....

      if(DEBUG == true) {
         System.out.printf("-- node ---------------------------------- \n" );
         System.out.printf("Local  (x,y) = ( %7.1f, %7.1f )\n", af.getX(), af.getY() );
         System.out.printf("Global (x,y) = ( %7.1f, %7.1f )\n", dGlobalX, dGlobalY );
         System.out.println("*** Leave PlanViewVisitor.draw( AbstractFeature af )...");
      }
   }

   // ============================================================
   // Draw details of a compound feature ...
   // ============================================================

   public void draw( AbstractCompoundFeature af ) {

      // Print local and global coordinates of anchor point ....

      double flatmatrix[] = new double[6];
      model.CompositeHierarchy.at.getMatrix( flatmatrix );
      double dGlobalX = flatmatrix[0]*af.getX() + flatmatrix[2]*af.getY() + flatmatrix[4];
      double dGlobalY = flatmatrix[1]*af.getX() + flatmatrix[3]*af.getY() + flatmatrix[5];

      if(DEBUG == true) {
         System.out.println("*** Enter PlanViewVisitor.draw( AbstractCompoundFeature af )...");
      }

      // Highlight compound feature shape when "cursor over shape" ....

      if (af.getShapeActive() == false)
         g2D.setColor( af.getColor() );
      else 
         g2D.setColor( Color.red );
      
      // ==================================================
      // Draw primitive feature items ordered by name ....
      // ==================================================

      TreeMap icopy = new TreeMap( af.items );
      Set st = icopy.keySet();
      Iterator itr = st.iterator();
      while (itr.hasNext()) {
         String key = (String) itr.next();
         Feature fm = (Feature) icopy.get(key);

         g2D.translate( fm.getX(),  fm.getY() );

         if(fm instanceof model.primitive.Circle) {
            model.primitive.Circle c = (model.primitive.Circle) fm;
            CompoundShape ps = new CircleShape( (int) 0, (int) 0, (int) c.getRadius() );

            // Draw either a filled shape of shape boundary ...

            if (af.getFilledShape() == false)
               g2D.draw( ps.getPath() );
            else
               g2D.fill( ps.getPath() );
         }
         
         if(fm instanceof model.primitive.Arc) {
             model.primitive.Arc c = (model.primitive.Arc) fm;
             CompoundShape ps = new ArcShape( (int) 0, (int) 0, (int) c.getRadius(), (int) c.getstartA(), (int)c.getextA() );

             // Draw either a filled shape of shape boundary ...

             if (af.getFilledShape() == false)
                g2D.draw( ps.getPath() );
             else
                g2D.fill( ps.getPath() );
          }

         if(fm instanceof model.primitive.Point) {
            model.primitive.Point p = (model.primitive.Point) fm;
            CompoundShape ps = new PointShape( (int) (-p.getWidth()/2.0),
                                               (int) (-p.getHeight()/2.0),
                                               (int) p.getWidth(),
                                               (int) p.getHeight() );

            // Draw either a filled shape of shape boundary ...

            if (af.getFilledShape() == false)
               g2D.draw( ps.getPath() );
            else
               g2D.fill( ps.getPath() );
         }

         if(fm instanceof model.primitive.Edge) {
            model.primitive.Edge e = (model.primitive.Edge) fm;
            CompoundShape ps = new LineShape( e.getX1(), e.getY1(), e.getX2(), e.getY2(), e.getThickness() );

            // Draw either a filled shape of shape boundary ...

            if (af.getFilledShape() == false)
               g2D.draw( ps.getPath() );
            else
               g2D.fill( ps.getPath() );
         }

         if(fm instanceof model.primitive.SimplePolygon) {
            model.primitive.SimplePolygon sp = (model.primitive.SimplePolygon) fm;
            double x[] = sp.getXcoord();
            double y[] = sp.getYcoord();

            int xcoord[] = new int [ x.length ];
            int ycoord[] = new int [ y.length ];
            for (int i = 0; i < x.length; i = i + 1 ) {
               xcoord[i] = (int) x[i];
               ycoord[i] = (int) y[i];
            }

            CompoundShape ps = new PolygonShape( xcoord, ycoord );

            // Draw either a filled shape of shape boundary ...

            if (af.getFilledShape() == false)
               g2D.draw( ps.getPath() );
            else
               g2D.fill( ps.getPath() );
         }

         g2D.translate( -fm.getX(),  -fm.getY() );
      }

      // Add label to shape ....

      g2D.translate(  af.getX(),   af.getY() );
      if (af.getName() != null && af.getDisplayName() == true && displayTag ) {
         g2D.translate(  af.getTextOffSetX(),  af.getTextOffSetY() );
         g2D.scale( 1, -1 );
         g2D.drawString( af.getName(), 0, 0 );
         g2D.scale( 1, -1 );
         g2D.translate( -af.getTextOffSetX(), -af.getTextOffSetY() );
      }

      g2D.translate( -af.getX(),  -af.getY() );

      if(DEBUG == true) {
         System.out.printf("Name = %s\n", af.getName() );
         System.out.printf("Local  (x,y) = ( %7.1f, %7.1f )\n", af.getX(), af.getY() );
         System.out.printf("Global (x,y) = ( %7.1f, %7.1f )\n", dGlobalX, dGlobalY );
      }
   }

   // ============================================================
   // Draw details of a composite hierarchy ...
   // ============================================================

   public void draw( CompositeHierarchy composite ) {}
   
   public void setTagDisplay( boolean displayTag )
   {
	   this.displayTag = displayTag;
   }

}
