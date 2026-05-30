package com.CronoEdu.auth.infraestructure.driver_adapters.jpa_repository;

import com.CronoEdu.auth.domain.model.Usuario;
import com.CronoEdu.auth.domain.model.gateway.UsuarioGateway;
import com.CronoEdu.auth.infraestructure.mapper.MapperUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsuarioDataGatewayImpl implements UsuarioGateway {

    private final MapperUsuario mapperUsuario;
    private final UsuarioDataJpaRepository repository;

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioData data = mapperUsuario.toData(usuario);
        return mapperUsuario.toUsuario(repository.save(data));
    }

    @Override
    public Usuario actualizar(Usuario usuario) {
        if (!repository.existsById(usuario.getCedula())) {
            throw new RuntimeException("Estudiante no encontrado con cedula: " + usuario.getCedula());
        }
        UsuarioData data = mapperUsuario.toData(usuario);
        return mapperUsuario.toUsuario(repository.save(data));
    }

    @Override
    public void eliminar(String cedula) {
        if (!repository.existsById(cedula)) {
            throw new RuntimeException("Estudiante no encontrado con cedula: " + cedula);
        }
        repository.deleteById(cedula);
    }

    @Override
    public Usuario buscarPorCedula(String cedula) {
        return repository.findById(cedula)
                .map(mapperUsuario::toUsuario)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con cedula: " + cedula));
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        return repository.findByCorreoInstitucional(correo)
                .map(mapperUsuario::toUsuario)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con correo: " + correo));
    }

}
