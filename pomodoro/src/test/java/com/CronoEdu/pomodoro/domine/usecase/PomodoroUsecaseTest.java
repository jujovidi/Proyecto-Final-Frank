package com.CronoEdu.pomodoro.domine.usecase;

import com.CronoEdu.pomodoro.domine.model.Pomodoro;
import com.CronoEdu.pomodoro.domine.model.gateway.PomodoroGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PomodoroUsecaseTest {

    @Mock
    private PomodoroGateway pomodoroGateway;

    @InjectMocks
    private PomodoroUsecase pomodoroUsecase;

    // ========================================
    // obtenerEstado()
    // ========================================
    @Test
    void obtenerEstado_DeberiaRetornarPomodoro_CuandoEstudianteExiste() {
        Pomodoro existente = new Pomodoro("uuid-1", "12345", 5);
        when(pomodoroGateway.buscarPorEstudiante("12345")).thenReturn(existente);

        Pomodoro resultado = pomodoroUsecase.obtenerEstado("12345");

        assertNotNull(resultado);
        assertEquals(5, resultado.getCiclosCompletados());
        assertEquals("12345", resultado.getEstudianteCedula());
        verify(pomodoroGateway).buscarPorEstudiante("12345");
        verify(pomodoroGateway, never()).guardar(any());
    }

    @Test
    void obtenerEstado_DeberiaCrearNuevo_CuandoEstudianteNoExiste() {
        when(pomodoroGateway.buscarPorEstudiante("99999"))
                .thenThrow(new RuntimeException("No encontrado"));
        when(pomodoroGateway.guardar(any(Pomodoro.class)))
                .thenAnswer(i -> i.getArgument(0));

        Pomodoro resultado = pomodoroUsecase.obtenerEstado("99999");

        assertNotNull(resultado);
        assertEquals(0, resultado.getCiclosCompletados());
        assertEquals("99999", resultado.getEstudianteCedula());
        verify(pomodoroGateway).buscarPorEstudiante("99999");
        verify(pomodoroGateway).guardar(any(Pomodoro.class));
    }

    @Test
    void obtenerEstado_DeberiaLanzarExcepcion_CuandoCedulaEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pomodoroUsecase.obtenerEstado(null));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
        verifyNoInteractions(pomodoroGateway);
    }

    @Test
    void obtenerEstado_DeberiaLanzarExcepcion_CuandoCedulaEsVacia() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pomodoroUsecase.obtenerEstado(""));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
    }

    @Test
    void obtenerEstado_DeberiaLanzarExcepcion_CuandoCedulaEsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pomodoroUsecase.obtenerEstado("   "));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
    }

    // ========================================
    // completarCiclo()
    // ========================================
    @Test
    void completarCiclo_DeberiaIncrementar_CuandoEstudianteExiste() {
        Pomodoro existente = new Pomodoro("uuid-1", "12345", 5);
        when(pomodoroGateway.buscarPorEstudiante("12345")).thenReturn(existente);
        when(pomodoroGateway.guardar(any(Pomodoro.class))).thenAnswer(i -> i.getArgument(0));

        Pomodoro resultado = pomodoroUsecase.completarCiclo("12345");

        assertNotNull(resultado);
        assertEquals(6, resultado.getCiclosCompletados());
        verify(pomodoroGateway).guardar(existente);
    }

    @Test
    void completarCiclo_DeberiaIncrementar_CuandoEstudianteNoExiste() {
        when(pomodoroGateway.buscarPorEstudiante("99999"))
                .thenThrow(new RuntimeException("No encontrado"));
        when(pomodoroGateway.guardar(any(Pomodoro.class)))
                .thenAnswer(i -> i.getArgument(0));

        Pomodoro resultado = pomodoroUsecase.completarCiclo("99999");

        assertNotNull(resultado);
        assertEquals(1, resultado.getCiclosCompletados());
    }

    @Test
    void completarCiclo_DeberiaLanzarExcepcion_CuandoCedulaEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pomodoroUsecase.completarCiclo(null));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
        verifyNoInteractions(pomodoroGateway);
    }

    @Test
    void completarCiclo_DeberiaLanzarExcepcion_CuandoCedulaEsVacia() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pomodoroUsecase.completarCiclo(""));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
    }

    // ========================================
    // reiniciar()
    // ========================================
    @Test
    void reiniciar_DeberiaPonerCiclosACero_CuandoEstudianteExiste() {
        Pomodoro existente = new Pomodoro("uuid-1", "12345", 10);
        when(pomodoroGateway.buscarPorEstudiante("12345")).thenReturn(existente);
        when(pomodoroGateway.guardar(any(Pomodoro.class))).thenAnswer(i -> i.getArgument(0));

        Pomodoro resultado = pomodoroUsecase.reiniciar("12345");

        assertNotNull(resultado);
        assertEquals(0, resultado.getCiclosCompletados());
        verify(pomodoroGateway).guardar(existente);
    }

    @Test
    void reiniciar_DeberiaCrearYCrear_CuandoEstudianteNoExiste() {
        when(pomodoroGateway.buscarPorEstudiante("99999"))
                .thenThrow(new RuntimeException("No encontrado"));
        when(pomodoroGateway.guardar(any(Pomodoro.class)))
                .thenAnswer(i -> i.getArgument(0));

        Pomodoro resultado = pomodoroUsecase.reiniciar("99999");

        assertNotNull(resultado);
        assertEquals(0, resultado.getCiclosCompletados());
    }

    @Test
    void reiniciar_DeberiaLanzarExcepcion_CuandoCedulaEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pomodoroUsecase.reiniciar(null));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
        verifyNoInteractions(pomodoroGateway);
    }

    @Test
    void reiniciar_DeberiaLanzarExcepcion_CuandoCedulaEsVacia() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pomodoroUsecase.reiniciar(""));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
    }
}
