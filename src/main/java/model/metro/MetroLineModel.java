package model.metro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.awt.Color;

import model.*;
import model.primitive.*;

public class MetroLineModel extends AbstractCompoundFeature {

    // Contructor methods ...
    
    public MetroLineModel() {}

    public MetroLineModel( String name, double xPoints[], double yPoints[] ) {
       super ();

       // Set name and default color ....

       setName( name );
       setColor ( Color.black );
       setX ( 0.0 );
       setY ( 0.0 );

       // Create metro track polygon model ...

       SimplePolygon track01 = new SimplePolygon( xPoints, yPoints );
       items.put( name, track01 );
    }

    public MetroLineModel( String name, ArrayList<Coordinate> perimeter ) {
       super ();
       setName( name );
       setColor ( Color.black );
       setX ( 0.0 );
       setY ( 0.0 );

       // Create arrays of x- and y- coordinates ...

       double xPoints[] = new double [ perimeter.size() ];
       double yPoints[] = new double [ perimeter.size() ];
       for ( int i = 0; i < perimeter.size(); i = i + 1 ) {
           xPoints[i] = perimeter.get(i).getX();
           yPoints[i] = perimeter.get(i).getY();
       }

       System.out.printf("*** In MetroLineModel() .... \n");

       for ( int i = 0; i < perimeter.size(); i = i + 1 ) {
          System.out.printf("-- x,y = %f %f\n", xPoints[i], yPoints[i]);
       }

       // Create metro track polygon model ...

       SimplePolygon track01 = new SimplePolygon( xPoints, yPoints );
       items.put( name, track01 );
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

    public MetroLineModel clone() {
       try {
           MetroLineModel cloned = (MetroLineModel) super.clone();
           return cloned;
       } catch ( CloneNotSupportedException e) {
           System.out.println("*** Error in MetroLineModel.clone() ... ");
           return null;
       }
    }

    // Convert description of metro station to a string ...

    public String toString() {
       String s = "MetroLineModel(\"" + getName() + "\")\n";
       return s;
    }
}
