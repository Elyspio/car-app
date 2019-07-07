package fr.elyspio.carapp;

import android.hardware.Sensor;

import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.elyspio.carapp.model.sensors.Accelerator;
import fr.elyspio.carapp.model.sensors.SensorBuilder;
import fr.elyspio.carapp.model.sensors.exceptions.DelayException;
import fr.elyspio.carapp.model.sensors.exceptions.UndefinedSensorException;
import fr.elyspio.carapp.model.sensors.observers.AccelerationObserver;

public class MainActivity extends AppCompatActivity {


    private String TAG = "carApp";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Accelerator accelerator = (Accelerator) new SensorBuilder(this)
                    .delay(SensorManager.SENSOR_DELAY_NORMAL)
                    .sensor(Sensor.TYPE_ACCELEROMETER)
                    .log()
                    .calibrationIteration(100)
                    .build();


            AccelerationObserver obs = new AccelerationObserver();
            accelerator.addObserver(obs);


        } catch (UndefinedSensorException | DelayException e) {
            e.printStackTrace();
        }


    }


}
