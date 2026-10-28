package vaz.sensor_monitoring.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vaz.sensor_monitoring.model.Measurement;
import vaz.sensor_monitoring.model.Sensor;
import vaz.sensor_monitoring.services.SensorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/monitor")
public class ManageController {

    private final SensorService sensorService;

    public ManageController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/sensors")
    public ResponseEntity<List<Sensor>> getAllSensorsWithMinTemp(
            @RequestParam(
                    value = "minMeasurements",
                    defaultValue = "0",
                    required = false) Integer minMeasurements)
    {
        List<Sensor> sensors = sensorService
                .getAllSensorsWithMinTemp(minMeasurements);

        if (sensors.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/sensors/{sensorId}/measurements")
    public ResponseEntity<List<Measurement>> getAllMeasurementSorted(
            @PathVariable Long sensorId)
    {
        List<Measurement> measurementsFound = sensorService
                .getAllMeasurementSorted(sensorId);

        if (measurementsFound.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(measurementsFound);
    }

    @PostMapping("/sensors")
    public ResponseEntity<Sensor> createSensor(
            @RequestBody Sensor sensor
    ) {
        Sensor sensorCreated = sensorService.createSensor(sensor);

        if (!sensorCreated.getMeasurements().isEmpty()) return ResponseEntity
                .badRequest().build();

        return ResponseEntity.ok(sensorCreated);
    }
    @PostMapping("/sensors/{sensorId}/measurements")
    public ResponseEntity<Measurement> createMeasurement(
            @PathVariable Long sensorId,
            @RequestBody Measurement measurement)
    {
        if (sensorId == null) return ResponseEntity.notFound().build();

        return ResponseEntity.status(201).body(sensorService
                .createMeasurement(sensorId,measurement));
    }

    @PatchMapping("/sensors/{sensorId}")
    public ResponseEntity<Optional<Sensor>> updateSensorInfos(
            @PathVariable Long sensorId,
            @RequestBody Sensor sensor)
    {
        Optional<Sensor> foundSensor =
                sensorService.updateSensorInfos(
                        sensorId,
                        sensor.getName(),
                        sensor.getLocation(),
                        sensor.getActive());

        if (foundSensor.isPresent()) return ResponseEntity
                .status(200).body(foundSensor);

        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/sensors/{sensorId}/measurements/{measurementId}")
    public ResponseEntity<Void> deleteMeasurement(
            @PathVariable Long sensorId,
            @PathVariable Long measurementId
            )
    {
        boolean measurementDeleted = sensorService
                .deleteMeasurement(sensorId, measurementId);

        if (!measurementDeleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sensors/{sensorId}/stat")
    public ResponseEntity<Double> OptionCalculate(
            @PathVariable Long sensorId,
            @RequestParam(
                    value = "option",
                    defaultValue = "average",
                    required = false) String option)
    {
        Optional<Sensor> sensorFound = sensorService.findSensorById(sensorId);

        if (sensorFound.isPresent()) {
            double result = sensorService.OptionCalculate(sensorId, option);
            return ResponseEntity.ok(result);
        } else {
           return ResponseEntity.notFound().build();
        }

    }


}
