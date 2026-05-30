package com.CronoEdu.horario.domine.usecase;

import com.CronoEdu.horario.domine.model.Horario;
import com.CronoEdu.horario.domine.model.Materia;
import com.CronoEdu.horario.domine.model.gateway.MateriaGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class MateriaUsecase {

    private final MateriaGateway materiaGateway;

    private static final Set<String> DIAS_VALIDOS = Set.of(
            "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO"
    );

    private static final String HORA_REGEX = "^([01]\\d|2[0-3]):[0-5]\\d$";

    public Materia guardarMateria(Materia materia) {
        validarMateria(materia, false);
        return materiaGateway.guardarMateria(materia);
    }

    public Materia actualizarMateria(Materia materia) {
        validarMateria(materia, true);
        return materiaGateway.actualizarMateria(materia);
    }

    public Materia buscarMateria(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de la materia es obligatorio");
        }
        return materiaGateway.buscarMateria(id);
    }

    public List<Materia> listarTodas() {
        return materiaGateway.listarTodas();
    }

    public List<Materia> buscarPorDia(String diaSemana) {
        if (diaSemana == null || diaSemana.isBlank()) {
            throw new IllegalArgumentException("El dia es obligatorio");
        }
        String dia = diaSemana.toUpperCase();
        if (!DIAS_VALIDOS.contains(dia)) {
            throw new IllegalArgumentException("Dia invalido. Use: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO");
        }
        return materiaGateway.buscarPorDia(dia);
    }

    public List<Materia> buscarPorEstudiante(String estudianteCedula) {
        if (estudianteCedula == null || estudianteCedula.isBlank()) {
            throw new IllegalArgumentException("La cedula del estudiante es obligatoria");
        }
        return materiaGateway.buscarPorEstudiante(estudianteCedula);
    }

    public void eliminarMateria(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de la materia es obligatorio para eliminarla");
        }
        materiaGateway.eliminarMateria(id);
    }

    private void validarMateria(Materia materia, boolean esActualizacion) {
        if (materia == null) {
            throw new IllegalArgumentException("La materia no puede ser nula");
        }

        if (!esActualizacion && (materia.getId() == null || materia.getId().isBlank())) {
            throw new IllegalArgumentException("El ID de la materia es obligatorio");
        }

        if (materia.getNombre() == null || materia.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la materia es obligatorio");
        }

        if (materia.getDocente() == null || materia.getDocente().isBlank()) {
            throw new IllegalArgumentException("El docente es obligatorio");
        }

        if (materia.getSalon() == null || materia.getSalon().isBlank()) {
            throw new IllegalArgumentException("El salon es obligatorio");
        }

        if (materia.getEstudianteCedula() == null || materia.getEstudianteCedula().isBlank()) {
            throw new IllegalArgumentException("La cedula del estudiante es obligatoria");
        }

        if (materia.getHorarios() == null || materia.getHorarios().isEmpty()) {
            throw new IllegalArgumentException("Debe asignar al menos un horario a la materia");
        }

        for (Horario horario : materia.getHorarios()) {
            validarHorario(horario);
        }
    }

    private void validarHorario(Horario horario) {
        if (horario.getDiaSemana() == null || horario.getDiaSemana().isBlank()) {
            throw new IllegalArgumentException("El dia de la semana es obligatorio en cada horario");
        }

        String dia = horario.getDiaSemana().toUpperCase();
        if (!DIAS_VALIDOS.contains(dia)) {
            throw new IllegalArgumentException(
                    "Dia invalido: " + horario.getDiaSemana() + ". Use: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO"
            );
        }
        horario.setDiaSemana(dia);

        if (horario.getHoraInicio() == null || !horario.getHoraInicio().matches(HORA_REGEX)) {
            throw new IllegalArgumentException(
                    "Hora de inicio invalida: " + horario.getHoraInicio() + ". Use formato HH:mm (ej: 08:00)"
            );
        }

        if (horario.getHoraFin() == null || !horario.getHoraFin().matches(HORA_REGEX)) {
            throw new IllegalArgumentException(
                    "Hora de fin invalida: " + horario.getHoraFin() + ". Use formato HH:mm (ej: 10:00)"
            );
        }

        if (horario.getHoraInicio().compareTo(horario.getHoraFin()) >= 0) {
            throw new IllegalArgumentException(
                    "La hora de inicio (" + horario.getHoraInicio() +
                    ") debe ser menor que la hora de fin (" + horario.getHoraFin() + ")"
            );
        }
    }

}
