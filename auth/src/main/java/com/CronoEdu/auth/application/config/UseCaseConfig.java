package com.CronoEdu.auth.application.config;

import com.CronoEdu.auth.domain.model.gateway.EncrypterGateway;
import com.CronoEdu.auth.domain.model.gateway.NotificationGateway;
import com.CronoEdu.auth.domain.model.gateway.UsuarioGateway;
import com.CronoEdu.auth.domain.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway, EncrypterGateway encrypterGateway, NotificationGateway notificationGateway) {
        return new UsuarioUseCase(usuarioGateway, encrypterGateway, notificationGateway);
    }
}
