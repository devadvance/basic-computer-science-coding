package com.cs1635.mgj7.handwrite;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

public class PreferencesActivity extends Activity {
	
	private int seekValueR, seekValueG, seekValueB;
	private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
	private SurfaceView mSurface;
	Intent resultIntent;
	private int colorHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		
		// Get the message from the intent
		resultIntent = new Intent();
		// TODO Add extras or a data URI to this intent as appropriate.
	    
	    mSurface = (SurfaceView) findViewById(R.id.colorSurface);
	    redSeekBar = (SeekBar) findViewById(R.id.seekBarR);
	    greenSeekBar = (SeekBar) findViewById(R.id.seekBarG);
	    blueSeekBar = (SeekBar) findViewById(R.id.seekBarB);
	    
	    redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
	    greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
	    blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_preferences, menu);
		return true;
	}
	
	
	private SeekBar.OnSeekBarChangeListener seekBarChangeListener
	= new SeekBar.OnSeekBarChangeListener()
	{

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
	  boolean fromUser) {
	// TODO Auto-generated method stub
		updateColorSurface();
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	// TODO Auto-generated method stub
	}
	};
	
	public void saveClick(View view) {
		// Work on here!!!
		resultIntent.putExtra("com.cs1635.mgj7.handwrite.colorvalue", colorHolder);
		setResult(Activity.RESULT_OK, resultIntent);
		Log.e("test", "Reached the saveClick method");
		finish();
		
	}
	private void updateColorSurface()
	{
	 seekValueR = redSeekBar.getProgress();
	 seekValueG = greenSeekBar.getProgress();
	 seekValueB = blueSeekBar.getProgress();
	 
	 colorHolder = (0xff000000
			  + seekValueR * 0x10000
			  + seekValueG * 0x100
			  + seekValueB);
	 
	 mSurface.setBackgroundColor(colorHolder);
	}
}