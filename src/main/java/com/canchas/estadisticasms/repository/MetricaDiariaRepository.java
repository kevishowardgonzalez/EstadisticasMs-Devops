package com.canchas.estadisticasms.repository;

import com.canchas.estadisticasms.model.MetricaDiaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MetricaDiariaRepository extends JpaRepository<MetricaDiaria, Long> {

    Optional<MetricaDiaria> findByFecha(LocalDate fecha);
}