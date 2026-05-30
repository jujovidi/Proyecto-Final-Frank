package com.CronoEdu.pomodoro.domine.model.gateway;

import com.CronoEdu.pomodoro.domine.model.Pomodoro;

public interface PomodoroGateway {

    Pomodoro guardar(Pomodoro pomodoro);

    Pomodoro buscarPorEstudiante(String estudianteCedula);

}
