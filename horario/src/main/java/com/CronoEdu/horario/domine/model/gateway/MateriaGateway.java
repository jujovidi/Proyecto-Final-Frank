package com.CronoEdu.horario.domine.model.gateway;

import com.CronoEdu.horario.domine.model.Materia;

import java.util.List;

public interface MateriaGateway {

    Materia guardarMateria(Materia materia);

    Materia actualizarMateria(Materia materia);

    Materia buscarMateria(String id);

    List<Materia> listarTodas();

    List<Materia> buscarPorDia(String diaSemana);

    List<Materia> buscarPorEstudiante(String estudianteCedula);

    void eliminarMateria(String id);

}
