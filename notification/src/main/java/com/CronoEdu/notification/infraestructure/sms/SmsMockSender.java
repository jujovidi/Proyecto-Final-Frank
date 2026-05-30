package com.CronoEdu.notification.infraestructure.sms;

import com.CronoEdu.notification.domine.model.gateway.SmsGateway;
import org.springframework.stereotype.Service;

@Service
public class SmsMockSender implements SmsGateway {

    @Override
    public void enviarSms(String numero, String mensaje) {
        System.out.println("Enviando SMS a " + numero + ": " + mensaje);
    }

}
