// Turnos (bitácora)
/*package com.seguridad.metricas.dto;

public class TurnosDTO {
    public Long manana; // 06:00–13:59
    public Long tarde;  // 14:00–21:59
    public Long noche;  // 22:00–05:59
    public Long sinSalida; // estado SALIDA_PENDIENTE
    public Long visitantes;  // opcional si existe tipoVisitante
    public Long proveedores; // opcional si existe tipoVisitante
    public TurnosDTO(Long m, Long t, Long n, Long ss, Long v, Long p) {
        this.manana = m; this.tarde = t; this.noche = n; this.sinSalida = ss;
        this.visitantes = v; this.proveedores = p;
    }
}
*/


package com.seguridad.metricas.dto;

public class TurnosDTO {
    public Long manana;
    public Long tarde;
    public Long noche;

    public TurnosDTO(Long manana, Long tarde, Long noche) {
        this.manana = manana;
        this.tarde = tarde;
        this.noche = noche;
    }
}
