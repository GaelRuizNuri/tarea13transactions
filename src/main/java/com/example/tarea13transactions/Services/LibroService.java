package com.example.tarea13transactions.Services;

import com.example.tarea13transactions.Entities.Libro;

import java.util.*;

public interface LibroService {
    List<Libro> findAll();
    Optional<Libro> findById(Long id);
    List<Libro> findByDisponibleTrue();
    List<Libro> findByCategoriaId(Long categoriaId);
    Optional<Libro> findByIsbn(String isbn);
    Libro save(Libro libro);
    Libro update(Long id, Libro libroDetails);
    void deleteById(Long id);
}
