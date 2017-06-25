package view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.floorplan.GridLineDirection;
import model.floorplan.GridLineModel;

public class TableViewAdapterCenterLine
	extends AbstractTableModel 
	implements Runnable
{
	int delay = 100; // delay in milliseconds ....

	private static final long serialVersionUID = 1L;

	private ArrayList<GridLineModel> centerlines;
	private String [] colNames={ "Name", "x", "y", "Shift value", "Status" };
	
	public TableViewAdapterCenterLine( ArrayList <GridLineModel> centerlines ) 
	{
		this.centerlines = centerlines;
	    Thread runner = new Thread(this);
	    runner.start();
	}

	public int getColumnCount() 
	{
	    return colNames.length;
	}

	public int getRowCount() 
	{
	    return centerlines.size();
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

	public synchronized void addRow( GridLineModel centerline ) 
	{
		centerlines.add ( centerline );
	    fireTableDataChanged();
	}

	public synchronized void changeStatus() 
	{
		fireTableDataChanged();
	}

	public Object getRowEntry( int row ) 
	{
		return centerlines.get(row);
	}

	public Class<?> getColumClass( int column ) 
	{
		return getValueAt(0,column).getClass();
	}

	public boolean isCellEditable(int row, int col) 
	{
		if (col == 3) {
			GridLineModel line = ((GridLineModel) centerlines.get( row ));
			if( line.isGridLineMoveable() )
				return true;
			else
				return false;
	    } else {
	        return false;
	    }
	}
	   
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) 
	{
		double value = Double.parseDouble( (String)aValue );
		   if( columnIndex == 3 ){
			   GridLineModel moveLine = ((GridLineModel) centerlines.get( rowIndex ));
			   if( moveLine.getGridLineDirection().equals( GridLineDirection.ParallelToYAxis ))
			   {
				   double v = moveLine.getShiftValue() + (double)value ;
				   moveLine.setShiftValue( v );
				   moveLine.move( (double)value, 0 );
			   }
			   else{
				   double v = moveLine.getShiftValue() + (double)value ;
				   moveLine.setShiftValue( v );
				   moveLine.move( 0, (double)value );
			   }
			   fireTableCellUpdated( rowIndex, columnIndex );
		   }
	}

	public Object getValueAt(int row, int col) 
	{
		switch (col)
		{
		case 0:
			return centerlines.get(row).getName();
	    case 1:
	        return String.format("%6.1f", ((GridLineModel)centerlines.get(row)).getAnchorX());
	    case 2:
	        return String.format("%6.1f", ((GridLineModel)centerlines.get(row)).getAnchorY());
	    case 3:
	    	return String.format("%6.1f", ((GridLineModel)centerlines.get(row)).getShiftValue());
	    case 4:
	    	return (new Boolean ( centerlines.get(row).getSelection() ));
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
