-- 1. Inserts de métricas diarias
INSERT INTO metrica_diaria (fecha, total_reservas_creadas, total_reservas_pagadas, total_reservas_canceladas, recaudacion_total) VALUES
                                                                                                                                     ('2026-05-20', 8, 7, 1, 210000.00),
                                                                                                                                     ('2026-05-21', 12, 10, 2, 315000.00),
                                                                                                                                     ('2026-05-22', 15, 14, 0, 420000.00),
                                                                                                                                     ('2026-05-23', 5, 4, 1, 115000.00);

-- 2. Inserts de estadísticas por cancha
INSERT INTO estadisticas_canchas (id_cancha, nombre_cancha, tipo_cancha, cantidad_usos_total, minutes_jugados_acumulados, ingresos_generados) VALUES
                                                                                                                                                  (1, 'Cancha de Fútbol 1', 'Futbolito', 45, 2700, 1575000.00),
                                                                                                                                                  (2, 'Cancha de Fútbol 2', 'Futbolito', 38, 2280, 1330000.00),
                                                                                                                                                  (3, 'Estadio Central Paddle', 'Paddle', 60, 4500, 1200000.00),
                                                                                                                                                  (4, 'Court Central Tenis', 'Tenis', 22, 1320, 330000.00);

-- 3. Inserts de horas pico
INSERT INTO estadisticas_horas_pico (hora_dia, cantidad_reservas) VALUES
                                                                      (9, 5),   (10, 8),  (11, 4),  (12, 3),
                                                                      (13, 2),  (14, 1),  (15, 6),  (16, 12),
                                                                      (17, 25), (18, 48), (19, 64), (20, 75),
                                                                      (21, 82), (22, 50), (23, 18);