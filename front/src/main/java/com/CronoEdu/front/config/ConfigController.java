package com.CronoEdu.front.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    @GetMapping(value = "/config.js", produces = "application/javascript")
    public String configJs() {
        String authUrl = getEnv("AUTH_URL", "http://localhost:8081");
        String horarioUrl = getEnv("HORARIO_URL", "http://localhost:8082");
        String pomodoroUrl = getEnv("POMODORO_URL", "http://localhost:8084");

        return """
            var AUTH_API = '%s/api/auth';
            var HORARIO_API = '%s/api/horario/materias';
            var POMODORO_API = '%s/api/pomodoro';
            """.formatted(authUrl, horarioUrl, pomodoroUrl);
    }

    private String getEnv(String key, String def) {
        String val = System.getenv(key);
        return val != null && !val.isEmpty() ? val : def;
    }
}
