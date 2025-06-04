package com.example.tarea13transactions.Services;

import com.example.tarea13transactions.Entities.Usuario;

import java.util.*;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    Usuario save(Usuario usuario);
    Usuario update(Long id, Usuario usuarioDetails);
    void deleteById(Long id);
}

