/**
  *  =====================================================================
  *  PolygonShape.java: Construct a house shape.
  *
  *     @param x the left of the bounding rectangle
  *     @param y the top of the bounding rectangle
  *     @param width the width of the bounding rectangle
  *
  *  =====================================================================
  */

package view.shape;

import java.lang.Math;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import view.*;

public class PolygonShape extends CompoundShape {
   public PolygonShape( int xPoints[], int yPoints[] ) {

      // Set the initial coordinate of the GeneralPath

      int x0 = xPoints[0];
      int y0 = yPoints[0];
      path.moveTo( x0, y0 );
        
      // Create polygon perimeter ...

      for ( int k = 1; k < xPoints.length; k++ ) {
         path.lineTo( xPoints[k], yPoints[k] );
      }
        
      // Close the shape

      path.closePath();
   }
}
