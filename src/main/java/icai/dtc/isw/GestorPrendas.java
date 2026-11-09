package icai.dtc.isw;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import icai.dtc.isw.dao.PrendaDAO;
import icai.dtc.isw.ui.SesionUsuario;

public class GestorPrendas { // SPRINT 1: EDITAR Y ELIMINAR PRENDAS

    // Busca una prenda por su id. Devuelve null si no existe.
    public Prenda buscarPorId(List<Prenda> prendas, int id) {
        for (Prenda prenda : prendas) {
            if (prenda.getId() == id) {
                return prenda;
            }
        }
        return null;
    }

    // Cambia los datos de la prenda con ese id.
    // Devuelve true si la ha encontrado y editado, false si no existe.
    public boolean editarPrenda(List<Prenda> prendas, int id, String nombre, String tipo, String color) {
        Prenda prenda = buscarPorId(prendas, id);
        if (prenda == null) {
            return false;
        }
        prenda.setNombre(nombre);
        prenda.setTipo(tipo); // antes ponía setEstilo: al editar el tipo se cambiaba el estilo
        prenda.setColor(color);
        return true;
    }

    // Elimina la prenda con ese id.
    // Devuelve true si la ha encontrado y borrado, false si no existe.
    // CORRECCIÓN: Se han quitado los parámetros "nombre", "tipo" y "color" que sobraban
    public boolean eliminarPrenda(List<Prenda> prendas, int id) {
        return prendas.removeIf(prenda -> prenda.getId() == id);
    }

    // ======================= SPRINT 2: PRENDAS GUARDADAS EN LA BD =======================
    // Los métodos de arriba (Sprint 1) siguen igual: trabajan solo con la lista y los usan los tests.

    private final PrendaDAO prendaDAO = new PrendaDAO();

    /**
     * Carga de la BD las prendas del usuario con sesión iniciada
     * (incluidas las guardadas en sesiones anteriores).
     * masRecientes = true -> más recientes primero; false -> más antiguas primero.
     */
    public List<Prenda> cargarPrendasUsuario(boolean masRecientes) {
        if (!SesionUsuario.haySesion()) {
            System.out.println("No hay sesión iniciada: no se cargan prendas.");
            return new ArrayList<>();
        }
        String email = SesionUsuario.getUsuarioActual().getEmail();
        return prendaDAO.getPrendasUsuario(email, masRecientes);
    }

    /** Guarda la prenda en la BD a nombre del usuario activo. Devuelve true si se ha guardado. */
    public boolean guardarPrenda(Prenda prenda) {
        if (!SesionUsuario.haySesion()) {
            System.out.println("No hay sesión iniciada: no se puede guardar la prenda.");
            return false;
        }
        prenda.setCorreoUsuario(SesionUsuario.getUsuarioActual().getEmail());
        return prendaDAO.guardarPrendaUsuario(prenda);
    }

    /** Edita la prenda en la BD y, si va bien, también en la lista. Devuelve true si se ha guardado. */
    public boolean editarPrendaGuardada(List<Prenda> prendas, int id, String nombre, String tipo, String color) {
        if (!SesionUsuario.haySesion()) {
            System.out.println("No hay sesión iniciada: no se puede editar la prenda.");
            return false;
        }
        String email = SesionUsuario.getUsuarioActual().getEmail();
        if (!prendaDAO.actualizarPrenda(id, nombre, tipo, color, email)) {
            return false;
        }
        return editarPrenda(prendas, id, nombre, tipo, color);
    }

    /** Borra la prenda de la BD y, si va bien, también de la lista. Devuelve true si se ha borrado. */
    public boolean eliminarPrendaGuardada(List<Prenda> prendas, int id) {
        if (!SesionUsuario.haySesion()) {
            System.out.println("No hay sesión iniciada: no se puede eliminar la prenda.");
            return false;
        }
        String email = SesionUsuario.getUsuarioActual().getEmail();
        if (!prendaDAO.eliminarPrenda(id, email)) {
            return false;
        }
        return eliminarPrenda(prendas, id);
    }

    // BONUS: ordenar una lista ya cargada sin volver a consultar la BD (Comparator).
    public void ordenarMasAntiguas(List<Prenda> prendas) {
        prendas.sort(Comparator.comparing(Prenda::getFechaCreacion,
                        Comparator.nullsLast(Comparator.<LocalDateTime>naturalOrder()))
                .thenComparingInt(Prenda::getId));
    }

    public void ordenarMasRecientes(List<Prenda> prendas) {
        prendas.sort(Comparator.comparing(Prenda::getFechaCreacion,
                        Comparator.nullsLast(Comparator.<LocalDateTime>reverseOrder()))
                .thenComparing(Comparator.comparingInt(Prenda::getId).reversed()));
    }
}