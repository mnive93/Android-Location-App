package com.tripmite.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
	
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.inputmethod.*;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.location.LocationListener;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.tripmite.app.R;

public class MainActivity2 extends FragmentActivity implements LocationListener,OnMapLongClickListener,GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
  private GoogleMap map;
  Location location;
LatLng fromPosition;
LatLng toPosition;
MarkerOptions markerOptions;
MarkerOptions markerfrom;
AlertDialog.Builder alert;
String comment;
String nameloc;
SharedPreferences myPreference;
DatabaseHandler db;
LocationManager locationManager;
double maxDistance;
boolean gpsIsEnabled,networkIsEnabled;
LocationClient mLocationClient;
LatLng currentpos;

@Override

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    db = new DatabaseHandler(this);
    setContentView(R.layout.activity_main2);
	 locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	 mLocationClient = new LocationClient(this, this, this);
     myPreference=PreferenceManager.getDefaultSharedPreferences(this);
    	 	  gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	  if(!gpsIsEnabled)
          {
       	   AlertDialog.Builder builder = new AlertDialog.Builder(this);
       	   builder.setTitle("Location Services Not Active");
       	   builder.setMessage("Please enable Location Services and GPS");
       	   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
       	   public void onClick(DialogInterface dialogInterface, int i) {
       	     // Show location settings when the user acknowledges the alert dialog
       	     Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
       	     startActivity(intent);
       	     }
       	   });
       	   Dialog alertDialog = builder.create();
       	   alertDialog.setCanceledOnTouchOutside(false);
       	   alertDialog.show();
       	   return;
          }
    	  alert = new AlertDialog.Builder(this);
  	    final View inflatedView = this.getLayoutInflater().inflate(R.layout.activity2, null);
  	    alert.setView(inflatedView);
  	    alert.setTitle("Enter the details");
  	    SeekBar seek = (SeekBar) inflatedView.findViewById(R.id.distanceseek);
  	    seek.setMax(100);
  	    seek.setProgress(5); // Set it to zero so it will start at the left-most edge
  	    seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

  	        @Override
  	        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
  	            progress = progress + 5; // Add the minimum value (1)
  	            double floatval = (double) progress / 10;
  	            TextView text = (TextView) inflatedView.findViewById(R.id.distval);
  	            text.setText(Double.toString(floatval) + MainActivity2.this.getResources().getString(R.string.km));
  	            maxDistance = floatval *1000;
  	        }

  			@Override
  			public void onStartTrackingTouch(SeekBar arg0) {
  				// TODO Auto-generated method stub
  				
  			}

  			@Override
  			public void onStopTrackingTouch(SeekBar arg0) {
  				// TODO Auto-generated method stub
  				
  			}
  	    });
 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		  
		  public void onClick(DialogInterface dialog, int whichButton) {
    	try
    	
        {
    		Dialog dialogView = (Dialog) dialog;
    		EditText namelocation=(EditText)dialogView.findViewById(R.id.name);
    		 EditText reminder=(EditText)dialogView.findViewById(R.id.comment); 
    		     		 Intent intent;	
    	        Bundle bundle = new Bundle();
    	nameloc = namelocation.getText().toString();
    	comment = reminder.getText().toString();
    	Toast.makeText(getApplicationContext(), nameloc, Toast.LENGTH_SHORT).show();
        
        String lat = String.valueOf(toPosition.latitude);
        String lon = String.valueOf(toPosition.longitude);
        if(nameloc.equals(null))
        {
        	Toast.makeText(getApplicationContext(), "Enter name and the range", Toast.LENGTH_SHORT).show();}
        else
        {

        db.addLocation(new SavedLocation(nameloc,lat,lon,comment,String.valueOf(maxDistance)));
        bundle.putString("Latitude",lat);
	     bundle.putString("Longitude",lon);
	     bundle.putString("Comment",comment);
	     bundle.putDouble("Range", maxDistance);
        if(myPreference.getBoolean("msg", true))
        {
         intent = new Intent(MainActivity2.this,Activity2.class);
        }
        else
        {
        	intent = new Intent(MainActivity2.this,StartAlarm.class);
        	bundle.putString("PhoneNumber", "");
        }
       
 	     intent.putExtras(bundle);
 	     startActivity(intent);
     	MainActivity2.this.finish();
        }
        }
	catch(Exception e)
	{
		
	}
    }    
            });
  	    AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
    	  autoCompView.setHint("Search");
    	    autoCompView.setAdapter(new ActivityAdapter(this, R.layout.items_list));
    	    autoCompView.setOnItemClickListener(new OnItemClickListener() {
    	    	 public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    	       
    	    	String str = (String) adapterView.getItemAtPosition(position);
    	  toPosition = getLocationString(str);
    	    try
    	    {
    	    	
    	          	  map.addMarker(new MarkerOptions().position(toPosition).title("SaveMe").snippet(str).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    	              map.animateCamera(CameraUpdateFactory.zoomTo(14));
    	          	  map.moveCamera(CameraUpdateFactory.newLatLng(toPosition));

    	                // Zoom in the Google Map

    	        
    	            markerOptions.position(toPosition);
    	            
    	            markerOptions.title("destination");
    	            markerOptions.snippet("You will reach there safely");
    	            markerOptions.draggable(true);
    	            map.addMarker(markerOptions);
    	           
    	            alert.show();
    	           
    	     
    	            
    	    }
    	    catch(Exception e)
    	    {
    	    	
    	    	e.printStackTrace();
    	    }
    	        
    	    	 }
    	    
    	    });


    	  
    
       // Setting latitude and longitude in the TextView tv_location
  }

public void setupmap()
{

	int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
	 
	   
    // Showing status
    if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

        int requestCode = 10;
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
        dialog.show();

    }else { // Google Play Services are available

       	  // boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
     	  	
 
        // Getting reference to the SupportMapFragment of activity_main.xml
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting GoogleMap object from the fragment
        
        map = fm.getMap();
        mLocationClient.connect();
               // Enabling MyLocation Layer of Google Map
     if(map!=null)
     {
       markerOptions = new MarkerOptions();
       markerfrom = new MarkerOptions();
       map.setOnMapLongClickListener(this);
       map.setMyLocationEnabled(true);
   	
       Criteria criteria = new Criteria();
	      String provider = locationManager.getBestProvider(criteria, true);
	       location = mLocationClient.getLastLocation();
	       if(location!=null)
	       {
	       fromPosition = new LatLng(location.getLatitude(),location.getLongitude());
	       map.moveCamera(CameraUpdateFactory.newLatLng(fromPosition));
	       map.animateCamera(CameraUpdateFactory.zoomTo(14));

	       } 
      
       
        // Zoom in the Google Map
     
       if(myPreference.getBoolean("traffic", true))
       {
       map.setTrafficEnabled(true);
       }
      
      
       }
       if(gpsIsEnabled)
       {
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
       }
       else if(networkIsEnabled)
       {
           locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,0, this);
       }
      
	Toast.makeText(getApplicationContext(), "hre",Toast.LENGTH_SHORT).show();

	
//LOADING CONTACTS 	
	    
    }
    }
	



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }
  private void drawMarker(Location location){
	 // map.clear();
	  LatLng currentPosition = new LatLng(location.getLatitude(),
	  location.getLongitude());
	  //map.addMarker(new MarkerOptions()
	  //.position(currentPosition).title("SaveMe").snippet("Place of Interest").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
	  map.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));

      // Zoom in the Google Map
      map.animateCamera(CameraUpdateFactory.zoomTo(14));

  }
  public LatLng getLocationString (String address)
  {
			double lng=0;
			double lat = 0;
			try
			{
	    HttpGet httpGet = new HttpGet(
	            "http://maps.google.com/maps/api/geocode/json?address="
	                    + URLEncoder.encode(address, "UTF-8") + "&ka&sensor=false");
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    StringBuilder stringBuilder = new StringBuilder();

	    try {
	        response = client.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuilder.append((char) b);
	        }
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }

	    JSONObject jsonObject = new JSONObject();
	    jsonObject = new JSONObject(stringBuilder.toString());
	   
	    lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lng");

	    lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lat");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	    return new LatLng(lat, lng);
	    
		}
		
@Override
public void onLocationChanged(Location location) {
//	 drawMarker(location);
	LatLng curr = new LatLng(location.getLatitude(),location.getLongitude());
	  map.moveCamera(CameraUpdateFactory.newLatLng(curr));
}

	

@Override
public void onProviderDisabled(String arg0) {

	Toast.makeText(getApplicationContext(), arg0,Toast.LENGTH_LONG).show();
}

@Override
public void onProviderEnabled(String arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	// TODO Auto-generated method stub
	Toast.makeText(getApplicationContext(), "Waiting for location...",Toast.LENGTH_LONG).show();
}
@Override
public void onMapLongClick(LatLng current) {
	// TODO Auto-generated method stub
	map.addMarker(new MarkerOptions() .position(current).title("CurrentLocation").snippet("Place of Interest").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));	
	toPosition = new LatLng(current.latitude,current.longitude);
	   markerOptions.position(toPosition);
       markerfrom.position(fromPosition);
       markerOptions.title("destination");
       markerOptions.snippet("You will reach there safely");
       markerOptions.draggable(true);
       map.addMarker(markerOptions);
       map.addMarker(markerfrom);
       alert.show();
      
}
@Override
protected void onStart() {
    super.onStart();
    try
    {
   mLocationClient.connect();
   setupmap();
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
}
@Override
protected void onStop() {
  
    mLocationClient.disconnect();
    super.onStop();
    finish();
}
@Override
public void onConnectionFailed(ConnectionResult result) {
	// TODO Auto-generated method stub
	
}
@Override
public void onConnected(Bundle connectionHint) {
	// TODO Auto-generated method stub
	Toast.makeText(getApplicationContext(), "Connected",Toast.LENGTH_SHORT).show();
}
@Override
public void onDisconnected() {
	// TODO Auto-generated method stub
	
}

}