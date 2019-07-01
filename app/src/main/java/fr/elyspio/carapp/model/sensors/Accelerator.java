package fr.elyspio.carapp.model.sensors;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;

public class Accelerator extends AbstractSensor {


    public Accelerator(Context ctx, int delay, int calibrationIteration) {
        super(ctx, Sensor.TYPE_ACCELEROMETER, delay);
        calibrate(calibrationIteration);
    }

    private void calibrate(int iterations) {
        super.calibrate(3, iterations);
    }


    @Override
    public boolean register(SensorEventListener listener) {
        return super.register(listener);
    }
}
