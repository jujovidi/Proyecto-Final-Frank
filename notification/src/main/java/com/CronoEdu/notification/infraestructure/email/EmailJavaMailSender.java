package com.CronoEdu.notification.infraestructure.email;

import com.CronoEdu.notification.domine.model.gateway.EmailGateway;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailJavaMailSender implements EmailGateway {

    private final JavaMailSender javaMailSender;

    public EmailJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void enviarEmail(String para, String asunto, String mensaje) {
        SimpleMailMessage correo = new SimpleMailMessage();
        correo.setTo(para);
        correo.setSubject(asunto);
        correo.setText(mensaje);
        javaMailSender.send(correo);
    }

}
