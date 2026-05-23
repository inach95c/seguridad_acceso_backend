/*package com.seguridad.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void crearUsuario_conPasswordInsegura_devuelve400() throws Exception {
        String json = """
            {
              "username": "guardia2",
              "password": "123",
              "role": "GUARDIA"
            }
            """;

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearUsuario_conUsernameDuplicado_devuelve409() throws Exception {
        String json = """
            {
              "username": "guardia1",
              "password": "Passw0rd!",
              "role": "GUARDIA"
            }
            """;

        // Simula que ya existe en BD
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict());
    }
}
*/
