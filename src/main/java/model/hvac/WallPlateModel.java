/**
  *  =====================================================================
  *  WallPlateModel.java: Construct a simple schematic for a duct wall plate
  *
  *     @param x the left of the bounding rectangle
  *     @param y the top of the bounding rectangle
  *     @param width the width of the bounding rectangle
  *  =====================================================================
  */

package model.hvac;

import model.*;
import model.primitive.*;

public class WallPlateModel extends AbstractCompoundFeature {

   public WallPlateModel( double x, double y, double width) {

      // Create label for the house model ...

      setName( "WallPlate Model" );

      // Save corner reference point for shape ...

      setX( (double) x );
      setY( (double) y );
      setHeight( (double) width );
      setWidth(  (double) width );

      // Initialize envelope ...
    
      be.addPoint( x, y );
      be.addPoint( x + width, y + width );

      // Create vertices for house schematic ...

      int pSize = 4;
      Point node01 = new Point( "n01",         x,              y, pSize, pSize );
      Point node02 = new Point( "n02", x + width,              y, pSize, pSize );
      Point node03 = new Point( "n03", x + width, y + 0.25*width, pSize, pSize );
      Point node04 = new Point( "n04", x + width, y + 0.50*width, pSize, pSize );
      Point node05 = new Point( "n05", x + width, y + 0.75*width, pSize, pSize );
      Point node06 = new Point( "n06", x + width, y + 1.00*width, pSize, pSize );
      Point node07 = new Point( "n07",         x, y + 1.00*width, pSize, pSize );
      Point node08 = new Point( "n08",         x, y + 0.75*width, pSize, pSize );
      Point node09 = new Point( "n09",         x, y + 0.50*width, pSize, pSize );
      Point node10 = new Point( "n10",         x, y + 0.25*width, pSize, pSize );

      // Connect edges for wallplate exterior ....

      Edge edge01 = new Edge( "e01",  node01, node02 );
      edge01.setThickness( pSize );
      Edge edge02 = new Edge( "e02",  node02, node06 );
      edge02.setThickness( pSize );
      Edge edge03 = new Edge( "e03",  node06, node07 );
      edge03.setThickness( pSize );
      Edge edge04 = new Edge( "e04",  node07, node01 );
      edge04.setThickness( pSize );

      // Connect edges for wallplate interior ...

      Edge edge05 = new Edge( "e05",  node03, node10 );
      edge05.setThickness( pSize/2 );
      Edge edge06 = new Edge( "e06",  node04, node09 );
      edge06.setThickness( pSize/2 );
      Edge edge07 = new Edge( "e07",  node05, node08 );
      edge07.setThickness( pSize/2 );

      // Add points to the model ...

      items.put( node01.getName(), node01 );
      items.put( node02.getName(), node02 );
      items.put( node03.getName(), node03 );
      items.put( node04.getName(), node04 );
      items.put( node05.getName(), node05 );
      items.put( node06.getName(), node06 );
      items.put( node07.getName(), node07 );
      items.put( node08.getName(), node08 );
      items.put( node09.getName(), node09 );
      items.put( node10.getName(), node10 );

      // Add edges to the model ...

      items.put( edge01.getName(), edge01 );
      items.put( edge02.getName(), edge02 );
      items.put( edge03.getName(), edge03 );
      items.put( edge04.getName(), edge04 );
      items.put( edge05.getName(), edge05 );
      items.put( edge06.getName(), edge06 );
      items.put( edge07.getName(), edge07 );
   }

   // Accept method for Feature Element visitor ...

   public void accept( FeatureElementVisitor visitor) {
        visitor.visit(this);
   }
}
