package com.CronoEdu.notification.domine.model.gateway;

public interface SmsGateway {
    void enviarSms(String numero, String mensaje);
}
