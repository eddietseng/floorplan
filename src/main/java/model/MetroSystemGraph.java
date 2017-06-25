/**
 * =======================================================================
 * MetroSystemGraph.java. Create a directed graph for the Washington DC
 *                        Metro System.
 *
 * Note: A directed graph is called strongly connected if there is a path
 * from each vertex in the graph to every other vertex. In particular,
 * this means paths in each direction; a path from a to b and also a path
 * from b to a.
 *
 * Written by: Mark Austin                                   October, 2011
 * =======================================================================
 */

package model;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Set;
import java.util.Enumeration;

import org.jgrapht.alg.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import model.metro.*;

public class MetroSystemGraph {
    String sName;
    HashMap<String,MetroStation>     stations;
    HashMap<String,Object>              lines;
    DirectedGraph<MetroStation, DefaultEdge> metro;

    // Constructor methods ....

    public MetroSystemGraph() {
       metro    = new DefaultDirectedGraph<MetroStation, DefaultEdge> (DefaultEdge.class);
    }

    public MetroSystemGraph( String sName, HashMap stations, HashMap lines ) {
       this.sName    = sName;
       this.stations = stations;
       this.lines    = lines;
       metro         = new DefaultDirectedGraph<MetroStation, DefaultEdge> (DefaultEdge.class);
    }

    // =============================================================
    // Assemble metro system graph ...
    // =============================================================

    public void metroNetwork() {
        MetroStation start, end; 

        System.out.println("Creating metro network..." );

        // Transfer HashMap of metro stations to directed graph ....

        Set st = stations.keySet();
        Iterator itr = st.iterator();
        while (itr.hasNext()) {
            String key = (String) itr.next();
            MetroStation m = (MetroStation) stations.get(key);
            metro.addVertex( m );
        }

        // Create track/links along red line .....

        String [] redLine = (String []) lines.get("Red");
        for ( int i = 1; i < redLine.length; i = i + 1 ) {
           start = (MetroStation) stations.get( redLine[i-1] );
           end   = (MetroStation) stations.get( redLine[i] );
           metro.addEdge( start, end );
           metro.addEdge( end, start );
        }

        // Create track/links along green line .....

        String [] greenLine = (String []) lines.get("Green");
        for ( int i = 1; i < greenLine.length; i = i + 1 ) {
           start = (MetroStation) stations.get( greenLine[i-1] );
           end   = (MetroStation) stations.get( greenLine[i] );
           metro.addEdge( start, end );
           metro.addEdge( end, start );
        }

        // Create track/links along yellow line .....

        String [] yellowLine = (String []) lines.get("Yellow");
        for ( int i = 1; i < yellowLine.length; i = i + 1 ) {
           start = (MetroStation) stations.get( yellowLine[i-1] );
           end   = (MetroStation) stations.get( yellowLine[i] );
           metro.addEdge( start, end );
           metro.addEdge( end, start );
        }

        // Create track/links along orange line .....

        String [] orangeLine = (String []) lines.get("Orange");
        for ( int i = 1; i < orangeLine.length; i = i + 1 ) {
           start = (MetroStation) stations.get( orangeLine[i-1] );
           end   = (MetroStation) stations.get( orangeLine[i] );
           metro.addEdge( start, end );
           metro.addEdge( end, start );
        }
    }

    // Print details of the Metro System 

    public void print() {
        System.out.println("Washington DC Metro System");
        System.out.println("==========================");
        System.out.println( stations.toString () );
    }   

    // =============================================================
    // Build metro system model and exercise graph algorithms ....
    // =============================================================

    public static void main(String args[]) {

        System.out.println("Create Washington DC Metro System Graph");
        System.out.println("=======================================");

        MetroSystemModel mm = new MetroSystemModel();
        mm.buildMetroSystem();

        // Build graph model of metro system ....

        MetroSystemGraph ms = new MetroSystemGraph("DC Metro", mm.getStations(), mm.getLines() );
        ms.metroNetwork();
        ms.print();

        // Retrieve and print details of individual metro stations ....

        System.out.println("");
        System.out.println("Print Metro Station Details");
        System.out.println("===========================");

        System.out.println( ms.stations.get(      "Greenbelt").toString() );
        System.out.println( ms.stations.get(    "Fort Totten").toString() );
        System.out.println( ms.stations.get( "L Enfant Plaza").toString() );

        // ====================================================
        // Graph Analysis ....
        // ====================================================

        // Part 1. Compute and print all the strongly connected components
        // of the directed graph

        StrongConnectivityInspector sci = new StrongConnectivityInspector( ms.metro );
        List stronglyConnectedSubgraphs = sci.stronglyConnectedSubgraphs();

        System.out.println("Strongly connected graph components:");
        System.out.println("========================================");
        for (int i = 0; i < stronglyConnectedSubgraphs.size(); i++) {
            System.out.println(stronglyConnectedSubgraphs.get(i));
        }
        System.out.println();

        // Part 2. Prints the shortest path from vertex i to vertex c. This
        // certainly exists for our particular directed graph.

        System.out.println("");
        System.out.println("Shortest path from \"Greenbelt\" to \"National Airport\"");
        System.out.println("========================================================");

        MetroStation start01 = (MetroStation) ms.stations.get( "Greenbelt" );
        MetroStation   end01 = (MetroStation) ms.stations.get( "National Airport");
        List path01 = DijkstraShortestPath.findPathBetween( ms.metro, start01, end01 );
        System.out.println(path01 + "\n");

        System.out.println("Shortest path from \"Greenbelt\" to \"New Carrollton\"");
        System.out.println("========================================================");

        MetroStation start02 = (MetroStation) ms.stations.get( "Greenbelt" );
        MetroStation   end02 = (MetroStation) ms.stations.get( "New Carrollton");

        List path02 = DijkstraShortestPath.findPathBetween( ms.metro, start02, end02 );
        System.out.println(path02 + "\n");
    }
}
