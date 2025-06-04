const API_BASE_URL = 'http://localhost:8080/library/api';
let modal = document.getElementById("modal-editar");

// Cargar préstamos al iniciar la página
document.addEventListener('DOMContentLoaded', function() {
    cargarPrestamos();
});

async function realizarPrestamo() {
    const usuarioId = document.getElementById('usuarioId').value;
    const libroId = document.getElementById('libroId').value;
    const resultadoDiv = document.getElementById('resultado');

    if (!usuarioId || !libroId) {
        mostrarResultado('Por favor complete todos los campos', 'error');
        return;
    }

    resultadoDiv.innerHTML = 'Procesando préstamo...';
    resultadoDiv.className = '';

    try {
        // 1. Verificar disponibilidad del libro
        const libro = await obtenerLibro(libroId);
        if (!libro.disponible) {
            throw new Error('El libro no está disponible');
        }

        // 2. Registrar el préstamo
        const prestamo = await registrarPrestamo(libroId, usuarioId);

        // 3. Actualizar estado del libro (manejado por el backend)

        // 4. Mostrar resultado y actualizar lista
        mostrarResultado(`
            Préstamo registrado exitosamente!<br>
            ID: ${prestamo.id}<br>
            Fecha de devolución: ${new Date(prestamo.fechaDevolucionPrevista).toLocaleDateString()}
        `, 'success');

        // Limpiar formulario y actualizar lista
        document.getElementById('usuarioId').value = '';
        document.getElementById('libroId').value = '';
        cargarPrestamos();

    } catch (error) {
        mostrarResultado(`Error: ${error.message}`, 'error');
        console.error('Error en el préstamo:', error);
    }
}

async function obtenerLibro(libroId) {
    const response = await fetch(`${API_BASE_URL}/libros/${libroId}`);
    if (!response.ok) {
        throw new Error('Libro no encontrado');
    }
    return await response.json();
}

async function registrarPrestamo(libroId, usuarioId) {
    const response = await fetch(`${API_BASE_URL}/prestamos?libroId=${libroId}&usuarioId=${usuarioId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al registrar el préstamo');
    }

    return await response.json();
}

async function cargarPrestamos() {
    try {
        const response = await fetch(`${API_BASE_URL}/prestamos`);
        if (!response.ok) {
            throw new Error('Error al obtener préstamos');
        }

        const prestamos = await response.json();
        mostrarPrestamos(prestamos);

    } catch (error) {
        mostrarResultado(`Error al cargar préstamos: ${error.message}`, 'error');
    }
}

function mostrarPrestamos(prestamos) {
    const cuerpoTabla = document.getElementById('cuerpo-tabla');
    cuerpoTabla.innerHTML = '';

    if (prestamos.length === 0) {
        cuerpoTabla.innerHTML = '<tr><td colspan="6">No hay préstamos registrados</td></tr>';
        return;
    }

    prestamos.forEach(prestamo => {
        const fila = document.createElement('tr');

        fila.innerHTML = `
            <td>${prestamo.id}</td>
            <td>${prestamo.libro.titulo} (ID: ${prestamo.libro.id})</td>
            <td>${prestamo.usuario.nombre} (ID: ${prestamo.usuario.id})</td>
            <td>${new Date(prestamo.fechaPrestamo).toLocaleDateString()}</td>
            <td>${prestamo.estado}</td>
            <td class="action-buttons">
                <button class="edit" onclick="abrirModalEditar(${prestamo.id})">Editar</button>
                <button class="return" onclick="devolverLibro(${prestamo.id})">Devolver</button>
                <button class="delete" onclick="eliminarPrestamo(${prestamo.id})">Eliminar</button>
            </td>
        `;

        cuerpoTabla.appendChild(fila);
    });
}

async function devolverLibro(prestamoId) {
    if (!confirm('¿Está seguro de marcar este préstamo como devuelto?')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/prestamos/${prestamoId}/devolver`, {
            method: 'PUT'
        });

        if (!response.ok) {
            throw new Error('Error al devolver el libro');
        }

        mostrarResultado('Libro devuelto exitosamente', 'success');
        cargarPrestamos();

    } catch (error) {
        mostrarResultado(`Error: ${error.message}`, 'error');
    }
}

async function eliminarPrestamo(prestamoId) {
    if (!confirm('¿Está seguro de eliminar este préstamo? Esta acción no se puede deshacer.')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/prestamos/${prestamoId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Error al eliminar el préstamo');
        }

        mostrarResultado('Préstamo eliminado exitosamente', 'success');
        cargarPrestamos();

    } catch (error) {
        mostrarResultado(`Error: ${error.message}`, 'error');
    }
}

// Funciones para el modal de edición
function abrirModalEditar(prestamoId) {
    document.getElementById('edit-prestamo-id').value = prestamoId;

    // Obtener datos actuales del préstamo
    fetch(`${API_BASE_URL}/prestamos/${prestamoId}`)
        .then(response => response.json())
        .then(prestamo => {
            document.getElementById('edit-estado').value = prestamo.estado;
            modal.style.display = "block";
        })
        .catch(error => {
            mostrarResultado(`Error al cargar préstamo: ${error.message}`, 'error');
        });
}

function cerrarModal() {
    modal.style.display = "none";
}

function guardarCambios() {
    const prestamoId = document.getElementById('edit-prestamo-id').value;
    const nuevoEstado = document.getElementById('edit-estado').value;

    fetch(`${API_BASE_URL}/prestamos/${prestamoId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            estado: nuevoEstado
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al actualizar el préstamo');
            }
            return response.json();
        })
        .then(() => {
            mostrarResultado('Préstamo actualizado exitosamente', 'success');
            cerrarModal();
            cargarPrestamos();
        })
        .catch(error => {
            mostrarResultado(`Error: ${error.message}`, 'error');
        });
}

function mostrarResultado(mensaje, tipo) {
    const resultadoDiv = document.getElementById('resultado');
    resultadoDiv.innerHTML = mensaje;
    resultadoDiv.className = tipo;
}

// Cerrar modal al hacer clic fuera de él
window.onclick = function(event) {
    if (event.target == modal) {
        cerrarModal();
    }
}