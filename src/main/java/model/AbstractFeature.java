/*
 * ===================================================================
 * AbstractFeature.java: Abstract class for geographic features ....
 * ===================================================================
 */

package model;

import java.awt.Color;
import java.awt.geom.*;  // Needed for affine transformation....
import java.awt.Point;

import model.rtree.HyperBoundingBox;
import model.rtree.HyperPoint;

public abstract class AbstractFeature implements Feature {
   protected Coordinate          c;        // Anchor point for feature ...
   protected BoundingEnvelope   be;
   protected String  sName  = null;        // Name of compound shape ....
   protected boolean displayName = true;   // Flag to display name (true by default)
   protected int textOffSetX =   0;        // x- offset for text label.
   protected int textOffSetY = -10;        // y- offset for text label.
   protected Color oldColor = Color.black; // Default color is black ...
   protected Color originalColor = null;   // Color of original floorplan components.
   protected Color    color = null;        // Color of abstract feature ...
   protected double height;                // Height of feature ...
   protected double  width;                // Width of feature ...
   protected boolean selected = false;
   private   GeneralPath  path;            // General path ...
   boolean filledShape = false;
   public boolean shapeActive = false;
   boolean DEBUG = false;

   public AbstractFeature () {
     c  = new Coordinate();
     be = new BoundingEnvelope();
   }

   public AbstractFeature ( double dX, double dY ) {
     c  = new Coordinate( dX, dY );
     be = new BoundingEnvelope( dX, dY );
   }

   // Methods to set/get the feature name ...

   public void setName( String sName ) {
      this.sName = sName;
   }

   public String getName() {
      return sName;
   }

   public void setDisplayName( boolean display ) {
      this.displayName = display;
   }

   public boolean getDisplayName() {
      return displayName;
   }

   // Methods to set/get the feature location ...

   public void setX( double dX ) {
      c.dX = dX;
   }

   public double getX() { 
      return c.dX;
   }

   public void setY( double dY ) {
      c.dY = dY;
   }

   public double getY() { 
      return c.dY;
   }

   // Methods to set/get the feature color ....

   public void setColor( Color color ) {
      this.color = color;
   }

   public Color getColor() {
      return color;
   }

   // Methods to get the feature coordinate ...

   public Coordinate getCoordinate() { 
      return c;
   }

   // Methods to set/get the feature height and width ...

   public void setHeight( double height ) {
     this.height = height;
   }

   public double getHeight() { 
     return height;
   }

   public void setWidth( double width ) {
     this.width = width;
   }

   public double getWidth() { 
     return width;
   }
   
   public double getArea() {
	 return ( this.height * this.width );
   }

   // Set/get x- and y-offsets for text string ...

   public void setTextOffSetX( int textOffSetX ) {
      this.textOffSetX = textOffSetX;
   }

   public void setTextOffSetY( int textOffSetY ) {
      this.textOffSetY = textOffSetY;
   }

   public int getTextOffSetX() {
      return textOffSetX;
   }

   public int getTextOffSetY() {
      return textOffSetY;
   }

   // Set/get filled shape flag ....

   public void setFilledShape ( boolean filledShape ) {
      this.filledShape = filledShape;
   }

   public boolean getFilledShape () {
      return filledShape;
   }

   // Methods to set/get feature selection ...

   public void setSelection(boolean b) {
      selected = b;
   }

   public boolean getSelection() {
      return selected;
   }
   
   public boolean getShapeActive() {
      return shapeActive;
   }
   
   // Highlighting ( or change the status of the )the component
   public void switchActivity() {
	  if ( selected == false ) {
		   this.originalColor = color;
		   if( this.color.equals( Color.red ) )
			   this.color           = Color.orange;
		   else
			   this.color           = Color.red;
           this.selected = true;
      } else {
           this.color               = this.originalColor;
           this.selected = false;
      }
   }

   // ========================================================================
   // Create placeholder search method ....
   // ========================================================================

   public void search ( AffineTransform at, int dx, int dy ) {
      double dLocalX = 0.0;
      double dLocalY = 0.0;

      // Transform pixel coords to local coordinates ....

        try {
           double flatmatrix[] = new double[6];
           AffineTransform inverse = at.createInverse();
           inverse.getMatrix( flatmatrix );
           dLocalX = flatmatrix[0]*dx + flatmatrix[2]*dy + flatmatrix[4];
           dLocalY = flatmatrix[1]*dx + flatmatrix[3]*dy + flatmatrix[5];
        } catch (java.awt.geom.NoninvertibleTransformException e){}

      // Save previous value of shapeActive ...

      boolean oldShapeActive = shapeActive;

      // Test for containment ...

      Point pt = new Point( (int) dLocalX, (int) dLocalY );
      if ( be.contains ( pt ) == true ) {
         shapeActive = true;
      }
      else
         shapeActive = false;

      // Look for a change in shape active status ...

      if ( shapeActive != oldShapeActive ) {
         System.out.println("*** In AbstractFeature.search(): Shape " + getName() + " is now " + shapeActive );
      }

   }
   
   /**
    * Return the polygon as String for JTS
    * @return String for JTS area calculation
    */
   public String getPolygonString(){
	   double x = this.c.getX();
	   double y = this.c.getY();
	   String s1 = "POLYGON((" + x +" "+ y +", " + x +" " + (y+width) +", " + (x+height) +" " + (y+width) +", " + (x+height) +" " + y + ", " + x +" "+ y +"))";
	   return s1;
   }

   // ========================================================================
   // Accept method for visiting descendants of AbstractFeature ...
   // ========================================================================
 
   public void accept( FeatureElementVisitor visitor ) {     
      visitor.visit( this );
   }

   // ========================================================================
   // Clone (i.e., deep copy) abstract feature contents ...
   // ========================================================================

   public AbstractFeature clone() throws CloneNotSupportedException {
      try {
          AbstractFeature cloned = (AbstractFeature) super.clone();
          cloned.c  = (Coordinate) this.c.clone();
          cloned.be = (BoundingEnvelope) this.be.clone();
          return cloned;
      } catch ( CloneNotSupportedException e) {
          System.out.println("*** Error in AbstractFeature.clone() ... ");
          return null;
      }
   }
   /**
    * Transform the feature to hyper bounding box
    * @return HyperBoundingBox the Hyper Bounding Box of this feature
    */
   public HyperBoundingBox transformToHyperBoundingBox()
   {
   	double dX2 = this.c.dX + height;
   	double dY2 = this.c.dY + width;
   	double A[] = new double[2];
    A[0] = Math.min ( this.c.dX, dX2 );
    A[1] = Math.min ( this.c.dY, dY2 );
    double B[] = new double[2];
    B[0] = Math.max ( this.c.dX, dX2 );
    B[1] = Math.max ( this.c.dY, dY2 );
    HyperPoint hpA = new HyperPoint ( A );
    HyperPoint hpB = new HyperPoint ( B );
    
    HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
   	
   	return hp;
   }
}

