package com.CronoEdu.auth.domain.usecase;

import com.CronoEdu.auth.domain.model.Notificacion;
import com.CronoEdu.auth.domain.model.Usuario;
import com.CronoEdu.auth.domain.model.gateway.EncrypterGateway;
import com.CronoEdu.auth.domain.model.gateway.NotificationGateway;
import com.CronoEdu.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;
    private final NotificationGateway notificationGateway;

    public Usuario registrar(Usuario usuario) {
        if (usuario.getCedula() == null || usuario.getCedula().isBlank()) {
            throw new IllegalArgumentException("La cedula es obligatoria");
        }
        if (usuario.getCorreoInstitucional() == null || usuario.getCorreoInstitucional().isBlank()) {
            throw new IllegalArgumentException("El correo institucional es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria");
        }
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        usuario.setPassword(encrypterGateway.encrypt(usuario.getPassword()));
        Usuario usuarioGuardado = usuarioGateway.guardar(usuario);

        notificationGateway.enviarMensaje(Notificacion.builder()
                .tipo("REGISTRO")
                .email(usuarioGuardado.getCorreoInstitucional())
                .mensaje("Usuario registrado exitosamente: " + usuarioGuardado.getNombre())
                .build());

        return usuarioGuardado;
    }

    public Usuario login(String correo, String password) {
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria");
        }

        Usuario usuario = usuarioGateway.buscarPorCorreo(correo);

        if (!encrypterGateway.checkPass(password, usuario.getPassword())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        notificationGateway.enviarMensaje(Notificacion.builder()
                .tipo("LOGIN")
                .email(usuario.getCorreoInstitucional())
                .mensaje("Inicio de sesion exitoso en CronoEdu. Bienvenido " + usuario.getNombre())
                .build());

        return usuario;
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        if (usuario.getCedula() == null || usuario.getCedula().isBlank()) {
            throw new IllegalArgumentException("La cedula es obligatoria");
        }

        Usuario existente = usuarioGateway.buscarPorCedula(usuario.getCedula());

        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            usuario.setPassword(encrypterGateway.encrypt(usuario.getPassword()));
        } else {
            usuario.setPassword(existente.getPassword());
        }

        Usuario usuarioActualizado = usuarioGateway.actualizar(usuario);

        notificationGateway.enviarMensaje(Notificacion.builder()
                .tipo("ACTUALIZACION")
                .email(usuarioActualizado.getCorreoInstitucional())
                .mensaje("Usuario actualizado: " + usuarioActualizado.getNombre())
                .build());

        return usuarioActualizado;
    }

    public void eliminarUsuario(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cedula es obligatoria");
        }

        Usuario usuario = usuarioGateway.buscarPorCedula(cedula);

        notificationGateway.enviarMensaje(Notificacion.builder()
                .tipo("ELIMINACION")
                .email(usuario.getCorreoInstitucional())
                .mensaje("Tu cuenta en CronoEdu ha sido eliminada. Si no realizaste esta accion, contacta al soporte.")
                .build());

        usuarioGateway.eliminar(cedula);
    }

    public Usuario buscarPorCedula(String cedula) {
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cedula es obligatoria");
        }
        return usuarioGateway.buscarPorCedula(cedula);
    }

}
