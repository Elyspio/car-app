package fr.elyspio.carapp.model.sensors.hardwares;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.elyspio.carapp.model.sensors.observers.Sensorable;


public abstract class AbstractSensor {

    private SensorManager manager;
    private Sensor sensor;
    private Context ctx;
    private int delay;
    private double[] offset;
    private Set<Sensorable> observers;
    private boolean calibrated;
    private static String TAG = "AbstractSensor";
    private double[] threshold;


    public AbstractSensor(Context ctx, int sensorType, double[] threshold) {
        this(ctx, sensorType, SensorManager.SENSOR_DELAY_FASTEST, threshold);
    }


    AbstractSensor(Context ctx, int sensorType, int refreshRate, double[] threshold) {
        this.ctx = ctx;
        this.threshold = threshold;
        delay = refreshRate;
        manager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(sensorType);
        observers = new HashSet<>();
        calibrated = false;
        AbstractSensor self = this;
        this.register(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (self.calibrated) {
                    List<Double> values = new ArrayList<>();
                    float[] floats = sensorEvent.values;
                    boolean signifiant = false;
                    for (int i = 0; i < floats.length; i++) {
                        double val = floats[i] - offset[i];
                        if (Math.abs(val) > self.threshold[i]) {
                            signifiant = true;
                        }
                        values.add(val);
                    }
                    if (signifiant) {
                        self.notifySensorUpdate(values);
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        });

    }


    protected void calibrate(final int nbData, final int iterations) {
        offset = new double[nbData];
        Log.d(TAG, "calibrate THRES length: " + threshold.length + "  " + threshold[0]);
        if (this.threshold.length == 1) {
            double value = threshold[0];
            this.threshold = new double[nbData];
            for (int i = 0; i < nbData; i++) {
                this.threshold[i] = value;
            }
        }
        Log.d(TAG, String.format("calibrate: nb:%d for %d iterations", nbData, iterations));
        final float[] data = new float[nbData + 1];
        for (int i = 0; i < nbData + 1; i++) {
            data[i] = 0;
        }
        final AbstractSensor self = this;

        this.register(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.d(TAG, "onSensorChanged: New Data");
                if (!calibrated) {
                    if (data.length + 1 == event.values.length) {
                        for (int i = 0; i < data.length; i++) {
                            data[i] += event.values[i];
                        }
                        data[data.length - 1] += 1;
                    } else {
                        Log.e(TAG, "checkCalibration: error no same length for new and past data");
                    }

                } else {
                    Log.e(TAG, "checkCalibration: sensor is already calibrated");
                }
                data[nbData] += 1;
                Log.d(TAG, String.format("onSensorChanged: calibated: %s/%d", data[nbData], iterations));
                if (data[nbData] == iterations) {
                    self.unregister(this);
                    for (int i = 0; i < nbData; i++) {
                        self.offset[i] = data[i] / iterations;
                    }
                    Log.d(TAG, "onSensorChanged: calibration done");
                    self.calibrated = true;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        });
    }


    private boolean register(SensorEventListener listener) {
        return manager.registerListener(listener, sensor, delay);
    }

    private void unregister(SensorEventListener listener) {
        manager.unregisterListener(listener);
    }


    public void removeObserver(Sensorable observer) {
        this.observers.remove(observer);
    }

    public void addObserver(Sensorable observer) {
        this.observers.add(observer);
    }

    public void notifySensorUpdate(List<Double> data) {
        observers.forEach(obs -> obs.updateSensor(data));
    }
}
