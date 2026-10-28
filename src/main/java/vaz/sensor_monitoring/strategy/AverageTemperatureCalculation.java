package vaz.sensor_monitoring.strategy;

import vaz.sensor_monitoring.model.Measurement;

import java.util.List;

public class AverageTemperatureCalculation implements StatisticStrategy{
    @Override
    public double calculate(List<Measurement> measurements) {

        return measurements.stream()
                .mapToDouble(Measurement::getTemperature)
                .average().orElse(0.0);
    }
}
