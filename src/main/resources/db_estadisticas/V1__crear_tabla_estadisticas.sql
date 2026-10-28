CREATE TABLE IF NOT EXISTS metrica_diaria (
                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                              fecha DATE NOT NULL UNIQUE,
                                              total_reservas_creadas INT DEFAULT 0,
                                              total_reservas_pagadas INT DEFAULT 0,
                                              total_reservas_canceladas INT DEFAULT 0,
                                              recaudacion_total DECIMAL(10, 2) DEFAULT 0.00
    );

CREATE TABLE IF NOT EXISTS estadisticas_canchas (
                                                    id_cancha INT PRIMARY KEY,
                                                    nombre_cancha VARCHAR(100) NOT NULL,
    tipo_cancha VARCHAR(50) NOT NULL,
    cantidad_usos_total INT DEFAULT 0,
    minutes_jugados_acumulados INT DEFAULT 0,
    ingresos_generados DECIMAL(10, 2) DEFAULT 0.00
    );

CREATE TABLE IF NOT EXISTS estadisticas_horas_pico (
                                                       hora_dia INT PRIMARY KEY,
                                                       cantidad_reservas INT DEFAULT 0
);