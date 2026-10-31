package com.canchas.estadisticasms;

import com.canchas.estadisticasms.dto.MetricaRequestDTO;
import com.canchas.estadisticasms.dto.MetricaResponseDTO;
import com.canchas.estadisticasms.exception.MetricaException;
import com.canchas.estadisticasms.model.MetricaDiaria;
import com.canchas.estadisticasms.repository.MetricaDiariaRepository;
import com.canchas.estadisticasms.service.MetricaDiariaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadisticasmsApplicationTests {

    @Mock
    private MetricaDiariaRepository repository;

    @InjectMocks
    private MetricaDiariaService service;

    @Test
    void testModelMetricaDiariaYCalculoEfectividad() {
        MetricaDiaria metrica = new MetricaDiaria();
        LocalDate hoy = LocalDate.now();

        metrica.setId(1L);
        metrica.setFecha(hoy);
        metrica.setTotalReservasCreadas(10);
        metrica.setTotalReservasPagadas(8);
        metrica.setTotalReservasCanceladas(2);
        metrica.setRecaudacionTotal(new BigDecimal("150000.00"));

        assertEquals(1L, metrica.getId());
        assertEquals(hoy, metrica.getFecha());
        assertEquals(10, metrica.getTotalReservasCreadas());
        assertEquals(8, metrica.getTotalReservasPagadas());
        assertEquals(2, metrica.getTotalReservasCanceladas());
        assertEquals(new BigDecimal("150000.00"), metrica.getRecaudacionTotal());

        // Cubre logica de negocio en modelo
        assertEquals(80.0, metrica.calcularPorcentajeEfectividad());

        MetricaDiaria vacia = new MetricaDiaria();
        assertEquals(0.0, vacia.calcularPorcentajeEfectividad());
    }

    @Test
    void testMetricaRequestDTO() {
        MetricaRequestDTO dto = new MetricaRequestDTO();
        LocalDate hoy = LocalDate.now();

        dto.setFecha(hoy);
        dto.setTotalReservas(12);
        dto.setIngresosTotales(new BigDecimal("180000.00"));
        dto.setCanchasActivas(4);
        dto.setTotalReservasCreadas(10);
        dto.setTotalReservasPagadas(8);
        dto.setTotalReservasCanceladas(2);
        dto.setRecaudacionTotal(new BigDecimal("150000.00"));

        assertEquals(hoy, dto.getFecha());
        assertEquals(12, dto.getTotalReservas());
        assertEquals(new BigDecimal("180000.00"), dto.getIngresosTotales());
        assertEquals(4, dto.getCanchasActivas());
        assertEquals(10, dto.getTotalReservasCreadas());
        assertEquals(8, dto.getTotalReservasPagadas());
        assertEquals(2, dto.getTotalReservasCanceladas());
        assertEquals(new BigDecimal("150000.00"), dto.getRecaudacionTotal());
    }

    @Test
    void testMetricaResponseDTO() {
        LocalDate hoy = LocalDate.now();
        MetricaResponseDTO dto = new MetricaResponseDTO(
                1L,
                hoy,
                15,
                new BigDecimal("200000.00"),
                5
        );

        assertEquals(1L, dto.getId());
        assertEquals(hoy, dto.getFecha());
        assertEquals(15, dto.getTotalReservas());
        assertEquals(new BigDecimal("200000.00"), dto.getIngresosTotales());
        assertEquals(5, dto.getCanchasActivas());
    }

    @Test
    void testMetricaException() {
        MetricaException ex = new MetricaException("Error de metricas");
        assertEquals("Error de metricas", ex.getMessage());
    }

    @Test
    void testServiceRegistrarNuevaMetrica() {
        LocalDate hoy = LocalDate.now();
        MetricaRequestDTO request = new MetricaRequestDTO();
        request.setFecha(hoy);
        request.setTotalReservas(5);
        request.setIngresosTotales(new BigDecimal("50000.00"));

        MetricaDiaria guardada = new MetricaDiaria();
        guardada.setId(10L);
        guardada.setFecha(hoy);
        guardada.setTotalReservasCreadas(5);
        guardada.setRecaudacionTotal(new BigDecimal("50000.00"));

        when(repository.findByFecha(hoy)).thenReturn(Optional.empty());
        when(repository.save(any(MetricaDiaria.class))).thenReturn(guardada);

        MetricaResponseDTO respuesta = service.registrarOActualizar(request);

        assertNotNull(respuesta);
        assertEquals(10L, respuesta.getId());
        assertEquals(5, respuesta.getTotalReservas());
    }

    @Test
    void testServiceListarTodas() {
        MetricaDiaria m = new MetricaDiaria();
        m.setId(1L);
        m.setFecha(LocalDate.now());
        m.setTotalReservasCreadas(3);
        m.setRecaudacionTotal(BigDecimal.TEN);

        when(repository.findAll()).thenReturn(List.of(m));

        List<MetricaResponseDTO> lista = service.listarTodas();
        assertEquals(1, lista.size());
    }
}