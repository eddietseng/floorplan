/*
 * ===================================================================
 * Tree.java: Create a tree ...
 * ===================================================================
 */

package model.urban;

import model.*;
import model.primitive.*;

public class Tree extends AbstractCompoundFeature {

   // Constructor methods ...

   public Tree() {}

   public Tree ( double dX, double dY, double dRadius ) {
      super ( dX, dY );
      setWidth( 2.0*dRadius );
      setHeight( 2.0*dRadius );

      // Create vertex for tree schematic ...

      Circle node01 = new Circle( "base", dX, dY );
      node01.setRadius( dRadius );

      // Initialize envelope ...
    
      be.addPoint( dX - dRadius, dY - dRadius );
      be.addPoint( dX + dRadius, dY + dRadius );

      // Add point to the model ...

      items.put( node01.getName(), node01 );
   }

   // Accept method for Feature Element visitor ...

   public void accept( FeatureElementVisitor visitor ) {
      visitor.visit(this);
   }

   // Create string representation of tree model ....

   public String toString() {
      String s = "Tree: Name = " + sName + "\n";
      s = s    + "     " + "Coordinate(x,y) = (" + getX() + "," + getY() + ")";

      return s;
   }
}
