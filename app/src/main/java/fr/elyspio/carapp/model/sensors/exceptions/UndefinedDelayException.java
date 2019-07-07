package fr.elyspio.carapp.model.sensors.exceptions;

public class UndefinedDelayException extends Exception {
    public UndefinedDelayException(int delay) {
        super("Undefined sensor delay : " + delay + "\nSee class SensorManager");
    }
}
