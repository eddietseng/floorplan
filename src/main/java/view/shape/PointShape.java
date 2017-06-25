/**
  *  ==========================================================================
  *  PointShape.java: Construct a small rectangular point shape centered at
  *  the centroid.
  *
  *  ==========================================================================
  */

package view.shape;

import java.awt.*;
import java.awt.geom.*;
import view.*;

public class PointShape extends CompoundShape {
   public PointShape( int width, int height ) {
      Rectangle2D.Double rect = new Rectangle2D.Double( -width/2.0, -height/2.0, (double) width, (double) height );
      add(rect);
   }

   public PointShape( int x, int y, int width, int height ) {
      Rectangle2D.Double rect = new Rectangle2D.Double( -width/2.0, -height/2.0, (double) width, (double) height );
      add(rect);
   }
}
