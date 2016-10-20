package com.fcsh.lizzy.sensorinfo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private SensorManager sm;
    //需要两个Sensor
    private Sensor accSensor;
    private Sensor magSensor;
    private Sensor tmpSensor;
    private Sensor preSensor;

    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];

    private static final String TAG = "sensor";

    private TextView sesorTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sesorTxt = (TextView)findViewById(R.id.sesorTxt);

        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        tmpSensor = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        preSensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);

        sm.registerListener(myListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(myListener, magSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(myListener, tmpSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(myListener, preSensor,SensorManager.SENSOR_DELAY_NORMAL);
        //更新显示数据的方法
        calculateOrientation();

    }
    //再次强调：注意activity暂停的时候释放
    public void onPause(){
        sm.unregisterListener(myListener);
        super.onPause();
    }


    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {

            switch (sensorEvent.sensor.getType())
            {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magneticFieldValues = sensorEvent.values;
                    calculateOrientation();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    accelerometerValues = sensorEvent.values;
                    calculateOrientation();
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    sesorTxt.setText("温度计："+ sensorEvent.values[0] );
                    break;
                case Sensor.TYPE_PRESSURE:
                    sesorTxt.setText("气压计："+ sensorEvent.values[0]);
                    break;

            }
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };


    private  void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        // 要经过一次数据格式的转换，转换为度
        values[0] = (float) Math.toDegrees(values[0]);
        Log.i(TAG, values[0]+"");

        if(values[0] >= -5 && values[0] < 5){
            sesorTxt.setText( "正北");
        }
        else if(values[0] >= 5 && values[0] < 85){
            sesorTxt.setText( "东北");
        }
        else if(values[0] >= 85 && values[0] <=95){
            sesorTxt.setText( "正东");
        }
        else if(values[0] >= 95 && values[0] <175){
            sesorTxt.setText("东南");
        }
        else if((values[0] >= 175 && values[0] <= 180) || (values[0]) >= -180 && values[0] < -175){
            sesorTxt.setText( "正南");
        }
        else if(values[0] >= -175 && values[0] <-95){
            sesorTxt.setText( "西南");
        }
        else if(values[0] >= -95 && values[0] < -85){
            sesorTxt.setText( "正西");
        }
        else if(values[0] >= -85 && values[0] <-5){
            sesorTxt.setText( "西北");
        }
    }


}