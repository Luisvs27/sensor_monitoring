package vaz.sensor_monitoring.strategy;

import vaz.sensor_monitoring.model.Measurement;

import java.util.List;

public interface StatisticStrategy {

    public double calculate(List<Measurement> measurements);
}
