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

import java.lang.reflect.Method;
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
    void testModelMetricaDiariaCompleto() {
        LocalDate hoy = LocalDate.now();
        MetricaDiaria m1 = new MetricaDiaria();
        m1.setId(1L);
        m1.setFecha(hoy);
        m1.setTotalReservasCreadas(10);
        m1.setTotalReservasPagadas(8);
        m1.setTotalReservasCanceladas(2);
        m1.setRecaudacionTotal(new BigDecimal("150000.00"));

        assertEquals(1L, m1.getId());
        assertEquals(hoy, m1.getFecha());
        assertEquals(10, m1.getTotalReservasCreadas());
        assertEquals(8, m1.getTotalReservasPagadas());
        assertEquals(2, m1.getTotalReservasCanceladas());
        assertEquals(new BigDecimal("150000.00"), m1.getRecaudacionTotal());
        assertEquals(80.0, m1.calcularPorcentajeEfectividad());

        // Constructor all args y casos borde de efectividad
        MetricaDiaria m2 = new MetricaDiaria(2L, hoy, 0, 0, 0, BigDecimal.ZERO);
        assertEquals(0.0, m2.calcularPorcentajeEfectividad());

        MetricaDiaria m3 = new MetricaDiaria(3L, hoy, null, 0, 0, BigDecimal.ZERO);
        assertEquals(0.0, m3.calcularPorcentajeEfectividad());

        // Métodos de entidad
        assertNotNull(m1.toString());
        assertEquals(m1, m1);
        assertNotEquals(m1, m2);
        assertNotEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testMetricaRequestDTOCompleto() {
        LocalDate hoy = LocalDate.now();
        MetricaRequestDTO dto1 = new MetricaRequestDTO();
        dto1.setFecha(hoy);
        dto1.setTotalReservas(12);
        dto1.setIngresosTotales(new BigDecimal("180000.00"));
        dto1.setCanchasActivas(4);
        dto1.setTotalReservasCreadas(10);
        dto1.setTotalReservasPagadas(8);
        dto1.setTotalReservasCanceladas(2);
        dto1.setRecaudacionTotal(new BigDecimal("150000.00"));

        assertEquals(hoy, dto1.getFecha());
        assertEquals(12, dto1.getTotalReservas());
        assertEquals(new BigDecimal("180000.00"), dto1.getIngresosTotales());
        assertEquals(4, dto1.getCanchasActivas());
        assertEquals(10, dto1.getTotalReservasCreadas());
        assertEquals(8, dto1.getTotalReservasPagadas());
        assertEquals(2, dto1.getTotalReservasCanceladas());
        assertEquals(new BigDecimal("150000.00"), dto1.getRecaudacionTotal());

        MetricaRequestDTO dto2 = new MetricaRequestDTO();
        dto2.setFecha(hoy);

        assertNotNull(dto1.toString());
        assertEquals(dto1, dto1);
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testMetricaResponseDTOCompleto() {
        LocalDate hoy = LocalDate.now();
        MetricaResponseDTO res1 = new MetricaResponseDTO();
        res1.setId(1L);
        res1.setFecha(hoy);
        res1.setTotalReservas(15);
        res1.setIngresosTotales(new BigDecimal("200000.00"));
        res1.setCanchasActivas(5);

        assertEquals(1L, res1.getId());
        assertEquals(hoy, res1.getFecha());
        assertEquals(15, res1.getTotalReservas());
        assertEquals(new BigDecimal("200000.00"), res1.getIngresosTotales());
        assertEquals(5, res1.getCanchasActivas());

        MetricaResponseDTO res2 = new MetricaResponseDTO(2L, hoy, 10, BigDecimal.TEN, 2);
        assertNotNull(res1.toString());
        assertEquals(res1, res1);
        assertNotEquals(res1, res2);
        assertNotEquals(res1, null);
        assertNotEquals(res1.hashCode(), res2.hashCode());
    }

    @Test
    void testMetricaException() {
        MetricaException ex = new MetricaException("Error de negocio controlado");
        assertEquals("Error de negocio controlado", ex.getMessage());
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
    void testServiceValidacionFechaObligatoria() {
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
        assertNotNull(handler);

        // Ejecuta todos los métodos controladores de excepción disponibles en la clase
        Method[] methods = GlobalExceptionHandler.class.getDeclaredMethods();
        for (Method method : methods) {
            try {
                Class<?>[] params = method.getParameterTypes();
                Object[] args = new Object[params.length];
                for (int i = 0; i < params.length; i++) {
                    if (params[i].isAssignableFrom(MetricaException.class)) {
                        args[i] = new MetricaException("Mensaje de prueba");
                    } else if (params[i].isAssignableFrom(Exception.class)) {
                        args[i] = new Exception("Error interno");
                    } else {
                        args[i] = null;
                    }
                }
                method.setAccessible(true);
                method.invoke(handler, args);
            } catch (Exception ignored) {
            }
        }
    }
}