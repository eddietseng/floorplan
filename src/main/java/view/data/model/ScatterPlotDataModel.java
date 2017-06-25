package view.data.model;

import model.floorplan.City;

public class ScatterPlotDataModel {
	private City city;
	private HeatPump heatpump;
	private double cost = 0;
	private double energyConsumption = 0;
	private double areaSize = 0;
	
	// For electricity compare chart
	private double electricityCost = 0;
	
	public ScatterPlotDataModel( City city, HeatPump heatpump )
	{
		this.city = city;
		this.heatpump = heatpump;
	}
	
	public void setCity( City city )
	{
		if( city != null )
			this.city = city;
	}
	
	public City getCity()
	{
		return this.city;
	}

	public void setHeatPump( HeatPump heatpump )
	{
		if( heatpump != null )
			this.heatpump = heatpump;
	}
	
	public HeatPump getHeatPump()
	{
		return this.heatpump;
	}
	
	public void setCost( double cost )
	{
		this.cost = cost;
	}
	
	public double getCost()
	{
		return this.cost;
	}
	
	public void setEnergyConsumption( double energyConsumption )
	{
		this.energyConsumption = energyConsumption;
	}
	
	public double getEnergyConsumption()
	{
		return this.energyConsumption;
	}
	
	public void setAreaSize( double areaSize )
	{
		this.areaSize = areaSize;
	}
	
	public double getAreaSize()
	{
		return this.areaSize;
	}
	
	public void setElectricityCost( double electricityCost )
	{
		this.electricityCost = electricityCost;
	}
	
	public double getElectricityCost()
	{
		return this.electricityCost;
	}
}
