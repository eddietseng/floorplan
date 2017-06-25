/*
 * ===================================================================
 * TableViewStations.java: Table view of the metro stations ....
 *
 * Written by: Mark Austin                                  June, 2012
 * ===================================================================
 */

package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.awt.ScrollPane;
import java.beans.PropertyChangeEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.event.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;

import javax.swing.table.TableModel;

import controller.AbstractController;
import controller.EngineeringController;
import model.metro.MetroStation;

import view.TableViewAdapterStations;

public class TableViewStations extends AbstractView {
   private Boolean DEBUG = true;
   private EngineeringController controller;
   private HashMap<String,MetroStation> stations;
   private JTable                 table;
   private TableViewAdapterStations tva;
   private TableRowSorter        sorter;

   public TableViewStations( EngineeringController controller ) {
      this.controller = controller;
   }

   public void changeStatus() {
      tva.changeStatus();
   }

   // Get table model ...

   public TableViewAdapterStations getTableViewAdapter() {
      return tva;
   }

   // Display frame ....

   public void display() {

      tva   = new TableViewAdapterStations();
      table = new JTable( tva );

      table.setRowHeight(45);
      table.setRowSelectionAllowed(true);
      table.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

      // Apply row sorter to JTable (new feature in JDK 6.0)

      table.setAutoCreateRowSorter(true);
      sorter = new TableRowSorter<TableViewAdapterStations>(tva);
      table.setRowSorter(sorter);

      // mouse listener uses table listener from above to notify people

      table.addMouseListener( new MouseListener() {

         // Click once to highlight appropriate entry in table view ...

         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
               int row = table.getSelectedRow();
               MetroStation m = (MetroStation) tva.getRowEntry(row);
               System.out.println("*** Go to controller.changeMetroStationStatus ( m )");
               controller.changeMetroStationStatus ( m );
            }
         }

         // Clear the highlighting from above

         public void mouseExited(MouseEvent e) {
            table.clearSelection();
         }

         public void mouseEntered(MouseEvent e) {}
         public void mousePressed(MouseEvent e) {}
         public void mouseReleased(MouseEvent e) {}
      });

      // Create gui components and layout ....

      JScrollPane scrollPane = new JScrollPane(table);
      scrollPane.setPreferredSize(new java.awt.Dimension(600, 600));

      JFrame frame = new JFrame("Table View: Metro Stations");
      frame.getContentPane().add( scrollPane );
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
      frame.pack();
      frame.setVisible(true);
   }

   // ===========================================================
   // Handle incoming PropertyChangeEvents sent by the controller
   // ===========================================================

   @Override
   public void modelPropertyChange( PropertyChangeEvent e ) {

      System.out.println("*** Enter TableStationView.modelPropertyChange() ... ");

      if (e.getPropertyName().equals( controller.METRO_STATION_PROPERTY ) ) {
          System.out.println("*** Inside TableViewStations().modelPropertyChange(): case METRO_STATION_PROPERTY !!!");
          MetroStation m = (MetroStation) (e.getNewValue());
	  System.out.println("*** MetroStation name = " + m.toString() );
      }

      if (e.getPropertyName().equals( controller.METRO_STATION_HASHMAP ) ) {
          System.out.println("*** Case METRO_STATION_HASHMAP !!!");
          stations = (HashMap) (e.getNewValue());
          tva.addStations ( stations );
      }
   }  
}
