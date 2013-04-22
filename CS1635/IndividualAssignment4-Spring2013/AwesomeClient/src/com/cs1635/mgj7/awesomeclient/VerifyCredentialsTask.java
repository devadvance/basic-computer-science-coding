package com.cs1635.mgj7.awesomeclient;

import java.io.IOException;

import com.twitterapime.rest.Timeline;
import com.twitterapime.rest.TweetER;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;

import android.os.AsyncTask;
import android.util.Log;

public class VerifyCredentialsTask extends AsyncTask<String, Integer, Long> {
	private UserAccountManager ua_manager;
	private TimelineFragment f;

	public VerifyCredentialsTask (UserAccountManager _m, TimelineFragment _f) {
		ua_manager = _m;
		f = _f;
	}
	
	@Override
	protected Long doInBackground(String... params) {
		try {
			ua_manager.verifyCredential();
			Log.w("AwesomeClient", "VERIFYTASK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LimitExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	protected void onPostExecute(Long result) {
        f.update();
    }

}
