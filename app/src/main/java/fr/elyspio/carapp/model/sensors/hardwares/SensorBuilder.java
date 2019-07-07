package fr.elyspio.carapp.model.sensors.hardwares;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import fr.elyspio.carapp.model.sensors.exceptions.UndefinedDelayException;
import fr.elyspio.carapp.model.sensors.exceptions.UndefinedSensorException;


public class SensorBuilder {

    private final static String TAG = "SensorBuider";
    private Context ctx;
    private boolean log;
    private Integer delay;
    private Integer type;
    private Integer calibrationIteration;
    private double[] threshold;

    /**
     * Builder to create CustomSensors with default delay: fastest and default calibration iterations: 10.
     *
     * @param ctx Context of the app.
     */
    public SensorBuilder(Context ctx) {
        this.ctx = ctx;
        log = false;
        delay = SensorManager.SENSOR_DELAY_FASTEST;
        type = null;
        calibrationIteration = 10;
        threshold = null;
    }


    /**
     * Make sensor automaticaly log his data.
     *
     * @return the current builder instance.
     */
    public SensorBuilder log() {
        this.log = true;
        return this;
    }

    public SensorBuilder threshold(double... threshold) {
        this.threshold = threshold;
        return this;
    }

    public SensorBuilder calibrationIteration(int iterations) {
        if (iterations > 0) {
            this.calibrationIteration = iterations;
        }
        return this;
    }

    /**
     * Set the type of the future sensor (Accelerometer, etc.)
     *
     * @param sensorType the type.
     * @return the current builder instance.
     * @throws UndefinedSensorException      if the type is not defined
     * @throws UnsupportedOperationException if the type is not implemented
     */
    public SensorBuilder sensor(int sensorType) throws UndefinedSensorException {
        int[] possibleSensors = new int[]{Sensor.TYPE_LINEAR_ACCELERATION};
        for (int type : possibleSensors) {
            if (sensorType == type) {
                this.type = sensorType;
                return this;
            }
        }
        throw new UndefinedSensorException(sensorType);
    }

    /**
     * Set the delay between each sensor update.
     *
     * @param newDelay the delay .
     * @return the current builder instance.
     * @throws UndefinedDelayException if the delay is not standard.
     */
    public SensorBuilder delay(int newDelay) throws UndefinedDelayException {
        int[] possibleDelays = new int[]{
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_GAME,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
        };

        for (int delay : possibleDelays) {
            if (newDelay == delay) {
                this.delay = newDelay;
                return this;
            }
        }

        throw new UndefinedDelayException(newDelay);

    }

    /**
     * Create a sensor from given arguments.
     *
     * @return the sensor.
     * @throws UndefinedSensorException      if the sensor type has not been defined.
     * @throws UnsupportedOperationException if the sensor type is not implemented yet.
     */
    public AbstractSensor build() throws UndefinedSensorException, UnsupportedOperationException {

        AbstractSensor sensor;

        if (type == null) {
            throw new UndefinedSensorException();
        }

        final double[] threshold;
        if (this.threshold != null) {
            threshold = this.threshold;
        } else {
            threshold = new double[]{0};
        }


        switch (type) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                sensor = new Accelerator(ctx, delay, calibrationIteration, threshold);
                break;

            default:
                throw new UnsupportedOperationException("type: " + type);
        }

        if (log) {
            sensor.addObserver(data -> {
                for (int i = 0; i < data.size(); i++) {
                    double value = data.get(i);
                    Log.d(TAG, String.format("log: data[%s] = %s", i, value));
                }
            });
        }

        return sensor;
    }

}
