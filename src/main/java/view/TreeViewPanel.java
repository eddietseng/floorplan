package view;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import model.AbstractCompoundFeature;

import controller.EngineeringController;

public class TreeViewPanel extends JPanel 
{
	private   Boolean DEBUG = true;
	private   EngineeringController controller;
	protected DefaultMutableTreeNode rootNode;
	protected TreeViewAdapterFloorplan tva;
	protected JTree          tree;
	int oldRow = -1;

	// Constructor for tree view panel ...

	public TreeViewPanel( TreeViewAdapterFloorplan tva, EngineeringController controller ) 
	{
		super( new GridLayout(1, 0) );

	    this.tva        = tva;
	    this.controller = controller;

	    tree = new JTree(tva){  
            public String convertValueToText(Object value, boolean selected,boolean expanded, boolean leaf, int row, boolean hasFocus){  
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;//get node from the value object 
                Object nodeObject = node.getUserObject();// get object wrapped by this node
                try{
                	if( nodeObject instanceof String )
                    	return nodeObject.toString();
                    else 
                    	return ((AbstractCompoundFeature)nodeObject).getName();//return what you want to display as text, in your case it may be name
                }
                catch( ClassCastException e ){
                	System.out.println( "ERROR : Object cannot be cast to model.AbstractCompoundFeature" );
                	return null;
                }
            }  
        };
	    tree.setEditable(true);

	    // Add mouse listener ....

	    tree.addMouseListener(new MouseAdapter() {
	         public void mouseClicked(MouseEvent mse) {
	            int selRow = tree.getRowForLocation(mse.getX(), mse.getY());
	            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	         // if(selRow != -1 && oldRow != selRow) {
	            if(selRow != -1) {
	               Object nodeInfo = node.getUserObject();
	               changeStatus( nodeInfo );
	               oldRow = selRow;
	            }
	         }
	    } );

	    tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
	    tree.setShowsRootHandles(true);

	    JScrollPane scrollPane = new JScrollPane(tree);
	    add(scrollPane);
	}
	
	public void changeStatus( Object nodeInfo ) 
	{
		if( nodeInfo instanceof AbstractCompoundFeature)
		{
			AbstractCompoundFeature s = (AbstractCompoundFeature) nodeInfo;
			controller.changeFloorplanComponentStatus ( s );
		} else {
			if( nodeInfo.equals( "Centerline" ) )
				System.out.println( "Do somthing for Centerline" );
			else if( nodeInfo.equals( "Spaces" ) )
				System.out.println( "Do somthing for Centerline" );
			else if( nodeInfo.equals( "Columns" ) )
				System.out.println( "Do somthing for Centerline" );
			else if( nodeInfo.equals( "Corners" ) )
				System.out.println( "Do somthing for Centerline" );
			else if( nodeInfo.equals( "Walls" ) )
				System.out.println( "Do somthing for Centerline" );
			else if( nodeInfo.equals( "Doors" ) )
				System.out.println( "Do somthing for Doors" );  //TODO
			else if( nodeInfo.equals( "Windows" ) )
				System.out.println( "Do somthing for Windows" );//TODO
			else
				System.out.println( "TreeViewPanel.changeStatus() Error: Don't know how to handle Node" + nodeInfo.toString() );
		}
	}
	
	// Add child to the currently selected node ...

	public DefaultMutableTreeNode addObject(Object child) 
	{
		DefaultMutableTreeNode parentNode = null;
	    TreePath parentPath = tree.getSelectionPath();

	    if (parentPath == null) {
	        parentNode = rootNode;
	    } else {
	        parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
	    }

	    return addObject(parentNode, child, true);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) 
	{
	    return addObject(parent, child, true );
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) 
	{
	    DefaultMutableTreeNode childNode        = new DefaultMutableTreeNode(child);
	    DefaultMutableTreeNode circleFolderNode = tva.getTypeFolderNode(child);

	    if (parent == null) {
	        parent = rootNode;
	    }

	    // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode

	    tva.insertNodeInto( childNode, circleFolderNode, circleFolderNode.getChildCount() );

	    // Make sure the user can see the new node.

	    if (shouldBeVisible) {
	        tree.scrollPathToVisible( new TreePath( childNode.getPath() ) );
	    }

	    return childNode;
	}

}
