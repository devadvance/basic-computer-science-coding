package com.cs1635.mgj7.awesomeclient;

import com.twitterapime.search.QueryComposer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends TimelineFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText("HOME FRAGMENT");
		return textView;
	}*/
	
	@Override
	protected void refresh() {
		super.refresh();
		//
		Log.w("AwesomeClient", "HOMEFRAGMENT");
		timeline.startGetHomeTweets(
			QueryComposer.append(sinceID, QueryComposer.includeEntities()),
			this);
		Log.w("AwesomeClient", "HOMEFRAGMENT2");
	}
}
