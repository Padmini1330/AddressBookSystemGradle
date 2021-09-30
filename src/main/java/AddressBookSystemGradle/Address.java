package AddressBookSystemGradle;


public class Address 
{
	private Integer addressID;
	private String city;
	private String zip;
	private String state;
	
	public Address(Integer addressID,String city,String state,String zip)
	{
		this.addressID=addressID;
		this.city=city;
		this.zip=zip;
		this.state=state;
	}
	public Address(String city,String state,String zip)
	{
		this.city=city;
		this.zip=zip;
		this.state=state;
	}
	
	public Integer getAddressID() 
	{
		return addressID;
	}
	
	public void setAddressID(Integer addressID) 
	{
		this.addressID = addressID;
	}
	
	public String getCity() 
	{
		return city;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}
	
	public String getZip() 
	{
		return zip;
	}
	
	public void setZip(String zip) 
	{
		this.zip = zip;
	}
	
	public String getState() 
	{
		return state;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
}