package com.CronoEdu.horario.infraestructure.entry_points;

import com.CronoEdu.horario.domine.model.Materia;
import com.CronoEdu.horario.domine.usecase.MateriaUsecase;
import com.CronoEdu.horario.infraestructure.mapper.MateriaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MateriaControllerTest {

    @Mock
    private MateriaUsecase materiaUsecase;

    @Mock
    private MateriaMapper materiaMapper;

    private MockMvc mockMvc;

    private final String materiaJson = """
            {"id":"CALC1","nombre":"Calculo","docente":"Dr.","salon":"A-101","estudianteCedula":"123","horarios":[]}""";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new MateriaController(materiaUsecase, materiaMapper)
        ).build();
    }

    @Test
    void guardarMateria_DeberiaRetornar201_CuandoExitoso() throws Exception {
        Materia materia = new Materia();
        materia.setId("CALC1");
        when(materiaMapper.toMateria(any())).thenReturn(materia);
        when(materiaUsecase.guardarMateria(any())).thenReturn(materia);

        mockMvc.perform(post("/api/horario/materias/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(materiaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("CALC1"));
    }

    @Test
    void guardarMateria_DeberiaRetornar400_CuandoBodyEsNull() throws Exception {
        mockMvc.perform(post("/api/horario/materias/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void guardarMateria_DeberiaRetornar400_CuandoIllegalArgument() throws Exception {
        when(materiaMapper.toMateria(any())).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(post("/api/horario/materias/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(materiaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void guardarMateria_DeberiaRetornarConflict_CuandoMateriaGuardadaEsNull() throws Exception {
        Materia materia = new Materia(); // id=null
        when(materiaMapper.toMateria(any())).thenReturn(materia);
        when(materiaUsecase.guardarMateria(any())).thenReturn(materia);

        mockMvc.perform(post("/api/horario/materias/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(materiaJson))
                .andExpect(status().isConflict());
    }

    @Test
    void actualizarMateria_DeberiaRetornar200_CuandoExitoso() throws Exception {
        Materia materia = new Materia();
        materia.setId("CALC1");
        when(materiaMapper.toMateria(any())).thenReturn(materia);
        when(materiaUsecase.actualizarMateria(any())).thenReturn(materia);

        mockMvc.perform(put("/api/horario/materias/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(materiaJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CALC1"));
    }

    @Test
    void actualizarMateria_DeberiaRetornar400_CuandoIllegalArgument() throws Exception {
        when(materiaMapper.toMateria(any())).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(put("/api/horario/materias/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(materiaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarMateria_DeberiaRetornar200_CuandoExiste() throws Exception {
        Materia materia = new Materia();
        materia.setId("CALC1");
        when(materiaUsecase.buscarMateria("CALC1")).thenReturn(materia);

        mockMvc.perform(get("/api/horario/materias/CALC1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CALC1"));
    }

    @Test
    void buscarMateria_DeberiaRetornar404_CuandoNoExiste() throws Exception {
        when(materiaUsecase.buscarMateria("NOEXISTE")).thenReturn(null);

        mockMvc.perform(get("/api/horario/materias/NOEXISTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarTodas_DeberiaRetornar200() throws Exception {
        when(materiaUsecase.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/horario/materias/all"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorEstudiante_DeberiaRetornar200() throws Exception {
        when(materiaUsecase.buscarPorEstudiante("123")).thenReturn(List.of());

        mockMvc.perform(get("/api/horario/materias/estudiante/123"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorEstudiante_DeberiaRetornar400_CuandoCedulaEsVacia() throws Exception {
        mockMvc.perform(get("/api/horario/materias/estudiante/ "))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorDia_DeberiaRetornar200() throws Exception {
        when(materiaUsecase.buscarPorDia("LUNES")).thenReturn(List.of());

        mockMvc.perform(get("/api/horario/materias/dia/LUNES"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarMateria_DeberiaRetornar200() throws Exception {
        doNothing().when(materiaUsecase).eliminarMateria("CALC1");

        mockMvc.perform(delete("/api/horario/materias/CALC1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Materia eliminada exitosamente"));
    }

    @Test
    void eliminarMateria_DeberiaRetornar400_CuandoIdInvalido() throws Exception {
        mockMvc.perform(delete("/api/horario/materias/ "))
                .andExpect(status().isBadRequest());
    }
}
