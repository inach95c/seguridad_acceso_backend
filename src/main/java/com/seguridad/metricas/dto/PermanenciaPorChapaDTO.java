/*package com.seguridad.metricas.dto;

public class PermanenciaPorChapaDTO {
    public String placa;
    public Double promedioHoras;
    public Long maxHoras;
    public Long minHoras;
    public Long casosLargos;

    public PermanenciaPorChapaDTO(String placa, Double promedioHoras, Long maxHoras, Long minHoras, Long casosLargos) {
        this.placa = placa;
        this.promedioHoras = promedioHoras;
        this.maxHoras = maxHoras;
        this.minHoras = minHoras;
        this.casosLargos = casosLargos;
    }
}

*/


package com.seguridad.metricas.dto;

public class PermanenciaPorChapaDTO {
    public String placa;
    public String tipoVisitante;   // 👈 nuevo campo
    public Double promedioHoras;
    public Long maxHoras;
    public Long minHoras;
    public Long casosLargos;
    public Boolean sospechoso;

    public PermanenciaPorChapaDTO(String placa, String tipoVisitante,
                                  Double promedioHoras, Long maxHoras,
                                  Long minHoras, Long casosLargos,Boolean sospechoso) {
        this.placa = placa;
        this.tipoVisitante = tipoVisitante;
        this.promedioHoras = promedioHoras;
        this.maxHoras = maxHoras;
        this.minHoras = minHoras;
        this.casosLargos = casosLargos;
        this.sospechoso = sospechoso;
    }
}





