package com.CronoEdu.horario.infraestructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MateriaDataJpaRepository extends JpaRepository<MateriaData, String> {

    @Query("SELECT DISTINCT m FROM MateriaData m JOIN FETCH m.horarios h WHERE h.diaSemana = :dia")
    List<MateriaData> findByHorariosDiaSemana(@Param("dia") String diaSemana);

}
