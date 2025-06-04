package com.example.tarea13transactions.Services;
import com.example.tarea13transactions.Entities.Prestamo;

import java.util.*;

public interface PrestamoService {
    List<Prestamo> findAll();
    Optional<Prestamo> findById(Long id);
    List<Prestamo> findByUsuarioId(Long usuarioId);
    List<Prestamo> findByEstado(Prestamo.EstadoPrestamo estado);
    Prestamo realizarPrestamo(Long libroId, Long usuarioId);
    Prestamo devolverLibro(Long id);
    void deleteById(Long id);
    Prestamo actualizarEstado(Long id, String estado);
}