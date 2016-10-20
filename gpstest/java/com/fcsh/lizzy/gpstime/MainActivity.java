package com.fcsh.lizzy.gpstime;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private String TAG = "MainActivity";
    LocationManager locMan;
    Location networkLocation = null;
    Button btGetTime;
    TextView timeTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btGetTime = (Button) findViewById(R.id.bt_get_time);
        timeTxt = (TextView) findViewById(R.id.textView);
        //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationManager locMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); //获取当前时间
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,  1000, 0, this);
        }
        catch ( SecurityException e)
        {
            Log.e(TAG, e.toString());
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        long time = location.getTime();
        Date date = new Date(time);
        timeTxt.setText(time + " NETWORK_PROVIDER " + date);
        //System.out.println(time + " NETWORK_PROVIDER " + date);
        networkLocation = location;
    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}