package view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.floorplan.RoomModel;

public class TableViewAdapterRoom 
	extends AbstractTableModel 
	implements Runnable
{
	int delay = 100; // delay in milliseconds ....

	private static final long serialVersionUID = 1L;

	private ArrayList<RoomModel> rooms;
	private String [] colNames={ "Name", "Role", "Area", "Zone" };
	
	public TableViewAdapterRoom( ArrayList <RoomModel> rooms ) 
	{
		this.rooms = rooms;
	    Thread runner = new Thread(this);
	    runner.start();
	}

	public int getColumnCount() 
	{
	    return colNames.length;
	}

	public int getRowCount() 
	{
	    return rooms.size();
	}

	public String getColumnName(int col) 
	{
		return colNames[col];
	}
	
	// JTable uses this method to determine the default renderer/
	// editor for each cell.  If we didn't implement this method,
	// then the last column would contain text ("true"/"false"),
	// rather than a check box.
	
	public Class getColumnClass(int c) 
	{
		return getValueAt(0, c).getClass();
	}

	public synchronized void addRow( RoomModel room ) 
	{
		rooms.add ( room );
	    fireTableDataChanged();
	}

	public synchronized void changeStatus() 
	{
		fireTableDataChanged();
	}

	public Object getRowEntry( int row ) 
	{
		return rooms.get(row);
	}

	public Class<?> getColumClass( int column ) 
	{
		return getValueAt(0,column).getClass();
	}

	public boolean isCellEditable(int row, int col) 
	{
		return false;
	}
	   
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) 
	{
		//TODO
	}

	public Object getValueAt(int row, int col) 
	{
		switch (col)
		{
		case 0:
			return rooms.get(row).getName();
	    case 1:
	        return rooms.get(row).getRole();
	    case 2:
	        return String.format("%6.1f", ((RoomModel)rooms.get(row)).getArea());
	    case 3:
	    	return rooms.get(row).getZone();
	    default:
	        return null;
	    }
	}

	public void run() 
	{
		while(true) 
		{
			// Blind update . . . we could check for real deltas if necessary
	        // We know there are no new columns, so don't fire a data change, only
	        // fire a row update . . . this keeps the table from flashing

	        fireTableRowsUpdated(0, getRowCount() - 1 );
	        try { Thread.sleep(delay); }
	        catch(InterruptedException ie) {}
	    }
	}
}
