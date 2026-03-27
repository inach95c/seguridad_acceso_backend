package com.seguridad.residentes;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "solicitudes_residente")
public class SolicitudResidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String tenant;

    @Column(name = "residente_username", nullable = false, length = 50)
    private String residenteUsername;

    @Column(nullable = false, length = 100)
    private String visitante;

    @Column(nullable = false, length = 100)
    private String destino;

    @Column(name = "fecha_hora", nullable = false)
    private Instant fechaHora;

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(length = 255)
    private String evidencia;

    @Column(name = "creado_en", updatable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en")
    private Instant actualizadoEn;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.creadoEn = now;
        this.actualizadoEn = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = Instant.now();
    }

    // =========================
    // Getters y Setters
    // =========================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }

    public String getResidenteUsername() { return residenteUsername; }
    public void setResidenteUsername(String residenteUsername) { this.residenteUsername = residenteUsername; }

    public String getVisitante() { return visitante; }
    public void setVisitante(String visitante) { this.visitante = visitante; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Instant getFechaHora() { return fechaHora; }
    public void setFechaHora(Instant fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEvidencia() { return evidencia; }
    public void setEvidencia(String evidencia) { this.evidencia = evidencia; }

    public Instant getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }

    public Instant getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(Instant actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
