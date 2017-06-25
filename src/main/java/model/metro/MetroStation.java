package model.metro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.awt.Color;

import model.*;
import model.primitive.*;

public class MetroStation extends AbstractCompoundFeature {
    protected boolean parking  = false;
    protected boolean security = false;
    protected boolean busroute = false;
    protected boolean waypoint = true;
    protected ArrayList<String> onTrack;
    String      mapFile;

    // Point size for drawing a metro station ...

    int pSize = 8;

    // Contructor methods ...
    
    public MetroStation() {}

    public MetroStation( String name, double dX, double dY ) {
       super ();
       setX( dX );
       setY( dY );
       setName( name );
       onTrack = new ArrayList<String>();  // Initialize array list of line colors

       // Save metro station height, width, and color ...

       setHeight( (double) pSize );
       setWidth( (double) pSize );
       setColor ( Color.black );
       setDisplayName ( true );

       setTextOffSetX(  pSize/2 + 4 );
       setTextOffSetY( -pSize/2 - 8 );

       // Initialize envelope ...

       be.addPoint( dX - pSize/2, dY - pSize/2 );
       be.addPoint( dX + pSize/2, dY + pSize/2 );

       // Create metro station point model ...

       Point pt01 = new Point( "pt01", dX, dY, pSize, pSize );

       // Add point to model ...

       items.put( pt01.getName(), pt01 );
    }

    public MetroStation( String name, double dX, double dY, boolean park, boolean sec, boolean br ) {
       super ();
       setX( dX );
       setY( dY );
       setName( name );
       onTrack = new ArrayList<String>();  // Initialize array list of line colors

       // Save metro station height, width, and color ...

       setHeight( (double) pSize );
       setWidth( (double) pSize );
       setColor ( Color.black );

       setTextOffSetX(  pSize/2 + 4 );
       setTextOffSetY( -pSize/2 - 8 );

       // Initialize envelope ...

       be.addPoint( dX - pSize/2, dY - pSize/2 );
       be.addPoint( dX + pSize/2, dY + pSize/2 );

       // Create metro station point model ...

       Point pt01 = new Point( "pt01", dX, dY, pSize, pSize );

       // Add point to model ...

       items.put( pt01.getName(), pt01 );

       // Save metro station attributes ...
         
       parking  = park;   
       security = sec;
       busroute = br;
    }
    
    public void setMapFile(String file){
       mapFile = file;
    }

    // Deal with station name ....

    public void setStationName ( String sName ) {
       setName ( sName );
    }

    public String getStationName () {
       return getName();
    }

    public Coordinate findCoord () {
       return getCoordinate();
    }

    // Retrieve arraylist of track memberships ....

    public ArrayList getTracks() {
       return onTrack;
    }

    // Assign metro station to a track ....

    public void add( String track ) {
       onTrack.add ( track );
    }

    // Deal with bus route ...
    
    public void setWayPoint( boolean waypoint ) {
       this.waypoint = waypoint;
    }

    public boolean getWayPoint() {
       return waypoint;
    }

    // Deal with bus route ...
    
    public void setBusRoute( boolean busroute ) {
       this.busroute = busroute;
    }

    public boolean getBusRoute() {
        return busroute;
    }

    // Deal with security ...

    public void setSecurity( boolean security ) {
       this.security = security;
    }
    
    public boolean getSecurity() {
        return security;
    }

    // Deal with parking ...
    
    public void setParking( boolean park ) {
       parking = park;
    }
    
    public boolean getParking() {
        return parking;
    }

    // ============================================================ 
    // Accept method for Feature Element visitor ...
    // ============================================================ 

    public void accept( FeatureElementVisitor visitor) {
       visitor.visit(this);
    }

    // ============================================================ 
    // Clone (i.e., deep copy) metro station object ...
    // ============================================================ 

    public MetroStation clone() {
       try {
           MetroStation cloned = (MetroStation) super.clone();
           cloned.onTrack = new ArrayList<String>();  

           // copy "on track" references ....

           Iterator tracks = this.onTrack.iterator();
           while (tracks.hasNext()) {
              cloned.onTrack.add((String) tracks.next());
           }

           return cloned;
       } catch ( CloneNotSupportedException e) {
           System.out.println("*** Error in MetroSystem.clone() ... ");
           return null;
       }
    }

    // Convert description of metro station to a string ...

    public String toString() {
       String s = "MetroStation(\"" + getName() + "\")\n" +
                  "   Coordinates = (" + getX() + "," + getY() + ")\n" +
                  "   Parking  = " + parking + "\n" +
                  "   Selected = " + getSelection() + "\n";

       // Walk along array list and add line names to string "s" ....

       if ( onTrack.size() > 0 ) {
          s = s.concat( "   Track   = { " );
          for (int i = 0; i < onTrack.size(); i = i + 1)
             s = s.concat( onTrack.get(i) + " ");
             s = s.concat("}\n");
         }

       return s;
   }
}
