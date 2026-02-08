package com.seguridad.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "usuarios", indexes = {
    @Index(name = "idx_usuarios_username", columnList = "username", unique = true)
})
public class Usuario {

    public enum Rol {
        GUARDIA,
        GERENTE_ADMIN,
        MASTER_ADMIN;

        @JsonCreator
        public static Rol fromString(String value) {
            if (value == null) return null;
            try {
                return Rol.valueOf(value.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ Rol inválido recibido: " + value);
                return null;
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank
    @Size(max = 255)
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20) // 👈 columna en BD = role
    private Rol rol;

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_por", length = 50, updatable = false)
    private String creadoPor;

    @Column(name = "actualizado_por", length = 50)
    private String actualizadoPor;

    @Column(name = "creado_en", updatable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en")
    private Instant actualizadoEn;

    private String telegramId; // campo reservado para integración futura

    // Hooks para timestamps
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.creadoEn = (this.creadoEn == null) ? now : this.creadoEn;
        this.actualizadoEn = now;
        if (this.activo == null) {
            this.activo = true;
        }
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

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }

    public Instant getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }

    public Instant getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(Instant actualizadoEn) { this.actualizadoEn = actualizadoEn; }

    public String getTelegramId() { return telegramId; }
    public void setTelegramId(String telegramId) { this.telegramId = telegramId; }
}
