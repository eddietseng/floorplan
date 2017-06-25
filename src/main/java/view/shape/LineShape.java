/**
  *  =====================================================================
  *  LineShape.java: Construct a line shape element ....
  *
  *     @param x the center left x coordinate ....
  *     @param y the center left y coordinate ....
  *     @param dX the change in x coordinate for the track element.
  *     @param dY the change in y coordinate for the track element.
  *  =====================================================================
  */

package view.shape;

import java.lang.Math;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import view.*;

public class LineShape extends CompoundShape {
   public LineShape( double x0, double y0, double x1, double y1, int iWidth ) {
      double dX0, dY0;
      double dX1, dY1;
      double dX2, dY2;
      double dX3, dY3;

      // Compute orientation of the track element ...

      double dAngle = getAngle( (double) (x1 - x0), (double) (y1 - y0) );

      // Set the initial coordinate of the GeneralPath

      dX0 = x0 + (iWidth/2.0)*Math.sin(dAngle);
      dY0 = y0 - (iWidth/2.0)*Math.cos(dAngle);

      dX1 = x0 - (iWidth/2.0)*Math.sin(dAngle);
      dY1 = y0 + (iWidth/2.0)*Math.cos(dAngle);

      dX2 = x1 - (iWidth/2.0)*Math.sin(dAngle);
      dY2 = y1 + (iWidth/2.0)*Math.cos(dAngle);

      dX3 = x1 + (iWidth/2.0)*Math.sin(dAngle);
      dY3 = y1 - (iWidth/2.0)*Math.cos(dAngle);

      // Create the track polygon ....

      path.moveTo( (int) dX0, (int) dY0 );
      path.lineTo( (int) dX1, (int) dY1 );
      path.lineTo( (int) dX2, (int) dY2 );
      path.lineTo( (int) dX3, (int) dY3 );
      path.closePath();
   }

   // Compute angle for coordinates in four quadrants ....

   public double getAngle( double dX, double dY ) {
       double angle = 0.0;

       if ( dY >= 0.0 && dX >= 0.0 )
            angle = Math.atan( dY/dX );
       if ( dY >= 0.0 && dX < 0.0 )
            angle = Math.PI + Math.atan( dY/dX );
       if ( dY  < 0.0 && dX < 0.0 )
            angle = Math.PI + Math.atan( dY/dX );
       if ( dY  < 0.0 && dX >= 0.0 )
            angle = 2*Math.PI + Math.atan( dY/dX );

       return angle;
   }
}
