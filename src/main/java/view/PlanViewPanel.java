/*
 * ============================================================================
 * PlanViewPanel.java:
 *
 * ============================================================================
 */ 

package view;

import java.awt.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.Point;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.lang.Math;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import model.*;
import model.CompositeHierarchy;
import controller.EngineeringController;

public class PlanViewPanel extends JPanel {
    private boolean DEBUG =    true;
    private PlanViewCanvas   canvas;
    private JScrollPane jScrollPane01;
    private JRadioButton     button01;
    private JRadioButton     button02;
    private JRadioButton     button03;
    private JRadioButton     button04;
    private JRadioButton     button05;
    private PlanViewMenuBar   menuBar;

    private EngineeringController            controller;
    
    private CompositeHierarchy base;

    private CompositeHierarchy      gridWorkspace;
    private CompositeHierarchy     metroWorkspace;
    private CompositeHierarchy     trackWorkspace;
    private CompositeHierarchy      areaWorkspace;
    private CompositeHierarchy     trainWorkspace;
    private CompositeHierarchy  buildingWorkspace;
    private CompositeHierarchy floorplanWorkspace;

    private HashMap grid;
    
    private boolean displayTag;

    // Constructor method ....
    
    public PlanViewPanel( EngineeringController controller ) {
       this.controller = controller;
    }
    
    // Initialize group layout of components ....

    public void initComponents() {

        // Create and add mousemotion listener to display panel ..

        canvas = new PlanViewCanvas( this );
        canvas.setBackground ( Color.white );
        canvas.setPreferredSize( new Dimension(1000, 750) );

        PlanViewCanvasListener mp = new PlanViewCanvasListener( canvas );
        canvas.addMouseListener( mp );
        canvas.addMouseMotionListener( mp );

        // Create menubar ...

        menuBar = new PlanViewMenuBar( this );

        String sButton01 = "Grid";
        String sButton02 = "Metro System";
        String sButton03 = "Metro Area";
        String sButton04 = "Building System";
        String sButton05 = "Floorplan System";

        button01 = new JRadioButton( sButton01 );
        button02 = new JRadioButton( sButton02 );
        button03 = new JRadioButton( sButton03 );
        button04 = new JRadioButton( sButton04 );
        button05 = new JRadioButton( sButton05 );

        // Initialize component properties ....

        jScrollPane01 = new JScrollPane( canvas );
        jScrollPane01.setPreferredSize( new Dimension( 1000, 750) );
        jScrollPane01.setVerticalScrollBarPolicy(   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
        jScrollPane01.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        // Listen for value changes in the scroll pane's scrollbars

        AdjustmentListener listener = new MyAdjustmentListener( canvas );
        jScrollPane01.getHorizontalScrollBar().addAdjustmentListener(listener);
        jScrollPane01.getVerticalScrollBar().addAdjustmentListener(listener);

        // Initialize component properties ....

        button01.addActionListener( new PlanViewButtonListener( this ) );
        button01.setActionCommand( sButton01 );

        button02.addActionListener( new PlanViewButtonListener( this ) );
        button02.setActionCommand( sButton02 );

        button03.addActionListener( new PlanViewButtonListener( this ) );
        button03.setActionCommand( sButton03 );

        button04.addActionListener( new PlanViewButtonListener( this ) );
        button04.setActionCommand( sButton04 );

        button05.addActionListener( new PlanViewButtonListener( this ) );
        button05.setActionCommand( sButton05 );

        // Add components to layout panels ...

        JPanel p1 = new JPanel();
        p1.add ( jScrollPane01 );

        JPanel p2 = new JPanel();
        p2.add ( button01 );
        //p2.add ( button02 );
        //p2.add ( button03 );
        //p2.add ( button04 );
        p2.add ( button05 );

        // Building layout of panels ....

        setLayout( new BorderLayout() );
        add ( menuBar, BorderLayout.NORTH );
        add (      p1, BorderLayout.CENTER );
        add (      p2, BorderLayout.SOUTH );
    }

    // Get reference to Metro Controller ...

    public EngineeringController getEngineeringController () {
       return controller;
    }

    // Set hashmap data  ...

    public void setMetroGridData ( HashMap grid ) {
       this.grid = grid;
       canvas.setMetroGridData ( grid );
    }
    
    public void setDisplayTag ( boolean displayTag ) {
    	this.displayTag = displayTag;
    	canvas.setDisplayTag( displayTag );
    }

    // Set composite hierarchy workspaces ...

    public void setMetroGridWorkspace ( CompositeHierarchy workspace ) {
       this.gridWorkspace = workspace;
       canvas.setMetroGridWorkspace ( workspace );
       canvas.repaint();
    }

    public void setMetroSystemWorkspace ( CompositeHierarchy workspace ) {
       this.metroWorkspace = workspace;
       canvas.setMetroSystemWorkspace ( workspace );
    }

    public void setMetroTrackWorkspace ( CompositeHierarchy workspace ) {
       this.trackWorkspace = workspace;
       canvas.setMetroTrackWorkspace ( workspace );
    }

    public void setMetroAreaWorkspace ( CompositeHierarchy workspace ) {
       this.areaWorkspace = workspace;
       canvas.setMetroAreaWorkspace ( workspace );
    }

    public void setMetroTrainsWorkspace( CompositeHierarchy workspace ) {
       this.trainWorkspace = workspace;
       canvas.setMetroTrainsWorkspace ( workspace );
    }

    public void setBuildingLayoutWorkspace ( CompositeHierarchy workspace ) {
       this.buildingWorkspace = workspace;
       canvas.setBuildingLayoutWorkspace ( workspace );
    }

    public void setFloorplanWorkspace ( CompositeHierarchy workspace ) {
       this.floorplanWorkspace = workspace;
       canvas.setFloorplanWorkspace ( workspace );
    }
    
    // propagate the status change down to the canvas
    public void setFloorplanSelectedStatus ( AbstractCompoundFeature component ) {
       canvas.setFloorplanSelectedStatus( component );
       canvas.paint();
    }
    
    // Get PlanViewCanvas ....

    public PlanViewCanvas getPlanViewCanvas() {
       return canvas;
    }
}

// ===============================================================
// Adjustment listener is called whenever the value of a scrollbar
// is changed either by the user or programmatically.
// ===============================================================

class MyAdjustmentListener implements AdjustmentListener {
    PlanViewCanvas canvas;

    public MyAdjustmentListener( PlanViewCanvas canvas ) {
       this.canvas = canvas;
    }

    public void adjustmentValueChanged(AdjustmentEvent evt) {
       Adjustable source = evt.getAdjustable();
       canvas.repaint();
    }
}

// ===============================================================
// ActionListener for the PlanViewPanel....
// ===============================================================

class PlanViewButtonListener implements ActionListener {
   private PlanViewPanel pvp;

   public PlanViewButtonListener( PlanViewPanel pvp ) {
      this.pvp = pvp;
   }

   public void actionPerformed(ActionEvent e) {

      if (e.getActionCommand().equals("Grid")) {
         System.out.println("*** In PlanViewButtonListener(): Clicked Grid");
         pvp.getPlanViewCanvas().setGridDisplay();
      }

      if (e.getActionCommand().equals("Metro System")) {
         System.out.println("*** In PlanViewButtonListener(): Clicked Metro System");
         pvp.getPlanViewCanvas().setMetroSystemDisplay();
      }

      if (e.getActionCommand().equals("Metro Area")) {
         System.out.println("*** In PlanViewButtonListener(): Clicked Metro Area");
         pvp.getPlanViewCanvas().setMetroAreaDisplay();
      }

      if (e.getActionCommand().equals("Building System")) {
         System.out.println("*** In PlanViewButtonListener(): Clicked Building System");
         pvp.getPlanViewCanvas().setBuildingLayoutDisplay();
      }

      if (e.getActionCommand().equals("Floorplan System")) {
         System.out.println("*** In PlanViewButtonListener(): Clicked Floorplan System");
         pvp.getPlanViewCanvas().setFloorplanDisplay();
      }
   }
}
