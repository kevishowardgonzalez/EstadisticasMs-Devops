package com.canchas.estadisticasms.controller;

import com.canchas.estadisticasms.dto.MetricaRequestDTO;
import com.canchas.estadisticasms.dto.MetricaResponseDTO;
import com.canchas.estadisticasms.service.MetricaDiariaService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
public class MetricaDiariaController {

    private final MetricaDiariaService service;

    public MetricaDiariaController(MetricaDiariaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MetricaResponseDTO> registrar(@RequestBody MetricaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarOActualizar(dto));
    }

    @GetMapping
    public ResponseEntity<List<MetricaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }
}