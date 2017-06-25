/**
  *  =====================================================================
  *  HouseModel.java: Construct a simple 2D model for a House.
  *
  *     @param x the left of the bounding rectangle
  *     @param y the top of the bounding rectangle
  *     @param width the width of the bounding rectangle
  *  =====================================================================
  */

package model.urban;

import model.*;
import model.primitive.*;

public class HouseModel extends AbstractCompoundFeature {

   public HouseModel( double x, double y, double width) {

      // Create label for the house model ...

      setName( "House Model" );

      // Save corner reference point for shape ...

      setX( (double) x );
      setY( (double) y );
      setHeight( (double) width );
      setWidth(  (double) width );

      // Initialize envelope ...
    
      be.addPoint( x, y );
      be.addPoint( x + width, y + width );

      // Create vertices for house schematic ...

      int pSize = 2;

      Point node01 = new Point(    "base01",             x,             y, pSize, pSize );
      Point node02 = new Point(    "base02",     x + width,             y, pSize, pSize );
      Point node03 = new Point( "ceiling01",             x, y + width/2.0, pSize, pSize );
      Point node04 = new Point( "ceiling02",     x + width, y + width/2.0, pSize, pSize );
      Point node05 = new Point(    "roof01", x + width/2.0,     y + width, pSize, pSize );

      // Connect edges to vertices ....

      Edge edge01 = new Edge( "edge01",  node01, node02 );
      edge01.setThickness( pSize );
      Edge edge02 = new Edge( "edge02",  node01, node03 );
      edge02.setThickness( pSize );
      Edge edge03 = new Edge( "edge03",  node02, node04 );
      edge03.setThickness( pSize );
      Edge edge04 = new Edge( "edge04",  node03, node04 );
      edge04.setThickness( pSize );
      Edge edge05 = new Edge( "edge05",  node03, node05 );
      edge05.setThickness( pSize );
      Edge edge06 = new Edge( "edge06",  node04, node05 );
      edge06.setThickness( pSize );

      // Add points to the model ...

      items.put( node01.getName(), node01 );
      items.put( node02.getName(), node02 );
      items.put( node03.getName(), node03 );
      items.put( node04.getName(), node04 );
      items.put( node05.getName(), node05 );

      // Add edges to the model ...

      items.put( edge01.getName(), edge01 );
      items.put( edge02.getName(), edge02 );
      items.put( edge03.getName(), edge03 );
      items.put( edge04.getName(), edge04 );
      items.put( edge05.getName(), edge05 );
      items.put( edge06.getName(), edge06 );
   }

   // Accept method for Feature Element visitor ...

   public void accept( FeatureElementVisitor visitor) {
        visitor.visit(this);
   }
}
