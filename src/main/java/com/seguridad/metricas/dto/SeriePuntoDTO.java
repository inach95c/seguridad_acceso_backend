/*// Serie temporal (comparativo)
package com.seguridad.metricas.dto;

import java.time.LocalDate;
public class SeriePuntoDTO {
    public LocalDate periodo; // inicio del periodo (día/semana/mes)
    public Long cantidad;
    public SeriePuntoDTO(LocalDate p, Long c) { this.periodo = p; this.cantidad = c; }
}
*/


package com.seguridad.metricas.dto;

import java.time.LocalDate;

public class SeriePuntoDTO {
    public LocalDate periodo; // inicio del periodo (día/semana/mes)
    public Long cantidad;

    public SeriePuntoDTO(LocalDate p, Long c) {
        this.periodo = p;
        this.cantidad = c;
    }
}
