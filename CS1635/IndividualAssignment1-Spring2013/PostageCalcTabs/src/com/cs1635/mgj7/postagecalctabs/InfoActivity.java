package com.cs1635.mgj7.postagecalctabs;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends Activity implements OnItemSelectedListener {

	TextView infoTextView;
	ImageView infoImageView;
	Spinner spinner;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		infoTextView = (TextView)findViewById(R.id.infoText);
		infoImageView = (ImageView)findViewById(R.id.infoImage);
		
		spinner = (Spinner)findViewById(R.id.spinnerInfo);
		spinner.setOnItemSelectedListener(this);
		
		Toast.makeText(InfoActivity.this, "Remember to scroll to see all info!",Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_info, menu);
		return true;
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
		//parent.getItemAtPosition(pos).toString().equals("--")
		
		//Log.d("postagecalculator.InfoActivity", "this: " + parent.getItemAtPosition(pos).toString());
		if (parent.getItemAtPosition(pos).toString().equals("Letters")) {
			infoTextView.setText(R.string.info_letters);
			infoImageView.setImageDrawable(getResources().getDrawable(R.drawable.usps_letters));
		}
		else if (parent.getItemAtPosition(pos).toString().equals("Large Envelopes")) {
			infoTextView.setText(R.string.info_large);
			infoImageView.setImageDrawable(getResources().getDrawable(R.drawable.usps_lrg_env));
		}
		else if (parent.getItemAtPosition(pos).toString().equals("Packages")) {
			infoTextView.setText(R.string.info_packages);
			infoImageView.setImageDrawable(getResources().getDrawable(R.drawable.usps_pkgservices));
		}
		
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
