package com.example.MC_PlanItOut;

public class PLan_List_Data {

	String name;
	String id;
	String address;
	String phone;
	String distance;
	
	void Plan_List_Data(String objectId, String placeName, String placeAddress, String placePhone, String placeDistance)
	{
		id = objectId;
		name  = placeName;
		address = placeAddress;
		phone = placePhone;
		distance = placeDistance;
	}
	
	@Override
	public String toString() {
	    return this.getName()+"\n"+this.getAddress()+"\n"+this.getPhone()+"\n"+this.getDistance();
	}
	
	
	public String getId()
	{
		return id; 
	}
	
	public String getName()
	{
		return name; 
	}
	
	public String getAddress()
	{
		return address; 
	}
	
	public String getPhone()
	{
		return phone; 
	}
	
	public String getDistance()
	{
		return distance; 
	}
	
	
}
