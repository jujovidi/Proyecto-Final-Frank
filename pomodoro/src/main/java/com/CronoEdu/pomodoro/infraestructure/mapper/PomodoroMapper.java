package com.CronoEdu.pomodoro.infraestructure.mapper;

import com.CronoEdu.pomodoro.domine.model.Pomodoro;
import com.CronoEdu.pomodoro.infraestructure.driver_adapters.jpa_repository.PomodoroData;
import org.springframework.stereotype.Component;

@Component
public class PomodoroMapper {

    public PomodoroData toData(Pomodoro domain) {
        if (domain == null) return null;
        return new PomodoroData(
                domain.getId(),
                domain.getEstudianteCedula(),
                domain.getCiclosCompletados()
        );
    }

    public Pomodoro toDomain(PomodoroData data) {
        if (data == null) return null;
        return new Pomodoro(
                data.getId(),
                data.getEstudianteCedula(),
                data.getCiclosCompletados()
        );
    }

}
