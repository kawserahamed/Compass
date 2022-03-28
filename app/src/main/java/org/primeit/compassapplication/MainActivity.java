package org.primeit.compassapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.primeit.compassapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    String TAG = "MainActivity";

    ActivityMainBinding binding;

    private static SensorManager sensorManager;
    private static Sensor accelerometerSensor;
    private static Sensor magnetometerSensor;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];

    boolean isLastAccelerometerArrayCopied = false;
    boolean isLastMagnetometerArrayCopied = false;

    long lastUpdatedTime = 0;
    float currentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometerSensor) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            isLastAccelerometerArrayCopied = true;
            Log.d(TAG, "isLastAccelerometerArrayCopied : " + true);
        } else if (event.sensor == magnetometerSensor) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            isLastMagnetometerArrayCopied = true;
            Log.d(TAG, "isLastMagnetometerArrayCopied : " + true);
        }

        if (isLastAccelerometerArrayCopied && isLastMagnetometerArrayCopied && System.currentTimeMillis() - lastUpdatedTime > 250) {
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix, orientation);

            float azimuthInRadians = orientation[0];
            float azimuthInDegree = (float) (Math.toDegrees(orientation[0])+360)%360;

            Log.d(TAG, "azimuthInRadians : " + azimuthInRadians);

            RotateAnimation rotateAnimation = new RotateAnimation(currentDegree, -azimuthInDegree,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);
            binding.compass.startAnimation(rotateAnimation);
            currentDegree = -azimuthInDegree;
            lastUpdatedTime = System.currentTimeMillis();

            int x = (int) azimuthInDegree;

            binding.textDegree.setText(x + "°");

        } else {
            Log.d(TAG, "else condition");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, magnetometerSensor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about:
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}