package com.seguridad.metricas.dto;

public class PermanenciaPorTipoDTO {
    public String tipoVisitante;
    public Double promedioHoras;
    public Long maxHoras;
    public Long minHoras;
    public Long casosLargos;

    public PermanenciaPorTipoDTO(String tipoVisitante, Double promedioHoras, Long maxHoras, Long minHoras, Long casosLargos) {
        this.tipoVisitante = tipoVisitante;
        this.promedioHoras = promedioHoras;
        this.maxHoras = maxHoras;
        this.minHoras = minHoras;
        this.casosLargos = casosLargos;
    }
}
