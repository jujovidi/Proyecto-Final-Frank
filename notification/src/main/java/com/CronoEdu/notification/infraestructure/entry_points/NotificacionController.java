package com.CronoEdu.notification.infraestructure.entry_points;

import com.CronoEdu.notification.domine.usecase.NotificacionUsecase;
import com.CronoEdu.notification.infraestructure.evento.dto.NotificacionEventoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificacion")
public class NotificacionController {

    private final NotificacionUsecase notificacionUsecase;

    public NotificacionController(NotificacionUsecase notificacionUsecase) {
        this.notificacionUsecase = notificacionUsecase;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviar(@RequestBody NotificacionEventoDTO evento) {
        try {
            notificacionUsecase.procesar(evento);
            return ResponseEntity.ok("Notificación enviada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
