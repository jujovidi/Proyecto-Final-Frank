package com.CronoEdu.auth.infraestructure.mapper;

import com.CronoEdu.auth.domain.model.Usuario;
import com.CronoEdu.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class MapperUsuario {

    public Usuario toUsuario(UsuarioData data) {
        if (data == null) return null;
        return new Usuario(
                data.getCedula(),
                data.getNombre(),
                data.getCarrera(),
                data.getUbicacionSemestral(),
                data.getCorreoInstitucional(),
                data.getPassword()
        );
    }

    public UsuarioData toData(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioData(
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getCarrera(),
                usuario.getUbicacionSemestral(),
                usuario.getCorreoInstitucional(),
                usuario.getPassword()
        );
    }

}
