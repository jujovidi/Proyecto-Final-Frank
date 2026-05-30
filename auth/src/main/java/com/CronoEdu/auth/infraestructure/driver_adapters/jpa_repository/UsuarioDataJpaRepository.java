package com.CronoEdu.auth.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioDataJpaRepository extends JpaRepository<UsuarioData, String> {

    Optional<UsuarioData> findByCorreoInstitucional(String correo);

}
