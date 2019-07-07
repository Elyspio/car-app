package fr.elyspio.carapp.model.sensors.hardwares;


import android.content.Context;
import android.hardware.Sensor;

public class Accelerator extends AbstractSensor {


    public Accelerator(Context ctx, int delay, int calibrationIteration, double[] threshold) {
        super(ctx, Sensor.TYPE_LINEAR_ACCELERATION, delay, threshold);
        calibrate(calibrationIteration);
    }

    private void calibrate(int iterations) {
        super.calibrate(3, iterations);
    }


}
