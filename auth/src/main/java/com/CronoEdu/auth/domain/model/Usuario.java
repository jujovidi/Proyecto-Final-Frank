package com.CronoEdu.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private String cedula;
    private String nombre;
    private String carrera;
    private String ubicacionSemestral;
    private String correoInstitucional;
    private String password;

}
