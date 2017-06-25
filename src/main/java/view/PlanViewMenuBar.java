/** 
  *  ======================================================================
  *  PlanMenuBar.java: Menu bar for simple GUI...
  * 
  *  Written By: Mark Austin                                      June 2012
  *  ======================================================================
  */ 

package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;

public class PlanViewMenuBar extends JMenuBar {
   private PlanViewPanel    mpp;
   private JButton      button1;
   private JButton      button2;
   private JButton      button3;
   private JButton      button4;
   private JButton      button5;

//   private Icon infoIcon  = new ImageIcon( "src/main/java/icons/info.gif" );

   private String[] taskLabels = {"Add Space Block",
                          "Add Support Column",
                          "Add Wall Corner",
                          "Add Exterior Wall",
                          "Add Portal",
                          "Add Door",
                          "Add Window" };

   // Constructor method for application version ....

   PlanViewMenuBar( PlanViewPanel mpp ) {
       this.mpp = mpp;

       // Create items for "file" pull-down menu ....

       JMenuItem openFile = new JMenuItem( "Open" );       
       openFile.addActionListener( new FileOpenListener() );
       
       // Import example 1
       
       JMenuItem importEx1 = new JMenuItem( "Import Example 1" );
       importEx1.addActionListener( new ExampleListener() );

       JMenuItem saveFile = new JMenuItem( "Save" );       
       saveFile.addActionListener( e ->
               System.out.println("*** Selected Save File!"));

       JMenuItem saveAsFile = new JMenuItem( "Save As" );       
       saveAsFile.addActionListener( e->
               System.out.println("*** Selected Save As File!") );

       // Create item to "exit" the system ...
       JMenuItem exitFile = new JMenuItem( "Exit" );       
       exitFile.addActionListener( e ->
               System.exit(0) );

       // Add a file menu to the toolbar ..

       JMenu fileMenu = new JMenu("File", true);
       fileMenu.add( openFile );
       fileMenu.addSeparator();
       fileMenu.add( importEx1 );
       fileMenu.addSeparator();
       fileMenu.add( saveFile );
       fileMenu.add( saveAsFile );
       fileMenu.add( exitFile );

       // Create items for "view" pull-down menu ....

       JMenu viewMenu = new JMenu("Layers");

       JCheckBoxMenuItem viewItem;
       String[] lbLabels =  { "Border Layer", "Grid Layer" };
       for (int i=0; i< lbLabels.length; i++) {
          viewItem = new JCheckBoxMenuItem( lbLabels[i], false );
          viewItem.setHorizontalTextPosition( JMenuItem.RIGHT );
          viewItem.addActionListener( new MenuListener() );
          viewMenu.add(viewItem);
       }

       // Create and add the grid menu and submenus...

       JMenu graphicsOptionsMenu = new JMenu("Options");
       JCheckBoxMenuItem cbItem;
       String[] cbLabels =  { "Snap to Grid", "Display Tag" };
       for (int i=0; i<cbLabels.length; i++) {
            cbItem = new JCheckBoxMenuItem( cbLabels[i] );
            cbItem.addActionListener( new MenuListener() );
            graphicsOptionsMenu.add(cbItem);
       }

       JMenu gridsizeOptionsMenu = new JMenu("Grid Size");

       JRadioButtonMenuItem rbItem;
       ButtonGroup group = new ButtonGroup();
       String[] gridLabels = { "5", "10", "20", "25", "40", "50", "100" };
       for (int i=0; i<gridLabels.length; i++) {
            rbItem = new JRadioButtonMenuItem( gridLabels[i] );
            rbItem.addActionListener( new MenuListener() );
            gridsizeOptionsMenu.add(rbItem);
            group.add(rbItem);
       }

       JMenu graphicsMenu = new JMenu("Graphics");
       graphicsMenu.add( graphicsOptionsMenu );
       graphicsMenu.add( gridsizeOptionsMenu );

       // Create label and combo box of editing tasks  ....

       JLabel taskLabel  = new JLabel( " Editing Task: ", JLabel.RIGHT);
       JComboBox taskBox = new JComboBox( taskLabels );
       taskBox.addActionListener( new TaskBoxListener() );

       // Create toolbar buttons for floorplan editing operations ....

       button1 = new JButton( new ImageIcon( "src/main/java/icons/none.png" ) );
       button1.setBackground(Color.lightGray);
       button1.setToolTipText("Draw Disabled");
       button1.addActionListener( new ShapeListener() );

       button2 = new JButton( new ImageIcon( "src/main/java/icons/query.png" ) );
       button2.setBackground(Color.lightGray);
       button2.setToolTipText("Query Schematic");
       button2.addActionListener( new ShapeListener() );

       button3 = new JButton( new ImageIcon( "src/main/java/icons/line.png" ) );
       button3.setBackground(Color.lightGray);
       button3.setToolTipText("Draw Line");
       button3.addActionListener( new ShapeListener() );

       button4 = new JButton( new ImageIcon( "src/main/java/icons/rectangle.png" ) );
       button4.setBackground(Color.lightGray);
       button4.setToolTipText("Draw Rectangle");
       button4.addActionListener( new ShapeListener() );

       button5 = new JButton( new ImageIcon( "src/main/java/icons/move.png" ) );
       button5.setBackground(Color.lightGray);
       button5.setToolTipText("Select and Move");
       button5.addActionListener( new ShapeListener() );

       // Add a menubar to the top of the graphics window ...

       JMenuBar menuBar = new JMenuBar();
       menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
       menuBar.setBorderPainted(true);

       menuBar.add( fileMenu );
       menuBar.add( viewMenu );
       menuBar.add( graphicsMenu );

       JPanel floorplanPanel = new JPanel();
       floorplanPanel.setBackground(Color.white);
       floorplanPanel.add( button1 );
       floorplanPanel.add( button2 );
       floorplanPanel.add( button3 );
       floorplanPanel.add( button4 );
       floorplanPanel.add( button5 );
       floorplanPanel.add( taskLabel );
       floorplanPanel.add( taskBox );

       this.setLayout( new BorderLayout() );
       this.setMargin( new Insets(5,5,5,5));
       this.setBorder( new BevelBorder(BevelBorder.RAISED) );
       this.setBorderPainted(true);
       this.add(          menuBar, BorderLayout.WEST );
       this.add(   floorplanPanel, BorderLayout.EAST );
   }     

   // Listener for "file open" class ....

   class FileOpenListener implements ActionListener {
       public void actionPerformed(ActionEvent e) {
          System.out.println("*** In FileOpen Listener!");
       }
   }

   // Listener for "draw grid" class ....

   class GridListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Draw Grid")) {
                mpp.getPlanViewCanvas().setGridDisplay();
                mpp.getPlanViewCanvas().repaint();
            }
        }
   }

   // Listener for handling menu selections ....

   class MenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            JMenuItem item = (JMenuItem) e.getSource();

            System.out.println("*** In MenuListener(): Label = " + item.getText() );

            // Adjust settings for draw box and draw grid ...

            if (item.getText().equals("Grid Layer")) {
                mpp.getPlanViewCanvas().setGridDisplay();
                mpp.getPlanViewCanvas().repaint();
            }

            // Determine which menu item has been selected ....

            if (item.getText().equals("Snap to Grid")) {
                boolean bSnapToGrid = item.getModel().isSelected();
                mpp.getEngineeringController().changeSnapToGridStatus( bSnapToGrid );
            }
            
            if (item.getText().equals("Display Tag")) {
            	boolean bDisplayTag = item.getModel().isSelected();
            	mpp.getEngineeringController().changeDisplayTagStatus( bDisplayTag );
            }

            // Set grid size from radio buttons ....

            if ( item.getText().equals("5") ||
            	 item.getText().equals("10") ||
                 item.getText().equals("20") ||
                 item.getText().equals("25") ||
                 item.getText().equals("40") ||
                 item.getText().equals("50") ||
                 item.getText().equals("100") ) {
                    int iGridSize = Integer.valueOf( item.getText() );
                    mpp.getEngineeringController().changeMetroGridStatus( iGridSize );
                }
        }
   }     

   // Listener for drawing rectangles and lines ....

   class ShapeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            JButton button = (JButton) e.getSource();
            System.out.println("In Shape Listener: button.setEnabled ( true ) ");

            // Determine which button has been pushed, process flags....

            button1.setBorder( new BevelBorder(BevelBorder.LOWERED) );
            button1.setBorderPainted(false);
            button2.setBorder( new BevelBorder(BevelBorder.LOWERED) );
            button2.setBorderPainted(false);
            button3.setBorder( new BevelBorder(BevelBorder.LOWERED) );
            button3.setBorderPainted(false);
            button4.setBorder( new BevelBorder(BevelBorder.LOWERED) );
            button4.setBorderPainted(false);
            button5.setBorder( new BevelBorder(BevelBorder.LOWERED) );
            button5.setBorderPainted(false);

            if (button.getToolTipText().equals("Draw Disabled")) {
                System.out.println("In Shape Listener: Draw Disabled() ....");
                button1.setBorder( new BevelBorder(BevelBorder.RAISED) );
                button1.setBorderPainted(true);
                mpp.getPlanViewCanvas().setDrawDisabled();
            }

            if (button.getToolTipText().equals("Query Schematic")) {
                button2.setBorder( new BevelBorder(BevelBorder.RAISED) );
                button2.setBorderPainted(true);
                mpp.getPlanViewCanvas().setQueryEnabled();
            }

            if (button.getToolTipText().equals("Draw Line")) {
                button3.setBorder( new BevelBorder(BevelBorder.RAISED) );
                button3.setBorderPainted(true);
                mpp.getPlanViewCanvas().setLineEnabled();
            }

            if (button.getToolTipText().equals("Draw Rectangle")) {
                button4.setBorder( new BevelBorder(BevelBorder.RAISED) );
                button4.setBorderPainted(true);
                mpp.getPlanViewCanvas().setRectangleEnabled();
            }

            if (button.getToolTipText().equals( "Select and Move")) {
                System.out.println("In Shape Listener: SelectAndMove() ....");
                button5.setBorder( new BevelBorder(BevelBorder.RAISED) );
                button5.setBorderPainted(true);
                mpp.getPlanViewCanvas().setSelectAndMove();
            }
        }
   }

   // Combo box listener to handle task selections

   class TaskBoxListener implements ActionListener {
       public void actionPerformed(ActionEvent e) {
          JComboBox tBox = (JComboBox) e.getSource();
          String task = (String) tBox.getSelectedItem();
          for (int i=0; i < taskLabels.length; i++) {
             if( task.equals( taskLabels[i] )) {
                 System.out.println("*** In TaskBoxListener: task = " + task );
                 mpp.getEngineeringController().changeFloorplanTaskStatus( task );
                 return;
             }
          }
       }
   }
   
   // Import floor plan example 1
   class ExampleListener implements ActionListener {
	   public void actionPerformed(ActionEvent e) {
		   JMenuItem item = (JMenuItem) e.getSource();
		   System.out.println("In Example Listener: button.importExample1 ( true ) ");
		   
		   if (item.getText().equals("Import Example 1")) {
			   mpp.getEngineeringController().importExample1( true );
		   }
	   }
   }
}
