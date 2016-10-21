package com.fcsh.lizzy.gpstime;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private String TAG = "MainActivity";
    LocationManager locMan;
    Button btUpdate;
    Button btStop;
    TextView timeTxt;
    CheckBox ckbGps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btUpdate = (Button) findViewById(R.id.bt_update);
        btStop = (Button) findViewById(R.id.bt_stop);
        timeTxt = (TextView) findViewById(R.id.textView);
        ckbGps = (CheckBox) findViewById(R.id.ckb_gps);
        locMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        btUpdate.setOnClickListener(handler);
        btStop.setOnClickListener(handler);
        ckbGps.setOnCheckedChangeListener(checkHandler);
    }

    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_update:
                    try {
                        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, MainActivity.this);
                    } catch (SecurityException e) {
                        Log.e(TAG, e.toString());
                    }

                    break;
                case R.id.bt_stop:
                    try {
                        locMan.removeUpdates(MainActivity.this);
                    } catch (SecurityException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener checkHandler = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                try {
                    locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, MainActivity.this);
                } catch (SecurityException e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                try {
                    locMan.removeUpdates(MainActivity.this);
                } catch (SecurityException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
    };


    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        long time = location.getTime();
        Date date = new Date(time);
        timeTxt.setText(date.toString());
        //System.out.println(time + " NETWORK_PROVIDER " + date);

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