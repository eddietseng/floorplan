/**
  *  =====================================================================
  *  FanModel.java: Construct a simple 2D model for a House.
  *
  *     @param x the left of the bounding rectangle
  *     @param y the top of the bounding rectangle
  *     @param width the width of the bounding rectangle
  *  =====================================================================
  */

package model.hvac;

import model.*;
import model.primitive.*;

public class FanModel extends AbstractCompoundFeature {

   public FanModel( double x, double y, double width) {

      // Create label for the house model ...

      setName( "Fan Model" );

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
      Point node01 = new Point( "n01",         x,             y, pSize, pSize );
      Point node02 = new Point( "n02", x + width,             y, pSize, pSize );
      Point node03 = new Point( "n03", x + width, y + width/2.0, pSize, pSize );
      Point node04 = new Point( "n04", x + width,     y + width, pSize, pSize );
      Point node05 = new Point( "n05",         x,     y + width, pSize, pSize );
      Point node06 = new Point( "n06",         x, y + width/2.0, pSize, pSize );

      Point  fan07 = new Point( "f07", x +     width/2.0, y + 0.50*width, pSize/2, pSize/2 );
      Point  fan08 = new Point( "f08", x +     width/3.0, y + 0.85*width, pSize/2, pSize/2 );
      Point  fan09 = new Point( "f09", x + 2.0*width/3.0, y + 0.85*width, pSize/2, pSize/2 );
      Point  fan10 = new Point( "f10", x +     width/3.0, y + 0.15*width, pSize/2, pSize/2 );
      Point  fan11 = new Point( "f11", x + 2.0*width/3.0, y + 0.15*width, pSize/2, pSize/2 );

      // Connect edges for exterior of fan ...

      Edge edge01 = new Edge( "e01",  node01, node02 );
      edge01.setThickness( pSize );
      Edge edge02 = new Edge( "e02",  node02, node04 );
      edge02.setThickness( pSize );
      Edge edge03 = new Edge( "e03",  node04, node05 );
      edge03.setThickness( pSize );
      Edge edge04 = new Edge( "e04",  node05, node06 );
      edge04.setThickness( pSize );
      Edge edge05 = new Edge( "e05",  node06, node01 );
      edge05.setThickness( pSize );
      Edge edge06 = new Edge( "e06",  node06, node03 );
      edge06.setThickness( pSize/2 );

      // Connect edges for fan interior ...

      Edge edge07 = new Edge( "e07",   fan07,  fan08 );
      edge07.setThickness( pSize/2 );
      Edge edge08 = new Edge( "e08",   fan08,  fan09 );
      edge08.setThickness( pSize/2 );
      Edge edge09 = new Edge( "e09",   fan07,  fan09 );
      edge09.setThickness( pSize/2 );
      Edge edge10 = new Edge( "e10",   fan07,  fan10 );
      edge10.setThickness( pSize/2 );
      Edge edge11 = new Edge( "e11",   fan10,  fan11 );
      edge11.setThickness( pSize/2 );
      Edge edge12 = new Edge( "e12",   fan11,  fan07 );
      edge12.setThickness( pSize/2 );

      // Add points to the model ...

      items.put( node01.getName(), node01 );
      items.put( node02.getName(), node02 );
      items.put( node03.getName(), node03 );
      items.put( node04.getName(), node04 );
      items.put( node05.getName(), node05 );
      items.put( node06.getName(), node06 );

      items.put( fan07.getName(), fan07 );
      items.put( fan08.getName(), fan08 );
      items.put( fan09.getName(), fan09 );
      items.put( fan10.getName(), fan10 );
      items.put( fan11.getName(), fan11 );

      // Add edges to the model ...

      items.put( edge01.getName(), edge01 );
      items.put( edge02.getName(), edge02 );
      items.put( edge03.getName(), edge03 );
      items.put( edge04.getName(), edge04 );
      items.put( edge05.getName(), edge05 );
      items.put( edge06.getName(), edge06 );

      items.put( edge07.getName(), edge07 );
      items.put( edge08.getName(), edge08 );
      items.put( edge09.getName(), edge09 );
      items.put( edge10.getName(), edge10 );
      items.put( edge11.getName(), edge11 );
      items.put( edge12.getName(), edge12 );
   }

   // Accept method for Feature Element visitor ...

   public void accept( FeatureElementVisitor visitor) {
        visitor.visit(this);
   }
}
