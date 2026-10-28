package vaz.sensor_monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vaz.sensor_monitoring.model.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}
