package com.seguridad.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {
    void deleteByUsuarioId(Long usuarioId); // 👈 elimina todas las bitácoras de un usuario
    //Optional<Usuario> findByUsername(String username);
    
    //para anomalias
 

        // Cuenta todas las entradas de bitácora cuya descripción sea "INTENTO_FALLIDO"
        @Query("SELECT COUNT(b) FROM Bitacora b WHERE b.descripcion = 'INTENTO_FALLIDO'")
        Long contarIntentosFallidos();

        // Alternativa flexible: contar por cualquier evento
        @Query("SELECT COUNT(b) FROM Bitacora b WHERE b.descripcion = :evento")
        Long contarPorEvento(@Param("evento") String evento);
        
        
        List<Bitacora> findTop5ByOrderByFechaHoraDesc();
    

}
