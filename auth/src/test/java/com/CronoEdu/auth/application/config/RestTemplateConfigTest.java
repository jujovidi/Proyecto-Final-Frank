package com.CronoEdu.auth.application.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class RestTemplateConfigTest {

    private final RestTemplateConfig config = new RestTemplateConfig();

    @Test
    void restTemplate_DeberiaCrearBean() {
        RestTemplate template = config.restTemplate();
        assertNotNull(template);
    }
}
