/*
 *  ======================================================================
 *  Edge.java: Definition of edge along the perimeter of a two-dimensional
 *             space.
 *
 *  Written By : Mark Austin                                      May 2006
 *  ======================================================================
 */

package model.floorplan;

import java.lang.Math.*;
import java.applet.*;
import java.util.*;
import java.io.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.font.*;
import java.awt.image.*;
import java.awt.geom.*;   // Needed for affine transformation....
import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.*;

public class Edge {
    protected String      sName;  // Name of edge ...
    private     Edge typeOfEdge;  // Stores edge type ...
    protected String     parent;  // Name of parent space ....
    protected String   neighbor;  // Name of neighboring space ....
    protected Coordinate vertex1;
    protected Coordinate vertex2;

    // Enumeration constants for architectural "edge" types

    public static final Edge  SPACE2D = new Edge("SPACE2D");
    public static final Edge BOUNDARY = new Edge("BOUNDARY");
    public static final Edge   PORTAL = new Edge("PORTAL");

    // Constructor methods ....

    public Edge() {}

    public Edge( String sName ) {
       this.sName = sName;
    }

    public Edge( String sName, Coordinate vertex1, Coordinate vertex2 ) {
       this.sName   = sName;
       this.vertex1 = vertex1;
       this.vertex2 = vertex2;
    }

    // Retrieve name of edge, vertex1 and vertex2 ...

    public String getName() {
       return sName;
    }

    public Coordinate getVertex1() {
       return vertex1;
    }

    public Coordinate getVertex2() {
       return vertex2;
    }

    // Create references to parent/neighboring spaces ...

    public void setParent ( String parent ) {
       this.parent = parent;
    }

    public void setNeighbor ( String neighbor ) {
       this.neighbor = neighbor;
    }

    public String getParent ()   { return this.parent; }
    public String getNeighbor () { return this.neighbor; }

    // Set/get "type" of edge ...

    public void setType ( final Edge typeOfEdge ) {
       this.typeOfEdge = typeOfEdge;
    }

    public Edge getType() {
       return this.typeOfEdge;
    }

    // Create string representation of edge ....

    public String toString() {
        String s = "Edge: " + sName + "\n";
        s += vertex1.toString();
        s += vertex2.toString();

        if ( parent != null || neighbor != null ) {
           s += "Parent space: " + parent + "\n";
           s += "Neighbouring space: " + neighbor + "\n";
        }

        return s;
    }
    
    public int hashCode() {
 	   int x = (int) ((int)this.vertex1.getX()+vertex2.getX());
 	   int y = (int) ((int)this.vertex1.getY()+vertex2.getY());
 	   return 3*x + y;
 	  }
    
    public boolean equals(Object o) {
 	    if (o instanceof Edge) {
 	      Edge other = (Edge) o;
 	      return ((this.vertex1.equals(other.vertex1) || this.vertex1.equals(other.vertex2)) && (this.vertex2.equals(other.vertex2) || this.vertex2.equals(other.vertex1)));
 	    }
 	    return false;
 	  }

    // Draw details of edge ....

    public void draw( Graphics gs, int width, int height ) {
       double x0 = 0.0; double y0 = 0.0;
       double x1 = 0.0; double y1 = 0.0;

       // Setup Affine Transformation and 2D graphics context ....

       AffineTransform at = new AffineTransform();
       at.translate(  20, height - 20 );
       at.scale( 1, -1);

       Graphics2D g2D = (Graphics2D) gs;
       g2D.setTransform (at);
       g2D.setColor(Color.red);

       BasicStroke stroke = new BasicStroke( 1 );
       g2D.setStroke(stroke);

       x0 = vertex1.getX(); y0 = vertex1.getY();
       x1 = vertex2.getX(); y1 = vertex2.getY();

       g2D.drawLine( (int) x0, (int) y0, (int) x1, (int) y1 );
    }

    // Exercise methods in Edge class ....

    public static void main( String args[] ) {

        // Create four nodes .....

        Coordinate nA = new Coordinate ("n1", 120.0, 380.0 );
        Coordinate nB = new Coordinate ("n2", 120.0, 240.0 );
        Coordinate nC = new Coordinate ("n3", 240.0, 240.0 );
        Coordinate nD = new Coordinate ("n4", 25.0, 0.0 );

        // Create four edges and set parameters ...

        Edge eA = new Edge ("e1", nA, nB );
        Edge eB = new Edge ("e2", nB, nC );
        Edge eC = new Edge ("e3", nC, nD );
        Edge eD = new Edge ("e4", nB, nC );

        // Create links to next edge is clockwise direction ...

        System.out.println ( eA.toString() );
        System.out.println ( eB.toString() );
        System.out.println ( eC.toString() );
        System.out.println ( eD.toString() );
        
        System.out.println ( eB.equals(eD) );
        System.out.println ( eB.hashCode() );
        System.out.println ( eD.hashCode() );
        
        Set<Edge> test = new HashSet();
        test.add(eA);
        test.add(eB);
        test.add(eC);
        test.add(eD);
        
        System.out.println("Set size: " + test.size());
    }
}
