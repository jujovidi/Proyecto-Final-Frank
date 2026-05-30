package com.CronoEdu.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion {
    private String tipo;
    private String email;
    private String mensaje;
}
