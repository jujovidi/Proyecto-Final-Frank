package com.CronoEdu.pomodoro.infraestructure.mapper;

import com.CronoEdu.pomodoro.domine.model.Pomodoro;
import com.CronoEdu.pomodoro.infraestructure.driver_adapters.jpa_repository.PomodoroData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PomodoroMapperTest {

    private final PomodoroMapper mapper = new PomodoroMapper();

    @Test
    void toData_DeberiaMapearCorrectamente() {
        Pomodoro domain = new Pomodoro("uuid-1", "123", 5);

        PomodoroData result = mapper.toData(domain);

        assertNotNull(result);
        assertEquals("uuid-1", result.getId());
        assertEquals("123", result.getEstudianteCedula());
        assertEquals(5, result.getCiclosCompletados());
    }

    @Test
    void toData_DeberiaRetornarNull_CuandoDomainEsNull() {
        assertNull(mapper.toData(null));
    }

    @Test
    void toDomain_DeberiaMapearCorrectamente() {
        PomodoroData data = new PomodoroData("uuid-1", "123", 5);

        Pomodoro result = mapper.toDomain(data);

        assertNotNull(result);
        assertEquals("uuid-1", result.getId());
        assertEquals("123", result.getEstudianteCedula());
        assertEquals(5, result.getCiclosCompletados());
    }

    @Test
    void toDomain_DeberiaRetornarNull_CuandoDataEsNull() {
        assertNull(mapper.toDomain(null));
    }
}
