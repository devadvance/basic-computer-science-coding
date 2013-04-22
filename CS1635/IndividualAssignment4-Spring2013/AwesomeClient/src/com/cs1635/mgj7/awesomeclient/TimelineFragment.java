package com.cs1635.mgj7.awesomeclient;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.Timeline;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TimelineFragment extends ListFragment implements SearchDeviceListener {

	static final int TWEET_COUNT = 200;
	
	protected List<Tweet> tweets;
	
	protected Query sinceID;
	
	protected Timeline timeline;
	
	private TimelineArrayAdapter adapter;
	
	private ProgressDialog progressDialog;
	
	private Runnable notifyNewTweet;
	
	private MainActivity my_activity;
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		tweets = new ArrayList<Tweet>();
		adapter = new TimelineArrayAdapter(getActivity(), R.layout.row, tweets);
		setListAdapter(adapter);
		
		setHasOptionsMenu(true);
		//
		//registerForContextMenu(getListView());
		//
		/*getListView().setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					viewTweet(tweets.get(position));
				}
			}
		);*/
		//
		
		//
		my_activity = (MainActivity) getActivity();
		
		VerifyCredentialsTask task = new VerifyCredentialsTask(my_activity.ua_manager, this);
		task.execute("VERIFY");
		

		
		//		
		
		
		notifyNewTweet = new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
				progressDialog.dismiss();
			}
		};
		
		sinceID = QueryComposer.count(TWEET_COUNT / 4);
		
		//
		//
		//refresh();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_refresh:
	           	update();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.tweet_list, container, false);
	}
	
	protected void refresh() {
		progressDialog =
			ProgressDialog.show(
				getActivity(), "", getString(R.string.refreshing), false);
		//
	}
	
	protected void update() {
		timeline = Timeline.getInstance(my_activity.ua_manager);
		refresh();
	}
	
	public void onListItemClick (ListView l, View v, int position, long id) {
		
		//Log.w("TrainScreen", "List clicked!");
		
		
		Tweet tweet = adapter.getItem(position);
		
		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
		String date = dateFormat.format(Long.parseLong(tweet.getString(MetadataSet.TWEET_PUBLISH_DATE)));
		// Get the poster's information
		UserAccount poster = tweet.getUserAccount();
        // Get the name of the poster
		String name = poster.getString(MetadataSet.USERACCOUNT_NAME);	
		// Get user (actual username)
		String username = poster.getString(MetadataSet.USERACCOUNT_USER_NAME);
		// Get the user's icon
		String imageUrl = poster.getString(MetadataSet.USERACCOUNT_PICTURE_URI);
		// Get tweet content
		String content = tweet.getString(MetadataSet.TWEET_CONTENT);
		
		Intent viewTweetIntent = new Intent(getActivity(), ViewTweet.class);
		
		viewTweetIntent.putExtra("CONTENT", content);
		viewTweetIntent.putExtra("IMAGE", imageUrl);
		viewTweetIntent.putExtra("USERNAME", username);
		viewTweetIntent.putExtra("NAME", name);
		viewTweetIntent.putExtra("DATE", date);
		viewTweetIntent.putExtra("UA", poster);
		this.startActivity(viewTweetIntent);
	}
	
	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchCompleted()
	 */
	@Override
	public void searchCompleted() {
		Collections.sort(tweets, new Comparator<Tweet>() {
			@Override
			public int compare(Tweet t1, Tweet t2) {
				long pd1 = Long.parseLong(t1.getString(MetadataSet.TWEET_ID));
				long pd2 = Long.parseLong(t2.getString(MetadataSet.TWEET_ID));
				//
				if (pd1 < pd2) {
					return 1;
				} else if (pd1 > pd2) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		if (tweets.size() > 0) {
			String lastTweetId = tweets.get(0).getString(MetadataSet.TWEET_ID);
			//
			sinceID =
				QueryComposer.append(
					QueryComposer.sinceID(lastTweetId),
					QueryComposer.count(TWEET_COUNT));
			//
//			saveLastTweetId(lastTweetId);
		}
		//
		Log.w("AwesomeClient", "Search completed");
		my_activity.runOnUiThread(notifyNewTweet);
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#searchFailed(java.lang.Throwable)
	 */
	@Override
	public void searchFailed(final Throwable exception) {
		my_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
				//
				//UIUtil.showMessage(my_activity, exception);
				Log.w("AwesomeClient", "Search failed.");
			}
		});
	}

	/**
	 * @see com.twitterapime.search.SearchDeviceListener#tweetFound(com.twitterapime.search.Tweet)
	 */
	@Override
	public void tweetFound(Tweet tweet) {
		tweets.add(tweet);
		Log.w("AwesomeClient", "Tweet found!");
	}

}
