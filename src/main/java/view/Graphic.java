/* 
 *  ==========================================================
 *  Graphic.java: Graphic interface ....
 *  ==========================================================
 */ 

package view;

import java.awt.*;
import java.awt.geom.*;

public interface Graphic {
    public void search( AffineTransform at, int dx, int dy ); // Containment search ...
}
