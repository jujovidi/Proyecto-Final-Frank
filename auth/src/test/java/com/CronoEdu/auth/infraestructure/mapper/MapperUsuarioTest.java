package com.CronoEdu.auth.infraestructure.mapper;

import com.CronoEdu.auth.domain.model.Usuario;
import com.CronoEdu.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapperUsuarioTest {

    private final MapperUsuario mapper = new MapperUsuario();

    @Test
    void toUsuario_DeberiaMapearCorrectamente() {
        UsuarioData data = new UsuarioData("123", "Juan", "Ing", "3er", "j@test.com", "pass");

        Usuario result = mapper.toUsuario(data);

        assertNotNull(result);
        assertEquals("123", result.getCedula());
        assertEquals("Juan", result.getNombre());
        assertEquals("Ing", result.getCarrera());
        assertEquals("3er", result.getUbicacionSemestral());
        assertEquals("j@test.com", result.getCorreoInstitucional());
        assertEquals("pass", result.getPassword());
    }

    @Test
    void toUsuario_DeberiaRetornarNull_CuandoDataEsNull() {
        assertNull(mapper.toUsuario(null));
    }

    @Test
    void toData_DeberiaMapearCorrectamente() {
        Usuario usuario = new Usuario("123", "Juan", "Ing", "3er", "j@test.com", "pass");

        UsuarioData result = mapper.toData(usuario);

        assertNotNull(result);
        assertEquals("123", result.getCedula());
        assertEquals("Juan", result.getNombre());
        assertEquals("Ing", result.getCarrera());
        assertEquals("3er", result.getUbicacionSemestral());
        assertEquals("j@test.com", result.getCorreoInstitucional());
        assertEquals("pass", result.getPassword());
    }

    @Test
    void toData_DeberiaRetornarNull_CuandoUsuarioEsNull() {
        assertNull(mapper.toData(null));
    }
}
