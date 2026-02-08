/*// Comparativo con serie
package com.seguridad.metricas.dto;

import java.util.List;
public class ComparativoDTO {
    public String granularidad; // "semanal" | "mensual"
    public List<SeriePuntoDTO> serie;
    public ComparativoDTO(String g, List<SeriePuntoDTO> s) { this.granularidad = g; this.serie = s; }
}
*/

package com.seguridad.metricas.dto;

import java.util.List;

public class ComparativoDTO {
    public String granularidad; // "semanal" | "mensual"
    public List<SeriePuntoDTO> serie;

    public ComparativoDTO(String g, List<SeriePuntoDTO> s) {
        this.granularidad = g;
        this.serie = s;
    }
}
