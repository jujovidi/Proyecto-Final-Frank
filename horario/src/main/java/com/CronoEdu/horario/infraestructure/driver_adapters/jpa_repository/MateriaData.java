package com.CronoEdu.horario.infraestructure.driver_adapters.jpa_repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "materias")
@Data
public class MateriaData {

    @Id
    @Column(unique = true, nullable = false, length = 50)
    private String id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String docente;

    @Column(nullable = false, length = 50)
    private String salon;

    @Column(length = 20)
    private String estudianteCedula;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<HorarioData> horarios;

}
