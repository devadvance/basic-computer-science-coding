package com.cs1635.mgj7.awesomeclient;

import android.widget.ArrayAdapter;

import java.text.DateFormat;
import java.util.List;

import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Tweet;
import com.twitterapime.model.MetadataSet;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class TimelineArrayAdapter extends ArrayAdapter<Tweet> {
	
	private Context context;

	private List<Tweet> tweets;
	
	
	public TimelineArrayAdapter(final Context context, int textViewResourceId, List<Tweet> tweets) {
		super(context, textViewResourceId, tweets);
		//
		this.context = context;
		this.tweets = tweets;
		//
		
	}
	
	/**
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
        //
        if (v == null) {
            LayoutInflater vi =
            	(LayoutInflater)context.getSystemService(
            		Context.LAYOUT_INFLATER_SERVICE);
            //
            v = vi.inflate(R.layout.row, null);
        }
        //
        Tweet tweet = tweets.get(position);
        //
        
        if (tweet != null) {
        	TextView tweettext = (TextView) v.findViewById(R.id.tweettext);
            TextView datetimetext = (TextView) v.findViewById(R.id.datetimetext);
            TextView postertext = (TextView) v.findViewById(R.id.postertext);
            // Use this later
            ImageView iv = (ImageView) v.findViewById(R.id.usericon);
            
			// Get the poster's information
			UserAccount poster = tweet.getUserAccount();
            // Get the username of the poster
			String username = poster.getString(MetadataSet.USERACCOUNT_NAME);	
			// Get the user's icon
			String imageUrl = poster.getString(MetadataSet.USERACCOUNT_PICTURE_URI);
            
            if (tweettext != null) {
            	tweettext.setText(tweet.getString(MetadataSet.TWEET_CONTENT));
            	
            }
			if (datetimetext != null) {
				DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
				datetimetext.setText("Posted: " + dateFormat.format(Long.parseLong(tweet.getString(MetadataSet.TWEET_PUBLISH_DATE))));
			}
			if (postertext != null) {
				postertext.setText(username);
            	
            }
			if (iv != null) {
				new ImageTask(iv).execute(imageUrl);
			}
        }
        
        return v;
	}
}
