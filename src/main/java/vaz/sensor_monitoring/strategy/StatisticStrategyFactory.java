package vaz.sensor_monitoring.strategy;

import org.springframework.stereotype.Component;

@Component
public class StatisticStrategyFactory {

    public StatisticStrategy createFromString(String option){

        return switch (option) {
            case "average" -> new AverageTemperatureCalculation();
            case "max" -> new MaxTemperatureCalculation();
            default -> throw new IllegalStateException("Unexpected value: " + option);
        };
    }

}
