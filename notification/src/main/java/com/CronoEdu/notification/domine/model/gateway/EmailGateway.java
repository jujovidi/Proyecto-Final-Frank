package com.CronoEdu.notification.domine.model.gateway;

public interface EmailGateway {
    void enviarEmail(String para, String asunto, String mensaje);
}
