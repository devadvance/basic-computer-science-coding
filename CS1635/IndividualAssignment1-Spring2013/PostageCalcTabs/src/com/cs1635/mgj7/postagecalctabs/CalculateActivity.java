package com.cs1635.mgj7.postagecalctabs;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.util.Log;

public class CalculateActivity extends Activity {
	
	EditText weightText;
	RadioGroup rGroup;
	RadioButton letters;
	RadioButton large;
	RadioButton packages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculate);
		
		weightText = (EditText)findViewById(R.id.weightInput);
        rGroup = (RadioGroup)findViewById(R.id.radioGroup1);
        letters = (RadioButton)findViewById(R.id.radioLetters);
        large = (RadioButton)findViewById(R.id.radioLarge);
        packages = (RadioButton)findViewById(R.id.radioPackages);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_calculate, menu);
		return true;
	}
	
	public void calculatePostage(View view) {
    	Double cost = 0.0;
    	String messageText;
    	
    	if (!(weightText.getText().toString().equals(""))) {
	    	
	    	cost = calculateLogic(weightText.getText().toString());
	    	
	    	if (cost == -1) {
	    		messageText = "You entered an invalid weight!\nMust be between 0 and 13 oz!";
	    	}
	    	else if (cost == -2) {
	    		messageText = "Letters cannot be that heavy!\nMust be between 0 and 3.5 oz!";
	    	}
	    	else if (cost == -3) {
	    		messageText = "Something wonky with the radio buttons...";
	    	}
	    	else {
	    		messageText = "$" + String.format("%.2f", cost);
	    	}
	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(CalculateActivity.this);
	    	builder.setMessage(messageText);
	    	builder.setTitle("Postage Amount");
	    	builder.setCancelable(false);
	    	builder.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int which) {
	    			dialog.cancel();
	    		}
	    	});
	    	AlertDialog alert = builder.create();
	    	alert.show();
	    }
    }
    
    public Double calculateLogic(String weightString) {
    	Double weight = 0.0;
    	Double cost = 0.0;
    	
    	try {
    		weight = Double.parseDouble(weightString);
    	}
    	catch (Exception e) {
    		Log.w("postagecalculator.MainActivity", "Something went wrong parsing a double: " + e);
    	}
    	
    	if ((weight <= 0) || (weight > 13)) {
    		cost = -1.0;
    	}
    	else {
	    	if (rGroup.getCheckedRadioButtonId() == letters.getId()) {
	    		cost = calcLetter(weight);
	    	}
	    	else if (rGroup.getCheckedRadioButtonId() == large.getId()) {
	    		cost = calcLarge(weight);
	    	}	
	    	else if (rGroup.getCheckedRadioButtonId() == packages.getId()) {
	    		cost = calcPackage(weight);
	    	}
	    	else {
	    		cost = -3.0;
	    	}
    	}
    	
    	return cost;
    }
    
    
    public Double calcLetter(Double weight) {
    	if (Math.ceil(weight) <= 3) {
    		return (0.45 + (Math.ceil(weight) - 1) * .20);
    	}
    	else if ((weight > 3.0) && (weight <= 3.5)) {
    		return 1.05;
    	}
    	else {
    		return -2.0;
    	}
    }
    
    public Double calcLarge(Double weight) {
    	return (0.70 + Math.ceil(weight) * .20);
    }
    
    public Double calcPackage(Double weight) {
    	if (Math.ceil(weight) <= 3) {
    		return 1.95;
    	}
    	else {
    		return (2.12 + ((Math.ceil(weight) - 4)* .17));
    	}
    }

}