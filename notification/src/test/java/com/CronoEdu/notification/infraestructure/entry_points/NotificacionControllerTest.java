package com.CronoEdu.notification.infraestructure.entry_points;

import com.CronoEdu.notification.application.service.NotificacionService;
import com.CronoEdu.notification.domain.model.Notificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NotificacionControllerTest {

    @Mock
    private NotificacionService notificacionService;

    private MockMvc mockMvc;
    private static final String JSON_NOTIF = """
            {"tipo":"REGISTRO","email":"test@test.com","mensaje":"Mensaje"}""";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new NotificacionController(notificacionService)
        ).build();
    }

    @Test
    void enviar_DeberiaRetornar200_CuandoExitoso() throws Exception {
        doNothing().when(notificacionService).procesar(any(Notificacion.class));

        mockMvc.perform(post("/api/notificacion/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_NOTIF))
                .andExpect(status().isOk())
                .andExpect(content().string("Notificacion recibida"));
    }

    @Test
    void enviar_DeberiaManejarExcepcion() throws Exception {
        doThrow(new RuntimeException("Error")).when(notificacionService).procesar(any(Notificacion.class));

        mockMvc.perform(post("/api/notificacion/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_NOTIF))
                .andExpect(status().isInternalServerError());
    }
}
