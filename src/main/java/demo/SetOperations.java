package demo;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;

public class SetOperations {
	
	public static void main( String args[] ) throws ParseException
	{
		Coordinate pt1 = new Coordinate(  0,  0, 0 );
		Coordinate pt2 = new Coordinate(  0, 10, 0 );
		Coordinate pt3 = new Coordinate( 10, 10, 0 );
		Coordinate pt4 = new Coordinate( 10,  0, 0 );
		
		List<Coordinate>  pointsA = new ArrayList<Coordinate>();
		
		pointsA.add( pt1 );
		pointsA.add( pt2 );
		pointsA.add( pt3 );
		pointsA.add( pt4 );
		pointsA.add( pt1 );
		
		Coordinate coordinatesA[] = pointsA.toArray( new Coordinate[ pointsA.size() ] );
		
		Coordinate pt5 = new Coordinate(  0, 30, 0 );
		Coordinate pt6 = new Coordinate(  0, 40, 0 );
		Coordinate pt7 = new Coordinate( 10, 40, 0 );
		Coordinate pt8 = new Coordinate( 10, 30, 0 );
		
		List<Coordinate>  pointsB = new ArrayList<Coordinate>();
		
		pointsB.add( pt5 );
		pointsB.add( pt6 );
		pointsB.add( pt7 );
		pointsB.add( pt8 );
		pointsB.add( pt5 );
		
		Coordinate coordinatesB[] = pointsB.toArray( new Coordinate[ pointsB.size() ] );
		
		Coordinate pt9  = new Coordinate( 30, 30, 0 );
		Coordinate pt10 = new Coordinate( 30, 40, 0 );
		Coordinate pt11 = new Coordinate( 40, 40, 0 );
		Coordinate pt12 = new Coordinate( 40, 30, 0 );
		
		List<Coordinate>  pointsC = new ArrayList<Coordinate>();
		
		pointsC.add( pt9  );
		pointsC.add( pt10 );
		pointsC.add( pt11 );
		pointsC.add( pt12 );
		pointsC.add( pt9  );
		
		Coordinate coordinatesC[] = pointsC.toArray( new Coordinate[ pointsC.size() ] );
		
		Coordinate pt13 = new Coordinate( 30,  0, 0 );
		Coordinate pt14 = new Coordinate( 30, 10, 0 );
		Coordinate pt15 = new Coordinate( 40, 10, 0 );
		Coordinate pt16 = new Coordinate( 40,  0, 0 );
		
		List<Coordinate>  pointsD = new ArrayList<Coordinate>();
		
		pointsD.add( pt13 );
		pointsD.add( pt14 );
		pointsD.add( pt15 );
		pointsD.add( pt16 );
		pointsD.add( pt13 );
		
		Coordinate coordinatesD[] = pointsD.toArray( new Coordinate[ pointsD.size() ] );
		
		List<Coordinate>  pointsE = new ArrayList<Coordinate>();
		
		pointsE.add( pt2 );
		pointsE.add( pt5 );
		pointsE.add( pt8 );
		pointsE.add( pt3 );
		pointsE.add( pt2 );
		
		Coordinate coordinatesE[] = pointsE.toArray( new Coordinate[ pointsE.size() ] );
		
		List<Coordinate>  pointsF = new ArrayList<Coordinate>();
		
		pointsF.add( pt8  );
		pointsF.add( pt7  );
		pointsF.add( pt10 );
		pointsF.add( pt9  );
		pointsF.add( pt8  );
		
		Coordinate coordinatesF[] = pointsF.toArray( new Coordinate[ pointsF.size() ] );
		
		List<Coordinate>  pointsG = new ArrayList<Coordinate>();
		
		pointsG.add( pt14 );
		pointsG.add( pt9  );
		pointsG.add( pt12 );
		pointsG.add( pt15 );
		pointsG.add( pt14 );
		
		Coordinate coordinatesG[] = pointsG.toArray( new Coordinate[ pointsG.size() ] );
		
		List<Coordinate>  pointsH = new ArrayList<Coordinate>();
		
		pointsH.add( pt4  );
		pointsH.add( pt3  );
		pointsH.add( pt14 );
		pointsH.add( pt13 );
		pointsH.add( pt4  );
		
		Coordinate coordinatesH[] = pointsH.toArray( new Coordinate[ pointsH.size() ] );
		
		List<Coordinate>  pointsI = new ArrayList<Coordinate>();
		
		pointsI.add( pt3  );
		pointsI.add( pt8  );
		pointsI.add( pt9  );
		pointsI.add( pt14 );
		pointsI.add( pt3  );
		
		Coordinate coordinatesI[] = pointsI.toArray( new Coordinate[ pointsI.size() ] );
		
		GeometryFactory fact = new GeometryFactory();
		
		Polygon polygonA = new Polygon( fact.createLinearRing( coordinatesA ), null, fact );
		Polygon polygonB = new Polygon( fact.createLinearRing( coordinatesB ), null, fact );
		Polygon polygonC = new Polygon( fact.createLinearRing( coordinatesC ), null, fact );
		Polygon polygonD = new Polygon( fact.createLinearRing( coordinatesD ), null, fact );
		Polygon polygonE = new Polygon( fact.createLinearRing( coordinatesE ), null, fact );
		Polygon polygonF = new Polygon( fact.createLinearRing( coordinatesF ), null, fact );
		Polygon polygonG = new Polygon( fact.createLinearRing( coordinatesG ), null, fact );
		Polygon polygonH = new Polygon( fact.createLinearRing( coordinatesH ), null, fact );
		Polygon polygonI = new Polygon( fact.createLinearRing( coordinatesI ), null, fact );
		
		double areaA = polygonA.getArea();
		System.out.println( "Area A = " + areaA );
		double areaB = polygonB.getArea();
		System.out.println( "Area B = " + areaB );
		double areaC = polygonC.getArea();
		System.out.println( "Area C = " + areaC );
		double areaD = polygonD.getArea();
		System.out.println( "Area D = " + areaD );
		double areaE = polygonE.getArea();
		System.out.println( "Area E = " + areaE );
		double areaF = polygonF.getArea();
		System.out.println( "Area F = " + areaF );
		double areaG = polygonG.getArea();
		System.out.println( "Area G = " + areaG );
		double areaH = polygonH.getArea();
		System.out.println( "Area H = " + areaH );
		double areaI = polygonI.getArea();
		System.out.println( "Area I = " + areaI );
		
		Geometry aUnionE = polygonA.union( polygonE );
		System.out.println( aUnionE );
		double areaAUnionE = aUnionE.getArea();
		System.out.println( "Area A union E = " + areaAUnionE );
		
		Geometry outerUnion = polygonA.union( polygonE ).union( polygonB )
				.union( polygonF ).union( polygonC ).union( polygonG )
				.union( polygonD ).union( polygonH );
		System.out.println( outerUnion );
		double outerUnionArea = outerUnion.getArea();
		System.out.println( "The union of all outer polygons = " + outerUnionArea );
		
		Geometry all = outerUnion.union( polygonI );
		System.out.println( all );
		double wholeArea = all.getArea();
		System.out.println( "Whole area = " + wholeArea );
		
		Geometry diffrence = all.difference( outerUnion );
		System.out.println( diffrence );
		double centerArea = diffrence.getArea();
		System.out.println( "Center area = " + centerArea );
		
		double area = polygonI.getArea();
		System.out.println( "Area I = " + area );
	}

}
