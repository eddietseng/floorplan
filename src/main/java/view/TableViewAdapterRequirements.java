package view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.floorplan.function.Function;

public class TableViewAdapterRequirements 	
extends AbstractTableModel 
implements Runnable
{
	int delay = 100; // delay in milliseconds ....

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Function> requirements;
	private String [] colNames={ "Requirement Name", "Status" };
	
	public TableViewAdapterRequirements( ArrayList<Function> requirements ) 
	{
		this.requirements = requirements;
	    Thread runner = new Thread(this);
	    runner.start();
	}
	
	public int getColumnCount() 
	{
	    return colNames.length;
	}

	public int getRowCount() 
	{
	    return requirements.size();
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
	
	public synchronized void addRow( Function requirement ) 
	{
		requirements.add ( requirement );
	    fireTableDataChanged();
	}
	
	public synchronized void changeStatus() 
	{
		fireTableDataChanged();
	}

	public Object getRowEntry( int row ) 
	{
		return requirements.get(row);
	}

	public Class<?> getColumClass( int column ) 
	{
		return getValueAt(0,column).getClass();
	}

	public boolean isCellEditable(int row, int col) 
	{
		//TODO
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
			return requirements.get(row).getReqName();
	    case 1:
	        return requirements.get(row).isValid();
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
