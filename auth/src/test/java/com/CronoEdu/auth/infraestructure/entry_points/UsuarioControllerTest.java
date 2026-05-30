package com.CronoEdu.auth.infraestructure.entry_points;

import com.CronoEdu.auth.domain.model.Usuario;
import com.CronoEdu.auth.domain.usecase.UsuarioUseCase;
import com.CronoEdu.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import com.CronoEdu.auth.infraestructure.mapper.MapperUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioUseCase usuarioUseCase;

    @Mock
    private MapperUsuario mapperUsuario;

    private MockMvc mockMvc;
    private static final String JSON_REGISTER = """
            {"cedula":"123","nombre":"Juan","carrera":"Ing","ubicacionSemestral":"3er","correoInstitucional":"j@test.com","password":"pass"}""";

    private final Usuario usuario = new Usuario("123", "Juan", "Ing", "3er", "j@test.com", "pass");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new UsuarioController(usuarioUseCase, mapperUsuario)
        ).build();
    }

    // === register ===
    @Test
    void register_DeberiaRetornar201_CuandoExitoso() throws Exception {
        when(mapperUsuario.toUsuario(any())).thenReturn(usuario);
        when(usuarioUseCase.registrar(any())).thenReturn(usuario);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_REGISTER))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cedula").value("123"));
    }

    @Test
    void register_DeberiaRetornar400_CuandoIllegalArgument() throws Exception {
        when(mapperUsuario.toUsuario(any())).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_REGISTER))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_DeberiaRetornar500_CuandoErrorInesperado() throws Exception {
        when(mapperUsuario.toUsuario(any())).thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_REGISTER))
                .andExpect(status().isInternalServerError());
    }

    // === login ===
    @Test
    void login_DeberiaRetornar200_CuandoExitoso() throws Exception {
        when(usuarioUseCase.login("j@test.com", "pass")).thenReturn(usuario);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correoInstitucional\":\"j@test.com\",\"password\":\"pass\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cedula").value("123"));
    }

    @Test
    void login_DeberiaRetornar401_CuandoCredencialesInvalidas() throws Exception {
        when(usuarioUseCase.login(any(), any())).thenThrow(new IllegalArgumentException("Credenciales invalidas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correoInstitucional\":\"j@test.com\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_DeberiaRetornar500_CuandoErrorInesperado() throws Exception {
        when(usuarioUseCase.login(any(), any())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correoInstitucional\":\"j@test.com\",\"password\":\"pass\"}"))
                .andExpect(status().isInternalServerError());
    }

    // === actualizar ===
    @Test
    void actualizar_DeberiaRetornar200_CuandoExitoso() throws Exception {
        when(mapperUsuario.toUsuario(any())).thenReturn(usuario);
        when(usuarioUseCase.actualizarUsuario(any())).thenReturn(usuario);

        mockMvc.perform(put("/api/auth/estudiantes/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_REGISTER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cedula").value("123"));
    }

    @Test
    void actualizar_DeberiaRetornar400_CuandoIllegalArgument() throws Exception {
        when(mapperUsuario.toUsuario(any())).thenReturn(usuario);
        when(usuarioUseCase.actualizarUsuario(any())).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(put("/api/auth/estudiantes/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_REGISTER))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_DeberiaRetornar404_CuandoRuntimeException() throws Exception {
        when(mapperUsuario.toUsuario(any())).thenReturn(usuario);
        when(usuarioUseCase.actualizarUsuario(any())).thenThrow(new RuntimeException("No encontrado"));

        mockMvc.perform(put("/api/auth/estudiantes/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_REGISTER))
                .andExpect(status().isNotFound());
    }

    // === eliminar ===
    @Test
    void eliminar_DeberiaRetornar200_CuandoExitoso() throws Exception {
        doNothing().when(usuarioUseCase).eliminarUsuario("123");

        mockMvc.perform(delete("/api/auth/estudiantes/123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Estudiante eliminado exitosamente"));
    }

    @Test
    void eliminar_DeberiaRetornar400_CuandoIllegalArgument() throws Exception {
        doThrow(new IllegalArgumentException("Error")).when(usuarioUseCase).eliminarUsuario("123");

        mockMvc.perform(delete("/api/auth/estudiantes/123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_DeberiaRetornar404_CuandoRuntimeException() throws Exception {
        doThrow(new RuntimeException("No encontrado")).when(usuarioUseCase).eliminarUsuario("123");

        mockMvc.perform(delete("/api/auth/estudiantes/123"))
                .andExpect(status().isNotFound());
    }

    // === buscarPorCedula ===
    @Test
    void buscarPorCedula_DeberiaRetornar200_CuandoExiste() throws Exception {
        when(usuarioUseCase.buscarPorCedula("123")).thenReturn(usuario);

        mockMvc.perform(get("/api/auth/estudiantes/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cedula").value("123"));
    }

    @Test
    void buscarPorCedula_DeberiaRetornar404_CuandoNoExiste() throws Exception {
        when(usuarioUseCase.buscarPorCedula("999")).thenThrow(new RuntimeException("No encontrado"));

        mockMvc.perform(get("/api/auth/estudiantes/999"))
                .andExpect(status().isNotFound());
    }
}
