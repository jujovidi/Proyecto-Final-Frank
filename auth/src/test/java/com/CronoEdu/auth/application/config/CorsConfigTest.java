package com.CronoEdu.auth.application.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private final CorsConfig config = new CorsConfig();

    @Test
    void corsConfigurer_DeberiaCrearBean() {
        WebMvcConfigurer configurer = config.corsConfigurer();
        assertNotNull(configurer);
    }
}
