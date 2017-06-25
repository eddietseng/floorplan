/*
 * ============================================================================
 * PrintViewPanel.java:
 *
 * ============================================================================
 */ 

package view;

import java.awt.*;
import java.awt.geom.*;
import java.awt.Point;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.List;
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
import model.floorplan.FloorplanResultModel;

public class PrintViewPanel extends JPanel implements DocumentListener {
    private JTextField          entry;
    private JLabel           jLabel01;
    private JScrollPane jScrollPane01;
    private JLabel             status;
    private JRadioButton     button01;
    private JRadioButton     button02;
    private JRadioButton     button03;
    private JRadioButton     button04;
    private JRadioButton     button05;
    private JRadioButton     button06;
    private JTextArea        textArea;
    
    final static Color  HILIT_COLOR   = Color.LIGHT_GRAY;
    final static Color  ERROR_COLOR   = Color.PINK;
    final static String CANCEL_ACTION = "cancel-search";
    
    final Color entryBg;
    final Highlighter hilit;
    final Highlighter.HighlightPainter painter;

    private CompositeHierarchy     gridWorkspace;
    private CompositeHierarchy    metroWorkspace;
    private CompositeHierarchy    trackWorkspace;
    private CompositeHierarchy     areaWorkspace;
    private CompositeHierarchy    trainWorkspace;
    private CompositeHierarchy buildingWorkspace;
    
    private FloorplanResultModel floorplanResultModel;

    // Constructor method ....
    
    public PrintViewPanel() {
        initComponents();

        hilit   = new DefaultHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
        textArea.setHighlighter(hilit);
        
        entryBg = entry.getBackground();
        entry.getDocument().addDocumentListener(this);
        
        InputMap  im = entry.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap am = entry.getActionMap();
        im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
        am.put(CANCEL_ACTION, new CancelAction());
    }
    
    // Initialize group layout of components ....

    private void initComponents() {
        entry    = new JTextField(20);
        textArea = new JTextArea();
        status   = new JLabel();

        String sButton01 = "Grid";
        String sButton02 = "Metro System";
        String sButton03 = "Metro Area";
        String sButton04 = "Building System";
        String sButton05 = "Building Operations";
        String sButton06 = "Floorplan Result";

        button01 = new JRadioButton( sButton01 );
        button02 = new JRadioButton( sButton02 );
        button03 = new JRadioButton( sButton03 );
        button04 = new JRadioButton( sButton04 );
        button05 = new JRadioButton( sButton05 );
        button06 = new JRadioButton( sButton06 );
        
        jLabel01 = new JLabel();

        // Group the radio buttons.

        ButtonGroup group = new ButtonGroup();
        group.add( button01 );
        group.add( button02 );
        group.add( button03 );
        group.add( button04 );
        group.add( button05 );
        group.add( button06 );

        // Initialize component properties ....

        textArea.setColumns(25);
        textArea.setLineWrap(true);
        textArea.setRows(25);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        Font font = new Font("Monospaced", Font.PLAIN, 14);
        textArea.setFont(font);
        textArea.setText(" .... ");

        jScrollPane01 = new JScrollPane(textArea);
        jScrollPane01.setPreferredSize(new Dimension( 700, 500));
        jScrollPane01.setVerticalScrollBarPolicy(   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
        jScrollPane01.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

        jLabel01.setText("Enter text to search:");

        // Initialize component properties ....

        button01.addActionListener( new PrintViewButtonListener( this ) );
        button01.setActionCommand( sButton01 );

        button02.addActionListener( new PrintViewButtonListener( this ) );
        button02.setActionCommand( sButton02 );

        button03.addActionListener( new PrintViewButtonListener( this ) );
        button03.setActionCommand( sButton03 );

        button04.addActionListener( new PrintViewButtonListener( this ) );
        button04.setActionCommand( sButton04 );

        button05.addActionListener( new PrintViewButtonListener( this ) );
        button05.setActionCommand( sButton05 );
        
        button06.addActionListener( new PrintViewButtonListener( this ) );
        button06.setActionCommand( sButton06 );

        // Add components to layout panels ...

        JPanel p1 = new JPanel();
        p1.setLayout( new FlowLayout() );
        p1.add ( jLabel01 );
        p1.add ( entry    );

        JPanel p2 = new JPanel();
        p2.add ( jScrollPane01 );

        JPanel p3 = new JPanel();
        p3.add ( status );

        JPanel p4 = new JPanel();
        p4.add ( button01 );
        //p4.add ( button02 );
        //p4.add ( button03 );
        //p4.add ( button04 );
        //p4.add ( button05 );
        p4.add ( button06 );

        JPanel p5 = new JPanel();
        p5.setLayout( new BorderLayout() );
        p5.add ( p3, BorderLayout.NORTH );
        p5.add ( p4, BorderLayout.SOUTH );

        // Building layout of panels ....

        setLayout( new BorderLayout() );
        add ( p1, BorderLayout.NORTH );
        add ( p2, BorderLayout.CENTER );
        add ( p5, BorderLayout.SOUTH );
    }

    // Set composite hierarchy workspaces ...

    public void setMetroGridWorkspace ( CompositeHierarchy workspace ) {
       this.gridWorkspace = workspace;
    }

    public void setMetroSystemWorkspace ( CompositeHierarchy workspace ) {
       this.metroWorkspace = workspace;
    }

    public void setMetroTrackWorkspace ( CompositeHierarchy workspace ) {
       this.trackWorkspace = workspace;
    }

    public void setMetroAreaWorkspace ( CompositeHierarchy workspace ) {
       this.areaWorkspace = workspace;
    }

    public void setMetroTrainsWorkspace( CompositeHierarchy workspace ) {
       this.trainWorkspace = workspace;
    }

    public void setBuildingLayoutWorkspace ( CompositeHierarchy workspace ) {
       this.buildingWorkspace = workspace;
    }

    // Print details of metro system, metro area, train capacity and 
    // operating cost....

    public void printMetroGrid() {
       PrintViewGridVisitor pvg = new PrintViewGridVisitor();
       gridWorkspace.accept( pvg );
       textArea.setText( pvg.getText() );
    }

    public void printMetroSystem() {
       PrintViewDetailsVisitor cpv = new PrintViewDetailsVisitor();
       metroWorkspace.accept( cpv );
       textArea.setText( cpv.getText() );
    }

    public void printMetroArea() {
       PrintViewDetailsVisitor cpv = new PrintViewDetailsVisitor();
       areaWorkspace.accept( cpv );
       textArea.setText( cpv.getText() );
    }

    public void printTrainSystem() {
       PrintViewDetailsVisitor cpv = new PrintViewDetailsVisitor();
       trainWorkspace.accept( cpv );
       textArea.setText( cpv.getText() );
    }

    public void printTrainOperations() {
       PrintViewOperationsVisitor cpv = new PrintViewOperationsVisitor();
       trainWorkspace.accept( cpv );
       textArea.setText( cpv.getText() );
    }

    // ==========================================================
    // Print building layout details and operations summmary ....
    // ==========================================================

    public void printBuildingSystem() {
       PrintViewDetailsVisitor cpv = new PrintViewDetailsVisitor();
       buildingWorkspace.accept( cpv );
       textArea.setText( cpv.getText() );
    }

    public void printBuildingOperations() {
       PrintViewOperationsVisitor cpv = new PrintViewOperationsVisitor();
       buildingWorkspace.accept( cpv );
       cpv.setSummary();
       textArea.setText( cpv.getText() );
    }

    // Search through text ...

    public void search() {
        hilit.removeAllHighlights();
        
        String s = entry.getText();
        if (s.length() <= 0) {
            message("Nothing to search");
            return;
        }
        
        String content = textArea.getText();
        int index = content.indexOf(s, 0);
        if (index >= 0) {   // match found
            try {
                int end = index + s.length();
                hilit.addHighlight(index, end, painter);
                textArea.setCaretPosition(end);
                entry.setBackground(entryBg);
                message("'" + s + "' found. Press ESC to end search");
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else {
            entry.setBackground(ERROR_COLOR);
            message("'" + s + "' not found. Press ESC to start a new search");
        }
    }

    void message(String msg) {
        status.setText(msg);
    }

    // DocumentListener methods
    
    public void insertUpdate(DocumentEvent ev) {
        search();
    }
    
    public void removeUpdate(DocumentEvent ev) {
        search();
    }
    
    public void changedUpdate(DocumentEvent ev) {}
    
    class CancelAction extends AbstractAction {
        public void actionPerformed(ActionEvent ev) {
            hilit.removeAllHighlights();
            entry.setText("");
            entry.setBackground(entryBg);
        }

    }

	public void setFloorplanResultModel( FloorplanResultModel floorplanResultModel) {
		this.floorplanResultModel = floorplanResultModel;
	}

	public void printFloorplanResultModel() {
		PrintViewFloorplanResultVisitor cpv = new PrintViewFloorplanResultVisitor();
		floorplanResultModel.accept( cpv );
	    textArea.setText( cpv.getText() );
	}
}

class PrintViewButtonListener implements ActionListener {
   private PrintViewPanel pvp;

   public PrintViewButtonListener( PrintViewPanel pvp ) {
      this.pvp = pvp;
   }

   public void actionPerformed(ActionEvent e) {

      if (e.getActionCommand().equals("Grid")) {
         pvp.printMetroGrid();
      }

      if (e.getActionCommand().equals("Metro System")) {
         pvp.printMetroSystem();
      }

      if (e.getActionCommand().equals("Metro Area")) {
         pvp.printMetroArea();
      }

      if (e.getActionCommand().equals("Building System")) {
         pvp.printBuildingSystem();
      }

      if (e.getActionCommand().equals("Building Operations")) {
         pvp.printBuildingOperations();
      }
      
      if (e.getActionCommand().equals("Floorplan Result") ) {
    	  pvp.printFloorplanResultModel();
      }
   }
}
