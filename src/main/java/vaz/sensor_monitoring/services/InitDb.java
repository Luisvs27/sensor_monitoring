package vaz.sensor_monitoring.services;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import vaz.sensor_monitoring.model.Sensor;
import vaz.sensor_monitoring.repository.SensorRepository;

import java.util.List;

@Component
public class InitDb implements ApplicationRunner {

    private final ObjectMapper mapper;
    private final SensorRepository sensorRepository;

    public InitDb(ObjectMapper mapper, SensorRepository sensorRepository) {
        this.mapper = mapper;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        ClassPathResource path = new ClassPathResource("sensor-data.json");

        List<Sensor> sensors = mapper
                .readerForListOf(Sensor.class)
                .readValue(path.getInputStream());

        for (Sensor sensor : sensors) {
            sensor.getMeasurements()
                    .forEach(m -> m.setSensor(sensor));
        }
        sensorRepository.saveAll(sensors);

    }
}
