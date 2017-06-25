/*
 * ===============================================================
 * MetroWayPoint.java: Create waypoint in the metro system.
 * 
 * -- A waypoint records its coordinate, its distance from the
 *    coordinate source, and a reference to the corresponding
 *    metro station (if it exists).
 * 
 * Written by: Mark Austin                           November 2011
 * ===============================================================
 */

package model.metro;

import model.Coordinate;
import model.metro.MetroStation;

public class MetroWayPoint { 
    protected double distanceFromOrigin = 0.0;
    protected MetroStation station = null;
    Coordinate coord = null;

    // Contructor methods ...
    
    public MetroWayPoint() {
        coord = new Coordinate();
    }

    public MetroWayPoint( MetroStation station ) {
        this.station = station;
        this.coord   = new Coordinate( station.getX(), station.getY() );
    }

    // Set/get distance from Origin ....

    public void setDistanceFromOrigin ( double distance ) {
       this.distanceFromOrigin = distance;
    }

    public double getDistanceFromOrigin () {
       return distanceFromOrigin;
    }

    // Convert description of train to a string format ...

    public String toString() {
       String s = "MetroWayPoint: coordinates = (" + coord.getX() + "," + coord.getY() + ")\n" +
                  "             : distance from origin = " + distanceFromOrigin + "\n";

       return s;
   }
}
