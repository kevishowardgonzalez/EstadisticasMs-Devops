package com.canchas.estadisticasms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricaResponseDTO {

    private Long id;
    private LocalDate fecha;
    private Integer totalReservas;
    private BigDecimal ingresosTotales;
    private Integer canchasActivas;
}