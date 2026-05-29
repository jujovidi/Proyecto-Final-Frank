package com.CronoEdu.horario.domine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Horario {

    private String id;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;

}
