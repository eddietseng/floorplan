/*
 *  ======================================================================
 *  PrintViewOperationsVisitor.java: Visit components and retrieve a
 *  summary of performance and cost of system operations ..
 *
 *  Modified by: Mark Austin                                    June, 2012
 *  ======================================================================
 */

package view;

import model.*;
 
public class PrintViewOperationsVisitor implements FeatureElementVisitor {
   String s = "";
   private double totalPrice;    
   private double totalPerformance;    
   private boolean headingPrinted = false;

   public void visit( AbstractCompoundFeature compound ) {
      print ( (AbstractCompoundFeature) compound );
   }

   public void visit( AbstractFeature feature ) {}
   public void visit( CompositeHierarchy composite ) {}

   public void start( CompositeHierarchy composite ) {

      // Print heading for the first time ....

      if ( headingPrinted == false ) {
         String s1 = String.format("------------------------------ \n" );
         String s2 = String.format("Summary of Building Operations \n" );
         String s3 = String.format("------------------------------ \n" );
         String s4 = String.format("\n" );
         s = s + s1 + s2 + s3 + s4;
      }

      // Reset heading flag ...

      headingPrinted = true;
   }

   public void finish( CompositeHierarchy composite ) {}

   // Assemble details of component operations .....

   public void print( AbstractCompoundFeature af ) {
      totalPrice       += af.getPrice();
      totalPerformance += af.getPerformance();

      // String representation of feature and price ...

      String s1 = String.format("Item: %17s : cost = %7.2f performance = %5.2f\n",
                                 af.getName(), af.getPrice(), af.getPerformance() );
      s = s + s1;
   }

   // ================================================
   // Set summary ....
   // ================================================

   public void setSummary() {
      String s1 = String.format("\n" );
      String s2 = String.format("-------------------------------------\n" );
      String s3 = String.format("Total Cost        = %7.2f \n", getTotalPrice() );
      String s4 = String.format("Total Performance = %7.2f \n", getTotalPerformance() );
      String s5 = String.format("-------------------------------------\n" );
      s = s + s1 + s2 + s3 + s4 + s5;
   }

   // ================================================
   // Retrieve text string of total cost ....
   // ================================================

   public String getText() {
      return s;
   }

   // ================================================
   // Return the internal state ....
   // ================================================

   public double getTotalPrice() {
      return totalPrice;   
   }

   public double getTotalPerformance() {
      return totalPerformance;   
   }
}
