package vaz.sensor_monitoring.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {

    @Id
    @Column(name = "measurement_id")
    private Long id;

    private double temperature;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date recordedAt;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    @JsonBackReference
    private Sensor sensor;

}
