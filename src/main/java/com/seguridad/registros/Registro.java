

/*
 * package com.seguridad.registros;
import com.seguridad.destinos.Destino;
import com.seguridad.users.Usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "registros", indexes = {
        @Index(name = "idx_registros_placa", columnList = "placa"),
        @Index(name = "idx_registros_estado", columnList = "estado"),
        @Index(name = "idx_registros_ingreso", columnList = "ingreso_fecha_hora")
})
public class Registro {

    public enum Estado {
        ABIERTO, SALIDA_PENDIENTE, COMPLETADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String folio;

    @NotBlank
    @Size(max = 15)
    @Column(nullable = false, length = 15)
    private String placa;

    @NotBlank
    @Size(max = 255)
    @Column(name = "placa_foto_url", nullable = false, length = 255)
    private String placaFotoUrl;

    @NotBlank
    @Size(max = 255)
    @Column(name = "licencia_foto_url", nullable = false, length = 255)
    private String licenciaFotoUrl;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    private Destino destino;

    @Column(nullable = false)
    private Boolean sospechoso = false;

    // Fecha/hora de ingreso automática
    @CreationTimestamp
    @Column(name = "ingreso_fecha_hora", nullable = false, updatable = false)
    private Instant ingresoFechaHora;

    @Column(name = "salida_fecha_hora")
    private Instant salidaFechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado;
    
    
//Relaciones con usuarios
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_entrada_id")
    private Usuario usuarioEntrada;

    public Usuario getUsuarioEntrada() {
		return usuarioEntrada;
	}

	public void setUsuarioEntrada(Usuario usuarioEntrada) {
		this.usuarioEntrada = usuarioEntrada;
	}

	public Usuario getUsuarioSalida() {
		return usuarioSalida;
	}

	public void setUsuarioSalida(Usuario usuarioSalida) {
		this.usuarioSalida = usuarioSalida;
	}
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_salida_id")
    private Usuario usuarioSalida;


    // =========================
    // Ciclo de vida
    // =========================
    @PrePersist
    protected void onCreate() {
        if (this.ingresoFechaHora == null) {
            this.ingresoFechaHora = Instant.now();
        }
    }

    // =========================
    // Constructores
    // =========================
    public Registro() {}

    public Registro(Long id, String folio, String placa, String placaFotoUrl, String licenciaFotoUrl,
                    Destino destino, Boolean sospechoso, Instant ingresoFechaHora,
                    Instant salidaFechaHora, Estado estado) {
        this.id = id;
        this.folio = folio;
        this.placa = placa;
        this.placaFotoUrl = placaFotoUrl;
        this.licenciaFotoUrl = licenciaFotoUrl;
        this.destino = destino;
        this.sospechoso = sospechoso;
        this.ingresoFechaHora = ingresoFechaHora;
        this.salidaFechaHora = salidaFechaHora;
        this.estado = estado;
    }

    // =========================
    // Getters y Setters
    // =========================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getPlacaFotoUrl() { return placaFotoUrl; }
    public void setPlacaFotoUrl(String placaFotoUrl) { this.placaFotoUrl = placaFotoUrl; }

    public String getLicenciaFotoUrl() { return licenciaFotoUrl; }
    public void setLicenciaFotoUrl(String licenciaFotoUrl) { this.licenciaFotoUrl = licenciaFotoUrl; }

    public Destino getDestino() { return destino; }
    public void setDestino(Destino destino) { this.destino = destino; }

    public Boolean getSospechoso() { return sospechoso; }
    public void setSospechoso(Boolean sospechoso) { this.sospechoso = sospechoso; }

    public Instant getIngresoFechaHora() { return ingresoFechaHora; }
    public void setIngresoFechaHora(Instant ingresoFechaHora) { this.ingresoFechaHora = ingresoFechaHora; }

    public Instant getSalidaFechaHora() { return salidaFechaHora; }
    public void setSalidaFechaHora(Instant salidaFechaHora) { this.salidaFechaHora = salidaFechaHora; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}

*/




/*
package com.seguridad.registros;

import com.seguridad.destinos.Destino;
import com.seguridad.users.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "registros", indexes = {
        @Index(name = "idx_registros_placa", columnList = "placa"),
        @Index(name = "idx_registros_estado", columnList = "estado"),
        @Index(name = "idx_registros_ingreso", columnList = "ingreso_fecha_hora")
})
public class Registro {

    // =========================
    // Enum de estado
    // =========================
    public enum Estado {
        ABIERTO,
        SALIDA_PENDIENTE,
        COMPLETADO
    }
    
    public enum TipoVisitante
    { VISITANTE, PROVEEDOR,OTRO }

    // =========================
    // Campos principales
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String folio;

    @NotBlank
    @Size(max = 15)
    @Column(nullable = false, length = 15)
    private String placa;

    @NotBlank
    @Size(max = 255)
    @Column(name = "placa_foto_url", nullable = false, length = 255)
    private String placaFotoUrl;

    @NotBlank
    @Size(max = 255)
    @Column(name = "licencia_foto_url", nullable = false, length = 255)
    private String licenciaFotoUrl;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    private Destino destino;

    @Column(nullable = false)
    private Boolean sospechoso = false;

    @CreationTimestamp
    @Column(name = "ingreso_fecha_hora", nullable = false, updatable = false)
    private Instant ingresoFechaHora;

    @Column(name = "salida_fecha_hora")
    private Instant salidaFechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado;
    
  

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_visitante", length = 20, nullable = false)
    private TipoVisitante tipoVisitante;

    public TipoVisitante getTipoVisitante() { return tipoVisitante; }
    public void setTipoVisitante(TipoVisitante tipoVisitante) { this.tipoVisitante = tipoVisitante; }

    
   
   
	// =========================
    // Relaciones con usuarios
    // =========================
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_entrada_id")
    private Usuario usuarioEntrada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_salida_id")
    private Usuario usuarioSalida;

    // =========================
    // Ciclo de vida
    // =========================
    @PrePersist
    protected void onCreate() {
        if (this.ingresoFechaHora == null) {
            this.ingresoFechaHora = Instant.now();
        }
    }

    // =========================
    // Constructores
    // =========================
    public Registro() {}

    public Registro(Long id, String folio, String placa, String placaFotoUrl, String licenciaFotoUrl,
                    Destino destino, Boolean sospechoso, Instant ingresoFechaHora,
                    Instant salidaFechaHora, Estado estado) {
        this.id = id;
        this.folio = folio;
        this.placa = placa;
        this.placaFotoUrl = placaFotoUrl;
        this.licenciaFotoUrl = licenciaFotoUrl;
        this.destino = destino;
        this.sospechoso = sospechoso;
        this.ingresoFechaHora = ingresoFechaHora;
        this.salidaFechaHora = salidaFechaHora;
        this.estado = estado;
    }

    // =========================
    // Getters y Setters
    // =========================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getPlacaFotoUrl() { return placaFotoUrl; }
    public void setPlacaFotoUrl(String placaFotoUrl) { this.placaFotoUrl = placaFotoUrl; }

    public String getLicenciaFotoUrl() { return licenciaFotoUrl; }
    public void setLicenciaFotoUrl(String licenciaFotoUrl) { this.licenciaFotoUrl = licenciaFotoUrl; }

    public Destino getDestino() { return destino; }
    public void setDestino(Destino destino) { this.destino = destino; }

    public Boolean getSospechoso() { return sospechoso; }
    public void setSospechoso(Boolean sospechoso) { this.sospechoso = sospechoso; }

    public Instant getIngresoFechaHora() { return ingresoFechaHora; }
    public void setIngresoFechaHora(Instant ingresoFechaHora) { this.ingresoFechaHora = ingresoFechaHora; }

    public Instant getSalidaFechaHora() { return salidaFechaHora; }
    public void setSalidaFechaHora(Instant salidaFechaHora) { this.salidaFechaHora = salidaFechaHora; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Usuario getUsuarioEntrada() { return usuarioEntrada; }
    public void setUsuarioEntrada(Usuario usuarioEntrada) { this.usuarioEntrada = usuarioEntrada; }

    public Usuario getUsuarioSalida() { return usuarioSalida; }
    public void setUsuarioSalida(Usuario usuarioSalida) { this.usuarioSalida = usuarioSalida; }
    
}

*/

package com.seguridad.registros;

import com.seguridad.destinos.Destino;
import com.seguridad.users.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "registros", indexes = {
        @Index(name = "idx_registros_placa", columnList = "placa"),
        @Index(name = "idx_registros_estado", columnList = "estado"),
        @Index(name = "idx_registros_ingreso", columnList = "ingreso_fecha_hora")
})
public class Registro {

    // =========================
    // Enum de estado
    // =========================
    public enum Estado {
        ABIERTO,
        SALIDA_PENDIENTE,
        COMPLETADO
    }

    // =========================
    // Enum de tipo visitante
    // =========================
    public enum TipoVisitante {
        VISITANTE,
        PROVEEDOR,
        OTRO
    }

    // =========================
    // Campos principales
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String folio;

    @NotBlank
    @Size(max = 15)
    @Column(nullable = false, length = 15)
    private String placa;

    @NotBlank
    @Size(max = 255)
    @Column(name = "placa_foto_url", nullable = false, length = 255)
    private String placaFotoUrl;

    @NotBlank
    @Size(max = 255)
    @Column(name = "licencia_foto_url", nullable = false, length = 255)
    private String licenciaFotoUrl;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    private Destino destino;

    @Column(nullable = false)
    private Boolean sospechoso = false;

    @CreationTimestamp
    @Column(name = "ingreso_fecha_hora", nullable = false, updatable = false)
    private Instant ingresoFechaHora;

    @Column(name = "salida_fecha_hora")
    private Instant salidaFechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_visitante", length = 20, nullable = false)
    private TipoVisitante tipoVisitante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_entrada_id")
    private Usuario usuarioEntrada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_salida_id")
    private Usuario usuarioSalida;

    // =========================
    // Ciclo de vida
    // =========================
    @PrePersist
    protected void onCreate() {
        if (this.ingresoFechaHora == null) {
            this.ingresoFechaHora = Instant.now();
        }
        if (this.tipoVisitante == null) {
            this.tipoVisitante = TipoVisitante.OTRO; // defensivo
        }
    }

    // =========================
    // Constructores
    // =========================
    public Registro() {}

    public Registro(Long id, String folio, String placa, String placaFotoUrl, String licenciaFotoUrl,
                    Destino destino, Boolean sospechoso, Instant ingresoFechaHora,
                    Instant salidaFechaHora, Estado estado, TipoVisitante tipoVisitante) {
        this.id = id;
        this.folio = folio;
        this.placa = placa;
        this.placaFotoUrl = placaFotoUrl;
        this.licenciaFotoUrl = licenciaFotoUrl;
        this.destino = destino;
        this.sospechoso = sospechoso;
        this.ingresoFechaHora = ingresoFechaHora;
        this.salidaFechaHora = salidaFechaHora;
        this.estado = estado;
        this.tipoVisitante = tipoVisitante;
    }

    // =========================
    // Getters y Setters
    // =========================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getPlacaFotoUrl() { return placaFotoUrl; }
    public void setPlacaFotoUrl(String placaFotoUrl) { this.placaFotoUrl = placaFotoUrl; }

    public String getLicenciaFotoUrl() { return licenciaFotoUrl; }
    public void setLicenciaFotoUrl(String licenciaFotoUrl) { this.licenciaFotoUrl = licenciaFotoUrl; }

    public Destino getDestino() { return destino; }
    public void setDestino(Destino destino) { this.destino = destino; }

    public Boolean getSospechoso() { return sospechoso; }
    public void setSospechoso(Boolean sospechoso) { this.sospechoso = sospechoso; }

    public Instant getIngresoFechaHora() { return ingresoFechaHora; }
    public void setIngresoFechaHora(Instant ingresoFechaHora) { this.ingresoFechaHora = ingresoFechaHora; }

    public Instant getSalidaFechaHora() { return salidaFechaHora; }
    public void setSalidaFechaHora(Instant salidaFechaHora) { this.salidaFechaHora = salidaFechaHora; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public TipoVisitante getTipoVisitante() { return tipoVisitante; }
    public void setTipoVisitante(TipoVisitante tipoVisitante) { this.tipoVisitante = tipoVisitante; }

    public Usuario getUsuarioEntrada() { return usuarioEntrada; }
    public void setUsuarioEntrada(Usuario usuarioEntrada) { this.usuarioEntrada = usuarioEntrada; }

    public Usuario getUsuarioSalida() { return usuarioSalida; }
    public void setUsuarioSalida(Usuario usuarioSalida) { this.usuarioSalida = usuarioSalida; }
}
