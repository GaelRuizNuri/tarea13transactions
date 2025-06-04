package com.example.tarea13transactions.Services.Impl;

import com.example.tarea13transactions.Entities.Libro;
import com.example.tarea13transactions.Entities.Prestamo;
import com.example.tarea13transactions.Entities.Usuario;
import com.example.tarea13transactions.Repositories.PrestamoRepository;
import com.example.tarea13transactions.Services.EmailService;
import com.example.tarea13transactions.Services.LibroService;
import com.example.tarea13transactions.Services.PrestamoService;
import com.example.tarea13transactions.Services.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroService libroService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @Override
    public Prestamo realizarPrestamo(Long libroId, Long usuarioId) {
        // 1. Verificar disponibilidad del libro
        Libro libro = libroService.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (!libro.getDisponible()) {
            throw new RuntimeException("El libro no está disponible");
        }

        // 2. Verificar usuario existe
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!usuario.getActivo()) {
            throw new RuntimeException("El usuario " + usuario.getNombre() + " está penalizado.");
        }

        // 3. Actualizar estado del libro
        libro.setDisponible(false);
        libroService.save(libro);

        // 4. Registrar préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(LocalDateTime.now());
        prestamo.setFechaDevolucionPrevista(LocalDateTime.now().plusDays(15));
        prestamo.setEstado(Prestamo.EstadoPrestamo.ACTIVO);

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        emailService.enviarNotificacionPrestamo(usuario.getEmail(), libro.getTitulo());

        return prestamoGuardado;
    }
    @Override
    public List<Prestamo> findAll(){
        return prestamoRepository.findAll();
    }
    @Override
    public Optional<Prestamo> findById(Long id) {
        return prestamoRepository.findById(id);
    }

    @Override
    public List<Prestamo> findByUsuarioId(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Prestamo> findByEstado(Prestamo.EstadoPrestamo estado) {
        return prestamoRepository.findByEstado(estado);
    }



    @Override
    public Prestamo devolverLibro(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (prestamo.getEstado() == Prestamo.EstadoPrestamo.COMPLETADO) {
            throw new RuntimeException("El préstamo ya fue completado");
        }

        // Actualizar estado del préstamo
        prestamo.setFechaDevolucionReal(LocalDateTime.now());
        prestamo.setEstado(Prestamo.EstadoPrestamo.COMPLETADO);

        // Actualizar disponibilidad del libro
        Libro libro = prestamo.getLibro();
        libro.setDisponible(true);
        libroService.save(libro);

        return prestamoRepository.save(prestamo);
    }

    @Override
    public Prestamo actualizarEstado(Long id, String estado) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        try {
            Prestamo.EstadoPrestamo nuevoEstado = Prestamo.EstadoPrestamo.valueOf(estado);
            prestamo.setEstado(nuevoEstado);
            Libro libro = prestamo.getLibro();
            if(prestamo.getEstado()==Prestamo.EstadoPrestamo.VENCIDO){
                libro.setDisponible(false);
                Usuario usuario = prestamo.getUsuario();
                usuario.setActivo(false);
                usuarioService.save(usuario);
            }
            else if (prestamo.getEstado()==Prestamo.EstadoPrestamo.ACTIVO) {
                libro.setDisponible(false);

            } else {
                libro.setDisponible(true);
                Usuario usuario = prestamo.getUsuario();
                usuario.setActivo(true);
                usuarioService.save(usuario);
            }
            libroService.save(libro);
            return prestamoRepository.save(prestamo);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado no válido: " + estado);
        }
    }

    @Override
    public void deleteById(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (prestamo.getEstado() == Prestamo.EstadoPrestamo.ACTIVO) {
            Libro libro = prestamo.getLibro();
            libro.setDisponible(true);
            libroService.save(libro);
        }

        prestamoRepository.deleteById(id);
    }
}
