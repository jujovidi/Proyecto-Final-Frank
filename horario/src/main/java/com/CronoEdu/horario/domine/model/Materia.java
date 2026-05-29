package com.CronoEdu.horario.domine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Materia {

    private String id;
    private String nombre;
    private String docente;
    private String salon;
    private List<Horario> horarios;

}
