package com.CronoEdu.pomodoro.infraestructure.entry_points;

import com.CronoEdu.pomodoro.domine.model.Pomodoro;
import com.CronoEdu.pomodoro.domine.usecase.PomodoroUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pomodoro")
@RequiredArgsConstructor
public class PomodoroController {

    private final PomodoroUsecase pomodoroUsecase;

    @GetMapping("/{cedula}")
    public ResponseEntity<?> obtenerEstado(@PathVariable String cedula) {
        try {
            Pomodoro pomodoro = pomodoroUsecase.obtenerEstado(cedula);
            return new ResponseEntity<>(pomodoro, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{cedula}/completar-ciclo")
    public ResponseEntity<?> completarCiclo(@PathVariable String cedula) {
        try {
            Pomodoro pomodoro = pomodoroUsecase.completarCiclo(cedula);
            return new ResponseEntity<>(pomodoro, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{cedula}/reiniciar")
    public ResponseEntity<?> reiniciar(@PathVariable String cedula) {
        try {
            Pomodoro pomodoro = pomodoroUsecase.reiniciar(cedula);
            return new ResponseEntity<>(pomodoro, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
