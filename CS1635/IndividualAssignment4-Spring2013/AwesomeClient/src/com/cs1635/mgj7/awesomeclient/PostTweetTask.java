package com.cs1635.mgj7.awesomeclient;

import com.twitterapime.rest.TweetER;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.Tweet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class PostTweetTask extends AsyncTask<String, Integer, Long>{
	private UserAccountManager m;
	private ProgressDialog progressDialog;

	public PostTweetTask (UserAccountManager _m, ProgressDialog progressDialog) {
		m = _m;
		this.progressDialog = progressDialog;
		
	}
	
	@Override
	protected Long doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		try {
			if (m.verifyCredential()) {
				Tweet t = new Tweet(params[0]);
				TweetER ter = TweetER.getInstance(m);
				t = ter.post(t);
				Log.w("AwesomeClient", "HERE2");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Long result) {
		progressDialog.dismiss();
	}

}
