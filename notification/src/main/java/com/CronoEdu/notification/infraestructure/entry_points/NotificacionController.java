package com.CronoEdu.notification.infraestructure.entry_points;

import com.CronoEdu.notification.application.service.NotificacionService;
import com.CronoEdu.notification.domain.model.Notificacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificacion")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping("/enviar")
    public ResponseEntity<String> recibirNotificacion(@RequestBody Notificacion notificacion) {
        try {
            notificacionService.procesar(notificacion);
            return ResponseEntity.ok("Notificacion recibida");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar notificacion: " + e.getMessage());
        }
    }

}
