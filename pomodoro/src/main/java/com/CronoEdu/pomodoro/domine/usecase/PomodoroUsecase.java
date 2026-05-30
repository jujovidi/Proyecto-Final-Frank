package com.CronoEdu.pomodoro.domine.usecase;

import com.CronoEdu.pomodoro.domine.model.Pomodoro;
import com.CronoEdu.pomodoro.domine.model.gateway.PomodoroGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PomodoroUsecase {

    private final PomodoroGateway pomodoroGateway;

    public Pomodoro obtenerEstado(String estudianteCedula) {
        if (estudianteCedula == null || estudianteCedula.isBlank()) {
            throw new IllegalArgumentException("La cedula del estudiante es obligatoria");
        }
        try {
            return pomodoroGateway.buscarPorEstudiante(estudianteCedula);
        } catch (RuntimeException e) {
            Pomodoro nuevo = new Pomodoro(null, estudianteCedula, 0);
            return pomodoroGateway.guardar(nuevo);
        }
    }

    public Pomodoro completarCiclo(String estudianteCedula) {
        if (estudianteCedula == null || estudianteCedula.isBlank()) {
            throw new IllegalArgumentException("La cedula del estudiante es obligatoria");
        }

        Pomodoro pomodoro = obtenerEstado(estudianteCedula);
        pomodoro.setCiclosCompletados(pomodoro.getCiclosCompletados() + 1);
        return pomodoroGateway.guardar(pomodoro);
    }

    public Pomodoro reiniciar(String estudianteCedula) {
        if (estudianteCedula == null || estudianteCedula.isBlank()) {
            throw new IllegalArgumentException("La cedula del estudiante es obligatoria");
        }

        Pomodoro pomodoro = obtenerEstado(estudianteCedula);
        pomodoro.setCiclosCompletados(0);
        return pomodoroGateway.guardar(pomodoro);
    }

}
