/*package com.seguridad.users;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioDTO {

    private String username;
    private String password;

    @JsonProperty("role") // 👈 acepta "role" del JSON y lo mapea a rol
    private Usuario.Rol rol;

    // =========================
    // Getters y Setters
    // =========================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario.Rol getRol() {
        return rol;
    }

    public void setRol(Usuario.Rol rol) {
        this.rol = rol;
    }
}
*/

package com.seguridad.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioDTO {

    private String username;
    private String password;

    @JsonProperty("role") // 👈 acepta "role" del JSON
    private Usuario.Rol rol;
    
   

    // =========================
    // Constructores
    // =========================
    public UsuarioDTO() {}

    @JsonCreator
    public UsuarioDTO(@JsonProperty("username") String username,
                      @JsonProperty("password") String password,
                      @JsonProperty("role") String role) {
        this.username = username;
        this.password = password;
        // Normalizar el valor recibido para que coincida con el enum
        if (role != null) {
            try {
                this.rol = Usuario.Rol.valueOf(role.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ Rol inválido recibido en DTO: " + role);
                this.rol = null; // se marca como nulo si no coincide
            }
        }
    }

    // =========================
    // Getters y Setters
    // =========================
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("role")
    public Usuario.Rol getRol() {
        return rol;
    }

    public void setRol(Usuario.Rol rol) {
        this.rol = rol;
    }
}

