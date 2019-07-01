package fr.elyspio.carapp.model.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public abstract class AbstractSensor {

    protected SensorManager manager;
    protected Sensor sensor;
    protected Context ctx;
    protected int delay;
    protected float[] offset;


    public AbstractSensor(Context ctx, int sensorType) {
        this(ctx, sensorType, SensorManager.SENSOR_DELAY_FASTEST);
    }


    public AbstractSensor(Context ctx, int sensorType, int refreshRate) {
        this.ctx = ctx;
        delay = refreshRate;
        manager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(sensorType);
        offset = new float[3];
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
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        });
    }
    public boolean register(SensorEventListener listener) {
        return manager.registerListener(listener, sensor, delay);
    }

    public void unregister(SensorEventListener listener) {
        manager.unregisterListener(listener);
    }

}
