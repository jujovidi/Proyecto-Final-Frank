package com.CronoEdu.notification.application.service;

import com.CronoEdu.notification.domain.model.Notificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private NotificacionService notificacionService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @BeforeEach
    void setUp() {
        notificacionService = new NotificacionService(mailSender);
        ReflectionTestUtils.setField(notificacionService, "from", "cronoedu@gmail.com");
        ReflectionTestUtils.setField(notificacionService, "emailEnabled", true);
    }

    // ========================================
    // procesar() con email habilitado
    // ========================================
    @Test
    void procesar_DeberiaEnviarEmail_CuandoTipoEsRegistro() {
        Notificacion notif = new Notificacion("REGISTRO", "estudiante@universidad.edu",
                "Usuario registrado exitosamente: Juan");

        notificacionService.procesar(notif);

        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage mensaje = messageCaptor.getValue();
        assertEquals("cronoedu@gmail.com", mensaje.getFrom());
        assertEquals("estudiante@universidad.edu", mensaje.getTo()[0]);
        assertTrue(mensaje.getSubject().contains("Registro Exitoso"));
        assertEquals("Usuario registrado exitosamente: Juan", mensaje.getText());
    }

    @Test
    void procesar_DeberiaEnviarEmail_CuandoTipoEsLogin() {
        Notificacion notif = new Notificacion("LOGIN", "estudiante@universidad.edu",
                "Inicio de sesion exitoso");

        notificacionService.procesar(notif);

        verify(mailSender).send(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getSubject().contains("Inicio de Sesion"));
    }

    @Test
    void procesar_DeberiaEnviarEmail_CuandoTipoEsActualizacion() {
        Notificacion notif = new Notificacion("ACTUALIZACION", "estudiante@universidad.edu",
                "Usuario actualizado");

        notificacionService.procesar(notif);

        verify(mailSender).send(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getSubject().contains("Datos Actualizados"));
    }

    @Test
    void procesar_DeberiaEnviarEmail_CuandoTipoEsEliminacion() {
        Notificacion notif = new Notificacion("ELIMINACION", "estudiante@universidad.edu",
                "Cuenta eliminada");

        notificacionService.procesar(notif);

        verify(mailSender).send(messageCaptor.capture());
        assertTrue(messageCaptor.getValue().getSubject().contains("Cuenta Eliminada"));
    }

    @Test
    void procesar_DeberiaEnviarEmail_CuandoTipoEsDesconocido() {
        Notificacion notif = new Notificacion("OTRO", "estudiante@universidad.edu",
                "Mensaje generico");

        notificacionService.procesar(notif);

        verify(mailSender).send(messageCaptor.capture());
        assertEquals("CronoEdu - Notificacion", messageCaptor.getValue().getSubject());
    }

    // ========================================
    // procesar() con email deshabilitado
    // ========================================
    @Test
    void procesar_NoDeberiaEnviarEmail_CuandoEmailDisabled() {
        ReflectionTestUtils.setField(notificacionService, "emailEnabled", false);
        Notificacion notif = new Notificacion("REGISTRO", "estudiante@universidad.edu",
                "Mensaje");

        notificacionService.procesar(notif);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    // ========================================
    // procesar() con email null o vacio
    // ========================================
    @Test
    void procesar_NoDeberiaEnviarEmail_CuandoEmailEsNull() {
        Notificacion notif = new Notificacion("REGISTRO", null, "Mensaje");

        notificacionService.procesar(notif);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void procesar_NoDeberiaEnviarEmail_CuandoEmailEsVacio() {
        Notificacion notif = new Notificacion("REGISTRO", "", "Mensaje");

        notificacionService.procesar(notif);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void procesar_NoDeberiaEnviarEmail_CuandoEmailEsBlank() {
        Notificacion notif = new Notificacion("REGISTRO", "   ", "Mensaje");

        notificacionService.procesar(notif);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    // ========================================
    // manejo de errores al enviar email
    // ========================================
    @Test
    void procesar_NoDeberiaLanzarExcepcion_CuandoMailSenderFalla() {
        doThrow(new RuntimeException("SMTP error"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        Notificacion notif = new Notificacion("REGISTRO", "estudiante@universidad.edu",
                "Mensaje");

        assertDoesNotThrow(() -> notificacionService.procesar(notif));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
