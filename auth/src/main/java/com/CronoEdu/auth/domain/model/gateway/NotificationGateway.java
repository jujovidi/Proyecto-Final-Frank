package com.CronoEdu.auth.domain.model.gateway;

import com.CronoEdu.auth.domain.model.Notificacion;

public interface NotificationGateway {

    void enviarMensaje(Notificacion notificacion);
}
