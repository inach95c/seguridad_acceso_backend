/* ok antes de telegram

package com.seguridad.registros;

import com.seguridad.destinos.Destino;
import com.seguridad.destinos.DestinoRepository;
import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import com.seguridad.users.BitacoraService;
import com.seguridad.users.TelegramService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final DestinoRepository destinoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BitacoraService bitacoraService;
    private final TelegramService telegramService; // 👈 nuevo

    private final Path storageRoot = Paths.get("uploads/registros");

    public RegistroService(RegistroRepository registroRepository,
                           DestinoRepository destinoRepository,
                           UsuarioRepository usuarioRepository,
                           BitacoraService bitacoraService,
                           TelegramService telegramService) {
        this.registroRepository = registroRepository;
        this.destinoRepository = destinoRepository;
        this.usuarioRepository = usuarioRepository;
        this.bitacoraService = bitacoraService;
        this.telegramService = telegramService;
        try {
            if (!Files.exists(storageRoot)) {
                Files.createDirectories(storageRoot);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creando carpeta de almacenamiento", e);
        }
    }

   
	public Registro registrarIngresoConFotos(Long destinoId, boolean sospechoso, String placa, MultipartFile placaFoto,
			MultipartFile licenciaFoto, Registro.TipoVisitante tipoVisitante, String usernameActual) {
		Destino destino = destinoRepository.findById(destinoId)
				.orElseThrow(() -> new IllegalArgumentException("Destino no encontrado"));

		if (placa == null || placa.isBlank()) {
			throw new IllegalArgumentException("La placa es obligatoria");
		}
		if (placaFoto.isEmpty() || licenciaFoto.isEmpty()) {
			throw new IllegalArgumentException("Las fotos son obligatorias");
		}

// Generar nombres únicos
		String folio = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		String placaNombre = "placa_" + folio + "_" + sanitize(placaFoto.getOriginalFilename());
		String licenciaNombre = "licencia_" + folio + "_" + sanitize(licenciaFoto.getOriginalFilename());

		try {
			Files.copy(placaFoto.getInputStream(), storageRoot.resolve(placaNombre),
					StandardCopyOption.REPLACE_EXISTING);
			Files.copy(licenciaFoto.getInputStream(), storageRoot.resolve(licenciaNombre),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("Error guardando imágenes", e);
		}

		Usuario usuarioActual = usuarioRepository.findByUsername(usernameActual)
				.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

// 👇 Construir URL pública (ajusta host/puerto según tu entorno)
		//String baseUrl = "http://localhost:8080/uploads/registros/";
		String baseUrl = "http://192.168.1.100:8080/uploads/registros/";
		String placaUrl = baseUrl + placaNombre;
		String licenciaUrl = baseUrl + licenciaNombre;

		Registro registro = new Registro();
		registro.setFolio(folio);
		registro.setPlaca(placa);
		registro.setPlacaFotoUrl(placaUrl); // ✅ ahora guarda la URL completa
		registro.setLicenciaFotoUrl(licenciaUrl); // ✅ ahora guarda la URL completa
		registro.setDestino(destino);
		registro.setSospechoso(sospechoso);
		registro.setTipoVisitante(tipoVisitante != null ? tipoVisitante : Registro.TipoVisitante.OTRO);
		registro.setEstado(Registro.Estado.SALIDA_PENDIENTE);
		registro.setIngresoFechaHora(Instant.now());
		registro.setUsuarioEntrada(usuarioActual);

		Registro guardado = registroRepository.save(registro);
		bitacoraService.registrarAccion("Ingreso registrado - folio " + folio, usuarioActual);
		

				
		// 👇 Notificación individual a Telegram
		if (usuarioActual.getTelegramId() != null) {
		    telegramService.sendMessage(
		        usuarioActual.getTelegramId(),
		        "✅ Ingreso registrado\nFolio: " + folio +
		        "\nPlaca: " + placa +
		        "\nDestino: " + destino.getNombre()
		    );
		}


		
		return guardado;
	}
    

    public Registro registrarSalida(String folio, String usernameActual) {
        Registro registro = registroRepository.findByFolio(folio)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

        Usuario usuarioActual = usuarioRepository.findByUsername(usernameActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        registro.setSalidaFechaHora(Instant.now());
        registro.setEstado(Registro.Estado.COMPLETADO);
        registro.setUsuarioSalida(usuarioActual);

        Registro actualizado = registroRepository.save(registro);
        bitacoraService.registrarAccion("Salida registrada - folio " + folio, usuarioActual);
        

        // 👇 Notificación individual a Telegram
        if (usuarioActual.getTelegramId() != null) {
            telegramService.sendMessage(
                usuarioActual.getTelegramId(),
                "📤 Salida registrada\nFolio: " + folio +
                "\nUsuario: " + usuarioActual.getUsername()
            );
        }

        
        
        return actualizado;
    }

    public List<Registro> obtenerRegistrosPorFecha(Instant inicio, Instant fin) {
        return registroRepository.findByIngresoFechaHoraBetween(inicio, fin);
    }

    public List<Registro> obtenerRegistrosAbiertos() {
        return registroRepository.findByEstado(Registro.Estado.SALIDA_PENDIENTE);
    }

    public List<Registro> obtenerRegistrosCompletados() {
        return registroRepository.findByEstado(Registro.Estado.COMPLETADO);
    }

    private String sanitize(String original) {
        if (original == null) return "image.jpg";
        return original.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
    

    // para integrar con telegram
    public void eliminarRegistro(Long id, String usernameActual) {
        Usuario usuarioActual = usuarioRepository.findByUsername(usernameActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        registroRepository.deleteById(id);
        bitacoraService.registrarAccion("Registro eliminado - ID " + id, usuarioActual);

        if (usuarioActual.getTelegramId() != null) {
            telegramService.sendMessage(
                usuarioActual.getTelegramId(),
                "❌ Registro eliminado\nID: " + id +
                "\nPor: " + usuarioActual.getUsername()
            );
        }
    }



}

*/



// ok de la version V16final
/*
package com.seguridad.registros;

import com.seguridad.destinos.Destino;
import com.seguridad.destinos.DestinoRepository;
import com.seguridad.notificaciones.NotificacionService;
import com.seguridad.notificaciones.TipoEvento;
import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import com.seguridad.users.BitacoraService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final DestinoRepository destinoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BitacoraService bitacoraService;
    private final NotificacionService notificacionService;

    private final Path storageRoot = Paths.get("uploads/registros");

    public RegistroService(RegistroRepository registroRepository,
                           DestinoRepository destinoRepository,
                           UsuarioRepository usuarioRepository,
                           BitacoraService bitacoraService,
                           NotificacionService notificacionService) {

        this.registroRepository = registroRepository;
        this.destinoRepository = destinoRepository;
        this.usuarioRepository = usuarioRepository;
        this.bitacoraService = bitacoraService;
        this.notificacionService = notificacionService;

        try {
            if (!Files.exists(storageRoot)) {
                Files.createDirectories(storageRoot);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creando carpeta de almacenamiento", e);
        }
    }

    // ============================================================
    // REGISTRAR INGRESO
    // ============================================================
    public Registro registrarIngresoConFotos(Long destinoId, boolean sospechoso, String placa,
                                             MultipartFile placaFoto, MultipartFile licenciaFoto,
                                             Registro.TipoVisitante tipoVisitante,
                                             String usernameActual) {

        Destino destino = destinoRepository.findById(destinoId)
                .orElseThrow(() -> new IllegalArgumentException("Destino no encontrado"));

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }
        if (placaFoto.isEmpty() || licenciaFoto.isEmpty()) {
            throw new IllegalArgumentException("Las fotos son obligatorias");
        }

        // Generar nombres únicos
        String folio = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String placaNombre = "placa_" + folio + "_" + sanitize(placaFoto.getOriginalFilename());
        String licenciaNombre = "licencia_" + folio + "_" + sanitize(licenciaFoto.getOriginalFilename());

        try {
            Files.copy(placaFoto.getInputStream(), storageRoot.resolve(placaNombre), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(licenciaFoto.getInputStream(), storageRoot.resolve(licenciaNombre), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando imágenes", e);
        }

        Usuario usuarioActual = usuarioRepository.findByUsername(usernameActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // URL pública (ajústala según tu entorno)
       //String baseUrl = "http://192.168.1.100:8080/uploads/registros/";
        String baseUrl = "https://seguridad-acceso-backend.onrender.com/uploads/registros/";
       
     // En tu servicio
     //   String baseUrl = "https://seguridad-accesos-api.onrender.com/uploads/registros/";
        String placaUrl = baseUrl + placaNombre;
        String licenciaUrl = baseUrl + licenciaNombre;

        Registro registro = new Registro();
        registro.setFolio(folio);
        registro.setPlaca(placa);
        registro.setPlacaFotoUrl(placaUrl);
        registro.setLicenciaFotoUrl(licenciaUrl);
        registro.setDestino(destino);
        registro.setSospechoso(sospechoso);
        registro.setTipoVisitante(tipoVisitante != null ? tipoVisitante : Registro.TipoVisitante.OTRO);
        registro.setEstado(Registro.Estado.SALIDA_PENDIENTE);
        registro.setIngresoFechaHora(Instant.now());
        registro.setUsuarioEntrada(usuarioActual);

        Registro guardado = registroRepository.save(registro);

        bitacoraService.registrarAccion("Ingreso registrado - folio " + folio, usuarioActual);

        // 🔔 Notificación por rol
        String mensaje = "✅ *Ingreso registrado*\n" +
                "📄 Folio: *" + folio + "*\n" +
                "🚗 Placa: *" + placa + "*\n" +
                "🏢 Destino: *" + destino.getNombre() + "*";

        notificacionService.notificar(TipoEvento.REGISTRO_INGRESO, mensaje);

        return guardado;
    }

    // ============================================================
    // REGISTRAR SALIDA
    // ============================================================
    public Registro registrarSalida(String folio, String usernameActual) {

        Registro registro = registroRepository.findByFolio(folio)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

        Usuario usuarioActual = usuarioRepository.findByUsername(usernameActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        registro.setSalidaFechaHora(Instant.now());
        registro.setEstado(Registro.Estado.COMPLETADO);
        registro.setUsuarioSalida(usuarioActual);

        Registro actualizado = registroRepository.save(registro);

        bitacoraService.registrarAccion("Salida registrada - folio " + folio, usuarioActual);

        // 🔔 Notificación por rol
        String mensaje = "📤 *Salida registrada*\n" +
                "📄 Folio: *" + folio + "*\n" +
                "👤 Registrada por: *" + usuarioActual.getUsername() + "*";

        notificacionService.notificar(TipoEvento.REGISTRO_SALIDA, mensaje);

        return actualizado;
    }

    // ============================================================
    // ELIMINAR REGISTRO
    // ============================================================

    
    public void eliminarRegistro(Long id, String usernameActual) {

        Usuario usuarioActual = usuarioRepository.findByUsername(usernameActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 👉 Recuperar el registro ANTES de eliminarlo
        Registro registro = registroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

        String folio = registro.getFolio();
        String placa = registro.getPlaca();
        boolean sospechoso = Boolean.TRUE.equals(registro.getSospechoso());
        String destino = registro.getDestino() != null ? registro.getDestino().getNombre() : "No especificado";

        // 👉 Eliminar
        registroRepository.delete(registro);

        // 👉 Bitácora
        bitacoraService.registrarAccion("Registro eliminado - folio " + folio, usuarioActual);

        // 👉 Mensaje enriquecido con DESTINO
        String mensaje = "🗑 *Registro eliminado*\n" +
                "📄 Folio: *" + folio + "*\n" +
                "🚗 Placa: *" + placa + "*\n" +
                "🏢 Destino: *" + destino + "*\n" +
                (sospechoso ? "⚠️ *Marcado como SOSPECHOSO*\n" : "") +
                "👤 Eliminado por: *" + usuarioActual.getUsername() + "*";

        // 👉 Notificación por rol
        notificacionService.notificar(TipoEvento.REGISTRO_ELIMINADO, mensaje);
    }

    
    // ============================================================
    // CONSULTAS
    // ============================================================
    public List<Registro> obtenerRegistrosPorFecha(Instant inicio, Instant fin) {
        return registroRepository.findByIngresoFechaHoraBetween(inicio, fin);
    }

    public List<Registro> obtenerRegistrosAbiertos() {
        return registroRepository.findByEstado(Registro.Estado.SALIDA_PENDIENTE);
    }

    public List<Registro> obtenerRegistrosCompletados() {
        return registroRepository.findByEstado(Registro.Estado.COMPLETADO);
    }

    // ============================================================
    // UTILIDAD
    // ============================================================
    private String sanitize(String original) {
        if (original == null) return "image.jpg";
        return original.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}

*/



package com.seguridad.registros;

import com.seguridad.destinos.Destino;
import com.seguridad.destinos.DestinoRepository;
import com.seguridad.notificaciones.NotificacionService;
import com.seguridad.notificaciones.TipoEvento;
import com.seguridad.service.CloudinaryService;
import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import com.seguridad.users.BitacoraService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final DestinoRepository destinoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BitacoraService bitacoraService;
    private final NotificacionService notificacionService;
    private final CloudinaryService cloudinaryService;

    public RegistroService(RegistroRepository registroRepository,
                           DestinoRepository destinoRepository,
                           UsuarioRepository usuarioRepository,
                           BitacoraService bitacoraService,
                           NotificacionService notificacionService,
                           CloudinaryService cloudinaryService) {

        this.registroRepository = registroRepository;
        this.destinoRepository = destinoRepository;
        this.usuarioRepository = usuarioRepository;
        this.bitacoraService = bitacoraService;
        this.notificacionService = notificacionService;
        this.cloudinaryService = cloudinaryService;
    }

    // ============================================================
    // REGISTRAR INGRESO (Cloudinary)
    // ============================================================
    public Registro registrarIngresoConFotos(Long destinoId, boolean sospechoso, String placa,
                                             MultipartFile placaFoto, MultipartFile licenciaFoto,
                                             Registro.TipoVisitante tipoVisitante,
                                             String usernameActual) {

        Destino destino = destinoRepository.findById(destinoId)
                .orElseThrow(() -> new IllegalArgumentException("Destino no encontrado"));

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }
        if (placaFoto == null || placaFoto.isEmpty()) {
            throw new IllegalArgumentException("La foto de la placa es obligatoria");
        }
        if (licenciaFoto == null || licenciaFoto.isEmpty()) {
            throw new IllegalArgumentException("La foto de la licencia es obligatoria");
        }

        Usuario usuarioActual = usuarioRepository.findByUsername(usernameActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // ============================
        // SUBIR A CLOUDINARY
        // ============================
        String placaUrl = cloudinaryService.uploadImage(placaFoto, "seguridad-acceso/placas");
        String licenciaUrl = cloudinaryService.uploadImage(licenciaFoto, "seguridad-acceso/licencias");

        // ============================
        // CREAR REGISTRO
        // ============================
        String folio = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Registro registro = new Registro();
        registro.setFolio(folio);
        registro.setPlaca(placa);
        registro.setPlacaFotoUrl(placaUrl);
        registro.setLicenciaFotoUrl(licenciaUrl);
        registro.setDestino(destino);
        registro.setSospechoso(sospechoso);
        registro.setTipoVisitante(tipoVisitante != null ? tipoVisitante : Registro.TipoVisitante.OTRO);
        registro.setEstado(Registro.Estado.SALIDA_PENDIENTE);
        registro.setIngresoFechaHora(Instant.now());
        registro.setUsuarioEntrada(usuarioActual);

        Registro guardado = registroRepository.save(registro);

        // ============================
        // BITÁCORA Y NOTIFICACIÓN
        // ============================
        bitacoraService.registrarAccion("Ingreso registrado - folio " + folio, usuarioActual);

        String mensaje = "✅ *Ingreso registrado*\n" +
                "📄 Folio: *" + folio + "*\n" +
                "🚗 Placa: *" + placa + "*\n" +
                "🏢 Destino: *" + destino.getNombre() + "*";

        notificacionService.notificar(TipoEvento.REGISTRO_INGRESO, mensaje);

        return guardado;
    }

    // ============================================================
    // REGISTRAR SALIDA
    // ============================================================
    public Registro registrarSalida(String folio, String usernameActual) {

        Registro registro = registroRepository.findByFolio(folio)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

        Usuario usuarioActual = usuarioRepository.findByUsername(usernameActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        registro.setSalidaFechaHora(Instant.now());
        registro.setEstado(Registro.Estado.COMPLETADO);
        registro.setUsuarioSalida(usuarioActual);

        Registro actualizado = registroRepository.save(registro);

        bitacoraService.registrarAccion("Salida registrada - folio " + folio, usuarioActual);

        String mensaje = "📤 *Salida registrada*\n" +
                "📄 Folio: *" + folio + "*\n" +
                "👤 Registrada por: *" + usuarioActual.getUsername() + "*";

        notificacionService.notificar(TipoEvento.REGISTRO_SALIDA, mensaje);

        return actualizado;
    }

    // ============================================================
    // ELIMINAR REGISTRO
    // ============================================================
    public void eliminarRegistro(Long id, String usernameActual) {

        Usuario usuarioActual = usuarioRepository.findByUsername(usernameActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Registro registro = registroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));

        String folio = registro.getFolio();
        String placa = registro.getPlaca();
        boolean sospechoso = Boolean.TRUE.equals(registro.getSospechoso());
        String destino = registro.getDestino() != null ? registro.getDestino().getNombre() : "No especificado";

        registroRepository.delete(registro);

        bitacoraService.registrarAccion("Registro eliminado - folio " + folio, usuarioActual);

        String mensaje = "🗑 *Registro eliminado*\n" +
                "📄 Folio: *" + folio + "*\n" +
                "🚗 Placa: *" + placa + "*\n" +
                "🏢 Destino: *" + destino + "*\n" +
                (sospechoso ? "⚠️ *Marcado como SOSPECHOSO*\n" : "") +
                "👤 Eliminado por: *" + usuarioActual.getUsername() + "*";

        notificacionService.notificar(TipoEvento.REGISTRO_ELIMINADO, mensaje);
    }

    // ============================================================
    // CONSULTAS
    // ============================================================
    public List<Registro> obtenerRegistrosPorFecha(Instant inicio, Instant fin) {
        return registroRepository.findByIngresoFechaHoraBetween(inicio, fin);
    }

    public List<Registro> obtenerRegistrosAbiertos() {
        return registroRepository.findByEstado(Registro.Estado.SALIDA_PENDIENTE);
    }

    public List<Registro> obtenerRegistrosCompletados() {
        return registroRepository.findByEstado(Registro.Estado.COMPLETADO);
    }
}


