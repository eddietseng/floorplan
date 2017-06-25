package demo;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import model.FloorplanModel;
import model.floorplan.FloorplanUtil;
import model.floorplan.Space2DModel;
import model.util.CoordinatePair;

public class SimpleExample {
	
	public static void main( String args[] ) throws ParseException
	{
		Coordinate pt1 = new Coordinate(  0,  0, 0 );
		Coordinate pt2 = new Coordinate(  0, 50, 0 );
		Coordinate pt3 = new Coordinate( 50, 50, 0 );
		Coordinate pt4 = new Coordinate( 50,  0, 0 );
		
		List<Coordinate>  points = new ArrayList<Coordinate>();
		
		points.add( pt1 );
		points.add( pt2 );
		points.add( pt3 );
		points.add( pt4 );
		points.add( pt1 );
		
		Coordinate coordinates[] = points.toArray( new Coordinate[ points.size() ] );
		
		GeometryFactory fact = new GeometryFactory();
		
		Polygon polygon = new Polygon( fact.createLinearRing( coordinates ), null, fact );
		System.out.println( polygon );
		double area = polygon.getArea();
		System.out.println( "Area = " + area );
	}

}
