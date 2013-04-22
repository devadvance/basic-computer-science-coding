package com.cs1635.mgj7.awesomeclient;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageTask extends AsyncTask<String, Void, Bitmap> {
	  ImageView imageView;

	  public ImageTask(ImageView imageView) {
	      this.imageView = imageView;
	  }

	  protected Bitmap doInBackground(String... urls) {
	      String url = urls[0];
	      Bitmap bitmap = null;
	      try {
	        InputStream in = new URL(url).openStream();
	        bitmap = BitmapFactory.decodeStream(in);
	        
	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	      return bitmap;
	  }

	  protected void onPostExecute(Bitmap result) {
		  imageView.setImageBitmap(result);
	  }
}
