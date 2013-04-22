package com.cs1635.mgj7.handwrite;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private DrawingPanel mPanel;
	private String fileNameInput;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Grab the DrawingPanel
		mPanel = (DrawingPanel)findViewById(R.id.drawingPanel1);
		
		if (savedInstanceState != null) {
			mPanel.setSerialPathList((List<SerialPath>) savedInstanceState.get("paths"));
			mPanel.setPointList((List<Point>) savedInstanceState.get("points"));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
	           	
	            Intent intent = new Intent(this, PreferencesActivity.class);
	            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivityForResult(intent, 1);
	            return true;
	        case R.id.menu_clear:
	        	mPanel.clearPanel();
	        	return true;
	        case R.id.menu_save:
	        	saveCase();
	        	return true;
	        case R.id.menu_load:
	        	loadCase();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public boolean saveCase() {
		final EditText input = new EditText(this);
		fileNameInput = null;
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
		alertDialogBuilder.setTitle("Enter Filename")
	    .setMessage("Enter a filename:")
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	
	        	fileNameInput = new String(input.getText().toString());
	        	saveDrawing(fileNameInput);
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            // Do nothing.
	        }
	    }).show();
		
		return true;
	}
	
	public boolean saveDrawing(String fileString) {
		
		ArrayList<Object> toSave = new ArrayList<Object>();
		toSave.add(mPanel.getSerialPathList());
		toSave.add(mPanel.getPointList());
		
		FileOutputStream fos;
		try {
			fos = openFileOutput(fileString, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			//os.writeObject(mPanel.getSerialPathList());
			os.writeObject(mPanel.getPointList());
			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean loadCase() {
		final String[] files = fileList();

		if (files.length <= 0) {
			Toast.makeText(MainActivity.this, "You have NO files!", Toast.LENGTH_SHORT).show();
		}
		else {
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("Load File")
			.setItems(files,new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int item) {
						loadDrawing(files[item]);
					}
				})
			.create()
			.show();
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadDrawing(String fileString) {
		ArrayList<Point> tempPoints = null;
		
		try {
			FileInputStream fis = openFileInput(fileString);
			ObjectInputStream is = new ObjectInputStream(fis);
			//mPanel.setSerialPathList( (ArrayList<SerialPath>) is.readObject() );
			tempPoints = (ArrayList<Point>) is.readObject();
			mPanel.load(tempPoints);
			
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle mBundle) {
	  super.onSaveInstanceState(mBundle);
	  mBundle.putSerializable("points", new ArrayList<Point>(mPanel.getPointList()));
	  mBundle.putSerializable("paths", new ArrayList<SerialPath>(mPanel.getSerialPathList()));
	}
	
	public void recognize(View view) {
		if (!(mPanel.isEmpty())) {
			URL tempURL = null;
			try {
				tempURL = new URL("http://cwritepad.appspot.com/reco/usen");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			RecognizeTask task = new RecognizeTask();
			task.execute(tempURL);
		}
		else {
			Toast.makeText(MainActivity.this, "Nothing drawn yet!",Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	      if (resultCode == Activity.RESULT_OK) {
	    	  Bundle dataBundle = data.getExtras();
	    	  int test = dataBundle.getInt("com.cs1635.mgj7.handwrite.colorvalue");
	    	  mPanel.setPaintColor(test);
	    	  mPanel.invalidate();  
	      }
	}
	
	
	private class RecognizeTask extends AsyncTask<URL, Integer, String> {
		@Override
		protected String doInBackground(URL... params) {
			HttpClient httpclient = new DefaultHttpClient();
			String url = params[0].toString();
		    HttpPost httppost = new HttpPost(url);
		    
		    List<Point> tempList = mPanel.getTerminatedPointList();
			String body = new String();;
		    try {
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("key", "11773edfd643f813c18d82f56a8104ed"));
		        nameValuePairs.add(new BasicNameValuePair("q", tempList.toString()));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        
		        HttpResponse response = httpclient.execute(httppost);
		        
		        if (response.getEntity().getContentLength() != 0) {
			        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			        
					body = rd.readLine();

		        }
		        else {
		        	body = "Invalid Entry";
		        }
		    } catch (ClientProtocolException e) {
		    	
		    } catch (IOException e) {
		    	
		    }
			return body;
		}
		
		@Override
		protected void onPostExecute(String result) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	    	builder.setMessage(result);
	    	builder.setTitle("Recognized Text");
	    	builder.setCancelable(false);
	    	builder.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int which) {
	    			dialog.dismiss();
	    		}
	    	});
	    	AlertDialog alert = builder.create();
	    	alert.show();
		}
	}
	
	
}
