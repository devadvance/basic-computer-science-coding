package com.cs1635.mgj7.awesomeclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserDialog extends DialogFragment {
	
	private TextView userText;
	private TextView nameText;
	private TextView followersText;
	private TextView followingText;
	private TextView tweetsText;
	
	private String username;
	private String name;
	private String followers;
	private String following;
	private String tweets;
	
	public UserDialog() {
		
	}
	
	public void setArguments (Bundle bundle) {
		username = bundle.getString("USERNAME");
		name = bundle.getString("NAME");
		followers = bundle.getString("FOLLOWERS");
		following = bundle.getString("FOLLOWING");
		tweets = bundle.getString("TWEETS");
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, container);
        userText = (TextView) view.findViewById(R.id.dialog_username);
        nameText = (TextView) view.findViewById(R.id.dialog_name);
        followersText = (TextView) view.findViewById(R.id.dialog_numberfollowers);
        followingText = (TextView) view.findViewById(R.id.dialog_numberfollowing);
        tweetsText = (TextView) view.findViewById(R.id.dialog_numbertweets);
        
        userText.setText("Username: " + username);
        nameText.setText("Name: " + name);
        followersText.setText("Followers: " + followers);
        followingText.setText("Following: " + following);
        tweetsText.setText("Number of tweets: " + tweets);
        
        
        getDialog().setTitle("User Info");

        return view;
    }
	
}
