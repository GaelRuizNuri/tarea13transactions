package com.example.tarea13transactions.Repositories;
import com.example.tarea13transactions.Entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByCategoriaId(Long categoriaId);
    List<Libro> findByDisponibleTrue();
    Optional<Libro> findByIsbn(String isbn);
}
