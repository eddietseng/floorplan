/*
 *  ====================================================================
 *  java-code-new.d/java-beans8.d/src/model/MetroAreaModel.java
 *  ====================================================================
 */

package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import java.beans.PropertyChangeEvent;

import model.primitive.*;
import model.urban.*;
import model.hvac.*;
import controller.EngineeringController;

public class MetroAreaModel extends AbstractModel {
   private Boolean DEBUG = true;
   private CompositeHierarchy  workspace;
   HashMap<String,Object>         things;

   // Constructor ....

   public MetroAreaModel() {
      System.out.println("Create Model of Entities in Washington DC Metro System Area");
      System.out.println("===========================================================");

      things    = new HashMap<String,Object>();
      workspace = new CompositeHierarchy( 0.0, 0.0, 0.0 );
      workspace.setName("Metro Area Workspace");
   }

   // Build model of Metro system area things ...

   public void initDefault() {
      buildMetroArea();
      setMetroArea();
      setMetroAreaComposite();
   }

   // ======================================================================
   // Create composite of Metro Area objects ...
   // ======================================================================

   public void buildMetroArea() {

      // Create outline for umd campus ...

      double x[] = { 0.0, 60.0, 100.0, 100.0,  90.0,  50.0,    0.0 };
      double y[] = { 0.0,  0.0,  40.0,  80.0, 100.0, 100.0,   50.0 };
      SimplePolygon umd = new SimplePolygon( x, y );
      umd.setName ("UMD Campus");
      umd.setColor( Color.orange );
      umd.setFilledShape( true );
      umd.setTextOffSetX(   0 );
      umd.setTextOffSetY( 105 );

      // Create generic gov't building shape ....

      double x2[] = { 0.0,   0.0,   50.0,   50.0,  120.0, 120.0,  150.0, 170.0, 170.0, 150.0 };
      double y2[] = { 0.0, 100.0,  100.0,   50.0,   50.0, 100.0,  100.0,  80.0,  20.0,   0.0 };
      SimplePolygon poly = new SimplePolygon( x2, y2 );
      poly.setName("Govt Building");
      poly.setTextOffSetX(   0 );
      poly.setTextOffSetY( 105 );
      poly.setColor( Color.blue );
      poly.setFilledShape( false );

      // Create car and house urban objects ...

      HouseModel house01 = new HouseModel( 0.0, 0.0, 50.0 );
      house01.setName("NZ Embassy");
      house01.setFilledShape( true );
      house01.setColor( Color.black );
      house01.setTextOffSetX(   0 );
      house01.setTextOffSetY(  60 );
      house01.setPrice( 100000.00 );

      HouseModel house02 = new HouseModel( 0.0, 0.0, 50.0 );
      house02.setName("Cathedral");
      house02.setFilledShape( true );
      house02.setColor( Color.orange );
      house02.setTextOffSetX(   0 );
      house02.setTextOffSetY(  60 );
      house02.setPrice( 100000.00 );

      CarModel car01 = new CarModel( 0.0, 0.0,  48.0 );
      car01.setName("Sports car");
      car01.setFilledShape( true );
      car01.setColor( Color.red );
      car01.setTextOffSetX(   0 );
      car01.setTextOffSetY(  35 );
      car01.setPrice(  50000.00 );

      // Create a small forest of trees ... 

      Tree tree01 = new Tree(   0.0,  0.0, 10.0 );
      tree01.setName("Big Fur");
      tree01.setFilledShape( true );
      tree01.setTextOffSetX( -10 );
      tree01.setTextOffSetY(  15 );
      tree01.setColor( Color.green );
      tree01.setPrice(  5000.00 );

      Tree tree02 = new Tree(   0.0, 50.0, 10.0 );
      tree02.setName("Willow");
      tree02.setFilledShape( true );
      tree02.setTextOffSetX( -10 );
      tree02.setTextOffSetY(  15 );
      tree02.setColor( Color.blue );
      tree02.setPrice(  1000.00 );

      Tree tree03 = new Tree(  70.0, 0.0, 5.0 );
      tree03.setName("Maple");
      tree03.setFilledShape( true );
      tree03.setTextOffSetX(  -5 );
      tree03.setTextOffSetY(  10 );
      tree03.setColor( Color.orange );
      tree03.setPrice(  2000.00 );

      // Add objects to HashMap ...

      things.put (    poly.getName(),     poly );
      things.put (     umd.getName(),      umd );
      things.put ( house01.getName(),  house01 );
      things.put ( house02.getName(),  house02 );
      things.put (   car01.getName(),    car01 );

      // Assemble composite hierarchy ...

      CompositeHierarchy area1 = new CompositeHierarchy( -200.0, -100.0, Math.PI/6.0 );
      area1.add( poly );

      CompositeHierarchy area2 = new CompositeHierarchy(  275.0, 375.0, 0.0 );
      area2.add( umd  );

      CompositeHierarchy area3 = new CompositeHierarchy( -100.0, 200.0, Math.PI/2.0 );
      area3.add( house01 );

      CompositeHierarchy area4 = new CompositeHierarchy(  400.0, 300.0, 0.0 );
      area4.add( car01 );

      CompositeHierarchy area5 = new CompositeHierarchy(  -50.0, 350.0, 0.0 );
      area5.add( house02 );

      // Add trees to forest coordinate system ...

      CompositeHierarchy forest = new CompositeHierarchy( 400.0, -100.0, 0.0 );
      forest.add( tree01 );
      forest.add( tree02 );
      forest.add( tree03 );

      // Assemble workspace hierarchy ....

      workspace.add( area1 );
      workspace.add( area2 );
      workspace.add( area3 );
      workspace.add( area4 );
      workspace.add( area5 );
      workspace.add( forest );
   }

   // ===============================================================
   // Propogate metro area objects and composite to controller ....
   // ===============================================================

   public void setMetroArea() {
      HashMap copy = new HashMap( things );
      firePropertyChange( EngineeringController.METRO_AREA_HASHMAP, (HashMap) null, copy );
   }

   public void setMetroAreaComposite() {
      CompositeHierarchy copy = workspace.clone();
      firePropertyChange( EngineeringController.METRO_AREA_COMPOSITE_PROPERTY,
                          (CompositeHierarchy) null, copy );
   }
}
