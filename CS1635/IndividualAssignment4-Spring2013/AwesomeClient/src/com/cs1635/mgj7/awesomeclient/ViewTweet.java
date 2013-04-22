package com.cs1635.mgj7.awesomeclient;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.UserAccount;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.util.Linkify;
import android.view.View.OnClickListener;

public class ViewTweet extends FragmentActivity {

	private String content;
	private String username;
	private String name;
	private String imageUrl;
	private String date;
	UserAccount posterUA;

	private TextView tweet;
	private TextView poster;
	private TextView datePosted;
	private ImageView usericon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_tweet);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);


		// Set stuff to display

		Intent sender = getIntent();
		content = sender.getStringExtra("CONTENT");
		name = sender.getStringExtra("NAME");
		username = sender.getStringExtra("USERNAME");
		imageUrl = sender.getStringExtra("IMAGE");
		date = sender.getStringExtra("DATE");
		posterUA = (UserAccount) sender.getSerializableExtra("UA");

		tweet = (TextView) findViewById(R.id.viewTweet_Text);
		poster = (TextView) findViewById(R.id.viewTweet_Poster);
		datePosted = (TextView) findViewById(R.id.viewTweet_Date);
		usericon = (ImageView) findViewById(R.id.viewTweet_image);

		tweet.setText(content);
		Linkify.addLinks(tweet, Linkify.WEB_URLS);
		poster.setText(name);
		datePosted.setText(date);

		poster.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showUserDialog();
			}
		});

		// Set user image
		new ImageTask(usericon).execute(imageUrl);
	}

	private void showUserDialog() {
		FragmentManager fm = getSupportFragmentManager();
		DialogFragment dialog = new UserDialog();
		Bundle bundle = new Bundle();
		bundle.putString("USERNAME", posterUA.getString(MetadataSet.USERACCOUNT_USER_NAME));
		bundle.putString("NAME", posterUA.getString(MetadataSet.USERACCOUNT_NAME));
		bundle.putString("FOLLOWING", posterUA.getString(MetadataSet.USERACCOUNT_FRIENDS_COUNT));
		bundle.putString("FOLLOWERS", posterUA.getString(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT));
		bundle.putString("TWEETS", posterUA.getString(MetadataSet.USERACCOUNT_TWEETS_COUNT));
		dialog.setArguments(bundle);
		dialog.show(fm, "dialog_layout");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_tweet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
