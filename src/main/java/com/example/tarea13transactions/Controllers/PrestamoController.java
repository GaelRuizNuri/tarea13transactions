package com.example.tarea13transactions.Controllers;
import com.example.tarea13transactions.Entities.Prestamo;
import com.example.tarea13transactions.Services.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping
    public List<Prestamo> getAllPrestamos() {
        return prestamoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> getPrestamoById(@PathVariable Long id) {
        return prestamoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Prestamo> getPrestamosByUsuario(@PathVariable Long usuarioId) {
        return prestamoService.findByUsuarioId(usuarioId);
    }

    @GetMapping("/estado/{estado}")
    public List<Prestamo> getPrestamosByEstado(@PathVariable Prestamo.EstadoPrestamo estado) {
        return prestamoService.findByEstado(estado);
    }

    @PostMapping
    public Prestamo createPrestamo(@RequestParam Long libroId, @RequestParam Long usuarioId) {
        return prestamoService.realizarPrestamo(libroId, usuarioId);
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolverLibro(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolverLibro(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizarPrestamo(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates) {

        if (updates.containsKey("estado")) {
            return ResponseEntity.ok(prestamoService.actualizarEstado(id, updates.get("estado")));
        }

        throw new RuntimeException("Campo no soportado para actualización");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        prestamoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}