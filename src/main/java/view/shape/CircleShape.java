/**
  *  =====================================================================
  *  CircleShape.java: Construct a circle shape centered at (x,y) with 
  *                    radius r = width/2.
  *
  *  =====================================================================
  */

package view.shape;

import java.awt.*;
import java.awt.geom.*;
import view.*;

public class CircleShape extends CompoundShape {
   public CircleShape( int x, int y, int radius ) {
      Ellipse2D.Double circle = new Ellipse2D.Double( x-radius, y-radius, (double) 2*radius, (double) 2*radius );
      add(circle);
   }
}
