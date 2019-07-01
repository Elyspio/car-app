package fr.elyspio.carapp.model.sensors.exceptions;

public class DelayException extends Exception {
    public DelayException(int delay) {
        super("Undefined sensor delay : " + delay + "\nSee class SensorManager");
    }
}
