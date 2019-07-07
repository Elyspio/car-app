package fr.elyspio.carapp.test;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import fr.elyspio.carapp.R;
import fr.elyspio.carapp.model.sensors.exceptions.UndefinedDelayException;
import fr.elyspio.carapp.model.sensors.exceptions.UndefinedSensorException;
import fr.elyspio.carapp.model.sensors.hardwares.Accelerator;
import fr.elyspio.carapp.model.sensors.hardwares.SensorBuilder;
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
                    .delay(SensorManager.SENSOR_DELAY_FASTEST)
                    .sensor(Sensor.TYPE_LINEAR_ACCELERATION)
                    .log()
                    .threshold(0.01)
                    .build();
            Log.d(TAG, "onCreate: accelerator OK");

            AccelerationObserver obs = new AccelerationObserver();
            accelerator.addObserver(obs);


        } catch (UndefinedSensorException | UndefinedDelayException e) {
            e.printStackTrace();
        }


    }


}
