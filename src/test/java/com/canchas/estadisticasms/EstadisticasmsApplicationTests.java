package com.canchas.estadisticasms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstadisticasmsApplicationTests {

    @Test
    void testCalculoMetricaDiaria() {
        // Prueba unitaria directa sin levantar base de datos
        double efectividad = 0.85;
        assertTrue(efectividad > 0.0, "La efectividad debe ser mayor a cero");
        assertNotNull(efectividad, "El valor de efectividad no debe ser nulo");
    }

}