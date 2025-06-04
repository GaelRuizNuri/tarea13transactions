package com.example.tarea13transactions.Repositories;

import com.example.tarea13transactions.Entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
