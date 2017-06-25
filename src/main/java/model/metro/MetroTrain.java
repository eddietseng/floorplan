package model.metro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.*;
import model.primitive.*;

public class MetroTrain extends AbstractCompoundFeature {
    protected String   onTrack = null;;
    protected String    status = null;;
    protected double distanceFromOrigin = 0.0;
    MetroStation origin = null;;
    Coordinate coord    = null;

    // Contructor methods ...
    
    public MetroTrain() {}

    public MetroTrain( String name ) {
       setName ( name );
       this.distanceFromOrigin = 0.0;

       // Create vertex for tree schematic ...

       Point train01 = new Point( "pt01", 0.0, 0.0 );

       // Add point to the model ...

       items.put( train01.getName(), train01 );
    }

    public MetroTrain( String name, MetroStation origin, double distanceFromOrigin ) {
       setName ( name );
       this.origin    = origin;
       this.distanceFromOrigin = distanceFromOrigin;

       if ( distanceFromOrigin == 0.0 ) {
            double xStart = origin.getX();
            double yStart = origin.getY();
            coord = new Coordinate ( xStart, yStart );
       } else {
            coord = new Coordinate ( 0.0, 0.0 );
       }

       // Create vertex for tree schematic ...

       Point train01 = new Point( "pt01", coord.getX(), coord.getY() );

       // Add point to the model ...

       items.put( train01.getName(), train01 );
    }

    // Set/get methods for train capacity ....

    public void setCapacity ( double capacity ) {
       setPerformance ( capacity );
    }

    public double getCapacity ( double capacity ) {
       return getPerformance();
    }

    // Assign train to a track ...

    public void setTrack ( String onTrack ) {
       this.onTrack = onTrack;
    }

    public String getTrack () {
       return onTrack;
    }

    // Assign train status ...

    public void setStatus ( String status ) {
       this.status = status;
    }

    public String getStatus () {
       return status;
    }

    // Retrieve train name ...

    public String getTrainName () {
        return getName();
    }

    // Distance from Origin ....

    public double getDistanceFromOrigin () {
        return distanceFromOrigin;
    }

    // Accept method for Feature Element visitor ...

    public void accept( FeatureElementVisitor visitor ) {
      visitor.visit(this);
    }

    // Clone (i.e., deep copy) train object ...

    public MetroTrain clone() {
       try {
           MetroTrain cloned = (MetroTrain) super.clone();
           return cloned;
       } catch ( CloneNotSupportedException e) {
           System.out.println("*** Error in MetroTrain.clone() ... ");
           return null;
       }
    }

    // Convert description of train to a string format ...

    public String toString() {
       String s = "MetroTrain(\"" + getName() + "\")\n" +
                  "   Distance from origin = " + distanceFromOrigin + "\n" +
                  "   Coordinates = (" + getX() + "," + getY() + ")\n";

       return s;
   }
}
