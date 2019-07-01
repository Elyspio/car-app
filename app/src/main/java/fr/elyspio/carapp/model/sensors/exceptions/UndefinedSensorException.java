package fr.elyspio.carapp.model.sensors.exceptions;

public class UndefinedSensorException extends Exception {

    public UndefinedSensorException() {
        super("No sensor type provided");
    }

    public UndefinedSensorException(int sensor) {
        super("Undefined sensor type: " + sensor);
    }
}
