package model.floorplan;

//The Floorplan Component Listener interface

import java.util.EventListener;

public interface FloorplanModelListener extends EventListener {
	public void modelMoved(GridLineEvent evt);

}
