package com.CronoEdu.notification.application.config;

import com.CronoEdu.notification.domine.model.gateway.EmailGateway;
import com.CronoEdu.notification.domine.model.gateway.SmsGateway;
import com.CronoEdu.notification.domine.usecase.NotificacionUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public NotificacionUsecase notificacionUsecase(EmailGateway emailGateway, SmsGateway smsGateway) {
        return new NotificacionUsecase(emailGateway, smsGateway);
    }

}
