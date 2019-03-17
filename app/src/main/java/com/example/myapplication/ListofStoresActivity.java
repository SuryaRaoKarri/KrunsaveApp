package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    double lat,lng;

    //OnCreate Start
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_stores);



        //Location Track Start
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListiner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("User Latitude:", Double.toString(location.getLatitude()));
                Log.i("User Longitute:", Double.toString(location.getLongitude()));
                lat= location.getLatitude();
                lng= location.getLongitude();
                //Send the lat and lng
                Getthestore getstore = new Getthestore();
                getstore.execute("http://10.0.2.2:5000/api/Info/"+lat+"/"+lng);
                locationManager.removeUpdates(this);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        //Location Track End

        //Check Again permission User GPS start
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //we have permission already
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListiner);

        }
        //Check again permission User GPS End
    }
//OnCreate End

    public void Createlistview(String storeresult) throws JSONException {
        ListView storelist = (ListView)findViewById(R.id.storeslist);

        ArrayList<String> storelistitems = new ArrayList<String>();

        //Convert the retreived data into json format
        JSONArray arr = new JSONArray(storeresult);

        for(int i=0; i<arr.length(); i++){
            JSONObject jsonObject = (JSONObject) arr.get(i);
            //storelistitems.add(Integer.toString(jsonObject.getInt("storeID")));
            //Log.i("List view items :::",Integer.toString(jsonObject.getInt("storeID")));
            storelistitems.add(jsonObject.getString("storeName")+"| "+jsonObject.getString("address")+"| "+ jsonObject.getString("dist")+"Km");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, storelistitems);

        storelist.setAdapter(arrayAdapter);
    }


    //Get the store information Start
    public class Getthestore extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestProperty("Content-Type", "application.json");
//                urlConnection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiI4IiwidW5pcXVlX25hbWUiOiJhZG1pbjRAZ21haWwuY29tIiwicm9sZSI6ImFkbWluIiwidXNlcklEIjoiOCIsIm5iZiI6MTU1MjY1MzI3NCwiZXhwIjoxNTUyNzM5Njc0LCJpYXQiOjE1NTI2NTMyNzR9.CvMN9LcbRyj-AJEf6UEolTppUKYE_oRQv5bBFTQROBenyxIVlnWW3e5cl5SGU6AEdRIx4e6J7ZlCt8D29frK1A");
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

    //Get the store information End


    //Add Permission Maps
    LocationManager locationManager;
    LocationListener locationListiner;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListiner);

        }
    }
    //Add permission maps End
}
