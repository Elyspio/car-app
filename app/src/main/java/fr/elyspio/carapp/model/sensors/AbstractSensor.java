package fr.elyspio.carapp.model.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public AbstractSensor(Context ctx, int sensorType) {
        this(ctx, sensorType, SensorManager.SENSOR_DELAY_FASTEST);
    }


    AbstractSensor(Context ctx, int sensorType, int refreshRate) {
        this.ctx = ctx;
        delay = refreshRate;
        manager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(sensorType);
        offset = new double[3];
        observers = new HashSet<>();
        calibrated = false;
        AbstractSensor self = this;
        this.register(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(self.calibrated) {
                    List<Double> values = new ArrayList<>();
                    float[] floats = sensorEvent.values;
                    for (int i = 0; i < floats.length; i++) {
                        double val = floats[i] - offset[i];
                        values.add(val);
                    }
                    self.notifySensorUpdate(values);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        });

    }


    protected void calibrate(final int nbData, final int iterations) {
        final float[] data = new float[nbData + 1];
        for (int i = 0; i < nbData + 1; i++) {
            data[i] = 0;
        }
        final AbstractSensor self = this;
        this.register(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                for (int i = 0; i < event.values.length; i++) {
                    data[i] += event.values[i];
                }
                data[nbData] = data[nbData]++;
                if (data[nbData] == iterations) {
                    self.unregister(this);
                    for (int i = 0; i < nbData; i++) {
                        self.offset[i] = data[i] / nbData;
                    }
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
