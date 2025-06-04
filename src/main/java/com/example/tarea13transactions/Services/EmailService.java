package com.example.tarea13transactions.Services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void enviarNotificacionPrestamo(String email, String tituloLibro) {
        System.out.println("Enviando notificación a: " + email);
        System.out.println("Mensaje: Has prestado el libro '" + tituloLibro + "'");
        throw new RuntimeException("Caída del sistema de notificaciones");
    }
}