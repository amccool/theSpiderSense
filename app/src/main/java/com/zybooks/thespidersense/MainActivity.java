package com.zybooks.thespidersense;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    private static final String TAG = MainActivity.class.getName();
    private static final String TEMP = "temp-sensor";
    private static final float LOW_TEMP = 0.00F;
    private static final float HIGH_TEMP = 50.00F;

    private SensorManager mSensorManager;
    private Sensor mTempSensor;

    private TextView howHotTextView;
    private TextView currentTempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTempTextView = findViewById(R.id.currentTempTextView);
        howHotTextView = findViewById(R.id.howHotIsItTextView);



        //look at what sensors are available
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : deviceSensors) {
            Log.d(TAG, "Sensor: " + sensor.getName() + " - " + sensor.getType());
        }

        //check the detail of temp sensor
        mTempSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTempSensor  == null) {
            // No ambient temp sensor on this device
            Log.d(TEMP, "No ambient temperature sensor");

            //set the text views to report no temperature sensor
            currentTempTextView.setText("no sensor");
            howHotTextView.setText("undefined");

        }
        else {
            // Find out about this sensor
            Log.d(TEMP, "Vendor: " + mTempSensor.getVendor());
            Log.d(TEMP, "Version: " + mTempSensor.getVersion());
            Log.d(TEMP, "Min delay: " + mTempSensor.getMinDelay());


//            currentTempTextView.setText(String.valueOf(tempCelsius));
//
//            if(tempCelsius < LOW_TEMP) {
//                howHotTextView.setText("SO COLD!!!!");
//            } else if (tempCelsius > HIGH_TEMP) {
//                howHotTextView.setText("@@ SMOKING @@");
//            } else {
//                howHotTextView.setText("goldilocks");
//            }
        }
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mTempSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float tempCelsius = sensorEvent.values[0];
            Log.d(TEMP, "tempCelsius = " + tempCelsius);

            currentTempTextView.setText(String.valueOf(tempCelsius));
            
            if(tempCelsius < LOW_TEMP) {
                howHotTextView.setText("SO COLD!!!!");
            } else if (tempCelsius > HIGH_TEMP) {
                howHotTextView.setText("@@ SMOKING @@");
            } else {
                howHotTextView.setText("goldilocks");
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            Log.d(TEMP, "accuracy = " + accuracy);
        }
    }
}