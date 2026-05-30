package com.CronoEdu.notification.application.service;

import com.CronoEdu.notification.domain.model.Notificacion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final JavaMailSender mailSender;

    @Value("${app.notification.email.from}")
    private String from;

    @Value("${app.notification.email.enabled:true}")
    private boolean emailEnabled;

    public void procesar(Notificacion notificacion) {
        System.out.println("Notificacion recibida - Tipo: " + notificacion.getTipo()
                + ", Email: " + notificacion.getEmail()
                + ", Mensaje: " + notificacion.getMensaje());

        if (emailEnabled && notificacion.getEmail() != null && !notificacion.getEmail().isBlank()) {
            enviarEmail(notificacion);
        }
    }

    private void enviarEmail(Notificacion notificacion) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(from);
            mensaje.setTo(notificacion.getEmail());
            mensaje.setSubject("CronoEdu - " + asuntoPorTipo(notificacion.getTipo()));
            mensaje.setText(notificacion.getMensaje());
            mailSender.send(mensaje);
            System.out.println("Email enviado a: " + notificacion.getEmail());
        } catch (Exception e) {
            System.out.println("Error al enviar email: " + e.getMessage());
        }
    }

    private String asuntoPorTipo(String tipo) {
        return switch (tipo) {
            case "REGISTRO" -> "Registro Exitoso";
            case "LOGIN" -> "Inicio de Sesion";
            case "ACTUALIZACION" -> "Datos Actualizados";
            case "ELIMINACION" -> "Cuenta Eliminada";
            default -> "Notificacion";
        };
    }

}
