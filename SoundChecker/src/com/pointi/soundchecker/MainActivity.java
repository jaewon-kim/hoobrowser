package com.pointi.soundchecker;

import java.io.IOException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	SoundMeter mMeter = null;
	static String TAG = "SOUND_CHECK"; 
	TextView tvAmp = null;
	TextView tvAmpEMA = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnStartRecording = (Button)findViewById(R.id.btn_start_recording);
		Button btnStopRecording = (Button)findViewById(R.id.btn_stop_recording);
		Button btnGetValue = (Button)findViewById(R.id.btn_get_value);
		final RelativeLayout rlBack = (RelativeLayout)findViewById(R.id.rl_back);
		
		tvAmp = (TextView)findViewById(R.id.tv_value_amplitude);
		tvAmpEMA = (TextView)findViewById(R.id.tv_value_amplitude_ema);
		
		class ProcValue extends AsyncTask{
			public boolean running = false;

			@Override
			protected void onProgressUpdate(Object... values) {
				// TODO Auto-generated method stub
				
				tvAmp.setText("" + mMeter.getAmplitude());
				tvAmpEMA.setText("" + mMeter.getAmplitudeEMA());
				Log.d(TAG,"::::::" + mMeter.getAmplitudeEMA());
				if(mMeter.getAmplitude() > 0.5 || mMeter.getAmplitudeEMA() > 0.5){
//					Log.d(TAG, ":::::::" +Color.RED);
					rlBack.setBackgroundColor(Color.RED);
				}
				else{
//					Log.d(TAG, ":::::::" +Color.WHITE);
					rlBack.setBackgroundColor(Color.WHITE);
				}
				
				super.onProgressUpdate(values);
			}

			@Override
			protected Object doInBackground(Object... arg0) {
				// TODO Auto-generated method stub
				while(running){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					publishProgress();
				}
				return null;
			}
			
		}
		
		final ProcValue procValue = new ProcValue();
		
		btnStartRecording.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mMeter == null){
					mMeter = new SoundMeter();
				}
				
				try {
					mMeter.start();
					procValue.running = true;
					procValue.execute();
					
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
		});
		
		btnStopRecording.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mMeter != null){
					mMeter.stop();
					procValue.running = false;
				}
				
			}
			
		});
		
		btnGetValue.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "getAmplitude::" + mMeter.getAmplitude());
				Log.d(TAG, "getAmplitudeEMA::" + mMeter.getAmplitudeEMA());
				
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
