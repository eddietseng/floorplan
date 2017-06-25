package view.shape;

import java.awt.*;
import java.awt.geom.*;

import view.*;

public class ArcShape extends CompoundShape{
	
	public ArcShape(int x, int y, double radius, int startA, int extA){
		Arc2D.Double Arc = new Arc2D.Double(x - radius, y - radius, 2 * radius, 2 * radius, startA, extA, Arc2D.OPEN);
		add(Arc);
	}

}
