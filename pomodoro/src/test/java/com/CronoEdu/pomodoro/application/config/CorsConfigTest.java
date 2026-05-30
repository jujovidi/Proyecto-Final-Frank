package com.CronoEdu.pomodoro.application.config;

import com.CronoEdu.pomodoro.domine.model.gateway.PomodoroGateway;
import com.CronoEdu.pomodoro.domine.usecase.PomodoroUsecase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CorsConfigTest {

    private final CorsConfig config = new CorsConfig();

    @Test
    void corsConfigurer_DeberiaCrearBean() {
        WebMvcConfigurer configurer = config.corsConfigurer();
        assertNotNull(configurer);
    }
}
