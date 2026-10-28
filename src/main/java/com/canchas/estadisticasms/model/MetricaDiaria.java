package com.canchas.estadisticasms.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "metrica_diaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricaDiaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate fecha;

    @Column(name = "total_reservas_creadas", nullable = false)
    private Integer totalReservasCreadas = 0;

    @Column(name = "total_reservas_pagadas", nullable = false)
    private Integer totalReservasPagadas = 0;

    @Column(name = "total_reservas_canceladas", nullable = false)
    private Integer totalReservasCanceladas = 0;

    @Column(name = "recaudacion_total", nullable = false)
    private BigDecimal recaudacionTotal = BigDecimal.ZERO;
}