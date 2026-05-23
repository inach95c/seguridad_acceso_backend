package com.seguridad.metricas.dto;

public class ReincidenciaPorPlacaDTO {
    private String placa;
    private long ingresos;
    private double promedioHoras;
    private double maxHoras;
    private double minHoras;
    private boolean frecuente;
    private boolean sospechosa;

    public ReincidenciaPorPlacaDTO(String placa, long ingresos,
                                   double promedioHoras, double maxHoras, double minHoras,
                                   boolean frecuente, boolean sospechosa) {
        this.placa = placa;
        this.ingresos = ingresos;
        this.promedioHoras = promedioHoras;
        this.maxHoras = maxHoras;
        this.minHoras = minHoras;
        this.frecuente = frecuente;
        this.sospechosa = sospechosa;
    }

    // Getters
    public String getPlaca() { return placa; }
    public long getIngresos() { return ingresos; }
    public double getPromedioHoras() { return promedioHoras; }
    public double getMaxHoras() { return maxHoras; }
    public double getMinHoras() { return minHoras; }
    public boolean isFrecuente() { return frecuente; }
    public boolean isSospechosa() { return sospechosa; }
}

