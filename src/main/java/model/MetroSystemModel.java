/*
 *  ====================================================================
 *  MetroSystemModel.java: Create model of metro system structure.
 *
 *  Written by: Mark Austin                                   June, 2012
 *  ====================================================================
 */

package model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.awt.Color;

import java.beans.PropertyChangeEvent;

import model.metro.*;
import model.primitive.*;
import model.urban.*;
import model.MetroSystemGraph;
import controller.EngineeringController;

public class MetroSystemModel extends AbstractModel {
   private Boolean DEBUG = true ;
   private CompositeHierarchy     mworkspace;
   private CompositeHierarchy     tworkspace;
   HashMap<String,MetroStation>     stations;
   HashMap<String,MetroLineModel>      lines;
   HashMap<String,Object>             things;
   HashMap<String,MetroLinePathway> pathways;
   MetroSystemGraph msg = null;

   // Constructor ....

   public MetroSystemModel() {
      System.out.println("Create Washington DC Metro System Model");
      System.out.println("=======================================");

      // Metro system hashmaps ...

      stations = new HashMap<String,MetroStation>();
      lines    = new HashMap<String,MetroLineModel>();
      things   = new HashMap<String,Object>();
      pathways = new HashMap<String,MetroLinePathway>();

      // Metro system workspace ....

      mworkspace  = new CompositeHierarchy( 0.0, 0.0, 0.0 );
      mworkspace.setName("Metro Station Workspace");
      tworkspace = new CompositeHierarchy( 0.0, 0.0, 0.0 );
      tworkspace.setName("Metro Lines Workspace");
   }

   // Default assembly of metro system and propagation to views ...

   public void initDefault() {

      // Build model of Washington DC Metro System and Trains ...

      buildMetroSystem();

      // Build graph model of metro stations and rail network ... 

      msg = new MetroSystemGraph("DC Metro", stations, things );

      // Transfer copies of models to registered views ...

      setMetroStation();
      setMetroStationComposite();
      setMetroLine();
      setMetroLineComposite();

   // setMetroPathways();
   }

   // ======================================================================
   // Retrieve references to hashmaps ...
   // ======================================================================

   public HashMap getStations() {
      return stations;
   }

   public HashMap getLines() {
      return things;
   }

   // ======================================================================
   // Assemble stations, lines and tracks for Washington DC Metro System ...
   // ======================================================================

   public void buildMetroSystem() {

      // Create metro stations along green and red lines ....

      MetroStation gA = new MetroStation(     "Greenbelt", 450.0,  450.0 );
      gA.setParking( true );
      stations.put(  "Greenbelt",  gA );

      MetroStation gB = new MetroStation(  "College Park", 400.0,  400.0 );
      gB.setParking( true );
      stations.put(  "College Park",  gB );

      MetroStation gC = new MetroStation( "Silver Spring", 150.0,  400.0 );
      gC.setParking( true );
      stations.put(  "Silver Spring",  gC );

      MetroStation gD = new MetroStation(   "Takoma Park", 150.0,  350.0 );
      gD.setParking( true );
      stations.put(  "Takoma Park",  gD );

      MetroStation gE = new MetroStation(   "Fort Totten", 250.0,  250.0 );
      gE.setParking( true );
      stations.put(  "Fort Totten",  gE );

      MetroStation gF = new MetroStation( "Union Station", 300.0,  150.0 );
      gF.setParking( false );
      stations.put( "Union Station",  gF );

      MetroStation gG = new MetroStation( "DuPont Circle",  -0.0,  250.0 );
      gG.setParking( false );
      stations.put( "DuPont Circle",  gG );

      MetroStation gH = new MetroStation( "Catholic Univ", 300.0,  200.0 );
      gH.setParking( false );
      stations.put( "Catholic Univ",  gH );

      MetroStation gI = new MetroStation( "Gallery Place", 200.0,  150.0 );
      gI.setParking( false );
      stations.put( "Gallery Place",  gI );

      MetroStation gJ = new MetroStation(   "Pentagon City", 100.0, -50.0 );
      gJ.setParking( true );
      stations.put( "Pentagon City",  gJ );

      MetroStation gK = new MetroStation("National Airport", 100.0, -100.0 );
      gK.setParking( true );
      stations.put( "National Airport",  gK );

      MetroStation gL = new MetroStation(     "Wheaton",  150.0,  500.0 );
      gL.setParking( true );
      stations.put( "Wheaton",   gL );

      MetroStation gM = new MetroStation(    "U Street",  200.0,  200.0 );
      gM.setParking( false );
      stations.put( "U Street",  gM );

      MetroStation gN = new MetroStation( "Shady Grove", -200.0,  450.0 );
      gN.setParking( true );
      stations.put( "Shady Grove",  gN );

      // Add stations along the orange line ....

      MetroStation oA = new MetroStation( "New Carrollton",  550.0,  275.0 );
      oA.setParking( true );
      stations.put( "New Carrollton",  oA );

      MetroStation oB = new MetroStation( "Stadium-Armory",  400.0,  125.0 );
      oB.setParking( true );
      stations.put( "Stadium-Armory",  oB );

      MetroStation oC = new MetroStation( "Eastern Market",  300.0,  100.0 );
      oC.setParking( true );
      stations.put( "Eastern Market",  oC );

      MetroStation oD = new MetroStation( "L Enfant Plaza",  200.0,  100.0 );
      oD.setParking( false );
      stations.put( "L Enfant Plaza",  oD );

      MetroStation oE = new MetroStation(    "Smithsonian",  105.0,  100.0 );
      oE.setParking( false );
      stations.put( "Smithsonian",  oE );

      MetroStation oF = new MetroStation(   "Metro Center",  105.0,  150.0 );
      oF.setParking( false );
      stations.put( "Metro Center",  oF );

      MetroStation oG = new MetroStation(            "GWU",    0.0,  150.0 );
      oG.setParking( false );
      stations.put( "GWU",  oG );

      MetroStation oH = new MetroStation(         "Rosalyn", -50.0,  100.0 );
      oH.setParking( false );
      stations.put( "Rosalyn",  oH );

      MetroStation oI = new MetroStation(   "Ballston-GMU", -150.0,  100.0 );
      oI.setParking( false );
      stations.put( "Ballston-GMU",  oI );

      MetroStation oJ = new MetroStation(         "Vienna", -300.0,  100.0 );
      oJ.setParking( true );
      stations.put( "Vienna",  oJ );

      // Define stations along the green and red lines ....

      String redLine[] = {  "Wheaton",
                      "Silver Spring",
                        "Takoma Park",
                        "Fort Totten",
                      "Catholic Univ", 
                      "Union Station",
                      "Gallery Place",
                       "Metro Center",
                      "DuPont Circle",
                        "Shady Grove" };

      String greenLine[] = {  "Greenbelt", 
                           "College Park",
                            "Fort Totten",
                               "U Street",
                          "Gallery Place", 
                         "L Enfant Plaza" };

      String yellowLine[] = { "National Airport",
                                 "Pentagon City",
                                "L Enfant Plaza",
                                 "Gallery Place" };

      String orangeLine[] = {   "New Carrollton",  
                                "Stadium-Armory",  
                                "Eastern Market",  
                                "L Enfant Plaza", 
                                   "Smithsonian", 
                                  "Metro Center", 
                                           "GWU", 
                                       "Rosalyn",  
                                  "Ballston-GMU", 
                                        "Vienna" };

      // Save arrays to lines hashtable ...

      things.put(    "Red",    redLine );
      things.put(  "Green",  greenLine );
      things.put( "Yellow", yellowLine );
      things.put( "Orange", orangeLine );

      // Add track assignments to metro station descriptions ....

      for ( int i = 0; i < redLine.length; i = i + 1 ) {
          MetroStation m = (MetroStation) stations.get( redLine[i] );
          m.add("Red");
      }

      for ( int j = 0; j < greenLine.length; j = j + 1 ) {
          MetroStation m = (MetroStation) stations.get( greenLine[j] );
          m.add("Green");
      }

      for ( int j = 0; j < yellowLine.length; j = j + 1 ) {
          MetroStation m = (MetroStation) stations.get( yellowLine[j] );
          m.add("Yellow");
      }

      for ( int j = 0; j < orangeLine.length; j = j + 1 ) {
           MetroStation m = (MetroStation) stations.get( orangeLine[j] );
           m.add("Orange");
      }

      // Assemble pathways of metro line waypoints ....

      String sPathway01 = "Red Line: Wheaton to Shady Grove";
      MetroLinePathway redLinePathwayWtoSG = new MetroLinePathway( sPathway01 );
      for ( int i = 0; i < redLine.length; i = i + 1 ) { 
          MetroStation m = (MetroStation) stations.get( redLine[i] );
          redLinePathwayWtoSG.add (m);
      }

      String sPathway02 = "Red Line: Shady Grove to Wheaton";
      MetroLinePathway redLinePathwaySGtoW = new MetroLinePathway( sPathway02 );
      for ( int i = redLine.length-1; i >= 0; i = i - 1 ) { 
          MetroStation m = (MetroStation) stations.get( redLine[i] );
          redLinePathwaySGtoW.add (m);
      }
   
      // Save schedules to pathways hash table ...

      pathways.put( sPathway01 , redLinePathwayWtoSG );
      pathways.put( sPathway02 , redLinePathwaySGtoW );

      // Copy references to metro stations into the workspace ....

      TreeMap tree = new TreeMap ( stations );
      Set st       = tree.keySet();
      Iterator itr = st.iterator();
      while (itr.hasNext()) {
         String key = (String) itr.next();
         MetroStation m = (MetroStation) stations.get(key);
         m.setFilledShape( true );
         mworkspace.add ( m );

         // Add car model to workspace for parking ... 

         if ( m.getParking() == true ) {
            double xoffSet = 15;
            double width   = 24;
            CarModel car01 = new CarModel( m.getX() + xoffSet, m.getY(), width );
            car01.setName( "Parking" );
            car01.setDisplayName( false );
            car01.setColor( Color.black );
            car01.setFilledShape( true );
            mworkspace.add ( car01 );
         }
      } 

      // Copy references to metro tracks into the workspace ....

      tree = new TreeMap ( things );
      st   = tree.keySet();
      itr  = st.iterator();
      while (itr.hasNext()) {
         String key = (String) itr.next();

         System.out.printf ("*** Building metro track: %s\n", key );

         // Compute perimeter of metrotrack region ...

         MetroLineRegion region = new MetroLineRegion( key );
         String line[] = (String []) things.get (key );
         for ( int i = 0; i < line.length; i = i + 1 ) {
            MetroStation m = (MetroStation) stations.get( line[i] );
            region.add( m );
         }

         int width = 4;
         region.assembleRegion ( width );
         ArrayList<Coordinate> perimeter = region.getRegion();
         System.out.println ( perimeter );

         // Create compound object for metrotrack ....

         MetroLineModel t = new MetroLineModel( key, perimeter );

         // Set track line label display to false ...

         t.setDisplayName( false );

         // Set track color ....

         if ( key.equals ( "Red" ) == true )
            t.setColor( Color.red );
         else if ( key.equals ( "Green" ) == true )
            t.setColor( Color.green );
         else if ( key.equals ( "Yellow" ) == true )
            t.setColor( Color.yellow );
         else if ( key.equals ( "Orange" ) == true )
            t.setColor( Color.orange );
         else if ( key.equals ( "Blue" ) == true )
            t.setColor( Color.blue );
         else 
            t.setColor( Color.black );

         // Add metro track to the lines hashmap and tworkspace ...

         lines.put( key, t );
         tworkspace.add ( t );
      } 
   }

   // ===========================================================
   // Set status of station in the metro system model ..
   // ===========================================================

   public void setMetroStationProperty( MetroStation station ) {

      if( DEBUG == true ) {
         System.out.println("*** Enter MetroSystemModel.setMetroStationProperty(): name = " + station.getName() );
      }

      // Update metro station object in workspace ...

      MetroStation update = (MetroStation) mworkspace.find ( station.getName() );
      update.setSelection ( true );

      MetroStation copy = update.clone();

      // Propogate change in model to controller and registered views ....

      if( DEBUG == true ) {
         System.out.println("*** GoTo MetroSystemModel.setMetroStationProperty().firePropertyChange()");
      }

      firePropertyChange( EngineeringController.METRO_STATION_PROPERTY, (MetroStation) null, copy );
   }

   // ===========================================================
   // Propogate metro stations to controller and registered views
   // ===========================================================

   public void setMetroStation() {
      HashMap copy = new HashMap(this.stations);
      firePropertyChange( EngineeringController.METRO_STATION_HASHMAP, (HashMap) null, copy );
   }

   // ==============================================================
   // Propogate metro system workspace hierarchy to registered views
   // ==============================================================

   public void setMetroStationComposite() {
      CompositeHierarchy copy = mworkspace.clone();
      firePropertyChange( EngineeringController.METRO_SYSTEM_COMPOSITE_PROPERTY,
                          (CompositeHierarchy) null, copy );
   }

   // ===========================================================
   // Propogate metro tracks to controller and registered views
   // ===========================================================

   public void setMetroLine() {
      HashMap copy = new HashMap(this.lines);
      firePropertyChange( EngineeringController.METRO_LINE_HASHMAP, (HashMap) null, copy );
   }

   // ==============================================================
   // Propogate metro system workspace hierarchy to registered views
   // ==============================================================

   public void setMetroLineComposite() {
      CompositeHierarchy copy = tworkspace.clone();
      firePropertyChange( EngineeringController.METRO_LINE_COMPOSITE_PROPERTY,
                          (CompositeHierarchy) null, copy );
   }

   // ================================================================
   // Propogate metro line pathways to controller and registered views
   // ================================================================

   public void setMetroPathways() {
      HashMap copy = new HashMap(this.pathways);
      firePropertyChange( EngineeringController.METRO_PATHWAY_HASHMAP, (HashMap) null, copy );
   }
}
