package com.qwake.app;

public class SavedLocation {

String _name;
String _latitude;
String _longitude;
String _comment;
String _range;
public SavedLocation()
{
	}
public SavedLocation(String name, String latitude,String longitude,String comment,String range){
    
    this._name = name;
    this._latitude = latitude;
    this._longitude=longitude;
    this._comment = comment;
    this._range = range;
}
public void setName(String name)
{ 
	this._name = name;
	
	}
public void setComment(String comment)
{
   this._comment = comment;	
}
public void setRange(String range)
{
this._range = range;	
}
public void setLatitude(String lat)
{
	this._latitude=lat;
	}
public void setLongitude(String lon)
{
	this._longitude=lon;
	}
public String getName()
{ 
	return this._name;
	
	}
public String getComment()
{
return this._comment;	
}
public String getLatitude()
{
	return this._latitude;
	}
public String getLongitude()
{
	return this._longitude;
	}
public String getRange()
{
return this._range;	
}
}
