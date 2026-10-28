package vaz.sensor_monitoring.strategy;

import vaz.sensor_monitoring.model.Measurement;

import java.util.List;

public class MaxTemperatureCalculation implements StatisticStrategy{
    @Override
    public double calculate(List<Measurement> measurements) {

        return measurements.stream()
                .mapToDouble(Measurement::getTemperature)
                .max().orElse(0.0);
    }
}
