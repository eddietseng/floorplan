package view;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.AbstractCompoundFeature;
import model.floorplan.Column2DModel;
import model.floorplan.Corner2DModel;
import model.floorplan.GridLineModel;
import model.floorplan.Space2DModel;
import model.floorplan.Wall2DModel;

public class TreeViewAdapterFloorplan 
  extends DefaultTreeModel
{
	static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode centerlineNode;
	private DefaultMutableTreeNode spaceNode;
	private DefaultMutableTreeNode columnNode;
	private DefaultMutableTreeNode cornerNode;
	private DefaultMutableTreeNode wallNode;
	
	public TreeViewAdapterFloorplan( DefaultMutableTreeNode rootNode )
	{
		super( rootNode );
		this.rootNode = rootNode;
		
		centerlineNode = new DefaultMutableTreeNode( "Centerline" );
		rootNode.add( centerlineNode );
		spaceNode = new DefaultMutableTreeNode( "Spaces" );
		rootNode.add( spaceNode );
		columnNode = new DefaultMutableTreeNode( "Columns" );
		rootNode.add( columnNode );
		cornerNode = new DefaultMutableTreeNode( "Corners" );
		rootNode.add( cornerNode );
		wallNode = new DefaultMutableTreeNode( "Walls" );
		rootNode.add( wallNode );
		
	}
	
	public DefaultMutableTreeNode getRootNode() 
	{
	    return rootNode;
	}
	
	public void changeStatus ( AbstractCompoundFeature s ) 
	{
	    DefaultMutableTreeNode highlightNode = searchNode( s.toString() );
	    if ( highlightNode == null ) 
	       System.out.println("*** In TreeViewAdapterFloorplan().changeStatus(): ERROR - selected node not found!");
	    else
	       System.out.println("*** In TreeViewAdapterFloorplan().changeStatus(): Found node for Feature = " + s.getName() );
	}
	
	public DefaultMutableTreeNode searchNode(String nodeStr) 
	{
		DefaultMutableTreeNode node = null;
	    Enumeration e = rootNode.breadthFirstEnumeration();
	    while (e.hasMoreElements()) {
	        node = (DefaultMutableTreeNode) e.nextElement();
	        if (nodeStr.equals( node.toString() )) {
	            return node;
	            }
	    }
	    return null;
	}
	
	// Define simple rule for sorting components according to the type ....

	public DefaultMutableTreeNode getTypeFolderNode( Object s ) 
	{
		if( s instanceof GridLineModel )
			return centerlineNode;
	    else if( s instanceof Space2DModel )
			return spaceNode;
		else if( s instanceof Column2DModel )
			return columnNode;
		else if( s instanceof Corner2DModel )
			return cornerNode;
		else if( s instanceof Wall2DModel )
			return wallNode;
		else
		{
			System.out.println( "TreeViewAdapterFloorplan.getTypeFolderNode() Error: Can't get type" + s.toString() );
			return null;
		}
	}
	
	
}
