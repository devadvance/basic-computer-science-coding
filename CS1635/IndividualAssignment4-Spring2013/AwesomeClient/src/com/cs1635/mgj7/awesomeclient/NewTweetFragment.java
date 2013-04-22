package com.cs1635.mgj7.awesomeclient;

import java.io.IOException;

import com.twitterapime.rest.TweetER;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Tweet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class NewTweetFragment extends Fragment {
	
	public NewTweetFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.new_tweet_layout, container, false);
		
		view.findViewById(R.id.newTweet_postButton).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				postTweet(v);
			}
		});
		
		return view;
	}
	
	public void postTweet(View v) {
		EditText content = (EditText) getView().findViewById(R.id.newTweet_content);
		
		String post = content.getText().toString();
		
		if ((post == null) && (post.equals(""))) {
			
		} else if (post.length() > 140) {
			
		} else {
			MainActivity my_activity = (MainActivity) getActivity();
			ProgressDialog progressDialog = ProgressDialog.show(
				getActivity(), "", "Posting...", false);
			new PostTweetTask(my_activity.ua_manager, progressDialog).execute(post);
		}
	}
}
