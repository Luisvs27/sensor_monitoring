package vaz.sensor_monitoring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vaz.sensor_monitoring.model.Measurement;
import vaz.sensor_monitoring.model.Sensor;
import vaz.sensor_monitoring.repository.MeasurementRepository;
import vaz.sensor_monitoring.repository.SensorRepository;
import vaz.sensor_monitoring.strategy.StatisticStrategy;
import vaz.sensor_monitoring.strategy.StatisticStrategyFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;
    private final StatisticService statisticService;
    private final StatisticStrategyFactory strategyFactory;

    public SensorService(SensorRepository sensorRepository, MeasurementRepository measurementRepository, StatisticService statisticService, StatisticStrategyFactory strategyFactory) {
        this.sensorRepository = sensorRepository;
        this.measurementRepository = measurementRepository;
        this.statisticService = statisticService;
        this.strategyFactory = strategyFactory;
    }

    // REQUESTS
    public List<Sensor> getAllSensorsWithMinTemp (Integer minMess) {

        List<Sensor> sensors = sensorRepository.findAll();

        return sensors.stream()
                .filter(m -> m.getMeasurements().size() >= minMess)
                .toList();
    }

    public List<Measurement> getAllMeasurementSorted(Long sensorId) {

         Optional<Sensor> sensor = sensorRepository.findById(sensorId);

         if (sensor.isPresent()) return measurementRepository.findAll()
                .stream()
                .filter(m -> m.getSensor() != null)
                .peek(sensors -> sensors.setSensor(null))
                .sorted(Comparator.comparing(Measurement::getRecordedAt))
                .toList();

         else throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                 "sensor ID not found");
    }

    public Sensor createSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    public Measurement createMeasurement(Long sensorId, Measurement measurement){

        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND
                ,"sensor ID not found"));

        measurement.setSensor(sensor);

        return measurementRepository.save(measurement);
    }

    public Optional<Sensor> updateSensorInfos(
            Long sensorId,
            String name,
            String location,
            Boolean active)
    {
        Optional<Sensor> sensorFound = sensorRepository.findById(sensorId);

        if (sensorFound.isEmpty()) return Optional.empty();
        Sensor sensorUpdate = sensorFound.get();

        if (name != null) sensorUpdate.setName(name);
        if (location != null) sensorUpdate.setLocation(location);
        if (active != null) sensorUpdate.setActive(active);

        return Optional.of(sensorRepository.save(sensorUpdate));
    }

    public boolean deleteMeasurement (
            Long sensorId,
            Long messId
            )
    {
        boolean sensorFound = sensorRepository.existsById(sensorId);
        Measurement measurementToDelete =
                measurementRepository
                        .findById(messId)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "measurement not found"));

        if (!sensorFound) {
            return false;
        }
        if (!measurementToDelete.getSensor().getId().equals(sensorId)) {
            return false;
        }
        measurementRepository.delete(measurementToDelete);
        measurementRepository.flush();
        return true;
    }
    public Optional<Sensor> findSensorById(Long sensorId){
        return sensorRepository.findById(sensorId);
    }

    public double OptionCalculate (Long sensorId, String option) {

        Optional<Sensor> sensorFound = sensorRepository.findById(sensorId);

        List<Measurement> measurements = measurementRepository.findAll();

        if (sensorFound.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "id sensor not found");

        StatisticStrategy strategy = strategyFactory.createFromString(option);

        statisticService.setStrategy(strategy);

        double result = statisticService.calculate(measurements);

        return result;
    }



}
