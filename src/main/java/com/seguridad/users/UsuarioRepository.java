package com.seguridad.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
   
    //para anomalias
   
       
        /**
         * Cuenta todos los usuarios cuyo campo activo = false.
         * Útil para saber cuántos están bloqueados/inactivos.
         */
        Long countByActivoFalse();
        /** * Cuenta todos los usuarios por rol específico. * Ejemplo: usuarioRepository.countByRol(Usuario.Rol.GUARDIA); */
        Long countByRol(Usuario.Rol rol);

    
     // Obtener todos los usuarios con un rol específico y telegramId no nulo
        List<Usuario> findByRolAndTelegramIdIsNotNull(Usuario.Rol rol);

        // Obtener usuarios de varios roles (GUARDIA, ADMIN, MASTER) con telegramId
        List<Usuario> findByRolInAndTelegramIdIsNotNull(Iterable<Usuario.Rol> roles);

        // Obtener todos los usuarios que tienen Telegram vinculado
        List<Usuario> findByTelegramIdIsNotNull();


}
