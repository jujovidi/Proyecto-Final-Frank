package com.CronoEdu.auth.infraestructure.entry_points;

import com.CronoEdu.auth.domain.model.Usuario;
import com.CronoEdu.auth.domain.usecase.UsuarioUseCase;
import com.CronoEdu.auth.infraestructure.driver_adapters.jpa_repository.UsuarioData;
import com.CronoEdu.auth.infraestructure.mapper.MapperUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final MapperUsuario mapperUsuario;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioData usuarioData) {
        try {
            Usuario usuario = mapperUsuario.toUsuario(usuarioData);
            Usuario usuarioGuardado = usuarioUseCase.registrar(usuario);
            return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al registrar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioData usuarioData) {
        try {
            Usuario usuarioLogueado = usuarioUseCase.login(
                    usuarioData.getCorreoInstitucional(),
                    usuarioData.getPassword()
            );
            return new ResponseEntity<>(usuarioLogueado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al iniciar sesion", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/estudiantes/{cedula}")
    public ResponseEntity<?> actualizar(@PathVariable String cedula, @RequestBody UsuarioData usuarioData) {
        try {
            usuarioData.setCedula(cedula);
            Usuario usuario = mapperUsuario.toUsuario(usuarioData);
            Usuario usuarioActualizado = usuarioUseCase.actualizarUsuario(usuario);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al actualizar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/estudiantes/{cedula}")
    public ResponseEntity<?> eliminar(@PathVariable String cedula) {
        try {
            usuarioUseCase.eliminarUsuario(cedula);
            return ResponseEntity.ok("Estudiante eliminado exitosamente");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al eliminar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/estudiantes/{cedula}")
    public ResponseEntity<?> buscarPorCedula(@PathVariable String cedula) {
        try {
            Usuario usuario = usuarioUseCase.buscarPorCedula(cedula);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
