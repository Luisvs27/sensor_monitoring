package vaz.sensor_monitoring.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vaz.sensor_monitoring.controller.ManageController;
import vaz.sensor_monitoring.model.Measurement;
import vaz.sensor_monitoring.model.Sensor;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ManageController.class)
class SensorServiceTest {

    @MockitoBean
    private SensorService sensorService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllSensorsWithMinTemp() throws Exception {
        Measurement m1 = new Measurement(1L, 21.5, new Date(), null);
        Measurement m2 = new Measurement(2L, 22.0, new Date(), null);
        Sensor sensor = new Sensor(
                1L,
                "S-001",
                "Sensor 1",
                "Room A",
                true,
                List.of(m1, m2)
        );

        when(sensorService.getAllSensorsWithMinTemp(2)).thenReturn(Collections.singletonList(sensor));

        mockMvc.perform(get("/monitor/sensors").param("minMeasurements", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Sensor 1")))
                .andExpect(jsonPath("$[0].measurements", hasSize(2)));
    }
}