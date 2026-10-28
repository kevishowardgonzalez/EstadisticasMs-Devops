package com.canchas.estadisticasms.service;

import com.canchas.estadisticasms.dto.MetricaRequestDTO;
import com.canchas.estadisticasms.dto.MetricaResponseDTO;
import com.canchas.estadisticasms.model.MetricaDiaria;
import com.canchas.estadisticasms.repository.MetricaDiariaRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MetricaDiariaService {

    private final MetricaDiariaRepository repository;

    public MetricaDiariaService(MetricaDiariaRepository repository) {
        this.repository = repository;
    }

    public MetricaResponseDTO registrarOActualizar(MetricaRequestDTO dto) {

        if (dto.getFecha() == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }

        // 🎯 CAPTURAMOS LOS VALORES DE FORMA INTELIGENTE (Combinando local y espejo)
        int creadas = dto.getTotalReservasCreadas() != null ? dto.getTotalReservasCreadas() : (dto.getTotalReservas() != null ? dto.getTotalReservas() : 0);
        int pagadas = dto.getTotalReservasPagadas() != null ? dto.getTotalReservasPagadas() : 0;
        int canceladas = dto.getTotalReservasCanceladas() != null ? dto.getTotalReservasCanceladas() : 0;

        BigDecimal recaudacion = dto.getRecaudacionTotal() != null ? dto.getRecaudacionTotal() :
                (dto.getIngresosTotales() != null ? dto.getIngresosTotales() : BigDecimal.ZERO);

        Optional<MetricaDiaria> existente = repository.findByFecha(dto.getFecha());
        MetricaDiaria metrica;

        if (existente.isPresent()) {
            metrica = existente.get();

            // Acumulamos los valores en los campos reales de tu SQL
            metrica.setTotalReservasCreadas(metrica.getTotalReservasCreadas() + creadas);
            metrica.setTotalReservasPagadas(metrica.getTotalReservasPagadas() + pagadas);
            metrica.setTotalReservasCanceladas(metrica.getTotalReservasCanceladas() + canceladas);
            metrica.setRecaudacionTotal(metrica.getRecaudacionTotal().add(recaudacion));
        } else {
            metrica = new MetricaDiaria();
            metrica.setFecha(dto.getFecha());
            metrica.setTotalReservasCreadas(creadas);
            metrica.setTotalReservasPagadas(pagadas);
            metrica.setTotalReservasCanceladas(canceladas);
            metrica.setRecaudacionTotal(recaudacion);
        }

        MetricaDiaria guardada = repository.save(metrica);

        // Retornamos la respuesta mapeada (puedes usar los mismos campos para mantener compatibilidad en el Response)
        return new MetricaResponseDTO(
                guardada.getId(),
                guardada.getFecha(),
                guardada.getTotalReservasCreadas(), // Mapeado a totalReservas en el DTO
                guardada.getRecaudacionTotal(),    // Mapeado a ingresosTotales en el DTO
                0 // canchasActivas por ahora va en cero ya que no está en la tabla
        );
    }

    public List<MetricaResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(m -> new MetricaResponseDTO(
                        m.getId(),
                        m.getFecha(),
                        m.getTotalReservasCreadas(),
                        m.getRecaudacionTotal(),
                        0
                ))
                .toList();
    }
}