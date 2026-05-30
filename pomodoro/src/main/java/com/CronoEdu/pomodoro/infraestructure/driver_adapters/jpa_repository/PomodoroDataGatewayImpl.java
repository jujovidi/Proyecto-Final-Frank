package com.CronoEdu.pomodoro.infraestructure.driver_adapters.jpa_repository;

import com.CronoEdu.pomodoro.domine.model.Pomodoro;
import com.CronoEdu.pomodoro.domine.model.gateway.PomodoroGateway;
import com.CronoEdu.pomodoro.infraestructure.mapper.PomodoroMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PomodoroDataGatewayImpl implements PomodoroGateway {

    private final PomodoroDataJpaRepository repository;
    private final PomodoroMapper mapper;

    @Override
    public Pomodoro guardar(Pomodoro pomodoro) {
        PomodoroData data = mapper.toData(pomodoro);
        return mapper.toDomain(repository.save(data));
    }

    @Override
    public Pomodoro buscarPorEstudiante(String estudianteCedula) {
        return repository.findByEstudianteCedula(estudianteCedula)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Pomodoro no encontrado para: " + estudianteCedula));
    }

}
