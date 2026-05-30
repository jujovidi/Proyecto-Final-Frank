package com.CronoEdu.horario.domine.usecase;

import com.CronoEdu.horario.domine.model.Horario;
import com.CronoEdu.horario.domine.model.Materia;
import com.CronoEdu.horario.domine.model.gateway.MateriaGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MateriaUsecaseTest {

    @Mock
    private MateriaGateway materiaGateway;

    @InjectMocks
    private MateriaUsecase materiaUsecase;

    private Materia materiaValida;
    private Horario horarioValido;

    @BeforeEach
    void setUp() {
        horarioValido = new Horario();
        horarioValido.setDiaSemana("LUNES");
        horarioValido.setHoraInicio("08:00");
        horarioValido.setHoraFin("10:00");

        materiaValida = new Materia();
        materiaValida.setId("CALC1");
        materiaValida.setNombre("Calculo I");
        materiaValida.setDocente("Dr. Gomez");
        materiaValida.setSalon("A-101");
        materiaValida.setEstudianteCedula("12345");
        materiaValida.setHorarios(List.of(horarioValido));
    }

    // ========================================
    // guardarMateria()
    // ========================================
    @Test
    void guardarMateria_DeberiaGuardar_CuandoDatosSonValidos() {
        when(materiaGateway.guardarMateria(any(Materia.class))).thenAnswer(i -> i.getArgument(0));

        Materia resultado = materiaUsecase.guardarMateria(materiaValida);

        assertNotNull(resultado);
        assertEquals("CALC1", resultado.getId());
        verify(materiaGateway).guardarMateria(materiaValida);
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoMateriaEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(null));
        assertEquals("La materia no puede ser nula", ex.getMessage());
        verifyNoInteractions(materiaGateway);
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoIdEsNull() {
        materiaValida.setId(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertEquals("El ID de la materia es obligatorio", ex.getMessage());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoIdEsVacio() {
        materiaValida.setId("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertEquals("El ID de la materia es obligatorio", ex.getMessage());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoNombreEsNull() {
        materiaValida.setNombre(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertEquals("El nombre de la materia es obligatorio", ex.getMessage());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoDocenteEsNull() {
        materiaValida.setDocente(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertEquals("El docente es obligatorio", ex.getMessage());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoSalonEsNull() {
        materiaValida.setSalon(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertEquals("El salon es obligatorio", ex.getMessage());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoEstudianteCedulaEsNull() {
        materiaValida.setEstudianteCedula(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoHorariosEsNull() {
        materiaValida.setHorarios(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertEquals("Debe asignar al menos un horario a la materia", ex.getMessage());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoHorariosEsVacio() {
        materiaValida.setHorarios(List.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertEquals("Debe asignar al menos un horario a la materia", ex.getMessage());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoHorarioDiaEsNull() {
        horarioValido.setDiaSemana(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertEquals("El dia de la semana es obligatorio en cada horario", ex.getMessage());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoHorarioDiaEsInvalido() {
        horarioValido.setDiaSemana("DOMINGO");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertTrue(ex.getMessage().contains("Dia invalido"));
    }

    @Test
    void guardarMateria_DeberiaNormalizarDia_CuandoDiaEstaEnMinusculas() {
        horarioValido.setDiaSemana("lunes");
        when(materiaGateway.guardarMateria(any(Materia.class))).thenAnswer(i -> i.getArgument(0));

        materiaUsecase.guardarMateria(materiaValida);

        assertEquals("LUNES", horarioValido.getDiaSemana());
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoHoraInicioEsNull() {
        horarioValido.setHoraInicio(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertTrue(ex.getMessage().contains("Hora de inicio invalida"));
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoHoraInicioFormatoInvalido() {
        horarioValido.setHoraInicio("25:00");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertTrue(ex.getMessage().contains("Hora de inicio invalida"));
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoHoraFinEsNull() {
        horarioValido.setHoraFin(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertTrue(ex.getMessage().contains("Hora de fin invalida"));
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoHoraFinEsMenorQueInicio() {
        horarioValido.setHoraInicio("10:00");
        horarioValido.setHoraFin("08:00");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertTrue(ex.getMessage().contains("debe ser menor"));
    }

    @Test
    void guardarMateria_DeberiaLanzarExcepcion_CuandoHoraInicioIgualHoraFin() {
        horarioValido.setHoraInicio("08:00");
        horarioValido.setHoraFin("08:00");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.guardarMateria(materiaValida));
        assertTrue(ex.getMessage().contains("debe ser menor"));
    }

    // ========================================
    // actualizarMateria()
    // ========================================
    @Test
    void actualizarMateria_DeberiaActualizar_CuandoDatosSonValidos() {
        when(materiaGateway.actualizarMateria(any(Materia.class))).thenAnswer(i -> i.getArgument(0));

        Materia resultado = materiaUsecase.actualizarMateria(materiaValida);

        assertNotNull(resultado);
        verify(materiaGateway).actualizarMateria(materiaValida);
    }

    @Test
    void actualizarMateria_DeberiaPermitirActualizarSinId_SiMateriaYaExiste() {
        materiaValida.setId(null);
        when(materiaGateway.actualizarMateria(any(Materia.class))).thenAnswer(i -> i.getArgument(0));

        Materia resultado = materiaUsecase.actualizarMateria(materiaValida);

        assertNotNull(resultado);
        verify(materiaGateway).actualizarMateria(materiaValida);
    }

    // ========================================
    // buscarMateria()
    // ========================================
    @Test
    void buscarMateria_DeberiaRetornarMateria_CuandoExiste() {
        when(materiaGateway.buscarMateria("CALC1")).thenReturn(materiaValida);

        Materia resultado = materiaUsecase.buscarMateria("CALC1");

        assertNotNull(resultado);
        assertEquals("Calculo I", resultado.getNombre());
        verify(materiaGateway).buscarMateria("CALC1");
    }

    @Test
    void buscarMateria_DeberiaLanzarExcepcion_CuandoIdEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.buscarMateria(null));
        assertEquals("El ID de la materia es obligatorio", ex.getMessage());
        verifyNoInteractions(materiaGateway);
    }

    @Test
    void buscarMateria_DeberiaLanzarExcepcion_CuandoIdEsVacio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.buscarMateria(""));
        assertEquals("El ID de la materia es obligatorio", ex.getMessage());
    }

    @Test
    void buscarMateria_DeberiaPropagarExcepcion_CuandoNoExiste() {
        when(materiaGateway.buscarMateria("NOEXISTE"))
                .thenThrow(new RuntimeException("Materia no encontrada"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> materiaUsecase.buscarMateria("NOEXISTE"));
        assertEquals("Materia no encontrada", ex.getMessage());
    }

    // ========================================
    // listarTodas()
    // ========================================
    @Test
    void listarTodas_DeberiaRetornarLista() {
        when(materiaGateway.listarTodas()).thenReturn(List.of(materiaValida));

        List<Materia> resultado = materiaUsecase.listarTodas();

        assertEquals(1, resultado.size());
        verify(materiaGateway).listarTodas();
    }

    @Test
    void listarTodas_DeberiaRetornarListaVacia_CuandoNoHayMaterias() {
        when(materiaGateway.listarTodas()).thenReturn(List.of());

        List<Materia> resultado = materiaUsecase.listarTodas();

        assertTrue(resultado.isEmpty());
    }

    // ========================================
    // buscarPorDia()
    // ========================================
    @Test
    void buscarPorDia_DeberiaRetornarMaterias_CuandoDiaEsValido() {
        when(materiaGateway.buscarPorDia("LUNES")).thenReturn(List.of(materiaValida));

        List<Materia> resultado = materiaUsecase.buscarPorDia("LUNES");

        assertEquals(1, resultado.size());
        verify(materiaGateway).buscarPorDia("LUNES");
    }

    @Test
    void buscarPorDia_DeberiaNormalizarMayusculas() {
        when(materiaGateway.buscarPorDia("LUNES")).thenReturn(List.of(materiaValida));

        List<Materia> resultado = materiaUsecase.buscarPorDia("lunes");

        assertEquals(1, resultado.size());
        verify(materiaGateway).buscarPorDia("LUNES");
    }

    @Test
    void buscarPorDia_DeberiaLanzarExcepcion_CuandoDiaEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.buscarPorDia(null));
        assertEquals("El dia es obligatorio", ex.getMessage());
        verifyNoInteractions(materiaGateway);
    }

    @Test
    void buscarPorDia_DeberiaLanzarExcepcion_CuandoDiaEsVacio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.buscarPorDia(""));
        assertEquals("El dia es obligatorio", ex.getMessage());
    }

    @Test
    void buscarPorDia_DeberiaLanzarExcepcion_CuandoDiaEsInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.buscarPorDia("FESTIVO"));
        assertTrue(ex.getMessage().contains("Dia invalido"));
    }

    // ========================================
    // buscarPorEstudiante()
    // ========================================
    @Test
    void buscarPorEstudiante_DeberiaRetornarMaterias_CuandoCedulaEsValida() {
        when(materiaGateway.buscarPorEstudiante("12345")).thenReturn(List.of(materiaValida));

        List<Materia> resultado = materiaUsecase.buscarPorEstudiante("12345");

        assertEquals(1, resultado.size());
        verify(materiaGateway).buscarPorEstudiante("12345");
    }

    @Test
    void buscarPorEstudiante_DeberiaLanzarExcepcion_CuandoCedulaEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.buscarPorEstudiante(null));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
        verifyNoInteractions(materiaGateway);
    }

    @Test
    void buscarPorEstudiante_DeberiaLanzarExcepcion_CuandoCedulaEsVacia() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.buscarPorEstudiante(""));
        assertEquals("La cedula del estudiante es obligatoria", ex.getMessage());
    }

    // ========================================
    // eliminarMateria()
    // ========================================
    @Test
    void eliminarMateria_DeberiaEliminar_CuandoIdEsValido() {
        materiaUsecase.eliminarMateria("CALC1");

        verify(materiaGateway).eliminarMateria("CALC1");
    }

    @Test
    void eliminarMateria_DeberiaLanzarExcepcion_CuandoIdEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.eliminarMateria(null));
        assertEquals("El ID de la materia es obligatorio para eliminarla", ex.getMessage());
        verifyNoInteractions(materiaGateway);
    }

    @Test
    void eliminarMateria_DeberiaLanzarExcepcion_CuandoIdEsVacio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> materiaUsecase.eliminarMateria(""));
        assertEquals("El ID de la materia es obligatorio para eliminarla", ex.getMessage());
    }

    @Test
    void eliminarMateria_DeberiaPropagarExcepcion_CuandoNoExiste() {
        doThrow(new RuntimeException("Materia no encontrada"))
                .when(materiaGateway).eliminarMateria("NOEXISTE");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> materiaUsecase.eliminarMateria("NOEXISTE"));
        assertEquals("Materia no encontrada", ex.getMessage());
    }
}
