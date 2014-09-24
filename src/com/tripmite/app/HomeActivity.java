package com.tripmite.app;




import com.tripmite.app.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
	
	 super.onCreate(savedInstanceState);
	    setContentView(R.layout.home);
	  Button save = (Button ) findViewById(R.id.save);
	  Button home = (Button ) findViewById(R.id.home);
	  Button settings = (Button) findViewById(R.id.settings);
	  Button help = (Button) findViewById(R.id.help);
	  
	    Typeface face = Typeface.createFromAsset(getAssets(),
	            "Fresko.ttf");
	TextView tv = (TextView) findViewById(R.id.hometitle);
	tv.setTypeface(face);
	try
	{
	  save.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	Intent intent = new Intent(HomeActivity.this,Saved.class);
	        	startActivity(intent);
	        
	
	        }
	 
	        });

	  home.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	
	        	Intent intent = new Intent(HomeActivity.this,MainActivity2.class);
	        	startActivity(intent);
	        }
	    });
	  settings.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	
	        	Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
	        	startActivity(intent);
	        }
	    });
	  help.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	
	        	Intent intent = new Intent(HomeActivity.this,HelpActivity.class);
	        	startActivity(intent);
	        }
	    });
  
	}
	catch(Exception e)
	{
		Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		
	}
	
}


}
