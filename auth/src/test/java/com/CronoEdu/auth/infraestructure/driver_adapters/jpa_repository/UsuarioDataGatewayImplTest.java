package com.CronoEdu.auth.infraestructure.driver_adapters.jpa_repository;

import com.CronoEdu.auth.domain.model.Usuario;
import com.CronoEdu.auth.infraestructure.mapper.MapperUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioDataGatewayImplTest {

    @Mock
    private MapperUsuario mapperUsuario;

    @Mock
    private UsuarioDataJpaRepository repository;

    @InjectMocks
    private UsuarioDataGatewayImpl gateway;

    private final Usuario usuario = new Usuario("123", "Juan", "Ing", "3er", "j@test.com", "pass");
    private final UsuarioData data = new UsuarioData("123", "Juan", "Ing", "3er", "j@test.com", "pass");

    @Test
    void guardar_DeberiaGuardarYRetornar() {
        when(mapperUsuario.toData(usuario)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);
        when(mapperUsuario.toUsuario(data)).thenReturn(usuario);

        Usuario result = gateway.guardar(usuario);

        assertNotNull(result);
        assertEquals("123", result.getCedula());
        verify(repository).save(data);
    }

    @Test
    void actualizar_DeberiaActualizar_CuandoExiste() {
        when(repository.existsById("123")).thenReturn(true);
        when(mapperUsuario.toData(usuario)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);
        when(mapperUsuario.toUsuario(data)).thenReturn(usuario);

        Usuario result = gateway.actualizar(usuario);

        assertNotNull(result);
        verify(repository).save(data);
    }

    @Test
    void actualizar_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(repository.existsById("123")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gateway.actualizar(usuario));
        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    @Test
    void eliminar_DeberiaEliminar_CuandoExiste() {
        when(repository.existsById("123")).thenReturn(true);

        gateway.eliminar("123");

        verify(repository).deleteById("123");
    }

    @Test
    void eliminar_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(repository.existsById("123")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gateway.eliminar("123"));
        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    @Test
    void buscarPorCedula_DeberiaRetornar_CuandoExiste() {
        when(repository.findById("123")).thenReturn(Optional.of(data));
        when(mapperUsuario.toUsuario(data)).thenReturn(usuario);

        Usuario result = gateway.buscarPorCedula("123");

        assertNotNull(result);
        assertEquals("123", result.getCedula());
    }

    @Test
    void buscarPorCedula_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> gateway.buscarPorCedula("999"));
    }

    @Test
    void buscarPorCorreo_DeberiaRetornar_CuandoExiste() {
        when(repository.findByCorreoInstitucional("j@test.com")).thenReturn(Optional.of(data));
        when(mapperUsuario.toUsuario(data)).thenReturn(usuario);

        Usuario result = gateway.buscarPorCorreo("j@test.com");

        assertNotNull(result);
        assertEquals("j@test.com", result.getCorreoInstitucional());
    }

    @Test
    void buscarPorCorreo_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(repository.findByCorreoInstitucional("no@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> gateway.buscarPorCorreo("no@test.com"));
    }
}
