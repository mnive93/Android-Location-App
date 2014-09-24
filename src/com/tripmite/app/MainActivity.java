package com.tripmite.app;

import java.util.Timer;
import java.util.TimerTask;

import com.tripmite.app.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.CountDownTimer;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    Typeface face = Typeface.createFromAsset(getAssets(),
        "Fresko.ttf");
TextView tv = (TextView)findViewById(R.id.title);
tv.setTypeface(face);
final long length_in_milliseconds = 2000;
	    CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
	       
	        @Override
	        public void onTick(long millisUntilFinished) 
	        {
	        
	    }

	        @Override
	        public void onFinish() {
	           Intent myintent = new Intent(MainActivity.this,HomeActivity.class);
	           startActivity(myintent);
	        finishscreen();   
	        }
	    }.start();
	}
	public void finishscreen()
	{ 
		this.finish();
		
	}
}
