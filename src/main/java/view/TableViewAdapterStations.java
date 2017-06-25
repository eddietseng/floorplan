/** 
  *  ======================================================================
  *  TableViewAdapterStations.java: Adapter for hashmap to table conversion
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

import model.metro.MetroStation;

public class TableViewAdapterStations extends AbstractTableModel {
   private ArrayList<MetroStation> stations;
   private String [] colNames={ "Name", "Coordinate", "Lines" };

   public TableViewAdapterStations() {
      stations = new ArrayList<MetroStation>();
   }

   public void addStations( HashMap<String,MetroStation> stations ) {

      // Transfer hashmp to arraylist ...

      Set st = stations.keySet();
      Iterator itr = st.iterator();
      while (itr.hasNext()) {
         String key = (String) itr.next();
         MetroStation m = (MetroStation) stations.get(key);
         this.stations.add( m );
      }

      // Update table properties ...

      fireTableDataChanged();
   }

   public int getColumnCount() {
      return colNames.length;
   }

   public int getRowCount() {
      return stations.size();
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
      return stations.get(row);
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
              return stations.get(row).getStationName();
         case 1:
              return String.format("(%6.1f, %6.1f )", stations.get(row).getX(),
                                                      stations.get(row).getY() );
         case 2:
              String s = "";
              ArrayList tracks = stations.get(row).getTracks();
              int onTrackSize  = tracks.size();
              if ( onTrackSize > 0 ) {
                 for (int i = 0; i < onTrackSize; i = i + 1)
                    s = s.concat( tracks.get(i) + " ");
              }
              return s;
         default:
              return null;
      }
   }
}
