package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ListofStoresActivity extends AppCompatActivity {
    String result = "";
    URL url;
    HttpURLConnection urlConnection=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_stores);

        Getthestore storetask = new Getthestore();
        storetask.execute("http://10.0.2.2:5000/api/Info/13.9907994/100.6214396");

    }

    public void Createlistview(String storeresult) throws JSONException {
        ListView storelist = (ListView)findViewById(R.id.storeslist);

        ArrayList<String> storelistitems = new ArrayList<String>();
        storelistitems.add("hey buddy");
        storelistitems.add("hello buddy");
        storelistitems.add("abc");

        //Convert the retreived data into json format
        JSONArray arr = new JSONArray(storeresult);

        for(int i=0; i<arr.length(); i++){
          JSONObject jsonObject = (JSONObject) arr.get(i);
          storelistitems.add(Integer.toString(jsonObject.getInt("storeID")));
          Log.i("List view items :::",Integer.toString(jsonObject.getInt("storeID")));
        }

        storelistitems.add(result);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, storelistitems);

        storelist.setAdapter(arrayAdapter);
    }

    public class Getthestore extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current=(char)data;
                    result +=current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Createlistview(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
