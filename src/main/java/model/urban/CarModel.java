/**
  *  =====================================================================
  *  CarModel.java: Construct a 2D car model ...
  *  =====================================================================
  */

package model.urban;

import model.*;
import model.primitive.*;

public class CarModel extends AbstractCompoundFeature {

   public CarModel( double x, double y, double width) {

      // Save corner reference point and width of shape ...

      setX( (double) x );
      setY( (double) y );
      setHeight( (double) (1.0/2.0)*width );
      setWidth( (double) width );

      // Initialize envelope ...
    
      be.addPoint( x, y );
      be.addPoint( x + width, y + width/2.0 );

      // Create vertices for car schematic ...

      int pSize = 2;

      Point body01 = new Point( "b01",         x,     y + width/6.0, pSize, pSize );
      Point body02 = new Point( "b02",         x,     y + width/3.0, pSize, pSize );
      Point body03 = new Point( "b03", x + width,     y + width/3.0, pSize, pSize );
      Point body04 = new Point( "b04", x + width,     y + width/6.0, pSize, pSize );

      // The bottom of the front windshield
      Point roof01 = new Point( "r01", x + width*5/6, y + width/3.0, pSize, pSize );
      // The front of the roof
      Point roof02 = new Point( "r02", x + width*2/3, y + width/2.0, pSize, pSize );
      // The rear of the roof
      Point roof03 = new Point( "r03",   x + width/3, y + width/2.0, pSize, pSize );
      // The bottom of the rear windshield ...
      Point roof04 = new Point( "r04",   x + width/6, y + width/3.0, pSize, pSize );

      // Connect edges to vertices ....

      Edge edge01 = new Edge( "edge01",  body01, body02 );
      edge01.setThickness( 2 );
      Edge edge02 = new Edge( "edge02",  body02, body03 );
      edge02.setThickness( 2 );
      Edge edge03 = new Edge( "edge03",  body03, body04 );
      edge03.setThickness( 2 );
      Edge edge04 = new Edge( "edge04",  body04, body01 );
      edge04.setThickness( 2 );
      Edge edge05 = new Edge( "roof01",  roof01, roof02 );
      edge05.setThickness( 2 );
      Edge edge06 = new Edge( "roof02",  roof02, roof03 );
      edge06.setThickness( 2 );
      Edge edge07 = new Edge( "roof03",  roof03, roof04 );
      edge07.setThickness( 2 );

      // Model the car wheels with circles ...

      Circle wheel01 = new Circle( "wheel01",  x +   width/4, y + width/12.0 );
      wheel01.setRadius( width/12.0 );
      Circle wheel02 = new Circle( "wheel02",  x + width*3/4, y + width/12.0 );
      wheel02.setRadius( width/12.0 );

      // Add vertices to model ...

      items.put( body01.getName(), body01 );
      items.put( body02.getName(), body02 );
      items.put( body03.getName(), body03 );
      items.put( body04.getName(), body04 );
      items.put( roof01.getName(), roof01 );
      items.put( roof02.getName(), roof02 );
      items.put( roof03.getName(), roof03 );
      items.put( roof04.getName(), roof04 );

      // Add edges to model ...

      items.put( edge01.getName(), edge01 );
      items.put( edge02.getName(), edge02 );
      items.put( edge03.getName(), edge03 );
      items.put( edge04.getName(), edge04 );
      items.put( edge05.getName(), edge05 );
      items.put( edge06.getName(), edge06 );
      items.put( edge07.getName(), edge07 );

      // Add wheels to model ...

      items.put( wheel01.getName(), wheel01 );
      items.put( wheel02.getName(), wheel02 );
   }

   // Accept method for Feature Element visitor ...

   public void accept( FeatureElementVisitor visitor) {
        visitor.visit(this);
   }
}
