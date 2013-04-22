package com.cs1635.mgj7.awesomeclient;

import com.twitterapime.rest.Timeline;
import com.twitterapime.rest.TweetER;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;

import android.os.AsyncTask;
import android.util.Log;

public class HomeTask extends AsyncTask<String, Integer, Long> {
	private UserAccountManager m;

	public HomeTask (UserAccountManager _m) {
		m = _m;
	}
	
	@Override
	protected Long doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		//if (m.verifyCredential()) {
			//Timeline tml = Timeline.getInstance(m);
			//Query q = QueryComposer.count(20);
			//tml.startGetHomeTweets(q, new SearchDeviceListener() {
			//	public void tweetFound(Tweet tweet) {
			//		System.out.println("Tweet received:\n" + tweet);
			//	}
			//};
		//}
		
		
		return null;
	}

}
