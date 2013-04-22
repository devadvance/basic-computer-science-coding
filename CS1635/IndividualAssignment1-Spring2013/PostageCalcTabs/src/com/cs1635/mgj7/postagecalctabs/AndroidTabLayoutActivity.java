package com.cs1635.mgj7.postagecalctabs;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation") 
public class AndroidTabLayoutActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tab_layout);
 
        TabHost tabHost = getTabHost();
 
        // Calculate tab
        TabSpec calculatespec = tabHost.newTabSpec("Calculate");
        calculatespec.setIndicator("Calculate");
        Intent calculateIntent = new Intent(this, CalculateActivity.class);
        calculatespec.setContent(calculateIntent);
 
        // Info tab
        TabSpec infospec = tabHost.newTabSpec("Info");
        infospec.setIndicator("Info");
        Intent infoIntent = new Intent(this, InfoActivity.class);
        infospec.setContent(infoIntent);
 
        tabHost.addTab(calculatespec);
        tabHost.addTab(infospec);
    }
}