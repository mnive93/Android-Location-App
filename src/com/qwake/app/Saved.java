package com.qwake.app;

import java.util.ArrayList;
import java.util.List;

import com.qwake.app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Saved extends Activity {
	AlertDialog.Builder alert;
	 List<SavedLocation> locationList ;
	 DatabaseHandler handler;
	 ArrayAdapter adapter;
	 int position;
	 List<String> namelist;
	 SharedPreferences myPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 Context context = getApplicationContext();
	    setContentView(R.layout.databaselist);
	  handler = new DatabaseHandler(this);
	  TextView t = (TextView) findViewById(R.id.saved);
	   Typeface face = Typeface.createFromAsset(getAssets(),
		        "Fresko.ttf");
	   t.setTypeface(face);
	  myPreference=PreferenceManager.getDefaultSharedPreferences(this);
	  final  ListView lv1=(ListView)findViewById(R.id.dblist);
	    final TextView tv = (TextView)findViewById(android.R.id.empty);
	   namelist = new ArrayList<String>();
	  locationList = handler.getAllLocations();
	    alert = new AlertDialog.Builder(this);
	     alert.setMessage("Are you sure you want to delete this item?");
	     alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			  
			  public void onClick(DialogInterface dialog, int whichButton) {

	               handler.DeleteRecord(namelist.get(position));
	               locationList.clear();
	        	   locationList.addAll(handler.getAllLocations());
	        	   namelist.clear();
	        	   for(SavedLocation loc : locationList)
	       	    {
	       	    	namelist.add(loc.getName());
	       	    	
	       	    }
	        	   if(!namelist.isEmpty())
	        	   {
	        		   adapter.add(namelist);
	        		   tv.setText("");
	        		   
	        	   }
	        	   else
	        	   {
	        		   lv1.setEmptyView(tv);
	        	   }
	        		   adapter.notifyDataSetChanged();
	 
				  
			  }
	     });
	    for(SavedLocation loc : locationList)
	    {
	    	namelist.add(loc.getName());
	   	
	    }
	 
	  adapter = new ArrayAdapter<String>(context,R.layout.layoutlist, namelist);
	 
	  
	  
	if(!adapter.isEmpty())
	{
		lv1.setAdapter(adapter);
		tv.setText("");
	}
	else
	{
		lv1.setEmptyView(tv);
	}   
	    lv1.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {
try
{
	position = pos;
	alert.show();
}
catch(Exception e)
{
Toast.makeText(Saved.this, e.getMessage(),Toast.LENGTH_LONG).show();	
}
                return true;
 
}
        }); 

				    lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			try
			{
				Intent intent;
				Bundle bundle = new Bundle();
	    
	    
				SavedLocation loc = locationList.get(arg2); 
				   bundle.putString("Name",loc.getName());
	        	   bundle.putString("Latitude",loc.getLatitude() );
	        	   bundle.putString("Longitude",loc.getLongitude());
	        	   bundle.putString("Comment",loc.getComment());
	        	   bundle.putDouble("Range",Float.parseFloat(loc.getRange()));
		        if(myPreference.getBoolean("msg", true))
		        {	
			 intent = new Intent(Saved.this,Activity2.class);
		        }
		        else
		        {
		        	 intent = new Intent(Saved.this,StartAlarm.class);
		 		    bundle.putString("PhoneNumber", "");
		        	
		        }
			 	    	   intent.putExtras(bundle);
	        	   startActivity(intent);
	        	   Saved.this.finish();

			}
			catch(Exception e)
			{
				Toast.makeText(Saved.this, e.getMessage(),Toast.LENGTH_SHORT).show();
				
			}
				
				
			}
	    });
	    
	}
}
