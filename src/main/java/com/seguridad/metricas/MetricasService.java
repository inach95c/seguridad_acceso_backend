package com.seguridad.metricas;

import com.seguridad.metricas.dto.*;
import com.seguridad.registros.Registro;
import com.seguridad.registros.RegistroRepository;
import com.seguridad.users.BitacoraRepository;
import com.seguridad.users.UsuarioRepository;
import com.seguridad.config.TenantContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricasService {

    private final RegistroRepository registroRepository;

    @Autowired 
    private UsuarioRepository usuarioRepository;

    @Autowired 
    private BitacoraRepository bitacoraRepository;

    public MetricasService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    // =========================
    // Permanencia por chapa
    // =========================
    public List<PermanenciaPorChapaDTO> calcularPermanenciaPorChapa(Instant inicio, Instant fin, int umbralHoras) {

        String tenant = TenantContext.getTenantId();

        List<Object[]> resultados = registroRepository.permanenciaPorChapa(inicio, fin, tenant);

        List<PermanenciaPorChapaDTO> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            String placa = (String) fila[0];
            String tipoVisitante = (String) fila[1];
            Double promedio = fila[2] != null ? ((Number) fila[2]).doubleValue() : 0.0;
            Long max = fila[3] != null ? ((Number) fila[3]).longValue() : 0L;
            Long min = fila[4] != null ? ((Number) fila[4]).longValue() : 0L;
            Boolean sospechoso = fila[5] != null ? (Boolean) fila[5] : false;

            Long casosLargos = registroRepository.countPermanenciaOverUmbralByPlaca(
                    placa, inicio, fin, umbralHoras, tenant
            );

            lista.add(new PermanenciaPorChapaDTO(
                    placa, tipoVisitante, promedio, max, min, casosLargos, sospechoso
            ));
        }
        return lista;
    }

    // =========================
    // Permanencia por Tipo
    // =========================
    public List<PermanenciaPorTipoDTO> calcularPermanenciaPorTipo(Instant inicio, Instant fin, int umbralHoras) {

        String tenant = TenantContext.getTenantId();

        List<Object[]> resultados = registroRepository.permanenciaPorTipo(inicio, fin, tenant);

        List<PermanenciaPorTipoDTO> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            String tipo = (String) fila[0];
            Double promedio = fila[1] != null ? ((Number) fila[1]).doubleValue() : 0.0;
            Long max = fila[2] != null ? ((Number) fila[2]).longValue() : 0L;
            Long min = fila[3] != null ? ((Number) fila[3]).longValue() : 0L;

            Long casosLargos = registroRepository.countPermanenciaOverUmbralByTipo(
                    tipo, inicio, fin, umbralHoras, tenant
            );

            lista.add(new PermanenciaPorTipoDTO(tipo, promedio, max, min, casosLargos));
        }
        return lista;
    }

    // =========================
    // Turnos
    // =========================
    public TurnosDTO calcularTurnos(Instant inicio, Instant fin) {

        String tenant = TenantContext.getTenantId();

        Long manana = registroRepository.turnoManana(inicio, fin, tenant);
        Long tarde = registroRepository.turnoTarde(inicio, fin, tenant);
        Long noche = registroRepository.turnoNoche(inicio, fin, tenant);

        return new TurnosDTO(manana, tarde, noche);
    }

    // =========================
    // Visitantes por tipo
    // =========================
    public ComparativoDTO calcularVisitantesPorTipo(Instant inicio, Instant fin, String granularidad) {

        String tenant = TenantContext.getTenantId();

        Long visitantes = registroRepository.countVisitantes(inicio, fin, tenant);
        Long proveedores = registroRepository.countProveedores(inicio, fin, tenant);
        Long otros = registroRepository.countOtros(inicio, fin, tenant);

        List<SeriePuntoDTO> serie = new ArrayList<>();
        serie.add(new SeriePuntoDTO(LocalDate.now(), visitantes + proveedores + otros));

        return new ComparativoDTO(granularidad, serie);
    }

    // =========================
    // Reincidencias
    // =========================
    public ReincidenciaPorPlacaDTO calcularReincidenciaPorPlaca(String placa, int umbral, Instant inicio, Instant fin) {

        String tenant = TenantContext.getTenantId();

        List<Registro> registros = registroRepository.findAllByPlacaAndIngresoFechaHoraBetweenAndTenant(
                placa, inicio, fin, tenant
        );

        long ingresos = registros.size();

        List<Double> horas = registros.stream()
                .filter(r -> r.getSalidaFechaHora() != null)
                .map(r -> Duration.between(r.getIngresoFechaHora(), r.getSalidaFechaHora()).toMinutes() / 60.0)
                .toList();

        double promedio = horas.isEmpty() ? 0 : horas.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double max = horas.isEmpty() ? 0 : horas.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double min = horas.isEmpty() ? 0 : horas.stream().mapToDouble(Double::doubleValue).min().orElse(0);

        boolean frecuente = ingresos > umbral;
        boolean sospechosa = ingresos > (umbral * 2);

        return new ReincidenciaPorPlacaDTO(placa, ingresos, promedio, max, min, frecuente, sospechosa);
    }

    // =========================
    // Ranking destinos
    // =========================
    public List<RankingItemDTO> rankingDestinos(Instant inicio, Instant fin) {

        String tenant = TenantContext.getTenantId();

        return registroRepository.rankingDestinos(inicio, fin, tenant).stream()
                .map(obj -> new RankingItemDTO((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

    // =========================
    // Actividad de usuarios
    // =========================
    public List<UsuariosActividadDTO> actividadUsuarios(Instant inicio, Instant fin) {

        String tenant = TenantContext.getTenantId();

        List<Object[]> ingresos = registroRepository.actividadUsuarios(inicio, fin, tenant);

        List<UsuariosActividadDTO> lista = new ArrayList<>();

        for (Object[] fila : ingresos) {
            String username = (String) fila[0];
            Long total = ((Number) fila[1]).longValue();
            lista.add(new UsuariosActividadDTO(username, total, 0L));
        }

        return lista;
    }

    // =========================
    // Anomalías
    // =========================
    public AnomaliasDTO obtenerAnomalias() {

        String tenant = TenantContext.getTenantId();
        Instant fechaLimite = Instant.now().minus(1, ChronoUnit.DAYS);

        Long sinSalida = registroRepository.contarSinSalidaOverUmbral(fechaLimite, tenant);
        Long incompletos = registroRepository.contarIncompletos(tenant);
        Long intentosFallidos = bitacoraRepository.contarIntentosFallidos(tenant);
        Long duplicados = registroRepository.contarDuplicados(tenant);
        Long fueraDeHorario = registroRepository.contarFueraDeHorario(tenant);
        Long usuariosBloqueados = usuarioRepository.countByActivoFalseAndTenant(tenant);
        Long registrosSinUsuario = registroRepository.contarRegistrosSinUsuario(tenant);
        Long eventosCriticos = registroRepository.contarEventosCriticos(tenant);

        return new AnomaliasDTO(
                sinSalida,
                incompletos,
                intentosFallidos,
                duplicados,
                fueraDeHorario,
                usuariosBloqueados,
                registrosSinUsuario,
                eventosCriticos
        );
    }

    // =========================
    // Cortes
    // =========================
    public CortesDTO obtenerCortes() {

        String tenant = TenantContext.getTenantId();
        Instant ahora = Instant.now();

        Instant inicioDia = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Long diario = registroRepository.contarPorRango(inicioDia, ahora, tenant);

        LocalDate inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY);
        Instant inicioSemanaInstant = inicioSemana.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Long semanal = registroRepository.contarPorRango(inicioSemanaInstant, ahora, tenant);

        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        Instant inicioMesInstant = inicioMes.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Long mensual = registroRepository.contarPorRango(inicioMesInstant, ahora, tenant);

        LocalDate inicioAnio = LocalDate.now().withDayOfYear(1);
        Instant inicioAnioInstant = inicioAnio.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Long anual = registroRepository.contarPorRango(inicioAnioInstant, ahora, tenant);

        Long ingresosConSalida = registroRepository.contarConSalida(tenant);
        Long ingresosSinSalida = registroRepository.contarSinSalida(tenant);

        Map<String, Long> porTipoVisitante = registroRepository.contarPorTipoVisitante(tenant)
                .stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));

        Map<String, Long> porEstado = registroRepository.contarPorEstado(tenant)
                .stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));

        return new CortesDTO(
                diario, semanal, mensual, anual,
                ingresosConSalida, ingresosSinSalida,
                porTipoVisitante, porEstado
        );
    }
}
