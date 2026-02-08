


package com.seguridad.metricas.dto;

public class UsuariosActividadDTO {
    private String usuario;
    private Long ingresos;
    private Long salidas;

    // Constructor vacío (Jackson lo necesita)
    public UsuariosActividadDTO() {}

    // Constructor con parámetros
    public UsuariosActividadDTO(String usuario, Long ingresos, Long salidas) {
        this.usuario = usuario;
        this.ingresos = ingresos;
        this.salidas = salidas;
    }

    // Getters y setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getIngresos() {
        return ingresos;
    }

    public void setIngresos(Long ingresos) {
        this.ingresos = ingresos;
    }

    public Long getSalidas() {
        return salidas;
    }

    public void setSalidas(Long salidas) {
        this.salidas = salidas;
    }
}



