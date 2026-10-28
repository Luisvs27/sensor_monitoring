package vaz.sensor_monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vaz.sensor_monitoring.model.Sensor;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {



}
