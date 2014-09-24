package com.tripmite.app;

import com.tripmite.app.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShowSaved extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		 super.onCreate(savedInstanceState);
		    setContentView(R.layout.showsaved);
		   final Bundle extras = getIntent().getExtras();
		    String name = extras.getString("Name");
		    String lat = extras.getString("Latitude");
		    String lon = extras.getString("Longitude");
		    String range = String.valueOf(extras.getInt("Range"));
		    String comment = extras.getString("Comment");
		    EditText nloc = (EditText) findViewById(R.id.nameloc);
		    EditText cloc = (EditText) findViewById(R.id.commentloc);
		    EditText rloc = (EditText) findViewById(R.id.rangeloc);
		   TextView latloc = (TextView) findViewById(R.id.latloc);
		   TextView lonloc = (TextView) findViewById(R.id.lonloc);
		   TextView nlab = (TextView) findViewById(R.id.nlab);
		   TextView rlab = (TextView) findViewById(R.id.rlab);
		   TextView clab  = (TextView) findViewById(R.id.clab);
		   TextView llab = (TextView) findViewById(R.id.llab);
		   TextView lolab = (TextView) findViewById(R.id.lolab);
		   Button ok = (Button)findViewById(R.id.okloc);
		   ok.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	
		        	Intent intent = new Intent(ShowSaved.this,Activity2.class);
		        	intent.putExtras(extras);
		        	startActivity(intent);
		        	ShowSaved.this.finish();
		        }
		    });
		   
		   
		   nloc.setText(name);
		   cloc.setText(comment);
		   rloc.setText(range);
		   latloc.setText(lat);
		   lonloc.setText(lon);
		   /**Typeface face = Typeface.createFromAsset(getAssets(),
		            "Pacifico.ttf");
		   nloc.setTypeface(face);
		   cloc.setTypeface(face);
		   rloc.setTypeface(face);
		   latloc.setTypeface(face);
		   lonloc.setTypeface(face);
		   
		   nlab.setTypeface(face);
		   clab.setTypeface(face);
		   rlab.setTypeface(face);
		   llab.setTypeface(face);
		   lolab.setTypeface(face);
		   **/
		    
	}
}
