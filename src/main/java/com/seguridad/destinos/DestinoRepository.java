package com.seguridad.destinos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DestinoRepository extends JpaRepository<Destino, Long> {
    List<Destino> findByActivoTrue();
    
    @Query("SELECT DISTINCT d.nombre FROM Destino d") 
    List<String> findDistinctDestinos();
}

