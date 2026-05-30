package com.CronoEdu.auth.infraestructure.notification;

import com.CronoEdu.auth.domain.model.Notificacion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationGatewayImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificationGatewayImpl gateway;

    @Test
    void enviarMensaje_DeberiaLlamarRestTemplate() {
        Notificacion notif = Notificacion.builder()
                .tipo("REGISTRO")
                .email("test@test.com")
                .mensaje("Mensaje")
                .build();

        when(restTemplate.postForEntity(any(String.class), any(), eq(String.class)))
                .thenReturn(null);

        gateway.enviarMensaje(notif);

        verify(restTemplate).postForEntity(
                eq("http://localhost:8083/api/notificacion/enviar"),
                eq(notif),
                eq(String.class)
        );
    }

    @Test
    void enviarMensaje_NoDeberiaLanzarExcepcion_CuandoRestTemplateFalla() {
        Notificacion notif = Notificacion.builder()
                .tipo("REGISTRO")
                .email("test@test.com")
                .mensaje("Mensaje")
                .build();

        when(restTemplate.postForEntity(any(String.class), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Error de conexion"));

        try {
            gateway.enviarMensaje(notif);
        } catch (Exception e) {
            throw new AssertionError("No deberia lanzar excepcion", e);
        }
    }
}
