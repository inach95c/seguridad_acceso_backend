/*package com.seguridad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SeguridadAccesosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeguridadAccesosApplication.class, args);
	}

}
*/


package com.seguridad;

import com.seguridad.users.Usuario;
import com.seguridad.users.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SeguridadAccesosApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeguridadAccesosApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            // Si no existe el usuario admin, lo creamos
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPasswordHash(encoder.encode("admin123")); // contraseña encriptada
                admin.setRol(Usuario.Rol.MASTER_ADMIN);
                admin.setActivo(true);

                usuarioRepository.save(admin);
                System.out.println("Usuario admin creado con contraseña 'admin123'");
            }
        };
    }
}
