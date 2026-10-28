package com.canchas.estadisticasms.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MetricaRequestDTO {

    private LocalDate fecha;

    private Integer totalReservas;
    private BigDecimal ingresosTotales;
    private Integer canchasActivas;

    // espejo
    private Integer totalReservasCreadas;
    private Integer totalReservasPagadas;
    private Integer totalReservasCanceladas;
    private BigDecimal recaudacionTotal;
}