package com.CronoEdu.pomodoro.infraestructure.entry_points;

import com.CronoEdu.pomodoro.domine.model.Pomodoro;
import com.CronoEdu.pomodoro.domine.usecase.PomodoroUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PomodoroControllerTest {

    @Mock
    private PomodoroUsecase pomodoroUsecase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new PomodoroController(pomodoroUsecase)
        ).build();
    }

    @Test
    void obtenerEstado_DeberiaRetornar200_CuandoExiste() throws Exception {
        Pomodoro p = new Pomodoro("uuid", "123", 5);
        when(pomodoroUsecase.obtenerEstado("123")).thenReturn(p);

        mockMvc.perform(get("/api/pomodoro/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ciclosCompletados").value(5));
    }

    @Test
    void obtenerEstado_DeberiaRetornar400_CuandoIllegalArgument() throws Exception {
        when(pomodoroUsecase.obtenerEstado("X")).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(get("/api/pomodoro/X"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void completarCiclo_DeberiaRetornar200() throws Exception {
        Pomodoro p = new Pomodoro("uuid", "123", 6);
        when(pomodoroUsecase.completarCiclo("123")).thenReturn(p);

        mockMvc.perform(post("/api/pomodoro/123/completar-ciclo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ciclosCompletados").value(6));
    }

    @Test
    void completarCiclo_DeberiaRetornar400_CuandoIllegalArgument() throws Exception {
        when(pomodoroUsecase.completarCiclo("X")).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(post("/api/pomodoro/X/completar-ciclo"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reiniciar_DeberiaRetornar200() throws Exception {
        Pomodoro p = new Pomodoro("uuid", "123", 0);
        when(pomodoroUsecase.reiniciar("123")).thenReturn(p);

        mockMvc.perform(post("/api/pomodoro/123/reiniciar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ciclosCompletados").value(0));
    }

    @Test
    void reiniciar_DeberiaRetornar400_CuandoIllegalArgument() throws Exception {
        when(pomodoroUsecase.reiniciar("X")).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(post("/api/pomodoro/X/reiniciar"))
                .andExpect(status().isBadRequest());
    }
}
