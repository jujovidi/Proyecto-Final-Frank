package com.CronoEdu.horario.application.config;

import com.CronoEdu.horario.domine.model.gateway.MateriaGateway;
import com.CronoEdu.horario.domine.usecase.MateriaUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public MateriaUsecase materiaUsecase(MateriaGateway materiaGateway) {
        return new MateriaUsecase(materiaGateway);
    }

}
