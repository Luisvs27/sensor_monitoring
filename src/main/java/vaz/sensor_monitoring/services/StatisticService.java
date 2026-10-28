package vaz.sensor_monitoring.services;

import lombok.Setter;
import org.springframework.stereotype.Service;
import vaz.sensor_monitoring.model.Measurement;
import vaz.sensor_monitoring.strategy.StatisticStrategy;

import java.util.List;


@Service
public class StatisticService {

    private StatisticStrategy strategy;

    public void setStrategy(StatisticStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(List<Measurement> measurements) {

        return strategy.calculate(measurements);
    }
}
