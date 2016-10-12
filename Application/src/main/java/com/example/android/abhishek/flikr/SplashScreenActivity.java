/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.abhishek.flikr;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.abhishek.flikr.displayingbitmaps.R;
import com.example.android.abhishek.flikr.ui.ImageGridActivity;
import com.example.android.abhishek.flikr.models.Photo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Some simple test data to use for this sample app.
 */

public class SplashScreenActivity extends AppCompatActivity {

    public final static List<String> imageUrls = new ArrayList<>();
    public final static List<String> imageThumbUrls = new ArrayList<>();

    final String BASE_URL = "https://api.flickr.com/services/rest/?";
    final String QUERY_METHOD = "method";
    final String QUERY_API_KEY = "api_key";
    final String QUERY_FORMAT = "format";
    final String QUERY_EXTRAS = "extras";
    final String QUERY_NOJSONCALLBACK = "nojsoncallback";
    final String METHOD = "flickr.photos.getRecent";
    final String API_KEY = "d98f34e2210534e37332a2bb0ab18887";
    final String FORMAT = "json";
    final String EXTRAS = "url_n";
    final String NOJSONCALLBACK = "1";
    final int TIME_OUT = 3000;

    AsyncTask fetchDataTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_METHOD, METHOD)
                        .appendQueryParameter(QUERY_API_KEY, API_KEY)
                        .appendQueryParameter(QUERY_FORMAT, FORMAT)
                        .appendQueryParameter(QUERY_EXTRAS, EXTRAS)
                        .appendQueryParameter(QUERY_NOJSONCALLBACK, NOJSONCALLBACK)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                String jsonResponse = buffer.toString();


                JSONObject responseObject = new JSONObject(jsonResponse);
                JSONObject photos = responseObject.getJSONObject("photos");
                JSONArray photoList = photos.getJSONArray("photo");

                for (int i = 0; i < photoList.length(); i++) {
                    Photo photo = new Photo();
                    JSONObject eachObject = (JSONObject) photoList.get(i);
                    Long id = Long.parseLong(eachObject.getString("id"));
                    photo.photo_id = id;
                    photo.owner = eachObject.getString("owner");
                    photo.secret = eachObject.getString("secret");
                    photo.server = Integer.parseInt(eachObject.getString("server"));
                    photo.farm = eachObject.getInt("farm");
                    photo.title = eachObject.getString("title");
                    photo.ispublic = eachObject.getInt("ispublic")==1 ? true : false;
                    photo.isfriend = eachObject.getInt("isfriend")==1 ? true : false;
                    photo.isfamily = eachObject.getInt("isfamily")==1 ? true : false;
                    try {
                        photo.url_n = eachObject.getString("url_n");
                        photo.height_n = eachObject.getInt("height_n");
                        photo.width_n = Integer.parseInt(eachObject.getString("width_n"));
                        photo.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("NO_IMAGE_URL", "Image URL not found for image with ID: " + id);
                    }
                }

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            imageUrls.clear();
            imageThumbUrls.clear();
            for ( Photo eachPhoto : Photo.find(Photo.class, null, null, null, null, null)) {
                imageUrls.add(eachPhoto.url_n);
            }
            imageThumbUrls.addAll(imageUrls);
            super.onPostExecute(o);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        fetchDataTask.execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, ImageGridActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_OUT);
    }

}
