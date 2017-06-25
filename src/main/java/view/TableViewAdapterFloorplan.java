package view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.floorplan.FloorplanResultModel;

public class TableViewAdapterFloorplan 
	extends AbstractTableModel 
	implements Runnable
{
	int delay = 100; // delay in milliseconds ....

	private static final long serialVersionUID = 1L;

	private ArrayList<FloorplanResultModel> results;
	private String [] colNames={ "Case/City", "Total Area", "Usable Area", "Total Price",
			"Required Cooling Load", "Zone 1 Area", "Zone 2 Area", "Zone 1 Cooling Load", "Zone 2 Cooling Load" };
	
	public TableViewAdapterFloorplan( ArrayList <FloorplanResultModel> results ) 
	{
		this.results = results;
	    Thread runner = new Thread(this);
	    runner.start();
	}

	public int getColumnCount() 
	{
	    return colNames.length;
	}

	public int getRowCount() 
	{
	    return results.size();
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

	public synchronized void addRow( FloorplanResultModel result ) 
	{
		results.add ( result );
	    fireTableDataChanged();
	}

	public synchronized void changeStatus() 
	{
		fireTableDataChanged();
	}

	public Object getRowEntry( int row ) 
	{
		return results.get(row);
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
	}

	public Object getValueAt(int row, int col) 
	{
		switch (col)
		{
		case 0:
			return new String( results.get(row).getCaseNumber() +"/" 
		+ results.get(row).getCity().getLocation() );
	    case 1:
	        return Double.toString( results.get(row).getTotalArea() );
	    case 2:
	        return Double.toString( results.get(row).getUsableArea() );
	    case 3:
	        return Double.toString( results.get(row).getTotalPrice() );
	    case 4:
	        return new String( results.get(row).getCoolingLoadWhole() + " Tons" );
	    case 5:
	        return Double.toString( results.get(row).getZone1Area() );
	    case 6:
	        return Double.toString( results.get(row).getZone2Area() );
	    case 7:
	        return new String( results.get(row).getCoolingLoadZone1() + " Tons" );
	    case 8:
	        return new String( results.get(row).getCoolingLoadZone2() + " Tons" );
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
