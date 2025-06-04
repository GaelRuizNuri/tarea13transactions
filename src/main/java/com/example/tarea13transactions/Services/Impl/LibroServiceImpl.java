package com.example.tarea13transactions.Services.Impl;
import com.example.tarea13transactions.Entities.Categoria;
import com.example.tarea13transactions.Entities.Libro;
import com.example.tarea13transactions.Repositories.LibroRepository;
import com.example.tarea13transactions.Services.CategoriaService;
import com.example.tarea13transactions.Services.LibroService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibroServiceImpl implements LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Override
    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    @Override
    public Optional<Libro> findById(Long id) {
        return libroRepository.findById(id);
    }

    @Override
    public List<Libro> findByDisponibleTrue() {
        return libroRepository.findByDisponibleTrue();
    }

    @Override
    public List<Libro> findByCategoriaId(Long categoriaId) {
        return libroRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public Optional<Libro> findByIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn);
    }

    @Override
    public Libro save(Libro libro) {
        if(libro.getCategoria() != null && libro.getCategoria().getId() != null) {
            Categoria categoria = categoriaService.findById(libro.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            libro.setCategoria(categoria);
        }
        return libroRepository.save(libro);
    }

    @Override
    public Libro update(Long id, Libro libroDetails) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));

        libro.setTitulo(libroDetails.getTitulo());
        libro.setAutor(libroDetails.getAutor());
        libro.setIsbn(libroDetails.getIsbn());
        libro.setDisponible(libroDetails.getDisponible());
        libro.setFechaPublicacion(libroDetails.getFechaPublicacion());

        if(libroDetails.getCategoria() != null) {
            Categoria categoria = categoriaService.findById(libroDetails.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            libro.setCategoria(categoria);
        }

        return libroRepository.save(libro);
    }

    @Override
    public void deleteById(Long id) {
        libroRepository.deleteById(id);
    }
}