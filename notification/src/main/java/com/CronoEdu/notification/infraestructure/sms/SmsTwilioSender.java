package com.CronoEdu.notification.infraestructure.sms;

import com.CronoEdu.notification.domine.model.gateway.SmsGateway;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class SmsTwilioSender implements SmsGateway {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String phoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public void enviarSms(String numero, String mensaje) {
        Message.creator(
                new PhoneNumber(numero),
                new PhoneNumber(phoneNumber),
                mensaje
        ).create();
    }

}
