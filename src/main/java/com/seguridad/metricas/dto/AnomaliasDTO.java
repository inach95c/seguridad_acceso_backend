package com.seguridad.metricas.dto;

public class AnomaliasDTO {

    private Long sinSalida;
    private Long incompletos;
    private Long intentosFallidos;
    private Long duplicados;
    private Long fueraDeHorario;
    private Long usuariosBloqueados;
    private Long registrosSinUsuario;
    private Long eventosCriticos;

    // ====== Constructor ======
    public AnomaliasDTO(Long sinSalida,
                        Long incompletos,
                        Long intentosFallidos,
                        Long duplicados,
                        Long fueraDeHorario,
                        Long usuariosBloqueados,
                        Long registrosSinUsuario,
                        Long eventosCriticos) {
        this.sinSalida = sinSalida;
        this.incompletos = incompletos;
        this.intentosFallidos = intentosFallidos;
        this.duplicados = duplicados;
        this.fueraDeHorario = fueraDeHorario;
        this.usuariosBloqueados = usuariosBloqueados;
        this.registrosSinUsuario = registrosSinUsuario;
        this.eventosCriticos = eventosCriticos;
    }

    // ====== Getters y Setters ======
    public Long getSinSalida() { return sinSalida; }
    public void setSinSalida(Long sinSalida) { this.sinSalida = sinSalida; }

    public Long getIncompletos() { return incompletos; }
    public void setIncompletos(Long incompletos) { this.incompletos = incompletos; }

    public Long getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Long intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public Long getDuplicados() { return duplicados; }
    public void setDuplicados(Long duplicados) { this.duplicados = duplicados; }

    public Long getFueraDeHorario() { return fueraDeHorario; }
    public void setFueraDeHorario(Long fueraDeHorario) { this.fueraDeHorario = fueraDeHorario; }

    public Long getUsuariosBloqueados() { return usuariosBloqueados; }
    public void setUsuariosBloqueados(Long usuariosBloqueados) { this.usuariosBloqueados = usuariosBloqueados; }

    public Long getRegistrosSinUsuario() { return registrosSinUsuario; }
    public void setRegistrosSinUsuario(Long registrosSinUsuario) { this.registrosSinUsuario = registrosSinUsuario; }

    public Long getEventosCriticos() { return eventosCriticos; }
    public void setEventosCriticos(Long eventosCriticos) { this.eventosCriticos = eventosCriticos; }
}
