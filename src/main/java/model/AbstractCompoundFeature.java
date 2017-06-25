/*
 * ======================================================================
 * AbstractCompoundFeature.java: Software support for a feature assembled
 * from simple feature primitives (e.g., collections of lines, dots).
 * 
 * Written by: Mark Austin                                     June, 2012 
 * ======================================================================
 */

package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import java.awt.geom.*;  // Needed for affine transformation....

import model.AbstractFeature;
import model.primitive.*;

public abstract class AbstractCompoundFeature extends AbstractFeature {
   private double price;
   private double performance;

   // Create an arraylists of simple features ...

   public HashMap<String,Feature> items = new HashMap<String,Feature>();

   // Constructor methods ...

   public AbstractCompoundFeature () {}

   public AbstractCompoundFeature ( double dX, double dY ) {
     super ( dX, dY );
   }

   // Set/get methods for price of compound feature ...

   public void setPrice ( double price ) {
      this.price = price;
   }

   public double getPrice () {
      return price;
   }

   // Set/get methods for performance of compound feature ...

   public void setPerformance ( double performance ) {
      this.performance = performance;
   }

   public double getPerformance () {
      return performance;
   }

   // Clone (i.e., deep copy) abstract compound feature object ...

   public AbstractCompoundFeature clone() throws CloneNotSupportedException {
       try {
           AbstractCompoundFeature copy = (AbstractCompoundFeature) super.clone();
           copy.items = new HashMap<String,Feature>( this.items );
           return copy;
       } catch ( CloneNotSupportedException e) {
           System.out.println("*** Error in AbstractCompoundFeature.clone() ... ");
           return null;
       }
   }
}
