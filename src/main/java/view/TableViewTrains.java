/*
 * ===================================================================
 * TableViewTrains.java: Table view of train behavior ....
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

import model.*;
import model.metro.*;

import view.TableViewAdapterTrains;

public class TableViewTrains extends AbstractView {
   private Boolean DEBUG = true;
   private EngineeringController controller;
   private HashMap<String,MetroTrain> trains;
   private JTable               table;
   private TableViewAdapterTrains tva;
   private TableRowSorter      sorter;

   public TableViewTrains( EngineeringController controller ) {
      this.controller = controller;
   }

   public void changeStatus() {
      tva.changeStatus();
   }

   // Get table model ...

   public TableViewAdapterTrains getTableViewAdapter() {
      return tva;
   }

   // Display frame ....

   public void display() {

      tva   = new TableViewAdapterTrains();
      table = new JTable( tva );

      table.setRowHeight(45);
      table.setRowSelectionAllowed(true);
      table.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

      // Apply row sorter to JTable (new feature in JDK 6.0)

      table.setAutoCreateRowSorter(true);
      sorter = new TableRowSorter<TableViewAdapterTrains>(tva);
      table.setRowSorter(sorter);

      // Mouse listener uses table listener from above to notify people

      table.addMouseListener( new MouseListener() {

         // Click once to highlight appropriate entry in table view ...

         public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 1) {
               int row = table.getSelectedRow();
               MetroTrain t = (MetroTrain) tva.getRowEntry(row);
               controller.changeMetroTrainStatus ( t );
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
      scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

      JFrame frame = new JFrame("Table View: Trains");
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

      if (e.getPropertyName().equals( controller.METRO_STATION_PROPERTY ) ) {
          System.out.println("*** Inside TableViewTrains().modelPropertyChange(): case METRO_STATION_PROPERTY !!!");
          MetroStation m = (MetroStation) (e.getNewValue());
	  System.out.println("*** IGNORE ... MetroStation name = " + m.toString() );
      }

      if (e.getPropertyName().equals( controller.METRO_TRAIN_HASHMAP ) ) {
          System.out.println("*** Inside METRO_TRAIN_HASHMAP !!!");
          trains = (HashMap) (e.getNewValue());
          tva.addTrains ( trains );
      }
   }  
}
