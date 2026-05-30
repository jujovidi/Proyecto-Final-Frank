package com.CronoEdu.pomodoro.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pomodoros")
@Data
public class PomodoroData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 20, unique = true)
    private String estudianteCedula;

    @Column(nullable = false)
    private Integer ciclosCompletados;

}
