package com.CronoEdu.horario.infraestructure.driver_adapters.jpa_repository;

import com.CronoEdu.horario.domine.model.Horario;
import com.CronoEdu.horario.domine.model.Materia;
import com.CronoEdu.horario.infraestructure.mapper.MateriaMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MateriaDataGatewayImplTest {

    @Mock
    private MateriaDataJpaRepository repository;

    @Mock
    private MateriaMapper mapper;

    @InjectMocks
    private MateriaDataGatewayImpl gateway;

    private final Horario horario = new Horario("h1", "LUNES", "08:00", "10:00");
    private final Materia materia = new Materia("CALC1", "Calculo", "Dr.", "A-101", "123", List.of(horario));
    private final MateriaData data = new MateriaData();
    private final HorarioData horarioData = new HorarioData();

    {
        horarioData.setId("h1");
        horarioData.setDiaSemana("LUNES");
        horarioData.setHoraInicio("08:00");
        horarioData.setHoraFin("10:00");
        data.setId("CALC1");
        data.setNombre("Calculo");
        data.setDocente("Dr.");
        data.setSalon("A-101");
        data.setEstudianteCedula("123");
        data.setHorarios(List.of(horarioData));
    }

    @Test
    void guardarMateria_DeberiaGuardar_CuandoNoExiste() {
        when(mapper.toMateriaData(materia)).thenReturn(data);
        when(repository.existsById("CALC1")).thenReturn(false);
        when(repository.save(data)).thenReturn(data);
        when(mapper.toMateria(data)).thenReturn(materia);

        Materia result = gateway.guardarMateria(materia);

        assertNotNull(result);
        verify(repository).save(data);
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoYaExiste() {
        when(mapper.toMateriaData(materia)).thenReturn(data);
        when(repository.existsById("CALC1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> gateway.guardarMateria(materia));
        verify(repository, never()).save(any());
    }

    @Test
    void actualizarMateria_DeberiaActualizar_CuandoExiste() {
        when(repository.existsById("CALC1")).thenReturn(true);
        when(mapper.toMateriaData(materia)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);
        when(mapper.toMateria(data)).thenReturn(materia);

        Materia result = gateway.actualizarMateria(materia);

        assertNotNull(result);
        verify(repository).save(data);
    }

    @Test
    void actualizarMateria_DeberiaLanzarExcepcion_CuandoNoExiste() {
        materia.setId("NOEXISTE");
        when(repository.existsById("NOEXISTE")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> gateway.actualizarMateria(materia));
    }

    @Test
    void buscarMateria_DeberiaRetornar_CuandoExiste() {
        when(repository.findById("CALC1")).thenReturn(Optional.of(data));
        when(mapper.toMateria(data)).thenReturn(materia);

        Materia result = gateway.buscarMateria("CALC1");

        assertNotNull(result);
    }

    @Test
    void buscarMateria_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(repository.findById("NOEXISTE")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> gateway.buscarMateria("NOEXISTE"));
    }

    @Test
    void listarTodas_DeberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(data));
        when(mapper.toMateria(data)).thenReturn(materia);

        List<Materia> result = gateway.listarTodas();

        assertEquals(1, result.size());
    }

    @Test
    void buscarPorDia_DeberiaRetornarLista() {
        when(repository.findByHorariosDiaSemana("LUNES")).thenReturn(List.of(data));
        when(mapper.toMateria(data)).thenReturn(materia);

        List<Materia> result = gateway.buscarPorDia("LUNES");

        assertEquals(1, result.size());
    }

    @Test
    void buscarPorEstudiante_DeberiaRetornarLista() {
        when(repository.findByEstudianteCedula("123")).thenReturn(List.of(data));
        when(mapper.toMateria(data)).thenReturn(materia);

        List<Materia> result = gateway.buscarPorEstudiante("123");

        assertEquals(1, result.size());
    }

    @Test
    void eliminarMateria_DeberiaEliminar_CuandoExiste() {
        when(repository.existsById("CALC1")).thenReturn(true);

        gateway.eliminarMateria("CALC1");

        verify(repository).deleteById("CALC1");
    }

    @Test
    void eliminarMateria_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(repository.existsById("NOEXISTE")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> gateway.eliminarMateria("NOEXISTE"));
    }
}
