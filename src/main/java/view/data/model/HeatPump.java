package view.data.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "heatpump")
@XmlAccessorType (XmlAccessType.FIELD)
public class HeatPump {
	
	public static final int TONS_TO_BTU = 12000;
	
	private String modelName;
	
	private double tons;
	
	private double price;
	
	private double seer;
	
	private double hspf;
	
	public HeatPump(){
		
	}
	
	public void setModelName( String modelName )
	{
		this.modelName = modelName;
	}
	
	public String getModelName()
	{
		return this.modelName;
	}
	
	public void setTons( double tons )
	{
		this.tons = tons;
	}
	
	public double getTons()
	{
		return this.tons;
	}
	
	public void setPrice( double price )
	{
		this.price = price;
	}
	
	public double getPrice()
	{
		return this.price;
	}
	
	public void setSeer( double seer )
	{
		this.seer = seer;
	}
	
	public double getSeer()
	{
		return this.seer;
	}
	
	public void setHspf( double hspf )
	{
		this.hspf = hspf;
	}
	
	public double getHspf()
	{
		return this.hspf;
	}
	
	public String toString()
	{
		String s = "Heat pump (" + this.modelName + "/" + this.tons + "/";
		s = s + this.price + "/" + this.seer + "/" + this.hspf + ")";
		
		return s;
	}
}
