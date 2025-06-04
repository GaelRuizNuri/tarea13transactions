package com.example.tarea13transactions.Services;


import com.example.tarea13transactions.Entities.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> findAll();
    Optional<Categoria> findById(Long id);
    Categoria save(Categoria categoria);
    Categoria update(Long id, Categoria categoriaDetails);
    void deleteById(Long id);
}