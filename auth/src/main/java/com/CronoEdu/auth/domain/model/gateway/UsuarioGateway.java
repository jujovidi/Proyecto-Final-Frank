package com.CronoEdu.auth.domain.model.gateway;

import com.CronoEdu.auth.domain.model.Usuario;

public interface UsuarioGateway {

    Usuario guardar(Usuario usuario);

    Usuario actualizar(Usuario usuario);

    void eliminar(String cedula);

    Usuario buscarPorCedula(String cedula);

    Usuario buscarPorCorreo(String correo);

}
