package com.CronoEdu.notification.domine.usecase;

import com.CronoEdu.notification.domine.model.gateway.EmailGateway;
import com.CronoEdu.notification.domine.model.gateway.SmsGateway;
import com.CronoEdu.notification.infraestructure.evento.dto.NotificacionEventoDTO;

public class NotificacionUsecase {

    private final EmailGateway emailGateway;
    private final SmsGateway smsGateway;

    public NotificacionUsecase(EmailGateway emailGateway, SmsGateway smsGateway) {
        this.emailGateway = emailGateway;
        this.smsGateway = smsGateway;
    }

    public void procesar(NotificacionEventoDTO evento) {
        switch (evento.getTipo()) {
            case "EMAIL" -> emailGateway.enviarEmail(evento.getEmail(), "Notificación", evento.getMensaje());
            case "SMS" -> smsGateway.enviarSms(evento.getTelefono(), evento.getMensaje());
            default -> throw new IllegalArgumentException("Tipo no soportado: " + evento.getTipo());
        }
    }

}
