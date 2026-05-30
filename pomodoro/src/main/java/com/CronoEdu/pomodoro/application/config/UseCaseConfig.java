package com.CronoEdu.pomodoro.application.config;

import com.CronoEdu.pomodoro.domine.model.gateway.PomodoroGateway;
import com.CronoEdu.pomodoro.domine.usecase.PomodoroUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public PomodoroUsecase pomodoroUsecase(PomodoroGateway pomodoroGateway) {
        return new PomodoroUsecase(pomodoroGateway);
    }

}
