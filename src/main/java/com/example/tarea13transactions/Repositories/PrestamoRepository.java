package com.example.tarea13transactions.Repositories;
import com.example.tarea13transactions.Entities.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByUsuarioId(Long usuarioId);
    List<Prestamo> findByEstado(Prestamo.EstadoPrestamo estado);
}