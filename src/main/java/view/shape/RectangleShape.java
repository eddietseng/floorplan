/**
  *  ==========================================================================
  *  RectangleShape.java: Construct a rectangle shape with bottom left-hand
  *  corner as the origin ...
  *
  *  ==========================================================================
  */

package view.shape;

import java.awt.*;
import java.awt.geom.*;
import view.*;

public class RectangleShape extends CompoundShape {
   public RectangleShape( int x, int y, int width, int height ) {

      // Now create shape with x = y = 0....

      Rectangle2D.Double rect = new Rectangle2D.Double( 0, 0, (double) width, (double) height );
      add(rect);
   }
}
