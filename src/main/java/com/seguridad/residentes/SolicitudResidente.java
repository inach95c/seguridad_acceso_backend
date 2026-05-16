/*package com.seguridad.residentes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seguridad.destinos.Destino;
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

    // 👇 Relación con Destino (lazy para eficiencia)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Destino destino;

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

    // =========================
    // Campo virtual para enviar el nombre del destino al frontend
    // =========================
    @Transient
    private String destinoNombre;

    public String getDestinoNombre() {
        return destino != null ? destino.getNombre() : null;
    }

    // =========================
    // Hooks de auditoría
    // =========================
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

    public Destino getDestino() { return destino; }
    public void setDestino(Destino destino) { this.destino = destino; }

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

*/


package com.seguridad.residentes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seguridad.destinos.Destino;
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

    // Relación con Destino
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Destino destino;

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

    // =========================
    // NUEVOS CAMPOS “nivel CondominioFeliz”
    // =========================

    @Column(name = "tipo_visitante", length = 50)
    private String tipoVisitante; // FAMILIAR, TECNICO, DELIVERY, etc.

    @Column(name = "motivo_visita", length = 255)
    private String motivoVisita; // Entrega, reparación, visita personal, etc.

    @Column(name = "placa_vehiculo", length = 20)
    private String placaVehiculo;

    @Column(name = "dni_visitante", length = 30)
    private String dniVisitante;

    @Column(name = "numero_acompanantes")
    private Integer numeroAcompanantes;

    // =========================
    // Campo virtual para enviar nombre del destino
    // =========================
    @Transient
    private String destinoNombre;

    public String getDestinoNombre() {
        return destino != null ? destino.getNombre() : null;
    }

    // =========================
    // Hooks de auditoría
    // =========================
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

    public Destino getDestino() { return destino; }
    public void setDestino(Destino destino) { this.destino = destino; }

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

    // ===== Nuevos campos =====

    public String getTipoVisitante() { return tipoVisitante; }
    public void setTipoVisitante(String tipoVisitante) { this.tipoVisitante = tipoVisitante; }

    public String getMotivoVisita() { return motivoVisita; }
    public void setMotivoVisita(String motivoVisita) { this.motivoVisita = motivoVisita; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

    public String getDniVisitante() { return dniVisitante; }
    public void setDniVisitante(String dniVisitante) { this.dniVisitante = dniVisitante; }

    public Integer getNumeroAcompanantes() { return numeroAcompanantes; }
    public void setNumeroAcompanantes(Integer numeroAcompanantes) { this.numeroAcompanantes = numeroAcompanantes; }
}

