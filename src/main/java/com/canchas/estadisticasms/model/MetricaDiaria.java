package com.canchas.estadisticasms.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad JPA que representa la tabla 'metrica_diaria' en la base de datos.
 * Almacena el consolidado diario de actividad y recaudación del sistema de
 * reservas de canchas.
 */
@Entity
@Table(name = "metrica_diaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricaDiaria {

    /** Identificador único autoincremental del registro diario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha correspondiente a las métricas registradas (única por día). */
    @Column(nullable = false, unique = true)
    private LocalDate fecha;

    /** Total acumulado de reservas creadas durante la jornada. */
    @Column(name = "total_reservas_creadas", nullable = false)
    private Integer totalReservasCreadas = 0;

    /** Total acumulado de reservas efectivamente pagadas durante la jornada. */
    @Column(name = "total_reservas_pagadas", nullable = false)
    private Integer totalReservasPagadas = 0;

    /** Total acumulado de reservas canceladas o anuladas durante la jornada. */
    @Column(name = "total_reservas_canceladas", nullable = false)
    private Integer totalReservasCanceladas = 0;

    /** Monto financiero total recaudado en la fecha por reservas pagadas. */
    @Column(name = "recaudacion_total", nullable = false)
    private BigDecimal recaudacionTotal = BigDecimal.ZERO;

    /**
     * Calcula el porcentaje de efectividad entre reservas pagadas y creadas.
     * Feature 2: Mejora de lógica de negocio para estadísticas.
     */
    public double calcularPorcentajeEfectividad() {
        if (this.totalReservasCreadas == null || this.totalReservasCreadas == 0) {
            return 0.0;
        }
        return ((double) this.totalReservasPagadas / this.totalReservasCreadas) * 100.0;
    }
}