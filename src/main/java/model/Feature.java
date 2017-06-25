/*
 * ===================================================================
 * Feature.java: Geographic feature interface ...
 * ===================================================================
 */

package model;

import java.awt.geom.*;   // Needed for affine transformation....
import java.awt.Color;    // Needed for feature color ...

public interface Feature extends Cloneable {
   public void    setName( String sName );
   public String  getName();
   public void    setX( double dX );
   public double  getX();
   public void    setY( double dY );
   public double  getY();
   public void    setColor( Color c );
   public Color   getColor();
   public void    setSelection( boolean b );
   public boolean getSelection();
   public void    accept( FeatureElementVisitor visitor );
   public void    search( AffineTransform at, int dx, int dy ); 
}
