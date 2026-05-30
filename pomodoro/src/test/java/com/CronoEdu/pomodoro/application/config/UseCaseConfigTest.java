package com.CronoEdu.pomodoro.application.config;

import com.CronoEdu.pomodoro.domine.model.gateway.PomodoroGateway;
import com.CronoEdu.pomodoro.domine.usecase.PomodoroUsecase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigTest {

    @Mock
    private PomodoroGateway pomodoroGateway;

    private final UseCaseConfig config = new UseCaseConfig();

    @Test
    void pomodoroUsecase_DeberiaCrearBean() {
        PomodoroUsecase useCase = config.pomodoroUsecase(pomodoroGateway);
        assertNotNull(useCase);
    }
}
