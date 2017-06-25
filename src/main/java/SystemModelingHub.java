/* 
 *  ================================================================================
 *  SystemModelingHub.java: Assemble mvc hub for modeling metro and building systems
 *  ================================================================================
 */ 

import javax.swing.SwingUtilities;

import model.MetroSystemModel;
import model.MetroAreaModel;
import model.MetroGridModel;
import model.MetroTrainModel;
import model.BuildingHVACModel;
import model.FloorplanModel;
import controller.EngineeringController;
import view.PlanView;
import view.PrintView;
import view.TabbedPaneView;
import view.TreeViewFloorplan;

public class SystemModelingHub {
   public static void main(String[] args) {

      // Run this in the Swing Event Thread.

      try {
          Runnable doActions = new Runnable() {
             public void run() {
                try {
                    new SystemModelingHub();
                } catch (Exception ex) {
                    System.err.println(ex.toString());
                    ex.printStackTrace();
                }
            }
          };
          SwingUtilities.invokeAndWait(doActions);
        } catch (Exception ex) {
            System.err.println(ex.toString());
            ex.printStackTrace();
        }
   }

   // Create a new instance of the MVC design pattern ....

   public SystemModelingHub() {

      // Create engineering controller and plan/table views ....

      EngineeringController engC = new EngineeringController();

      // Create metro system models ....

      MetroSystemModel metroS = new MetroSystemModel();
      MetroAreaModel   metroA = new MetroAreaModel();
      MetroGridModel   metroG = new MetroGridModel();
      MetroTrainModel  metroT = new MetroTrainModel( engC );

      // Create building layout model ....

      BuildingHVACModel buildingA = new BuildingHVACModel();
      FloorplanModel   floorplanA = new FloorplanModel();

      // Create plan, print and table views ....

      PrintView  printV = new PrintView( engC );
      PlanView   planV  = new PlanView( engC );
      
      TreeViewFloorplan treeviewF = new TreeViewFloorplan( engC );
      TabbedPaneView tabbedpaneF = new TabbedPaneView( engC );

   // TableViewStations tableS = new TableViewStations( engC );
   // TableViewTrains   tableT = new TableViewTrains( engC );

      // Add views and models to engineering controller ....

      engC.addView( printV );
   // engC.addView( tableS );
      engC.addView( planV );
   // engC.addView( tableT );
      engC.addView( treeviewF );
      engC.addView( tabbedpaneF );

      engC.addModel( metroS );
      engC.addModel( metroA );
      engC.addModel( metroT );
      engC.addModel( metroG );
      engC.addModel( buildingA );
      engC.addModel( floorplanA );

      // Display views and then initialize model ....

   // tableS.display();
      printV.display();
       planV.display();
   // tableT.display();
      treeviewF.display();
      tabbedpaneF.display();

      metroS.initDefault();
      metroA.initDefault();
      metroT.initDefault();
      metroG.initDefault();

      buildingA.initDefault();
      floorplanA.initDefault();

   }
}
