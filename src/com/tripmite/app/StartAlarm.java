package com.tripmite.app;

import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.location.LocationListener;
import android.media.RingtoneManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripmite.app.R;

public class StartAlarm extends FragmentActivity implements LocationListener{
	private GoogleMap map;
	LatLng fromPosition;
	LatLng toPosition;
	Location location;
	NotificationManager mNotificationManager;
	NotificationCompat.Builder mBuilder;
	String comment;
	double range;
	String phno;
	Uri soundUri;
	double dist;
	float speed;
 AlertDialog.Builder alert;

	LocationManager locationManager;
	 SharedPreferences myPreference;
	 	int timeinterval;
	 	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main3);
	     Bundle extras = getIntent().getExtras();
	    comment = extras.getString("Comment");
	    range = extras.getDouble("Range");
	    phno = extras.getString("PhoneNumber");
	    Button b = (Button)findViewById(R.id.cancel);
	    soundUri  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	    alert = new AlertDialog.Builder(this);
	    alert.setMessage("Are you sure you want to cancel the alarm?");
	    Context context = getApplicationContext();
	     myPreference=PreferenceManager.getDefaultSharedPreferences(this);
	    timeinterval = 5*60*1000;
	     b.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            
	            alert.show();
	        }
	        });
	  
	    
	    	 Double lat = Double.valueOf(extras.getString("Latitude"));
		   	  Double lon = Double.valueOf(extras.getString("Longitude"));
		    toPosition = new LatLng(lat,lon);
		//ALERT BUTTON
		    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	      		  
	      		  public void onClick(DialogInterface dialog, int whichButton) {
	          	
	        	mNotificationManager.cancelAll();
	          	locationManager.removeUpdates(StartAlarm.this);
	  	      locationManager = null;
	        	if((!phno.equals("")) && (dist <= range))
	        	{
	        	sendmessage();
	        	}
	        	StartAlarm.this.finish();
	          
	        }
	    });
alert.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
	      		  
	      		  public void onClick(DialogInterface dialog, int whichButton) {
	          	
	       dialog.cancel();   
	        }
	    });
	    mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("QWAKE");
	   Intent resultIntent = new Intent(this,StartAlarm.class);
	   int flags = Intent.FLAG_ACTIVITY_NEW_TASK
               | Intent.FLAG_ACTIVITY_SINGLE_TOP
               | Intent.FLAG_ACTIVITY_CLEAR_TOP;
	   resultIntent.setFlags(flags);
	   extras.putInt("Notification", 1234);
	   resultIntent.putExtras(extras);
	   PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	   mBuilder.setContentIntent(resultPendingIntent);
       mBuilder.setOngoing(true);
 mNotificationManager =
	       (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
try
{
	    	 SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	    		
	        map = fm.getMap();
	        if(map!=null)
	        {
	        	setupmap();
	        	
	        }
	        mNotificationManager.notify(1234, mBuilder.build());

	         locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
   	     boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
   	     boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
   	     Criteria criteria = new Criteria();
   	      String provider = locationManager.getBestProvider(criteria, true);
   	       location = locationManager.getLastKnownLocation(provider);
   	         fromPosition = new LatLng(location.getLatitude(),location.getLongitude());
   	        Location from = new Location("");
   	        from.setLatitude(fromPosition.latitude);
   	        from.setLongitude(fromPosition.longitude);
   	     Location to = new Location("");
	        to.setLatitude(toPosition.latitude);
	        to.setLongitude(toPosition.longitude);
	        dist =from.distanceTo(to);
	        if(myPreference.getBoolean("radius",false))
		 	   {
	        	int r = (int) range;
		        	 map.addCircle(new CircleOptions()
		             .center(new LatLng(toPosition.latitude, toPosition.longitude))
		             .radius(r)
		             .fillColor(Color.GREEN));
		 		   
		 	   }
   	         if(location!=null)
	         {
	        	 drawMarker(location);
	         }

	        if(gpsIsEnabled)
	         {
	             locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeinterval, 0, this);
	         }
	         else if(networkIsEnabled)
	         {
	             locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeinterval,0, this);
	         }
	        map.animateCamera(CameraUpdateFactory.zoomTo(13));
	       
}
catch(Exception e)
{
	Toast.makeText(StartAlarm.this, e.getMessage(), Toast.LENGTH_LONG).show();
}

}
	
	
	private void drawMarker(Location location){
		  map.clear();
		  LatLng currentPosition = new LatLng(location.getLatitude(),
		  location.getLongitude());
		  map.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
		  map.addMarker(new MarkerOptions().position(toPosition).title("SaveMe").snippet("Place of Interest").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		  if(myPreference.getBoolean("radius",false))
	 	   {
			  int r = (int) range;
	        	 map.addCircle(new CircleOptions()
	             .center(new LatLng(toPosition.latitude, toPosition.longitude))
	             .radius(r)
	             .fillColor(Color.GREEN));
	 		   
	 	   }   
	      // Zoom in the Google Map
	      map.animateCamera(CameraUpdateFactory.zoomTo(14));

	  }
	 @Override
	    protected void onNewIntent(Intent intent) 
	    {
		 Bundle extras = intent.getExtras();
		
		 Double lat = Double.valueOf(extras.getString("Latitude"));
	   	  Double lon = Double.valueOf(extras.getString("Longitude"));
	    toPosition = new LatLng(lat,lon);
	    range = extras.getDouble("Range");
	    phno = extras.getString("PhoneNumber");
	    comment = extras.getString("Comment");
		 Toast.makeText(StartAlarm.this,
	              "on new intent"+range, Toast.LENGTH_SHORT)
	              .show();   
				 
	    }
public void calculate_interval(double distance)
{ 
	timeinterval = 2*60*1000;
	if(speed > 0.0)
	{
	float dist = (float) distance;
	if(dist > 2000 )
	{
	float time = dist / speed;
	timeinterval = (int)(( time / 5)*1000);
	}

}
}	public void sendmessage()
{
	Toast.makeText(StartAlarm.this, "Sending SMS....!"+phno,
		Toast.LENGTH_LONG).show();
	
		String address="";

		
	try
	{
		
	SmsManager smsManager = SmsManager.getDefault();

	address = getLocationName(toPosition.latitude,toPosition.longitude);
	smsManager.sendTextMessage(phno, null, "The user has successfully reached  !"+address+" Message sent from Qwake - Your guardian angel", null, null);	
	Toast.makeText(StartAlarm.this, "SMS Sent!"+phno,
			Toast.LENGTH_LONG).show();
	}
	catch (Exception e) {
		Toast.makeText(StartAlarm.this,
			"SMS faild, please try again later!",
			Toast.LENGTH_LONG).show(); 
		e.printStackTrace();
	  }

	}
	    @Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	    	drawMarker(location);
		
	    	if(toPosition!=null)
	    	{
	    		Location to = new Location("");
	    		to.setLatitude(toPosition.latitude);
	    		to.setLongitude(toPosition.longitude);
	    	 dist =location.distanceTo(to);
	    		speed = location.getSpeed();
	    	    calculate_interval(dist);
	    	 if(dist <= range )
	    	{
	    		mBuilder.setContentText(comment);
	    			mBuilder.setSound(soundUri);
	    			
	    			  mNotificationManager.notify(1234, mBuilder.build());
	    			  if(myPreference.getBoolean("radius",false))
	    		 	   {
	    				  int r = (int) range;
	    		        	 map.addCircle(new CircleOptions()
	    		             .center(new LatLng(toPosition.latitude, toPosition.longitude))
	    		             .radius(r) 
	    		             .fillColor(Color.RED));
	    		 		   
	    		 	   }		  
	    	}		
	    		}

	    		
	    	}
		
	
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	      getMenuInflater().inflate(R.menu.activity_main, menu);
	      return true;
	    }
	   

	public void setupmap()
	{
		
		   map.setMyLocationEnabled(true);
		 
	}
	@Override
	public void onProviderDisabled(String arg0) {
		Toast.makeText(getApplicationContext(), "Gps Disabled !!! Please Enable It.",
                Toast.LENGTH_SHORT).show();
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	public String getLocationName(double lat, double lon) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        StringBuilder sb = new StringBuilder();
        try {
            List<Address> adresses = geocoder.getFromLocation(lat, lon, 1);
           
            if (adresses.size() > 0) {
                Address address = adresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    sb.append(address.getAddressLine(i)).append("\n");
                sb.append(address.getCountryName()).append("\n");
            }
           
//          Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

	}
