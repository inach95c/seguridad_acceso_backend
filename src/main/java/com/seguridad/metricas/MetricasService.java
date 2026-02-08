package com.seguridad.metricas;

import com.seguridad.metricas.dto.*;
import com.seguridad.registros.Registro;
import com.seguridad.registros.RegistroRepository;
import com.seguridad.users.BitacoraRepository;
import com.seguridad.users.UsuarioRepository;

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
        List<Object[]> resultados = registroRepository.permanenciaPorChapa(inicio, fin);

        List<PermanenciaPorChapaDTO> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            String placa = (String) fila[0];
            String tipoVisitante = (String) fila[1]; // 👈 ahora sí existe en la query
            Double promedio = fila[2] != null ? ((Number) fila[2]).doubleValue() : 0.0;
            Long max = fila[3] != null ? ((Number) fila[3]).longValue() : 0L;
            Long min = fila[4] != null ? ((Number) fila[4]).longValue() : 0L;
            Boolean sospechoso = fila[5] != null ? (Boolean) fila[5] : false; // 👈 tomado de BD
            Long casosLargos = registroRepository.countPermanenciaOverUmbralByPlaca(placa, inicio, fin, umbralHoras);
           // Boolean sospechoso = max > 48; // 👈 criterio ejemplo 
            
            lista.add(new PermanenciaPorChapaDTO(placa, tipoVisitante, promedio, max, min, casosLargos,sospechoso));
        }
        return lista;
    }

    // =========================
    // Permanencia por Tipo
    // =========================
    public List<PermanenciaPorTipoDTO> calcularPermanenciaPorTipo(Instant inicio, Instant fin, int umbralHoras) {
        List<Object[]> resultados = registroRepository.permanenciaPorTipo(inicio, fin);

        List<PermanenciaPorTipoDTO> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            String tipo = (String) fila[0];
            Double promedio = fila[1] != null ? ((Number) fila[1]).doubleValue() : 0.0;
            Long max = fila[2] != null ? ((Number) fila[2]).longValue() : 0L;
            Long min = fila[3] != null ? ((Number) fila[3]).longValue() : 0L;
            Long casosLargos = registroRepository.countPermanenciaOverUmbralByTipo(tipo, inicio, fin, umbralHoras);

            lista.add(new PermanenciaPorTipoDTO(tipo, promedio, max, min, casosLargos));
        }
        return lista;
    }

    // =========================
    // Turnos
    // =========================
    public TurnosDTO calcularTurnos(Instant inicio, Instant fin) {
        Long manana = registroRepository.turnoManana(inicio, fin);
        Long tarde = registroRepository.turnoTarde(inicio, fin);
        Long noche = registroRepository.turnoNoche(inicio, fin);
        return new TurnosDTO(manana, tarde, noche);
    }

    // =========================
    // Visitantes por tipo (comparativo simple)
    // =========================
    public ComparativoDTO calcularVisitantesPorTipo(Instant inicio, Instant fin, String granularidad) {
        Long visitantes = registroRepository.countVisitantes(inicio, fin);
        Long proveedores = registroRepository.countProveedores(inicio, fin);
        Long otros = registroRepository.countOtros(inicio, fin);

        List<SeriePuntoDTO> serie = new ArrayList<>();
        serie.add(new SeriePuntoDTO(LocalDate.now(), visitantes + proveedores + otros));

        return new ComparativoDTO(granularidad, serie);
    }

    // =========================
    // Reincidencias
    // =========================
    
        public ReincidenciaPorPlacaDTO calcularReincidenciaPorPlaca(String placa, int umbral, Instant inicio, Instant fin) {
            List<Registro> registros = registroRepository.findAllByPlacaAndIngresoFechaHoraBetween(placa, inicio, fin);

            long ingresos = registros.size();

            // Calcular permanencia en horas
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
    // Ranking de destinos
    // =========================
    public List<RankingItemDTO> rankingDestinos(Instant inicio, Instant fin) {
        return registroRepository.rankingDestinos(inicio, fin).stream()
                .map(obj -> new RankingItemDTO((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

    // =========================
    // Actividad de usuarios
    // =========================
  
        public List<UsuariosActividadDTO> actividadUsuarios() {
            List<UsuariosActividadDTO> ingresos = registroRepository.contarIngresos();
            List<UsuariosActividadDTO> salidas = registroRepository.contarSalidas();

            Map<String, UsuariosActividadDTO> mapa = new HashMap<>();

            // Usamos getters en lugar de dto.usuario
            for (UsuariosActividadDTO dto : ingresos) {
                mapa.put(dto.getUsuario(), dto);
            }

            for (UsuariosActividadDTO dto : salidas) {
                mapa.compute(dto.getUsuario(), (k, v) -> {
                    if (v == null) {
                        return dto;
                    } else {
                        v.setSalidas(dto.getSalidas());
                        return v;
                    }
                });
            }

            return new ArrayList<>(mapa.values());
        }
    


    // =========================
    // Anomalías
    // =========================
  
            public AnomaliasDTO obtenerAnomalias() {
                // 👇 Calculamos el umbral de tiempo (ejemplo: hace 1 día)
                Instant fechaLimite = Instant.now().minus(1, ChronoUnit.DAYS);

                Long sinSalida = registroRepository.contarSinSalidaOverUmbral(fechaLimite);
                Long incompletos = registroRepository.contarIncompletos();

                // 👇 Usamos el método fijo o flexible según tu repositorio
                Long intentosFallidos = bitacoraRepository.contarIntentosFallidos();
                // Long intentosFallidos = bitacoraRepository.contarPorEvento("INTENTO_FALLIDO");

                Long duplicados = registroRepository.contarDuplicados();
                Long fueraDeHorario = registroRepository.contarFueraDeHorario();
                Long usuariosBloqueados = usuarioRepository.countByActivoFalse();
                Long registrosSinUsuario = registroRepository.contarRegistrosSinUsuario();
                Long eventosCriticos = registroRepository.contarEventosCriticos();

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
    // Cortes (conteos por granularidad)


                public CortesDTO obtenerCortes() {
                    Instant ahora = Instant.now();

                    // Diario
                    Instant inicioDia = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
                    Long diario = registroRepository.contarPorRango(inicioDia, ahora);

                    // Semanal
                    LocalDate inicioSemana = LocalDate.now().with(DayOfWeek.MONDAY);
                    Instant inicioSemanaInstant = inicioSemana.atStartOfDay(ZoneId.systemDefault()).toInstant();
                    Long semanal = registroRepository.contarPorRango(inicioSemanaInstant, ahora);

                    // Mensual
                    LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
                    Instant inicioMesInstant = inicioMes.atStartOfDay(ZoneId.systemDefault()).toInstant();
                    Long mensual = registroRepository.contarPorRango(inicioMesInstant, ahora);

                    // Anual
                    LocalDate inicioAnio = LocalDate.now().withDayOfYear(1);
                    Instant inicioAnioInstant = inicioAnio.atStartOfDay(ZoneId.systemDefault()).toInstant();
                    Long anual = registroRepository.contarPorRango(inicioAnioInstant, ahora);

                    // Extras
                    Long ingresosConSalida = registroRepository.contarConSalida();
                    Long ingresosSinSalida = registroRepository.contarSinSalida();

                    Map<String, Long> porTipoVisitante = registroRepository.contarPorTipoVisitante()
                            .stream()
                            .collect(Collectors.toMap(
                                    row -> row[0].toString(),
                                    row -> (Long) row[1]
                            ));

                    Map<String, Long> porEstado = registroRepository.contarPorEstado()
                            .stream()
                            .collect(Collectors.toMap(
                                    row -> row[0].toString(),
                                    row -> (Long) row[1]
                            ));

                    return new CortesDTO(diario, semanal, mensual, anual,
                                         ingresosConSalida, ingresosSinSalida,
                                         porTipoVisitante, porEstado);
                }
            

            
}//fin
