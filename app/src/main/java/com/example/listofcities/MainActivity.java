package com.example.listofcities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView citiesTextView;

    public String readJSONFromAsset(String s) {
        String json = null;
        try {
            InputStream is = getAssets().open(s);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public ArrayList<String> city(JSONObject jsonObject)
    {
        final ArrayList<String> cities= new ArrayList<>();
        try {
            JSONArray arrJson = jsonObject.getJSONArray(autoCompleteTextView.getText().toString());
            for(int i = 0; i < arrJson.length(); i++) {
                try {
                    cities.add(arrJson.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cities;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoCompleteTextView=findViewById(R.id.autoCompleteTextView);
        citiesTextView=findViewById(R.id.citiesTextView);
              try
                  {
                      final JSONObject jsonObject = new JSONObject(readJSONFromAsset("cities.json"));
                      final Iterator<String> iter = jsonObject.keys(); //This should be the iterator you want.
                      final ArrayList<String> country = new ArrayList<>();
                      while (iter.hasNext()) {
                          String key = iter.next();
                          country.add(key);
                      }
                      final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, country);
                      autoCompleteTextView.setAdapter(arrayAdapter);
                      autoCompleteTextView.setThreshold(1);
                         }
              catch (Exception e)
              {
                  e.printStackTrace();
              }
        try {
            final JSONObject jsonObject = new JSONObject(readJSONFromAsset("cities.json"));
            final Iterator<String> iter = jsonObject.keys(); //This should be the iterator you want.
            final ArrayList<String> cities = new ArrayList<>();
            while (iter.hasNext()) {
                String key = iter.next();
                //country.add(key);
                final JSONArray iter2 = (JSONArray) jsonObject.get(key);
                for (int i=0;i<iter2.length();i++)
                    cities.add((String) iter2.get(i));
            }


            citiesTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);

                }

            });

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, cities);
            citiesTextView.setAdapter(arrayAdapter);
            citiesTextView.setThreshold(1);

        } catch (JSONException e) {
            e.printStackTrace();
        }



      }
}