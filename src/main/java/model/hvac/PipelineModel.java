package model.hvac;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.awt.Color;

import model.*;
import model.primitive.*;

public class PipelineModel extends AbstractCompoundFeature {

    // Contructor methods ...
    
    public PipelineModel() {}

    public PipelineModel( String name, double xPoints[], double yPoints[] ) {
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

    public PipelineModel( String name, ArrayList<Coordinate> perimeter ) {
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

       System.out.printf("*** In PipelineModel() .... \n");

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

    public PipelineModel clone() {
       try {
           PipelineModel cloned = (PipelineModel) super.clone();
           return cloned;
       } catch ( CloneNotSupportedException e) {
           System.out.println("*** Error in PipelineModel.clone() ... ");
           return null;
       }
    }

    // Convert description of metro station to a string ...

    public String toString() {
       String s = "PipelineModel(\"" + getName() + "\")\n";
       return s;
    }
}
