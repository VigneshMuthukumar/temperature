package com.example.vignesh.temperature;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import com.example.vignesh.*;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;




public class MainActivity extends AppCompatActivity {

    private Thermometer thermometer;
    private float temperature;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thermometer = (Thermometer) findViewById(R.id.thermometer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        simulateAmbientTemperature();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterAll();
    }

    private void simulateAmbientTemperature() {
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Random random = new Random();
                temperature = random.nextInt(32)+(-10);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(temperature >= 5){
                            sendAlert();
                        }
                        thermometer.setCurrentTemp(temperature);
                        getSupportActionBar().setTitle(getString(R.string.app_name) + " : " + temperature);
                    }
                });
            }
        }, 0, 4500);
    }
    public void sendAlert(){
        Toast.makeText(getApplicationContext(), "Exceeded Temperature, Trying to send MESSAGE",
                Toast.LENGTH_SHORT).show();
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.SEND_SMS}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 0:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("9080816659", null,
                            "Warning ! Temperate Exceeded 10'C", null, null);
            }
        }
    }
    private void unregisterAll() {
        timer.cancel();
    }

}
