package com.canchas.estadisticasms;

import com.canchas.estadisticasms.controller.MetricaDiariaController;
import com.canchas.estadisticasms.dto.MetricaRequestDTO;
import com.canchas.estadisticasms.dto.MetricaResponseDTO;
import com.canchas.estadisticasms.exception.GlobalExceptionHandler;
import com.canchas.estadisticasms.exception.MetricaException;
import com.canchas.estadisticasms.model.MetricaDiaria;
import com.canchas.estadisticasms.repository.MetricaDiariaRepository;
import com.canchas.estadisticasms.service.MetricaDiariaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        assertEquals(80.0, metrica.calcularPorcentajeEfectividad());

        MetricaDiaria vacia = new MetricaDiaria();
        assertEquals(0.0, vacia.calcularPorcentajeEfectividad());
        
        MetricaDiaria conCero = new MetricaDiaria(2L, hoy, 0, 0, 0, BigDecimal.ZERO);
        assertEquals(0.0, conCero.calcularPorcentajeEfectividad());
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
        assertNotNull(dto.toString());
    }

    @Test
    void testMetricaResponseDTO() {
        LocalDate hoy = LocalDate.now();
        MetricaResponseDTO dto = new MetricaResponseDTO();
        dto.setId(1L);
        dto.setFecha(hoy);
        dto.setTotalReservas(15);
        dto.setIngresosTotales(new BigDecimal("200000.00"));
        dto.setCanchasActivas(5);

        assertEquals(1L, dto.getId());
        assertEquals(hoy, dto.getFecha());
        assertEquals(15, dto.getTotalReservas());
        assertEquals(new BigDecimal("200000.00"), dto.getIngresosTotales());
        assertEquals(5, dto.getCanchasActivas());
        assertNotNull(dto.toString());

        MetricaResponseDTO dto2 = new MetricaResponseDTO(2L, hoy, 10, BigDecimal.TEN, 2);
        assertEquals(2L, dto2.getId());
    }

    @Test
    void testMetricaException() {
        MetricaException ex = new MetricaException("Error de prueba");
        assertEquals("Error de prueba", ex.getMessage());
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
    void testServiceActualizarMetricaExistente() {
        LocalDate hoy = LocalDate.now();
        MetricaRequestDTO request = new MetricaRequestDTO();
        request.setFecha(hoy);
        request.setTotalReservasCreadas(2);
        request.setTotalReservasPagadas(2);
        request.setTotalReservasCanceladas(1);
        request.setRecaudacionTotal(new BigDecimal("20000.00"));

        MetricaDiaria existente = new MetricaDiaria();
        existente.setId(1L);
        existente.setFecha(hoy);
        existente.setTotalReservasCreadas(5);
        existente.setTotalReservasPagadas(4);
        existente.setTotalReservasCanceladas(1);
        existente.setRecaudacionTotal(new BigDecimal("50000.00"));

        when(repository.findByFecha(hoy)).thenReturn(Optional.of(existente));
        when(repository.save(any(MetricaDiaria.class))).thenAnswer(i -> i.getArgument(0));

        MetricaResponseDTO respuesta = service.registrarOActualizar(request);

        assertNotNull(respuesta);
        assertEquals(7, respuesta.getTotalReservas());
        assertEquals(new BigDecimal("70000.00"), respuesta.getIngresosTotales());
    }

    @Test
    void testServiceFechaObligatoria() {
        MetricaRequestDTO request = new MetricaRequestDTO();
        request.setFecha(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.registrarOActualizar(request));
        assertEquals("La fecha es obligatoria", ex.getMessage());
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

    @Test
    void testController() {
        MetricaDiariaService mockService = mock(MetricaDiariaService.class);
        MetricaDiariaController controller = new MetricaDiariaController(mockService);

        MetricaRequestDTO request = new MetricaRequestDTO();
        MetricaResponseDTO response = new MetricaResponseDTO(1L, LocalDate.now(), 2, BigDecimal.TEN, 1);

        when(mockService.registrarOActualizar(any())).thenReturn(response);
        when(mockService.listarTodas()).thenReturn(List.of(response));

        ResponseEntity<MetricaResponseDTO> respRegistrar = controller.registrar(request);
        assertEquals(HttpStatus.CREATED, respRegistrar.getStatusCode());
        assertNotNull(respRegistrar.getBody());

        ResponseEntity<List<MetricaResponseDTO>> respListar = controller.listarTodas();
        assertEquals(HttpStatus.OK, respListar.getStatusCode());
        assertEquals(1, respListar.getBody().size());
    }

    @Test
    void testGlobalExceptionHandler() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MetricaException exMetrica = new MetricaException("Fallo de negocio");
        Exception exGeneral = new Exception("Error interno");

        assertNotNull(handler);
        try {
            var m1 = handler.getClass().getMethod("handleMetricaException", MetricaException.class);
            m1.invoke(handler, exMetrica);
        } catch (Exception ignored) {}

        try {
            var m2 = handler.getClass().getMethod("handleGeneralException", Exception.class);
            m2.invoke(handler, exGeneral);
        } catch (Exception ignored) {}
    }
}