/** 
  *  ======================================================================
  *  TableViewAdapterTrains.java: Adapter for hashmap to table conversion.
  * 
  *  Written By: Mark Austin                                      June 2012
  *  ======================================================================
  */ 

package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import model.metro.MetroTrain;

public class TableViewAdapterTrains extends AbstractTableModel {
   private ArrayList<MetroTrain> trains;
   private String [] colNames={ "Name", "Line", "Status" };

   public TableViewAdapterTrains() {
      trains = new ArrayList<MetroTrain>();
   }

   public void addTrains( HashMap<String,MetroTrain> trains ) {

      // Transfer hashmap to arraylist ...

      Set st = trains.keySet();
      Iterator itr = st.iterator();
      while (itr.hasNext()) {
         String key = (String) itr.next();
         MetroTrain t = (MetroTrain) trains.get(key);
         this.trains.add( t );
      }

      // Update table properties ...

      fireTableDataChanged();
   }

   public int getColumnCount() {
      return colNames.length;
   }

   public int getRowCount() {
      return trains.size();
   }

   public String getColumnName(int col) {
      return colNames[col];
   }

   // JTable uses this method to determine the default renderer/
   // editor for each cell.  If we didn't implement this method,
   // then the last column would contain text ("true"/"false"),
   // rather than a check box.
         
   public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
   }

   public synchronized void changeStatus() {
      fireTableDataChanged();
   }

   public Object getRowEntry( int row ) {
      return trains.get(row);
   }

   public Class<?> getColumClass( int column ) {
      return getValueAt(0,column).getClass();
   }

   public boolean isCellEditable(int row, int col) {
      if (col == 2) {
          return true;
      } else {
          return false;
      }
   }

   public Object getValueAt(int row, int col) {
      switch (col) {
         case 0:
              return trains.get(row).getTrainName();
         case 1:
              return trains.get(row).getTrack();
         case 2:
              return trains.get(row).getStatus();
         default:
              return null;
      }
   }
}
