package com.CronoEdu.horario.infraestructure.mapper;

import com.CronoEdu.horario.domine.model.Horario;
import com.CronoEdu.horario.domine.model.Materia;
import com.CronoEdu.horario.infraestructure.driver_adapters.jpa_repository.HorarioData;
import com.CronoEdu.horario.infraestructure.driver_adapters.jpa_repository.MateriaData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MateriaMapperTest {

    private final MateriaMapper mapper = new MateriaMapper();

    @Test
    void toMateriaData_DeberiaMapearCorrectamente() {
        Horario h = new Horario("h1", "LUNES", "08:00", "10:00");
        Materia m = new Materia("CALC1", "Calculo", "Dr. Gomez", "A-101", "123", List.of(h));

        MateriaData result = mapper.toMateriaData(m);

        assertNotNull(result);
        assertEquals("CALC1", result.getId());
        assertEquals("Calculo", result.getNombre());
        assertEquals("Dr. Gomez", result.getDocente());
        assertEquals("A-101", result.getSalon());
        assertEquals("123", result.getEstudianteCedula());
        assertNotNull(result.getHorarios());
        assertEquals(1, result.getHorarios().size());
        assertEquals("LUNES", result.getHorarios().get(0).getDiaSemana());
        assertEquals(result, result.getHorarios().get(0).getMateria());
    }

    @Test
    void toMateriaData_DeberiaRetornarNull_CuandoMateriaEsNull() {
        assertNull(mapper.toMateriaData(null));
    }

    @Test
    void toMateriaData_DeberiaMapearSinHorarios_CuandoHorariosEsNull() {
        Materia m = new Materia("CALC1", "Calculo", "Dr.", "A-101", "123", null);

        MateriaData result = mapper.toMateriaData(m);

        assertNotNull(result);
        assertNull(result.getHorarios());
    }

    @Test
    void toMateria_DeberiaMapearCorrectamente() {
        HorarioData hd = new HorarioData();
        hd.setId("h1");
        hd.setDiaSemana("LUNES");
        hd.setHoraInicio("08:00");
        hd.setHoraFin("10:00");

        MateriaData md = new MateriaData();
        md.setId("CALC1");
        md.setNombre("Calculo");
        md.setDocente("Dr. Gomez");
        md.setSalon("A-101");
        md.setEstudianteCedula("123");
        md.setHorarios(List.of(hd));

        Materia result = mapper.toMateria(md);

        assertNotNull(result);
        assertEquals("CALC1", result.getId());
        assertEquals("Calculo", result.getNombre());
        assertEquals("123", result.getEstudianteCedula());
        assertNotNull(result.getHorarios());
        assertEquals(1, result.getHorarios().size());
        assertEquals("LUNES", result.getHorarios().get(0).getDiaSemana());
    }

    @Test
    void toMateria_DeberiaRetornarNull_CuandoDataEsNull() {
        assertNull(mapper.toMateria(null));
    }

    @Test
    void toMateria_DeberiaMapearSinHorarios_CuandoHorariosEsNull() {
        MateriaData md = new MateriaData();
        md.setId("CALC1");
        md.setNombre("Calculo");
        md.setDocente("Dr.");
        md.setSalon("A-101");
        md.setEstudianteCedula("123");

        Materia result = mapper.toMateria(md);

        assertNotNull(result);
        assertNull(result.getHorarios());
    }
}
