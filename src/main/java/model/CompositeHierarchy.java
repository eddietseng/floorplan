/* 
 *  ===========================================================================
 *  CompositeHierarchy.java: Create a composite hierarchy model of features ...
 * 
 *  Written By: Mark Austin                                     June-July, 2012 
 *  ===========================================================================
 */ 

package model;

import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Graphics2D;
import java.awt.geom.*;   // Needed for affine transformation....
import java.lang.reflect.*;
 
public class CompositeHierarchy extends AbstractFeature {
    private boolean DEBUG = false;
    private static int iCurrentLevel = 0;
    private static double dCurrentXOffSet  = 0.0;
    private static double dCurrentYOffSet  = 0.0;
    private static double dCurrentRotation = 0.0;

    public static AffineTransform at = new AffineTransform();
    public static Graphics2D g2D;

    // Coordinates and orientation of composite graphic ...

    private double     xOffset;
    private double     yOffset;
    private double    rotation;

    // Collection of child graphics.

    private ArrayList<Feature> children = new ArrayList<Feature>();
 
    // Constructor for composite hierarchy ...
    
    public CompositeHierarchy(){};

    public CompositeHierarchy( double xOffset, double yOffset, double rotation ) {
       this.xOffset  = xOffset;
       this.yOffset  = yOffset;
       this.rotation = rotation;
    }
    
    // Detail setting ...
    
    public void setxOffset(double xOffset){
    	this.xOffset  = xOffset;
    }
    
    public void setyOffset(double yOffset){
    	this.yOffset  = yOffset;
    }
    
    public void setrotation(double rotation){
    	this.rotation = rotation;
    }

    // Set Affine Fransformation  ...

    public void setAffineTransform ( AffineTransform at ) {
       this.at = at;
    }

    public void setGraphics2D ( Graphics2D g2D ) {
       this.g2D = g2D;
    }

    // Get current level ....

    public int getCurrentLevel() {
       return iCurrentLevel;
    }

    // Increment transformation parameters ...

    public void transformationIncrement() {
        iCurrentLevel = iCurrentLevel + 1;
        dCurrentXOffSet  = dCurrentXOffSet  + xOffset;
        dCurrentYOffSet  = dCurrentYOffSet  + yOffset;
        dCurrentRotation = dCurrentRotation + rotation;
        this.at.translate( xOffset, yOffset );
        this.at.rotate( rotation );
        g2D.translate( xOffset, yOffset );
        g2D.rotate( rotation );
    }

    // Decrement transformation parameters ...

    public void transformationDecrement() {
        iCurrentLevel    = iCurrentLevel - 1;
        dCurrentXOffSet  = dCurrentXOffSet  - xOffset;
        dCurrentYOffSet  = dCurrentYOffSet  - yOffset;
        dCurrentRotation = dCurrentRotation - rotation;
        at.rotate( -rotation );
        at.translate( -xOffset, -yOffset );
        g2D.rotate( -rotation );
        g2D.translate( -xOffset, -yOffset );
    }

    // Add a feature to the composition.

    public void add( Feature item ) {
        children.add( item );
    }
 
    // Remove a feature from the composition.

    public void remove( Feature item ) {
        children.remove( item );
    }
    
    public ArrayList<Feature> getChildren() {
    	return children;
    }

    // ================================================================
    // Find a feature among the children  ....
    // ================================================================

    public Feature find( String sName ) {
       Feature foundItem = null;

       System.out.println("*** In model.CompositeHierarchy.find(): sName = " + sName );

       Iterator itr = children.iterator();
       while ( itr.hasNext() ) {
          Feature item = (Feature) itr.next();
          if ( sName.equals ( item.getName() ) == true ) {
             System.out.println("*** FOUND IT!!! ");
             foundItem = item;
          }
       }

       return foundItem;

    }

    // ================================================================
    // Search the composite hieararchy ....
    // ================================================================

    public void search( AffineTransform at, int dx, int dy ) {

        // Update parameters ...

        transformationIncrement();

        // Search children objects ...

        for (Feature items : children) {
           items.search( at, (int) dx, (int) dy );
        }

        // Decrement level, x- and y- offsets and rotation....

        transformationDecrement();
    }

    // ================================================================
    // Accept method for visiting the composite hierarchy children ....
    // ================================================================
 
    public void accept( FeatureElementVisitor visitor ) {     

       // Step 1: Initialize parameters for visitor actions ...

       visitor.start ( this );

       // Step 2: Visit children of composite hiearchy ....

       for (Feature items : children) {
          items.accept( visitor );
       }

       // Step 3: Wrap-up parameters for visitor actions ...

       visitor.finish ( this );
    }

    // ================================================================
    // Clone (i.e., deep copy) composite hierarchy structure ...
    // ================================================================

    public CompositeHierarchy clone() {
       try {
           CompositeHierarchy copy = (CompositeHierarchy) super.clone();
           copy.children = new ArrayList<Feature>();

           Class parTypes[] = null;
           Object argList[] = null;

           Iterator itr = children.iterator();
           while ( itr.hasNext() ) {
              Feature item = (Feature) itr.next();
              try {
                 Method  m = item.getClass().getMethod("clone", parTypes );
                 Feature f = (Feature) m.invoke( item , argList );
                 copy.add ( f );
             } catch (Throwable e) {}
           }

           return copy;
       } catch ( CloneNotSupportedException e) {
           System.out.println("*** Error in CompositeHierarchy.clone() ... ");
           return null;
       }
   }
}

