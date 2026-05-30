package com.CronoEdu.auth.infraestructure.notification;

import com.CronoEdu.auth.domain.model.Notificacion;
import com.CronoEdu.auth.domain.model.gateway.NotificationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NotificationGatewayImpl implements NotificationGateway {

    private final RestTemplate restTemplate;

    private final String notificationUrl = "http://localhost:8083/api/notificacion/enviar";

    @Override
    public void enviarMensaje(Notificacion notificacion) {
        try {
            restTemplate.postForEntity(notificationUrl, notificacion, String.class);
            System.out.println("Notificacion enviada: " + notificacion.getTipo());
        } catch (Exception e) {
            System.out.println("Error al enviar notificacion: " + e.getMessage());
        }
    }

}
