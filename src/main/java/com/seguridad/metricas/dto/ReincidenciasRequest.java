package com.seguridad.metricas.dto;

import java.util.List;

public class ReincidenciasRequest {
    private List<String> placas;
    private int umbralFrecuentes;

    public ReincidenciasRequest() {}

    public ReincidenciasRequest(List<String> placas, int umbralFrecuentes) {
        this.placas = placas;
        this.umbralFrecuentes = umbralFrecuentes;
    }

    public List<String> getPlacas() { return placas; }
    public void setPlacas(List<String> placas) { this.placas = placas; }

    public int getUmbralFrecuentes() { return umbralFrecuentes; }
    public void setUmbralFrecuentes(int umbralFrecuentes) { this.umbralFrecuentes = umbralFrecuentes; }
}
