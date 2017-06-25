package view;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.floorplan.Wall2DModel;

public class TableViewAdapterWall 
	extends AbstractTableModel 
	implements Runnable
{
	int delay = 100; // delay in milliseconds ....

	private static final long serialVersionUID = 1L;

	private ArrayList<Wall2DModel> walls;
	private String [] colNames={ "Name", "Location", "Width", "Height", "Area", "Price", "Status" };
	
	public TableViewAdapterWall( ArrayList <Wall2DModel> walls ) 
	{
		this.walls = walls;
	    Thread runner = new Thread(this);
	    runner.start();
	}

	public int getColumnCount() 
	{
	    return colNames.length;
	}

	public int getRowCount() 
	{
	    return walls.size();
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

	public synchronized void addRow( Wall2DModel wall ) 
	{
		walls.add ( wall );
	    fireTableDataChanged();
	}

	public synchronized void changeStatus() 
	{
		fireTableDataChanged();
	}

	public Object getRowEntry( int row ) 
	{
		return walls.get(row);
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
			return walls.get(row).getName();
	    case 1:
	        return String.format("(%6.1f, %6.1f )", walls.get(row).getX(),
	        		                                walls.get(row).getY() );
	    case 2:
	        return String.format("%8.1e", ((Wall2DModel) walls.get(row)).getWidth());
	    case 3:
	        return String.format("%8.1e", ((Wall2DModel) walls.get(row)).getHeight());
	    case 4:
	    	return String.format("%8.1e", ((Wall2DModel) walls.get(row)).getArea());
	    case 5:
	    	return new String( "$ " + new DecimalFormat("#").format(((Wall2DModel) walls.get(row)).getPrice()) );
	    case 6:
	    	return (new Boolean ( walls.get(row).getSelection() ));
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
