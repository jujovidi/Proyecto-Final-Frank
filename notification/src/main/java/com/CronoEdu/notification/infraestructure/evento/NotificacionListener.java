package com.CronoEdu.notification.infraestructure.evento;

import com.CronoEdu.notification.domine.usecase.NotificacionUsecase;
import com.CronoEdu.notification.infraestructure.evento.dto.NotificacionEventoDTO;
import org.springframework.stereotype.Service;

@Service
public class NotificacionListener {

    private final NotificacionUsecase notificacionUsecase;

    public NotificacionListener(NotificacionUsecase notificacionUsecase) {
        this.notificacionUsecase = notificacionUsecase;
    }

    public void procesarEvento(NotificacionEventoDTO evento) {
        notificacionUsecase.procesar(evento);
    }

}
