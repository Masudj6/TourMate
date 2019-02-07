package com.example.lazy_programmer.tourmate.Weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by lazy-programmer on 9/22/17.
 */

public class DownloadImage extends AsyncTask<String,Void,Bitmap> {

    public ImageView img;

    public DownloadImage(ImageView img) {
        this.img = img;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String urldisplay = "https:"+params[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        img.setImageBitmap(result);
    }
}
