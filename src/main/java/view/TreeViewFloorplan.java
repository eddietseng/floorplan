package view;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import model.AbstractCompoundFeature;
import model.AbstractModel;
import model.FloorplanModel;
import model.floorplan.GridLineModel;



import controller.AbstractController;
import controller.EngineeringController;

public class TreeViewFloorplan extends AbstractView
{
	private EngineeringController controller;
	private DefaultMutableTreeNode rootNode;

	private TreeViewAdapterFloorplan tva;
	private TreeViewPanel   tvp;
	
	public TreeViewFloorplan( AbstractController controller ) 
	{
		this.controller = (EngineeringController) controller;

	    // Create blank table view adapter ...

	    rootNode  = new DefaultMutableTreeNode("Floorplan Model");
	    tva       = new TreeViewAdapterFloorplan( rootNode );
	}
	 
	// Add new feature to the tree view panel (tvp) ....

	public void addShape( AbstractCompoundFeature s )
	{
	    tvp.addObject ( null, (Object) s );
	}

	// Change feature of an incoming item ....

	public void changeStatus( AbstractCompoundFeature s ) 
	{
	    tva.changeStatus ( s );
	}
	
	public TreeViewAdapterFloorplan getTreeViewAdapter() 
	{
	      return tva;
	}

	// Display frame ....
	
	public void display() 
	{
		tvp = new TreeViewPanel( tva, controller );

	    // Create gui components and layout ....

	    JScrollPane scrollPane = new JScrollPane(tvp);
	    scrollPane.setPreferredSize(new java.awt.Dimension(300, 600));

	    JFrame frame = new JFrame("Floorplan Tree View");
	    frame.getContentPane().add( scrollPane );
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    frame.pack();
	    frame.setVisible(true);
	}
	
	

	@Override
	public void modelPropertyChange(PropertyChangeEvent e) 
	{
		System.out.println("*** Enter TreeViewFloorplan.modelPropertyChange() ... ");
		
		// Status change to ball already in tree view ....
		
		if (e.getPropertyName().equals( controller.FLOORPLAN_SELECTED_PROPERTY_STATUS ) )
		{
			AbstractCompoundFeature newObject = (AbstractCompoundFeature) (e.getNewValue());
	        changeStatus( newObject );
	    }

	    // Add new feature entry to tree view ....
		
	    if (e.getPropertyName().equals( controller.FLOORPLAN_HASHMAP ) )
	    {
	    	if( e.getNewValue() != null && e.getNewValue() instanceof HashMap )
	    	{
	    		ArrayList<GridLineModel> addedLines = new ArrayList<GridLineModel>();
	    		ArrayList<String> keys = new ArrayList<String>();
	    		for( AbstractModel model : controller.getRegisteredModels() )
	    		{
	    			if( model instanceof FloorplanModel )
	    			{
	    				keys.addAll( ((FloorplanModel)model).addedKeys );
	    				//((FloorplanModel)model).addedKeys.clear();
	    				addedLines.addAll( ((FloorplanModel)model).getGridSystem().addedLines );
	    				//((FloorplanModel)model).getGridSystem().addedLines.clear();
	    			}
	    		}
	    		
	    		for( int i = 0; i < keys.size(); i++ )
	    		{
	    			AbstractCompoundFeature newObject = 
	    					(AbstractCompoundFeature)((HashMap)e.getNewValue()).get( keys.get(i) );
	    			addShape( newObject );
	    		}
	    		
	    		for( int i = 0; i < addedLines.size(); i++ )
	    		{
	    			GridLineModel newLine = addedLines.get( i );
	    			addShape( newLine );
	    		}
	    	}   
	    }
	}
}
