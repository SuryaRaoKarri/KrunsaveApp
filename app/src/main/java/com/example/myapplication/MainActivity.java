package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //Global Variables to access them in all files
    String jwtKey = "";
    //Global Variables to access then in all files End

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DownloadTask task1 = new DownloadTask();
//        task1.execute("http://10.0.2.2:5000/api/edit/editstore");

        //Location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListiner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.i("User Current Location:", location.toString());
                userLocation = location.toString();

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //we have permission already
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListiner);

        }
    }
//Oncreate end

    ///Login button start
    public void Login(View view){
        Toast.makeText(MainActivity.this, "Coming Soon!!", Toast.LENGTH_LONG).show();
    }
    //Login button End

    ///Register button start
    public void Register(View view){
        Toast.makeText(MainActivity.this, "Coming Soon!!", Toast.LENGTH_LONG).show();
    }
    //Register button End

    //View store start
    public void Viewstorepage(View view){
        Intent intent = new Intent(getApplicationContext(), ListofStoresActivity.class);
        startActivity(intent);
    }
    //View store end

    public void ShowReq(View view){
//        DownloadTask task = new DownloadTask();
//        task.execute("http://10.0.2.2:5000/api/edit/editstore");
//        Log.i("MSG" , result);

        EditText lat = (EditText) findViewById(R.id.lat);
        EditText lng = (EditText) findViewById(R.id.lng);

        Toast.makeText(getApplicationContext(), lat.getText(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), lng.getText(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), userLocation, Toast.LENGTH_LONG).show();
    }

    String userLocation="";
    String result = "";
    URL url;
    HttpURLConnection urlConnection = null;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            try{
                url=new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("content-Type", "application/json");
//                urlConnection.setDoOutput(true);
//                urlConnection.setDoInput(true);
//                urlConnection.connect();
//                DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
//                os.writeBytes("{'storeID':1, 'storeName':'Hello'}");
//                os.flush();
//                os.close();
//                Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
//                Log.i("MSG" , urlConnection.            getResponseMessage());

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!= -1){
                    char current = (char) data;
                    result +=current;
                    data = reader.read();

                }
                urlConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

    }
    //Download Task End


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
