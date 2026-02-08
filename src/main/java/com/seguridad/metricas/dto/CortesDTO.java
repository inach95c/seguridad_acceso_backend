package com.seguridad.metricas.dto;

import java.util.Map;

public class CortesDTO {
    private Long diario;
    private Long semanal;
    private Long mensual;
    private Long anual;

    private Long ingresosConSalida;
    private Long ingresosSinSalida;

    private Map<String, Long> porTipoVisitante;
    private Map<String, Long> porEstado;

    // Constructor básico
    public CortesDTO(Long diario, Long semanal, Long mensual, Long anual) {
        this.diario = diario;
        this.semanal = semanal;
        this.mensual = mensual;
        this.anual = anual;
    }

    // Constructor completo
    public CortesDTO(Long diario, Long semanal, Long mensual, Long anual,
                     Long ingresosConSalida, Long ingresosSinSalida,
                     Map<String, Long> porTipoVisitante, Map<String, Long> porEstado) {
        this.diario = diario;
        this.semanal = semanal;
        this.mensual = mensual;
        this.anual = anual;
        this.ingresosConSalida = ingresosConSalida;
        this.ingresosSinSalida = ingresosSinSalida;
        this.porTipoVisitante = porTipoVisitante;
        this.porEstado = porEstado;
    }

    // Getters y Setters
    public Long getDiario() { return diario; }
    public void setDiario(Long diario) { this.diario = diario; }

    public Long getSemanal() { return semanal; }
    public void setSemanal(Long semanal) { this.semanal = semanal; }

    public Long getMensual() { return mensual; }
    public void setMensual(Long mensual) { this.mensual = mensual; }

    public Long getAnual() { return anual; }
    public void setAnual(Long anual) { this.anual = anual; }

    public Long getIngresosConSalida() { return ingresosConSalida; }
    public void setIngresosConSalida(Long ingresosConSalida) { this.ingresosConSalida = ingresosConSalida; }

    public Long getIngresosSinSalida() { return ingresosSinSalida; }
    public void setIngresosSinSalida(Long ingresosSinSalida) { this.ingresosSinSalida = ingresosSinSalida; }

    public Map<String, Long> getPorTipoVisitante() { return porTipoVisitante; }
    public void setPorTipoVisitante(Map<String, Long> porTipoVisitante) { this.porTipoVisitante = porTipoVisitante; }

    public Map<String, Long> getPorEstado() { return porEstado; }
    public void setPorEstado(Map<String, Long> porEstado) { this.porEstado = porEstado; }
}
