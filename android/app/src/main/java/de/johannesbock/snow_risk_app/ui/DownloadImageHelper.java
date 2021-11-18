package de.johannesbock.snow_risk_app.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * This class helps to download the weather icon provided by the Weather API
 * An object of this class will download the image as bitmap in the background and update the give imageview
 */
public class DownloadImageHelper extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = DownloadImageHelper.class.getName();

    ImageView imageView;

    public DownloadImageHelper(ImageView imageView) {
        this.imageView = imageView;
    }

    /**
     * This function fetches the bitmap data of the image in the background
     * @param urls a list of the urls
     * @return returns the icon bitmap
     */
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];

        Bitmap icon = null;

        try {
            InputStream inputStream = new java.net.URL(url).openStream();
            icon = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        return icon;

    }

    /**
     * Uses the bitmap from the background work and adds it to the imageview object of the class
     * @param bitmap a bitmap object with the image for the imageview
     */
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}
