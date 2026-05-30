package com.CronoEdu.notification.infraestructure.evento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionEventoDTO {

    private String tipo;
    private String email;
    private String telefono;
    private String mensaje;

}
