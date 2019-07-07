package fr.elyspio.carapp.model.sensors.observers;

import java.util.List;

public interface Sensorable {

    void updateSensor(List<Double> data);

}
