package view;

import model.AbstractCompoundFeature;
import model.AbstractFeature;
import model.CompositeHierarchy;
import model.FeatureElementVisitor;
import model.floorplan.FloorplanResultModel;
import model.floorplan.RoomModel;
import model.floorplan.function.BCMaxUnitAreaFunction;
import model.floorplan.function.BCMinKitchenWidthFunction;
import model.floorplan.function.BCMinLivingroomAreaFunction;
import model.floorplan.function.BCMinRoomWidthFunction;
import model.floorplan.function.BCMinUnitAreaFunction;
import model.floorplan.function.Function;
import model.floorplan.function.MCMinBedroomAreaFunction;
import model.floorplan.function.MCMinKitchenAreaFunction;
import model.floorplan.function.MCMinLivingroomAreaFunction;

public class PrintViewFloorplanResultVisitor implements FeatureElementVisitor {
	String s = "";
	private boolean headingPrinted = false;

	@Override
	public void visit(AbstractFeature feature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbstractCompoundFeature feature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CompositeHierarchy composite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(CompositeHierarchy composite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finish(CompositeHierarchy composite) {
		// TODO Auto-generated method stub

	}

	public void visit(FloorplanResultModel model) {
		print((FloorplanResultModel) model);
	}
	
	public void visit(RoomModel roomModel) {
		print((RoomModel) roomModel);	
	}

	public void visit(Function function) {
		print((Function) function);
	}

	private void print(FloorplanResultModel model) {
		// String representation of general info ...

		String s1 = String.format( "Floorplan : Cooling Load Zone1 = %.2f\n", model.getCoolingLoadZone1());
		String s3 = String.format( "Cooling Load Zone2       = %.2f\n", model.getCoolingLoadZone2());
		String s2 = String.format( "Cooling Load Whole = %.2f\n", model.getCoolingLoadWhole());
		String s4 = String.format( "Total Area               = %.2f square feet\n", model.getTotalArea());
		String s5 = String.format( "Usable Area              = %.2f square feet\n", model.getUsableArea());
		String s6 = String.format( "Total Price              = %.2f\n", model.getTotalPrice());

		s = s + s1 + s2 + s3 + s4 + s5 + s6;
	}
	
	private void print(RoomModel roomModel) {
		// String representation of general info ...
		String s0 = "-------------------------\n";
		String s1 = String.format( "Room : Room Name        = %17s\n", roomModel.getName());
		String s2 = String.format( "Role of the room = %17s\n", roomModel.getRole());
		String s3 = String.format( "Area             = %.2f square feet\n", roomModel.getArea());
		
		s = s + s0 + s1 + s2 + s3;
	}
	
	private void print(Function function) {
		// String representation of general info ...
		String s0 = "-------------------------\n";
		String s1 = "";
		String s2 = "";
		String s3 = "";
		String s4 = "";
		
		if( function instanceof BCMaxUnitAreaFunction ){
			s1 = String.format( "Requirement : Name = %s\n", ((BCMaxUnitAreaFunction)function).getReqName());
			double[] x = ((BCMaxUnitAreaFunction)function).getInputMatrix();
			s2 = String.format( "Input value for area = %.2f\n", x[2] );
			s3 = String.format( "Is requirement valid ? %b\n", ((BCMaxUnitAreaFunction)function).isValid());
		}
		else if( function instanceof BCMinKitchenWidthFunction ){
			s1 = String.format( "Requirement : Name = %s\n", ((BCMinKitchenWidthFunction)function).getReqName());
			double[] x = ((BCMinKitchenWidthFunction)function).getInputMatrix();
			s2 = "Listed width for Kitchen : ";
			for( int i = 0; i < x.length; i++ ){
				s2 = s2 + x[i];
				if( i == (x.length - 1) )
					s2 = s2 + "\n";
				else
					s2 = s2 + ", ";
			}
			s3 = String.format( "Is requirement valid ? %b\n", ((BCMinKitchenWidthFunction)function).isValid());
		}
		else if( function instanceof BCMinLivingroomAreaFunction ){
			s1 = String.format( "Requirement : Name = %s\n", ((BCMinLivingroomAreaFunction)function).getReqName());
			double[] x = ((BCMinLivingroomAreaFunction)function).getInputMatrix();
			s2 = String.format( "Number of occupants = %f\n", x[1] );
			s3 = "Listed area of Livingroom : ";
			for( int i = 2; i < x.length; i++ ){
				s3 = s3 + x[i];
				if( i == (x.length - 1) )
					s3 = s3 + "\n";
				else
					s3 = s3 + ", ";
			}
			s4 = String.format( "Is requirement valid ? %b\n", ((BCMinLivingroomAreaFunction)function).isValid());
		}
		else if( function instanceof BCMinRoomWidthFunction ){
			s1 = String.format( "Requirement : Name = %s\n", ((BCMinRoomWidthFunction)function).getReqName());
			double[] x = ((BCMinRoomWidthFunction)function).getInputMatrix();
			s2 = "Listed width for Room : ";
			for( int i = 0; i < x.length; i++ ){
				s2 = s2 + x[i];
				if( i == (x.length - 1) )
					s2 = s2 + "\n";
				else
					s2 = s2 + ", ";
			}
			s3 = String.format( "Is requirement valid ? %b\n", ((BCMinRoomWidthFunction)function).isValid());
		}
		else if( function instanceof BCMinUnitAreaFunction ){
			s1 = String.format( "Requirement : Name = %s\n", ((BCMinUnitAreaFunction)function).getReqName());
			double[] x = ((BCMinUnitAreaFunction)function).getInputMatrix();
			s2 = "Listed areas of Rooms : " ;
			for( int i = 1; i < x.length; i++ ){
				s2 = s2 + x[i];
				if( i == (x.length - 1) )
					s2 = s2 + "\n";
				else
					s2 = s2 + ", ";
			}
			s3 = String.format( "Is requirement valid ? %b\n", ((BCMinUnitAreaFunction)function).isValid());
		}
		else if( function instanceof MCMinBedroomAreaFunction ){
			s1 = String.format( "Requirement : Name = %s\n", ((MCMinBedroomAreaFunction)function).getReqName());
			double[] x = ((MCMinBedroomAreaFunction)function).getInputMatrix();
			s2 = String.format( "Number of occupants = %f\n", x[1] );
			s3 = "Listed areas of Rooms : " ;
			for( int i = 2; i < x.length; i++ ){
				s3 = s3 + x[i];
				if( i == (x.length - 1) )
					s3 = s3 + "\n";
				else
					s3 = s3 + ", ";
			}
			s4 = String.format( "Is requirement valid ? %b\n", ((MCMinBedroomAreaFunction)function).isValid());
		}
		else if( function instanceof MCMinKitchenAreaFunction ){
			s1 = String.format( "Requirement : Name = %s\n", ((MCMinKitchenAreaFunction)function).getReqName());
			double[] x = ((MCMinKitchenAreaFunction)function).getInputMatrix();
			s2 = String.format( "Number of occupants = %f\n", x[0] );
			s3 = String.format( "Input value for area = %.2f\n", x[1] );
			s4 = String.format( "Is requirement valid ? %b\n", ((BCMaxUnitAreaFunction)function).isValid());
		}
		else if( function instanceof MCMinLivingroomAreaFunction ){
			s1 = String.format( "Requirement : Name = %s\n", ((MCMinLivingroomAreaFunction)function).getReqName());
			double[] x = ((MCMinLivingroomAreaFunction)function).getInputMatrix();
			s2 = String.format( "Number of occupants = %f\n", x[1] );
			s3 = "Listed areas of Livingrooms : " ;
			for( int i = 2; i < x.length; i++ ){
				s3 = s3 + x[i];
				if( i == (x.length - 1) )
					s3 = s3 + "\n";
				else
					s3 = s3 + ", ";
			}
			s4 = String.format( "Is requirement valid ? %b\n", ((MCMinLivingroomAreaFunction)function).isValid());
		}
		
		s = s + s0 + s1 + s2 + s3 + s4;
	}

	public void start(FloorplanResultModel model) {
		// Print heading for the first time ....

		if (headingPrinted == false) {
			String s1 = String.format("--------------------------------- \n");
			String s2 = String.format("Summary of Floorplan Result Model \n");
			String s3 = String.format("--------------------------------- \n");
			String s4 = String.format("\n");
			s = s + s1 + s2 + s3 + s4;
		}

		// Reset heading flag ...

		headingPrinted = true;
	}

	public void finish(FloorplanResultModel model) {}

	public String getText() {
		return s;
	}
}
