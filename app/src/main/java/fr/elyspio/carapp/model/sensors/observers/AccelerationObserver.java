package fr.elyspio.carapp.model.sensors.observers;

import android.util.Log;

import java.util.List;

public class AccelerationObserver implements Sensorable {

    private final static String TAG = "AccelationObserver";

    @Override
    public void updateSensor(List<Double> data) {
        double x = data.get(0);
        double y = data.get(1);
        double z = data.get(2);

        Log.d(TAG, "X:" + x);
        Log.d(TAG, "Y:" + y);
        Log.d(TAG, "Z:" + z);
    }
}
