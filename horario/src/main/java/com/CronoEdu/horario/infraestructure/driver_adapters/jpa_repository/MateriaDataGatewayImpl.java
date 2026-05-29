package com.CronoEdu.horario.infraestructure.driver_adapters.jpa_repository;

import com.CronoEdu.horario.domine.model.Materia;
import com.CronoEdu.horario.domine.model.gateway.MateriaGateway;
import com.CronoEdu.horario.infraestructure.mapper.MateriaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MateriaDataGatewayImpl implements MateriaGateway {

    private final MateriaDataJpaRepository materiaDataJpaRepository;
    private final MateriaMapper materiaMapper;

    @Override
    public Materia guardarMateria(Materia materia) {
        MateriaData materiaData = materiaMapper.toMateriaData(materia);

        if (materiaDataJpaRepository.existsById(materiaData.getId())) {
            throw new IllegalArgumentException(
                    "Ya existe una materia con el ID: " + materiaData.getId()
            );
        }

        MateriaData materiaGuardada = materiaDataJpaRepository.save(materiaData);
        return materiaMapper.toMateria(materiaGuardada);
    }

    @Override
    public Materia actualizarMateria(Materia materia) {
        if (!materiaDataJpaRepository.existsById(materia.getId())) {
            throw new IllegalArgumentException(
                    "No existe una materia con el ID: " + materia.getId()
            );
        }

        MateriaData materiaData = materiaMapper.toMateriaData(materia);
        MateriaData materiaActualizada = materiaDataJpaRepository.save(materiaData);
        return materiaMapper.toMateria(materiaActualizada);
    }

    @Override
    public Materia buscarMateria(String id) {
        return materiaDataJpaRepository.findById(id)
                .map(materiaMapper::toMateria)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada con ID: " + id));
    }

    @Override
    public List<Materia> listarTodas() {
        List<MateriaData> todas = materiaDataJpaRepository.findAll();
        return todas.stream()
                .map(materiaMapper::toMateria)
                .toList();
    }

    @Override
    public List<Materia> buscarPorDia(String diaSemana) {
        List<MateriaData> porDia = materiaDataJpaRepository.findByHorariosDiaSemana(diaSemana);
        return porDia.stream()
                .map(materiaMapper::toMateria)
                .toList();
    }

    @Override
    public void eliminarMateria(String id) {
        if (!materiaDataJpaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: materia no encontrada con ID: " + id);
        }
        materiaDataJpaRepository.deleteById(id);
    }

}
