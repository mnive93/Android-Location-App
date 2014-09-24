package com.qwake.app;

import java.util.ArrayList;

import com.qwake.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class Activity2 extends Activity implements OnItemClickListener {
	String toNumberValue="";
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		 super.onCreate(savedInstanceState);
		    setContentView(R.layout.contact);
		  final   Bundle extras = getIntent().getExtras();
		    ContactAdapter con = new ContactAdapter();
			  Button b = (Button) findViewById(R.id.okay);
			
			  b.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View v) {
			        	Intent intent = new Intent(Activity2.this,StartAlarm.class);
			        	extras.putString("PhoneNumber",toNumberValue);
			        	intent.putExtras(extras);
			        	startActivity(intent);
			        	Activity2.this.finish();
			          
			        }
			    });
			  
					   	  ArrayList<String> data = con.readContactData(getBaseContext());
		    	final AutoCompleteTextView contacts = (AutoCompleteTextView) findViewById(R.id.contacts);
		    	adapter = new ArrayAdapter<String>
		        (this, android.R.layout.simple_dropdown_item_1line, data);
		       contacts.setThreshold(1);	
		     
		       contacts.setAdapter(adapter);
		      
		       contacts.setOnItemClickListener(this);
		    	
		    	 

	}
	  public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
	    	 // TODO Auto-generated method stub
	         ContactAdapter con = new ContactAdapter();
         // Get Array index value for selected name
	    	int i = con.getIndex(""+adapterView.getItemAtPosition(position));
          
         
         // If name exist in name ArrayList
         if (i >= 0) {
              
             // Get Phone Number
             toNumberValue = con.getPhoneNumber(i);
              
             InputMethodManager imm = (InputMethodManager) getSystemService(
                     INPUT_METHOD_SERVICE);
                 imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

             // Show Alert       
             Toast.makeText(getBaseContext(), 
                         "Position:"+position+" Name:"+adapterView.getItemAtPosition(position)+" Number:"+toNumberValue,
                         Toast.LENGTH_LONG).show();
              
             
              
         }
      
 }


}
