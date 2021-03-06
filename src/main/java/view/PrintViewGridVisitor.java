/*
 *  =========================================================================
 *  PrintViewGridVisitor.java: Create a print view of the grid workspace.
 *
 *  Written by: Mark Austin                                        July, 2012
 *  =========================================================================
 */

package view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import java.awt.geom.*;  // Needed for affine transformation....

import model.*;
import model.primitive.*;
 
public class PrintViewGridVisitor implements FeatureElementVisitor {
   String s = "";
   private AffineTransform at;

   public void visit( AbstractCompoundFeature compound ) {
      print ( (AbstractCompoundFeature) compound );
   }

   public void visit( AbstractFeature poly ) {
      print ( (AbstractFeature) poly );
   }

   public void visit( CompositeHierarchy composite ) {
      System.out.println("*** In PrintViewVisitor.visit(CompositeHierarchy composite)");
   }

   // Increment coordinate system at beginning of composite hieararchy.

   public void start( CompositeHierarchy composite ) {

      // Increment transformation parameters ...

      composite.transformationIncrement();

      this.at = composite.at;

      String s1 = String.format("\n");
      String s2 = String.format("Start Composite Hierarchy: %s\n", composite.getName() );
      String s3 = String.format("=================================================\n");
      String s4 = String.format("Level = %2d\n", composite.getCurrentLevel() );
      String s5 = String.format("-------------------------------------------------\n");
      String s6 = String.format("\n");
      s = s + s1 + s2 + s3 + s4 + s5 + s6;
   }

   // Decrement coordinate system at end of composite hieararchy.

   public void finish( CompositeHierarchy composite ) {

      String s1 = String.format("\n");
      String s2 = String.format("-------------------------------------------------\n");
      String s3 = String.format("End level = %2d\n", composite.getCurrentLevel() );
      String s4 = String.format("=================================================\n");
      s = s + s1 + s2 + s3 + s4;

      composite.transformationDecrement();
   }

   // Retrieve text string ... 

   public String getText() {
      return s;
   }

   // Print details of primative shapes ...

   public void print( AbstractFeature af ) {

      // Print local and global coordinates of anchor point ....

      double flatmatrix[] = new double[6];
      model.CompositeHierarchy.at.getMatrix( flatmatrix );
      double dGlobalX = flatmatrix[0]*af.getX() + flatmatrix[2]*af.getY() + flatmatrix[4];
      double dGlobalY = flatmatrix[1]*af.getX() + flatmatrix[3]*af.getY() + flatmatrix[5];

      String s1 = String.format("-- node -------------------------------\n" );
      String s2 = String.format("Local  (x,y) = ( %6.1f, %6.1f )\n", af.getX(), af.getY() );
      String s3 = String.format("Global (x,y) = ( %6.1f, %6.1f )\n", dGlobalX, dGlobalY );
      s = s + s1 + s2 + s3;
   }

   // Print details of a compound feature ...

   public void print( AbstractCompoundFeature af ) {}
}
