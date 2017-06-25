package model.floorplan;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import model.AbstractCompoundFeature;
import model.FloorplanModel;
import model.rtree.HyperBoundingBox;
import model.rtree.HyperPoint;
import model.rtree.RTreeException;

public class FloorplanUtil {
	public static Geometry getGeometry( FloorplanModel model, Space2DModel sp ) throws ParseException
	{
		GeometryFactory fact = new GeometryFactory();
		WKTReader wktRdr = new WKTReader(fact);
		ArrayList<Geometry> geometryList = new ArrayList<Geometry>();
		if( insideModel( model, sp ) == true )
		{
			double dX1 = sp.getX();
			double dX2 = sp.getX() + sp.getHeight();
			double dY1 = sp.getY();
			double dY2 = sp.getY() + sp.getWidth();
			
			// Check for overlap of input rectangle and spaces stored in r-tree ...

		    double A[] = new double[2];
		    A[0] = Math.min ( dX1, dX2 );
		    A[1] = Math.min ( dY1, dY2 );
		    double B[] = new double[2];
		    B[0] = Math.max ( dX1, dX2 );
		    B[1] = Math.max ( dY1, dY2 );
		    HyperPoint hpA = new HyperPoint ( A );
		    HyperPoint hpB = new HyperPoint ( B );

		    // Create references to results on component-level r-trees...

		    Object[] result = null;  // level 2 (component level)

		    // Retrieve intersections from component-level r-tree ...

		    try {
		        HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
		        result = model.getSecondlevelTree().intersects( hp );
		    } catch (RTreeException e) {
		        // if problems occur - in the real world you have
		        // to handle the exceptions :-)
		        e.printStackTrace();
		    }
		    
		    if( result.length == 0 ) {
		    	System.out.println("*** ERROR: No component overlap with this space!!");
		    	return null;
		    }
		    
		    Integer temp;
		    for(int i=0; i < result.length; i++) {
		    	temp = (Integer) result[i];
		    	IndexArch np = (IndexArch) model.getComponentsIndexList().get( (temp.intValue()-1) );
		    	// Process column index ....
		    	if( np.getIndex() == IndexArch.COLUMN ) {
		    		Column2DModel spTemp = (Column2DModel) np.getSpace();
		    		if( sp.overlap( spTemp ) == true ) {
		    			String wkt = spTemp.getPolygonString();
		    			Geometry geo = wktRdr.read( wkt );
		    			geometryList.add( geo );
		    		}
		    	}
		    	if( np.getIndex() == IndexArch.CORNER ){
		    		Corner2DModel spTemp = (Corner2DModel) np.getSpace();
		    		if( sp.overlap( spTemp ) == true ) {
		    			String wkt = spTemp.getPolygonString();
		    			Geometry geo = wktRdr.read( wkt );
		    			geometryList.add( geo );
		    		}
		    	}
		    	if( np.getIndex() == IndexArch.WALL ){
		    		Wall2DModel spTemp = (Wall2DModel) np.getSpace();
		    		if( sp.overlap( spTemp ) == true ) {
		    			String wkt = spTemp.getPolygonString();
		    			Geometry geo = wktRdr.read( wkt );
		    			geometryList.add( geo );
		    		}
		    	}          
		    }
		    String spwkt = sp.getPolygonString();
		    Geometry spGeo = wktRdr.read( spwkt );
		    Geometry union = geometryList.get( 0 );
		    for( int i = 1; i < geometryList.size(); i++ )
		    {
		    	union = union.union( geometryList.get( i ) );
		    }
		    Geometry totalGeo = union.union( spGeo );
		    Geometry Area = totalGeo.difference( union );
		    return Area;
		}
		else
			return null;
	}
	
	public static ArrayList<Double> getLength( FloorplanModel model, ArrayList<Space2DModel> spaces ) throws ParseException
	{
		ArrayList<Double> x = new ArrayList<Double>();
		if( !spaces.isEmpty() )
		{
			Space2DModel initialSp = spaces.get( 0 );
			Geometry initialArea = getGeometry( model, initialSp );
			for( int i = 1; i < spaces.size(); i++ )
			{
				Space2DModel sp = spaces.get(i);
				Geometry area = getGeometry( model, sp );
				if( area != null )
				{
					initialArea = initialArea.union(area);
				}
			}
			if( initialArea != null ){
				Coordinate[] cor = initialArea.getCoordinates();
				//System.out.println( "FloorplanUtil.getLength() Coordinates( 0 ) = " + cor[0] );
				for( int i = 1; i < cor.length; i++)
				{
					double length = 0;
					if( cor[ i - 1 ].x == cor[i].x )
					{
						length = Math.abs( cor[ i - 1 ].y - cor[i].y );
						x.add( length );
					}
					else if( cor[ i - 1 ].y == cor[i].y )
					{
						length = Math.abs( cor[ i - 1 ].x - cor[i].x );
						x.add( length );
					}
					else
						System.out.println( "FloorplanUtil.getLength() Can't calculate the length for"
								+ " Point (" + (i-1) + ") : " + cor[i-1] + " and Point (" + i + ") : "
								+ cor[i] );
				}
				/*for( int i = 0; i < x.size(); i++ ){
					System.out.println( "FloorplanUtil.getLength() length " + i + " : = " + x.get(i) );
				}*/
				return x;
			}
			else{
				System.out.println( "FloorplanUtil.getLength() initialArea == null" );
				return x;
			}
		}
		else{
			System.out.println( "FloorplanUtil.getLength() spaces is Empty" );
			return x;
		}
	}
	
	/**
	 * Throw Space2DModel inside the whole RTree, to get the area/coordinates of the geometry 
	 * @param model FloorplanModel
	 * @param sp Space2DModel
	 * @return double area
	 * @throws ParseException
	 */
	public static double getArea( FloorplanModel model, Space2DModel sp ) throws ParseException
	{
		if( getGeometry( model, sp ) != null )
		{
			return getGeometry( model, sp ).getArea();
		}
		else
			return 0;
	}
	
	public static double getArea( FloorplanModel model, ArrayList<Space2DModel> spaces ) throws ParseException
	{
		if( !spaces.isEmpty() )
		{
			ArrayList<Double> area = new ArrayList<Double>();
			for( Space2DModel space : spaces )
			{
				area.add( getArea( model, space ) );
			}
			if( !area.isEmpty() )
			{
				double sum = 0;
				for( Double a : area )
					sum += a;
				return sum;
			}
		}
		return 0;
	}
	
	/**
	 * Returns the totalArea ( calArea == true) or Total Parameter (calArea == false)
	 * @param model FloorplanModel
	 * @param calArea boolean
	 * @return double value
	 * @throws ParseException
	 */
	public static double getTotalArea( FloorplanModel model, boolean calArea ) throws ParseException
	{
		GeometryFactory fact = new GeometryFactory();
		WKTReader wktRdr = new WKTReader(fact);
		ArrayList<Geometry> geometryList = new ArrayList<Geometry>();
		if( !model.getSpaceList().isEmpty() )
		{
			ArrayList<Geometry> totalGeometry = new ArrayList<Geometry>();
			for( int j = 0; j < model.getSpaceList().size(); j++ )
			{
				Space2DModel sp = (Space2DModel) model.getSpaceList().get( j );
				if( insideModel( model, sp ) == true )
				{
					double dX1 = sp.getX();
					double dX2 = sp.getX() + sp.getHeight();
					double dY1 = sp.getY();
					double dY2 = sp.getY() + sp.getWidth();
					
					// Check for overlap of input rectangle and spaces stored in r-tree ...

				    double A[] = new double[2];
				    A[0] = Math.min ( dX1, dX2 );
				    A[1] = Math.min ( dY1, dY2 );
				    double B[] = new double[2];
				    B[0] = Math.max ( dX1, dX2 );
				    B[1] = Math.max ( dY1, dY2 );
				    HyperPoint hpA = new HyperPoint ( A );
				    HyperPoint hpB = new HyperPoint ( B );

				    // Create references to results on component-level r-trees...

				    Object[] result = null;  // level 2 (component level)

				    // Retrieve intersections from component-level r-tree ...

				    try {
				        HyperBoundingBox hp = new HyperBoundingBox( hpA, hpB );
				        result = model.getSecondlevelTree().intersects( hp );
				    } catch (RTreeException e) {
				        // if problems occur - in the real world you have
				        // to handle the exceptions :-)
				        e.printStackTrace();
				    }
				    
				    if( result.length == 0 ) {
				    	System.out.println("*** ERROR: No component overlap with this space!!");
				    	return 0;
				    }
				    
				    Integer temp;
				    for(int i=0; i < result.length; i++) {
				    	temp = (Integer) result[i];
				    	IndexArch np = (IndexArch) model.getComponentsIndexList().get( (temp.intValue()-1) );
				    	// Process column index ....
				    	if( np.getIndex() == IndexArch.COLUMN ) {
				    		Column2DModel spTemp = (Column2DModel) np.getSpace();
				    		if( sp.overlap( spTemp ) == true ) {
				    			String wkt = spTemp.getPolygonString();
				    			Geometry geo = wktRdr.read( wkt );
				    			geometryList.add( geo );
				    		}
				    	}
				    	if( np.getIndex() == IndexArch.CORNER ){
				    		Corner2DModel spTemp = (Corner2DModel) np.getSpace();
				    		if( sp.overlap( spTemp ) == true ) {
				    			String wkt = spTemp.getPolygonString();
				    			Geometry geo = wktRdr.read( wkt );
				    			geometryList.add( geo );
				    		}
				    	}
				    	if( np.getIndex() == IndexArch.WALL ){
				    		Wall2DModel spTemp = (Wall2DModel) np.getSpace();
				    		if( sp.overlap( spTemp ) == true ) {
				    			String wkt = spTemp.getPolygonString();
				    			Geometry geo = wktRdr.read( wkt );
				    			geometryList.add( geo );
				    		}
				    	}          
				    }
				    String spwkt = sp.getPolygonString();
				    Geometry spGeo = wktRdr.read( spwkt );
				    Geometry union = geometryList.get( 0 );
				    for( int i = 1; i < geometryList.size(); i++ )
				    {
				    	union = union.union( geometryList.get( i ) );
				    }
				    Geometry totalGeo = union.union( spGeo );
				    totalGeometry.add( totalGeo );
				}
			}
			if( !totalGeometry.isEmpty() )
			{
				Geometry total = totalGeometry.get( 0 );
			    for( int i = 1; i < totalGeometry.size(); i++ )
			    {
			    	total = total.union( totalGeometry.get( i ) );
			    }
			    if( calArea == true )
			    	return total.getArea();
			    else
			    	return total.getLength();
			}
			return 0;
		}
		else
			return 0;
	}
	
	public static boolean insideModel( FloorplanModel model, Space2DModel sp )
	{
		boolean isInsideModel = false;
		for( int i = 0; i < model.getSpaceList().size(); i++ )
		{
			Space2DModel space = (Space2DModel) model.getSpaceList().get( i );
			if( space.getX() == sp.getX() && space.getY() == sp.getY() &&
					space.getHeight() == sp.getHeight() && space.getWidth() == sp.getWidth())
			{
				isInsideModel = true;
				return isInsideModel;
			}
		}
		return isInsideModel;
	}
	
	/**
	 * Update the whole rTree
	 * @param model the floorplan model that the rTree is in
	 */
	public static void updateWholeRTree( FloorplanModel model )
	{
		for( int i = 0; i < model.getSpaceList().size(); i++ )
		{
			Space2DModel sp = (Space2DModel) model.getSpaceList().get( i );
			FloorplanUtil.updateRTree(model, sp);
		}
		
		for( int i = 0; i < model.getCornerList().size(); i++ )
		{
			Corner2DModel cr = (Corner2DModel) model.getCornerList().get( i );
			FloorplanUtil.updateRTree(model, cr);
		}
		
		for( int i = 0; i < model.getWallList().size(); i++ )
		{
			Wall2DModel wall = (Wall2DModel) model.getWallList().get( i );
			FloorplanUtil.updateRTree(model, wall);
		}
	}
	
	/**
	 * Update the feature geometry in rTree
	 * @param model the floorplan that the feature is in
	 * @param feature the feature that need to update
	 */
	public static void updateRTree( FloorplanModel model, AbstractCompoundFeature feature )
	{
		if( feature instanceof Space2DModel )
		{
			String name = feature.getName();
			String[] parts = name.split( "-" );
			int number = Integer.parseInt( parts[1] );
			try {
				model.getToplevelTree().delete( ((Space2DModel)feature).getHyperBoundingBox(), number );
			} catch (RTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				((Space2DModel) feature).setHyperBoundingBox( feature.transformToHyperBoundingBox() );
				model.getToplevelTree().insert( new Integer( number ), feature.transformToHyperBoundingBox() );
			} catch (RTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if( feature instanceof Corner2DModel )
		{
			int number = ((Corner2DModel) feature).getNoComponent();
			try {
				model.getSecondlevelTree().delete( ((Corner2DModel)feature).getHyperBoundingBox(), number );
			} catch (RTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				((Corner2DModel) feature).setHyperBoundingBox( feature.transformToHyperBoundingBox() );
				model.getSecondlevelTree().insert( new Integer( number ), feature.transformToHyperBoundingBox() );
			} catch (RTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if( feature instanceof Wall2DModel )
		{
			int number = ((Wall2DModel) feature).getNoComponent();
			try {
				model.getSecondlevelTree().delete( ((Wall2DModel)feature).getHyperBoundingBox(), number );
			} catch (RTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				((Wall2DModel) feature).setHyperBoundingBox( feature.transformToHyperBoundingBox() );
				model.getSecondlevelTree().insert( new Integer( number ), feature.transformToHyperBoundingBox() );
			} catch (RTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
