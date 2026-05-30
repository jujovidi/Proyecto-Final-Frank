package com.CronoEdu.pomodoro.infraestructure.driver_adapters.jpa_repository;

import com.CronoEdu.pomodoro.domine.model.Pomodoro;
import com.CronoEdu.pomodoro.infraestructure.mapper.PomodoroMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PomodoroDataGatewayImplTest {

    @Mock
    private PomodoroDataJpaRepository repository;

    @Mock
    private PomodoroMapper mapper;

    @InjectMocks
    private PomodoroDataGatewayImpl gateway;

    private final Pomodoro domain = new Pomodoro("uuid-1", "123", 5);
    private final PomodoroData data = new PomodoroData("uuid-1", "123", 5);

    @Test
    void guardar_DeberiaGuardarYRetornar() {
        when(mapper.toData(domain)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);
        when(mapper.toDomain(data)).thenReturn(domain);

        Pomodoro result = gateway.guardar(domain);

        assertNotNull(result);
        assertEquals(5, result.getCiclosCompletados());
        verify(repository).save(data);
    }

    @Test
    void buscarPorEstudiante_DeberiaRetornar_CuandoExiste() {
        when(repository.findByEstudianteCedula("123")).thenReturn(Optional.of(data));
        when(mapper.toDomain(data)).thenReturn(domain);

        Pomodoro result = gateway.buscarPorEstudiante("123");

        assertNotNull(result);
        assertEquals("123", result.getEstudianteCedula());
    }

    @Test
    void buscarPorEstudiante_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(repository.findByEstudianteCedula("999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> gateway.buscarPorEstudiante("999"));
    }
}
