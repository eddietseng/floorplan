package model.floorplan;

import java.util.ArrayList;

import view.PrintViewFloorplanResultVisitor;

import com.vividsolutions.jts.io.ParseException;

import model.FloorplanModel;

/**
 * This model is used to store the attributes of a room.
 * A room maybe a selection of spaces.
 */
public class RoomModel {
	protected FloorplanModel model;
	protected ArrayList<Space2DModel> spaces;
	protected String name;
	protected double area;
	protected String role;
	protected double price;
	protected String zone;
	protected int occupants = 0;
	protected final double DEFAULT_AREA_RATIO = 1;
	
	public final static String LIVING_ROOM = "Living room";
	public final static String KITCHEN     = "Kitchen";
	public final static String BED_ROOM    = "Bed room";
	public final static String OFFICE      = "Office";
	public final static String UTILITY     = "Utility";
	public final static String ZONE_1      = "Zone 1";
	public final static String ZONE_2      = "Zone 2";
	
	
	/**
	 * Default constructor
	 */
	public RoomModel(){}
	
	/**
	 * Create Room Model by using the given List of spaces.
	 * @param s the ArrayList of Space2DModel
	 */
	public RoomModel( ArrayList<Space2DModel> s , String name){
		this.spaces = s;
		this.name = name;
	}
	
	/**
	 * Set the spaces of the room model
	 * @param space
	 */
	public void setSpaces( ArrayList<Space2DModel> space )
	{
		this.spaces = space;
	}
	
	/**
	 * Get the ArrayList of Space2DModel
	 * @return ArrayList<Space2DModel>
	 */
	public ArrayList<Space2DModel> getSpaces(){
		return this.spaces;
	}
	
	/**
	 * Sets the name of the room
	 * @param name String the name
	 */
	public void setName( String name ){
		if( name.length() > 0 )
		{
			name = name.trim();
			this.name = name;
		}
	}
	
	/**
	 * Returns the name of the room
	 * @return String the name of the room
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Calculate the area based on the default ratio
	 */
	public void calculateArea(){
		calculateArea( DEFAULT_AREA_RATIO );
	}
	
	/**
	 * Calculate the area based on the given ratio
	 * @param areaRatio
	 */
	public void calculateArea( double areaRatio ) {
		try {
			this.area = FloorplanUtil.getArea( this.model , this.getSpaces() ) * areaRatio;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the size of the room
	 * @return double Area
	 */
	public double getArea(){
		return this.area;
	}
	
	/**
	 * Returns the lengths of the room
	 * @return ArrayList<Double> arraylist of length
	 */
	public ArrayList<Double> getLength(){
		ArrayList<Double> x = new ArrayList<Double>();
		try {
			x = FloorplanUtil.getLength(model, spaces);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return x;
	}
	
	/**
	 * Sets the role of the room
	 * @param role String
	 */
	public void setRole( String role ){
		System.out.println( "RoomModel.setRole() called" );
		if( role.equals( BED_ROOM ) )
			this.role = BED_ROOM;
		else if( role.equals( LIVING_ROOM ) )
			this.role = LIVING_ROOM;
		else if( role.equals( KITCHEN ) )
			this.role = KITCHEN;
		else if( role.equals( OFFICE ) )
			this.role = OFFICE;
		else if( role.equals( UTILITY ) )
			this.role = UTILITY;
		else
			this.role = "";
	}
	
	/**
	 * Returns the role of the room
	 * @return String the role of the room
	 */
	public String getRole(){
		return this.role;
	}
	
	/**
	 * Sets the Floorplan Model of this room
	 * @param model
	 */
	public void setFloorplanModel( FloorplanModel model) {
		if( this.model == null ? true : !this.model.equals( model ) )
			this.model = model;
	}
	
	/**
	 * Gets the Floorplan Model of this room
	 * @return FloorplanModel
	 */
	public FloorplanModel getFloorplanModel()
	{
		return this.model;
	}
	
	/**
	 * Sets the price of the room
	 * @param price double
	 */
	public void setPrice( double price )
	{
		if( this.price != price )
			this.price = price;
	}
	
	/**
	 * Gets the price of the room
	 * @return price double
	 */
	public double getPrice()
	{
		return price;
	}
	
	/**
	 * Sets the zone of the room
	 * @param zone 
	 */
	public void setZone( String zone )
	{
		if( zone.length() != 0 && this.zone != zone )
			this.zone = zone;
	}
	
	/**
	 * Gets the zone of the room
	 * @return zone 
	 */
	public String getZone()
	{
		return this.zone;
	}
	
	/**
	 * Sets the occupants of the room
	 * @param occupants
	 */
	public void setOccupants( int occupants )
	{
		if(  this.occupants != occupants )
			this.occupants = occupants;
	}
	
	/**
	 * Gets the occupants of the room
	 * @return occupants
	 */
	public int getOccupants()
	{
		return this.occupants;
	}
	

	public void initialModel() 
	{
		this.spaces = new ArrayList<Space2DModel>();	
		setOccupants(0);
	}
	
	public RoomModel clone()
	{
		RoomModel newRoom = new RoomModel();
		newRoom.setName(this.name );
		newRoom.setPrice( this.price );
		newRoom.setRole( this.role );
		newRoom.setSpaces( this.spaces );
		newRoom.setZone( this.zone );
		newRoom.setOccupants( this.occupants );
		return newRoom;
	}
	
	public void accept( PrintViewFloorplanResultVisitor visitor ){
		visitor.visit( this );
	}
}
