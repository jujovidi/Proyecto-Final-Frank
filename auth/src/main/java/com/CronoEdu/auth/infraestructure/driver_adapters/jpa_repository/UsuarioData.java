package com.CronoEdu.auth.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "estudiantes")
@Data
public class UsuarioData {

    @Id
    @Column(nullable = false, length = 20, unique = true)
    private String cedula;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String carrera;

    @Column(nullable = false, length = 20)
    private String ubicacionSemestral;

    @Column(nullable = false, length = 150, unique = true)
    private String correoInstitucional;

    @Column(nullable = false)
    private String password;

}
