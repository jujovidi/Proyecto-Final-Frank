package com.CronoEdu.auth.domain.usecase;

import com.CronoEdu.auth.domain.model.Notificacion;
import com.CronoEdu.auth.domain.model.Usuario;
import com.CronoEdu.auth.domain.model.gateway.EncrypterGateway;
import com.CronoEdu.auth.domain.model.gateway.NotificationGateway;
import com.CronoEdu.auth.domain.model.gateway.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private EncrypterGateway encrypterGateway;

    @Mock
    private NotificationGateway notificationGateway;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        usuarioValido = new Usuario();
        usuarioValido.setCedula("12345");
        usuarioValido.setNombre("Juan Perez");
        usuarioValido.setCarrera("Ingenieria");
        usuarioValido.setUbicacionSemestral("3er Semestre");
        usuarioValido.setCorreoInstitucional("juan@universidad.edu");
        usuarioValido.setPassword("password123");
    }

    // ========================================
    // registrar()
    // ========================================
    @Test
    void registrar_DeberiaGuardarYNotificar_CuandoDatosSonValidos() {
        when(encrypterGateway.encrypt("password123")).thenReturn("encryptedPass");
        when(usuarioGateway.guardar(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioUseCase.registrar(usuarioValido);

        assertNotNull(resultado);
        assertEquals("encryptedPass", resultado.getPassword());
        verify(encrypterGateway).encrypt("password123");
        verify(usuarioGateway).guardar(usuarioValido);
        verify(notificationGateway).enviarMensaje(any(Notificacion.class));
    }

    @Test
    void registrar_DeberiaLanzarExcepcion_CuandoCedulaEsNull() {
        usuarioValido.setCedula(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.registrar(usuarioValido));
        assertEquals("La cedula es obligatoria", ex.getMessage());
        verifyNoInteractions(encrypterGateway, usuarioGateway, notificationGateway);
    }

    @Test
    void registrar_DeberiaLanzarExcepcion_CuandoCedulaEsVacia() {
        usuarioValido.setCedula("   ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.registrar(usuarioValido));
        assertEquals("La cedula es obligatoria", ex.getMessage());
        verifyNoInteractions(encrypterGateway, usuarioGateway, notificationGateway);
    }

    @Test
    void registrar_DeberiaLanzarExcepcion_CuandoCorreoEsNull() {
        usuarioValido.setCorreoInstitucional(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.registrar(usuarioValido));
        assertEquals("El correo institucional es obligatorio", ex.getMessage());
    }

    @Test
    void registrar_DeberiaLanzarExcepcion_CuandoCorreoEsVacio() {
        usuarioValido.setCorreoInstitucional("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.registrar(usuarioValido));
        assertEquals("El correo institucional es obligatorio", ex.getMessage());
    }

    @Test
    void registrar_DeberiaLanzarExcepcion_CuandoPasswordEsNull() {
        usuarioValido.setPassword(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.registrar(usuarioValido));
        assertEquals("La contrasena es obligatoria", ex.getMessage());
    }

    @Test
    void registrar_DeberiaLanzarExcepcion_CuandoPasswordEsVacio() {
        usuarioValido.setPassword("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.registrar(usuarioValido));
        assertEquals("La contrasena es obligatoria", ex.getMessage());
    }

    @Test
    void registrar_DeberiaLanzarExcepcion_CuandoNombreEsNull() {
        usuarioValido.setNombre(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.registrar(usuarioValido));
        assertEquals("El nombre es obligatorio", ex.getMessage());
    }

    @Test
    void registrar_DeberiaLanzarExcepcion_CuandoNombreEsVacio() {
        usuarioValido.setNombre("   ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.registrar(usuarioValido));
        assertEquals("El nombre es obligatorio", ex.getMessage());
    }

    // ========================================
    // login()
    // ========================================
    @Test
    void login_DeberiaRetornarUsuario_CuandoCredencialesSonValidas() {
        Usuario usuarioBD = new Usuario();
        usuarioBD.setCorreoInstitucional("juan@universidad.edu");
        usuarioBD.setPassword("encryptedPass");
        usuarioBD.setNombre("Juan Perez");

        when(usuarioGateway.buscarPorCorreo("juan@universidad.edu")).thenReturn(usuarioBD);
        when(encrypterGateway.checkPass("password123", "encryptedPass")).thenReturn(true);

        Usuario resultado = usuarioUseCase.login("juan@universidad.edu", "password123");

        assertNotNull(resultado);
        assertEquals("juan@universidad.edu", resultado.getCorreoInstitucional());
        verify(notificationGateway).enviarMensaje(any(Notificacion.class));
    }

    @Test
    void login_DeberiaLanzarExcepcion_CuandoPasswordNoCoincide() {
        Usuario usuarioBD = new Usuario();
        usuarioBD.setCorreoInstitucional("juan@universidad.edu");
        usuarioBD.setPassword("encryptedPass");

        when(usuarioGateway.buscarPorCorreo("juan@universidad.edu")).thenReturn(usuarioBD);
        when(encrypterGateway.checkPass("wrongpass", "encryptedPass")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.login("juan@universidad.edu", "wrongpass"));
        assertEquals("Credenciales invalidas", ex.getMessage());
        verifyNoInteractions(notificationGateway);
    }

    @Test
    void login_DeberiaLanzarExcepcion_CuandoCorreoEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.login(null, "password123"));
        assertEquals("El correo es obligatorio", ex.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway, notificationGateway);
    }

    @Test
    void login_DeberiaLanzarExcepcion_CuandoCorreoEsVacio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.login("", "password123"));
        assertEquals("El correo es obligatorio", ex.getMessage());
    }

    @Test
    void login_DeberiaLanzarExcepcion_CuandoPasswordEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.login("juan@universidad.edu", null));
        assertEquals("La contrasena es obligatoria", ex.getMessage());
    }

    @Test
    void login_DeberiaLanzarExcepcion_CuandoPasswordEsVacio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.login("juan@universidad.edu", ""));
        assertEquals("La contrasena es obligatoria", ex.getMessage());
    }

    @Test
    void login_DeberiaLanzarExcepcion_CuandoUsuarioNoExiste() {
        when(usuarioGateway.buscarPorCorreo("noexiste@test.com"))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioUseCase.login("noexiste@test.com", "password123"));
        assertEquals("Usuario no encontrado", ex.getMessage());
        verifyNoInteractions(encrypterGateway, notificationGateway);
    }

    // ========================================
    // actualizarUsuario()
    // ========================================
    @Test
    void actualizarUsuario_DeberiaActualizarYNotificar_CuandoDatosSonValidosSinNuevoPassword() {
        Usuario actualizado = new Usuario();
        actualizado.setCedula("12345");
        actualizado.setNombre("Juan Actualizado");
        actualizado.setCarrera("Ingenieria");
        actualizado.setUbicacionSemestral("3er Semestre");
        actualizado.setCorreoInstitucional("juan@universidad.edu");
        actualizado.setPassword(null);

        Usuario existente = new Usuario();
        existente.setPassword("encryptedExistingPass");

        when(usuarioGateway.buscarPorCedula("12345")).thenReturn(existente);
        when(usuarioGateway.actualizar(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioUseCase.actualizarUsuario(actualizado);

        assertNotNull(resultado);
        assertEquals("encryptedExistingPass", resultado.getPassword());
        verify(encrypterGateway, never()).encrypt(any());
        verify(notificationGateway).enviarMensaje(any(Notificacion.class));
    }

    @Test
    void actualizarUsuario_DeberiaEncriptarNuevoPassword_CuandoSeProporciona() {
        Usuario actualizado = new Usuario();
        actualizado.setCedula("12345");
        actualizado.setNombre("Juan Actualizado");
        actualizado.setCarrera("Ingenieria");
        actualizado.setUbicacionSemestral("3er Semestre");
        actualizado.setCorreoInstitucional("juan@universidad.edu");
        actualizado.setPassword("nuevoPassword123");

        Usuario existente = new Usuario();
        existente.setPassword("oldEncryptedPass");

        when(usuarioGateway.buscarPorCedula("12345")).thenReturn(existente);
        when(encrypterGateway.encrypt("nuevoPassword123")).thenReturn("newEncryptedPass");
        when(usuarioGateway.actualizar(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioUseCase.actualizarUsuario(actualizado);

        assertEquals("newEncryptedPass", resultado.getPassword());
        verify(encrypterGateway).encrypt("nuevoPassword123");
        verify(notificationGateway).enviarMensaje(any(Notificacion.class));
    }

    @Test
    void actualizarUsuario_DeberiaLanzarExcepcion_CuandoCedulaEsNull() {
        Usuario usuario = new Usuario();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.actualizarUsuario(usuario));
        assertEquals("La cedula es obligatoria", ex.getMessage());
        verifyNoInteractions(usuarioGateway, encrypterGateway, notificationGateway);
    }

    @Test
    void actualizarUsuario_DeberiaLanzarExcepcion_CuandoCedulaEsVacia() {
        Usuario usuario = new Usuario();
        usuario.setCedula("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.actualizarUsuario(usuario));
        assertEquals("La cedula es obligatoria", ex.getMessage());
    }

    @Test
    void actualizarUsuario_DeberiaPropagarExcepcion_CuandoUsuarioNoExiste() {
        Usuario actualizado = new Usuario();
        actualizado.setCedula("99999");
        actualizado.setPassword(null);

        when(usuarioGateway.buscarPorCedula("99999"))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioUseCase.actualizarUsuario(actualizado));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    // ========================================
    // eliminarUsuario()
    // ========================================
    @Test
    void eliminarUsuario_DeberiaEliminarYNotificar_CuandoCedulaEsValida() {
        Usuario existente = new Usuario();
        existente.setCedula("12345");
        existente.setCorreoInstitucional("juan@universidad.edu");
        existente.setNombre("Juan Perez");

        when(usuarioGateway.buscarPorCedula("12345")).thenReturn(existente);

        usuarioUseCase.eliminarUsuario("12345");

        verify(notificationGateway).enviarMensaje(any(Notificacion.class));
        verify(usuarioGateway).eliminar("12345");
    }

    @Test
    void eliminarUsuario_DeberiaLanzarExcepcion_CuandoCedulaEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.eliminarUsuario(null));
        assertEquals("La cedula es obligatoria", ex.getMessage());
        verifyNoInteractions(usuarioGateway, notificationGateway);
    }

    @Test
    void eliminarUsuario_DeberiaLanzarExcepcion_CuandoCedulaEsVacia() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.eliminarUsuario("   "));
        assertEquals("La cedula es obligatoria", ex.getMessage());
    }

    @Test
    void eliminarUsuario_DeberiaPropagarExcepcion_CuandoUsuarioNoExiste() {
        when(usuarioGateway.buscarPorCedula("99999"))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioUseCase.eliminarUsuario("99999"));
    }

    // ========================================
    // buscarPorCedula()
    // ========================================
    @Test
    void buscarPorCedula_DeberiaRetornarUsuario_CuandoExiste() {
        when(usuarioGateway.buscarPorCedula("12345")).thenReturn(usuarioValido);

        Usuario resultado = usuarioUseCase.buscarPorCedula("12345");

        assertNotNull(resultado);
        assertEquals("12345", resultado.getCedula());
        verify(usuarioGateway).buscarPorCedula("12345");
    }

    @Test
    void buscarPorCedula_DeberiaLanzarExcepcion_CuandoCedulaEsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.buscarPorCedula(null));
        assertEquals("La cedula es obligatoria", ex.getMessage());
        verifyNoInteractions(usuarioGateway);
    }

    @Test
    void buscarPorCedula_DeberiaLanzarExcepcion_CuandoCedulaEsVacia() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioUseCase.buscarPorCedula(""));
        assertEquals("La cedula es obligatoria", ex.getMessage());
    }

    @Test
    void buscarPorCedula_DeberiaPropagarExcepcion_CuandoUsuarioNoExiste() {
        when(usuarioGateway.buscarPorCedula("99999"))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioUseCase.buscarPorCedula("99999"));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }
}
