package model;

import model.floorplan.FloorplanResultModel;
import model.floorplan.RoomModel;

public interface FloorplanModelVisitor {
	void visit( FloorplanModel model );
	void visit( AbstractFeature         feature );
    void visit( AbstractCompoundFeature feature );
    void visit( CompositeHierarchy    composite );
    void visit( RoomModel model );
    void visit( FloorplanResultModel result );
    void start( FloorplanModel model );
    void finish( FloorplanModel model );

}
