package com.CronoEdu.horario.infraestructure.entry_points;

import com.CronoEdu.horario.domine.model.Materia;
import com.CronoEdu.horario.domine.usecase.MateriaUsecase;
import com.CronoEdu.horario.infraestructure.driver_adapters.jpa_repository.MateriaData;
import com.CronoEdu.horario.infraestructure.mapper.MateriaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horario/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaUsecase materiaUsecase;
    private final MateriaMapper materiaMapper;

    @PostMapping("/save")
    public ResponseEntity<?> guardarMateria(@RequestBody MateriaData materiaData) {
        if (materiaData == null) {
            return new ResponseEntity<>("El cuerpo de la solicitud no puede estar vacio", HttpStatus.BAD_REQUEST);
        }
        try {
            Materia materiaMapeada = materiaMapper.toMateria(materiaData);
            Materia materiaGuardada = materiaUsecase.guardarMateria(materiaMapeada);

            if (materiaGuardada != null && materiaGuardada.getId() != null) {
                return new ResponseEntity<>(materiaGuardada, HttpStatus.CREATED);
            }
            return new ResponseEntity<>("No se pudo guardar la materia", HttpStatus.CONFLICT);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al guardar la materia", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> actualizarMateria(@RequestBody MateriaData materiaData) {
        if (materiaData == null) {
            return new ResponseEntity<>("El cuerpo de la solicitud no puede estar vacio", HttpStatus.BAD_REQUEST);
        }
        try {
            Materia materiaMapeada = materiaMapper.toMateria(materiaData);
            Materia materiaActualizada = materiaUsecase.actualizarMateria(materiaMapeada);

            if (materiaActualizada != null && materiaActualizada.getId() != null) {
                return new ResponseEntity<>(materiaActualizada, HttpStatus.OK);
            }
            return new ResponseEntity<>("No se pudo actualizar la materia", HttpStatus.CONFLICT);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al actualizar la materia", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMateria(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            return new ResponseEntity<>("El ID de la materia es obligatorio", HttpStatus.BAD_REQUEST);
        }
        try {
            Materia materiaEncontrada = materiaUsecase.buscarMateria(id);

            if (materiaEncontrada != null && materiaEncontrada.getId() != null) {
                return new ResponseEntity<>(materiaEncontrada, HttpStatus.OK);
            }
            return new ResponseEntity<>("Materia no encontrada con ID: " + id, HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al buscar la materia", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> listarTodas() {
        try {
            List<Materia> materias = materiaUsecase.listarTodas();
            return new ResponseEntity<>(materias, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al listar materias", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dia/{diaSemana}")
    public ResponseEntity<?> buscarPorDia(@PathVariable String diaSemana) {
        try {
            List<Materia> materias = materiaUsecase.buscarPorDia(diaSemana);
            return new ResponseEntity<>(materias, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al buscar materias por dia", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMateria(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            return new ResponseEntity<>("El ID de la materia es obligatorio", HttpStatus.BAD_REQUEST);
        }
        try {
            materiaUsecase.eliminarMateria(id);
            return ResponseEntity.ok("Materia eliminada exitosamente");

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al eliminar la materia", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
