/**
  * ================================================================================
  * CompoundShape.java: Model component shapes as objects of type GeneralPath.
  *
  * Written By: Mark Austin                                               July, 2012
  * ================================================================================
  */

package view;

import java.awt.*;
import java.awt.geom.*;

public abstract class CompoundShape {
   protected GeneralPath path;  // General path ...

   public CompoundShape() {
      path = new GeneralPath();
   }

   protected void add(Shape s) {
      path.append(s, false);
   }

   public GeneralPath getPath() {
      return path;
   }
}
